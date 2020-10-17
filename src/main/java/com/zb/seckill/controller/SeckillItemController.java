package com.zb.seckill.controller;

import cn.hutool.core.util.ObjectUtil;
import com.zb.seckill.domain.SeckillItem;
import com.zb.seckill.domain.SeckillOrder;
import com.zb.seckill.domain.User;
import com.zb.seckill.dto.ResponseResult;
import com.zb.seckill.dto.SeckillURL;
import com.zb.seckill.service.SeckillItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpSession;
import java.util.Date;
import java.util.List;

@Controller
public class SeckillItemController {

    @Autowired
    private SeckillItemService seckillItemService;

    //查询所有商品
    @RequestMapping("/getAllItem")
    @ResponseBody
    public ModelAndView getAllItem(){
        ModelAndView mv = new ModelAndView();
        List<SeckillItem> items = seckillItemService.getAllItem();
        mv.addObject("items",items);
        mv.setViewName("itemList");
        return mv;
    }

    //根据商品id查询单个商品
    @RequestMapping("/getItemById/{id}")
    @ResponseBody
    public ModelAndView getItemById(@PathVariable("id") Integer id){
        ModelAndView mv = new ModelAndView();
        SeckillItem item = seckillItemService.getItemById(id);
        mv.addObject("item",item);
        mv.setViewName("seckill");
        return mv;
    }

    //返回系统时间
    @RequestMapping(value = "/getTime",method = RequestMethod.POST)
    @ResponseBody
    public ResponseResult<Long> getTime(){
//        System.out.println("获取时间的方法执行了");
        Long time =  new Date().getTime();
        ResponseResult<Long> result = new ResponseResult<>(true,time,"ok");
        return result;
    }

    @RequestMapping("/getSeckillURL/{id}")
    @ResponseBody
    public ResponseResult<SeckillURL> getSeckillURL(@PathVariable("id") Integer id, HttpSession session){
        //判断用户是否登录
        User user = (User) session.getAttribute("user");
        if(ObjectUtil.isEmpty(user)){
            return new ResponseResult<>(false,null,"noLogin");
        }
//        测试用session.removeAttribute("user");
        //调用service 包装ResponseResult返回即可
        SeckillURL seckillURL = seckillItemService.getSeckillURL(id);
        ResponseResult<SeckillURL> result = new ResponseResult<>(true,seckillURL,"ok");
        return result;
    }

    @RequestMapping("/startSeckillItem/{md5URL}/{itemId}")
    @ResponseBody
    public ResponseResult<SeckillOrder> startSeckillItem(
            @PathVariable("md5URL") String md5URL, @PathVariable("itemId") Integer itemId,
            HttpSession session){

        ResponseResult<SeckillOrder> responseResult = new ResponseResult<>();
       //从session中拿取登录是存入的user对象
        User user = (User) session.getAttribute("user");
        //判断是否是登录状态
        if(ObjectUtil.isEmpty(user)){
//            System.out.println("user为null了==============================================");
            responseResult.setSuccess(false);
            responseResult.setData(null);
            responseResult.setMessage("noLogin");
            return responseResult;
        }
        //测试使用
//        session.removeAttribute("user");
        //判断md5URL是否正确
        if(ObjectUtil.isEmpty(md5URL) || ObjectUtil.isEmpty(itemId) ||
                !seckillItemService.judgeMD5URL(itemId.toString(),md5URL)){
            responseResult.setSuccess(false);
            responseResult.setData(null);
            responseResult.setMessage("urlException");
            return responseResult;
        }
        //同一个用户发送一次请求后5分钟内不能再次发送请求
        boolean excludeRepeatedlyRequestResult = seckillItemService.excludeRepeatedlyRequest(itemId,user);
            if(!excludeRepeatedlyRequestResult){
                responseResult.setSuccess(false);
                responseResult.setData(null);
//            System.out.println("===========================发送多次请求=============================");
                responseResult.setMessage("RepeatedlyRequestException");
                return responseResult;
        }
        //redis减库存
        boolean downStockResult = seckillItemService.downStock(itemId);
        if(!downStockResult){
            responseResult.setSuccess(false);
            responseResult.setData(null);
            responseResult.setMessage("understock");
            return responseResult;
        }
        //数据库减库存并生成订单 保持原子性
        SeckillOrder seckillOrder = seckillItemService.flushDB(user,itemId);
        responseResult.setSuccess(true);
        responseResult.setData(seckillOrder);
        responseResult.setMessage("购买成功");
        return responseResult;
        //清除生成订单未支付的用户
    }
}
