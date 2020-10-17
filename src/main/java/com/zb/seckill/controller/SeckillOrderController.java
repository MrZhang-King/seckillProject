package com.zb.seckill.controller;

import cn.hutool.core.util.ObjectUtil;
import com.zb.seckill.domain.SeckillItem;
import com.zb.seckill.domain.SeckillOrder;
import com.zb.seckill.domain.User;
import com.zb.seckill.dto.ResponseResult;
import com.zb.seckill.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class SeckillOrderController {

    @Autowired
    private SeckillOrderService seckillOrderService;
    //辅助跳转支付页面
    @RequestMapping("/orderPay/{orderCode}")
    public String jumpOrderPay(@PathVariable("orderCode") String orderCode, Model model){
        //根据orderCode查询订单信息
        SeckillOrder seckillOrder = seckillOrderService.getOrderByCode(orderCode);
        //根据商品id查询商品信息
        SeckillItem seckillItem = seckillOrderService.getItemById(seckillOrder.getItemId().toString());
        //放入request作用域中
        model.addAttribute("order",seckillOrder);
        model.addAttribute("item",seckillItem);
        return "orderPay";
    }

    @RequestMapping("payMoney/{orderCode}")
    @ResponseBody
    public ResponseResult<String> payMoney(@PathVariable("orderCode") String orderCode, HttpSession session){
        ResponseResult<String> responseResult = new ResponseResult<>();
        User user = (User) session.getAttribute("user");
        if(ObjectUtil.isEmpty(user)){
            responseResult.setSuccess(false);
            responseResult.setData("支付失败");
            responseResult.setMessage("noLogin");
            return responseResult;
        }
        byte payMoneyResult = seckillOrderService.payMoney(orderCode);

//        1  订单失效
//        2  该订单是已支付的订单
//        3  支付成功
        if(payMoneyResult == 3) {
            responseResult.setSuccess(true);
            responseResult.setData("支付成功");
            responseResult.setMessage("ok");
        }else if(payMoneyResult == 2){
            responseResult.setSuccess(false);
            responseResult.setData("支付失败");
            responseResult.setMessage("received");
        }else{
            responseResult.setSuccess(false);
            responseResult.setData("支付失败");
            responseResult.setMessage("timeOut");
        }
        return responseResult;
    }
}
