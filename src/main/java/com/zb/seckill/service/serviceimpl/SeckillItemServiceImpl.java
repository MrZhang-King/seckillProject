package com.zb.seckill.service.serviceimpl;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.crypto.digest.DigestUtil;
import com.zb.seckill.dao.RedisDao;
import com.zb.seckill.dao.SeckillItemDao;
import com.zb.seckill.dao.SeckillOrderDao;
import com.zb.seckill.domain.SeckillItem;
import com.zb.seckill.domain.SeckillOrder;
import com.zb.seckill.domain.User;
import com.zb.seckill.dto.ResponseResult;
import com.zb.seckill.dto.SeckillURL;
import com.zb.seckill.exception.SeckillItemException;
import com.zb.seckill.service.SeckillItemService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class SeckillItemServiceImpl implements SeckillItemService {

    @Autowired
    private SeckillItemDao seckillItemDao;
    @Autowired
    private RedisDao redisDao;
    @Autowired
    private SeckillOrderDao seckillOrderDao;

    public List<SeckillItem> getAllItem(){
        return seckillItemDao.selectAllItem();
    }

    @Override
    public SeckillItem getItemById(Integer id) {
        if(ObjectUtil.isEmpty(id) || id <= 0){
            throw new SeckillItemException("前端的item的id参数有误");
        }
        SeckillItem item = seckillItemDao.selectItemById(id);
        if(item == null){
            throw new SeckillItemException("通过item的id没有从数据库查询到item的相关信息");
        }
        return item;
    }

    //获取秒杀的url
    @Override
    public SeckillURL getSeckillURL(Integer itemId) {
        //调用方法，拿取SeckillItem对象
        SeckillItem seckillItem = this.getItemToRedis(itemId);
        //包装SeckillURL对象
        return this.getSeckillURL(seckillItem);
    }

    //判断请求中的md5URL与系统通过itemId生成的URL是否一致
    public boolean judgeMD5URL(String itemId, String md5URL){
        if(StrUtil.isEmpty(itemId) || StrUtil.isEmpty(md5URL)){
            return false;
        }
        return getMD5URL(itemId).equals(md5URL);
    }

    ////筛除重复请求
    // 同一个用户发送一次请求后5分钟内不能再次发送请求
    @Override
    public boolean excludeRepeatedlyRequest(Integer itemId, User user) {
        //同一个用户发送一次请求后5分钟内不能再次发送请求
        String key = user.getUphone() + "_" + itemId;
        //从Redis中通过key拿取itemID，
        Integer redisItemId = redisDao.getValue(key);
        //如果能拿到，说明该用户已经发送过请求了，不能再次发送请求
        if(!ObjectUtil.isEmpty(redisItemId)){
            return false;
        }
        //走到这，说明是第一次发送请求，需要将用户信息和商品id存在Redis中，防止用户发送多次请求时做判断
        redisDao.setEx(key,itemId,60 * 5);
        return true;
    }
    //减库存
    @Override
    public boolean downStock(Integer itemId){
       //执行lua脚本
        Integer result = redisDao.executeLuaDownStock("stock_" + itemId);
        // -1 库存不足
        // -2 不存在
        // 整数是正常操作，减库存成功
        if(result == -1){
            return false;
        }
        if(result == -2){
            return false;
        }
        return true;
    }
    //数据库减库存并生成订单
    @Override
    @Transactional
    public SeckillOrder flushDB(User user, Integer itemId){
        //更新数据库
        seckillItemDao.downStock(itemId);

        //生成订单，存入数据库
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setItemId(itemId);
        String orderCodeUUID = IdUtil.simpleUUID();
        seckillOrder.setOrderCode(orderCodeUUID);
        seckillOrder.setUserId(user.getUid());
        seckillOrder.setState(1);
        seckillOrder.setCreateTime(new Date());
        //将订单存入数据库中
        seckillOrderDao.insert(seckillOrder);
        //将订单信息存入redis中，方便判断订单状态（五分钟未支付订单失效）
        // key--> order_ + uuid  value--> seckillOrder订单
        redisDao.setEx("order_" + orderCodeUUID,seckillOrder,60 * 5);
        return seckillOrder;
    }
    //==================================================================

    /**
     * 包装SeckillURL对象
     * 判断商品的秒杀时间是否满足[start <= now <= end]
     * 满足给正确的url，不满足则url为null
     * @param seckillItem 商品对象
     * @return SeckillURL
     */
    private SeckillURL getSeckillURL(SeckillItem seckillItem){
        SeckillURL seckillURL = null;
        Long startTime = seckillItem.getStartTime().getTime();
        Long endTime = seckillItem.getEndTime().getTime();
        Long nowTime = new Date().getTime();
        String itemId = seckillItem.getId().toString();
        //[start <= now <= end]
//        1596085200000 start
//        1596042147697 now
//        1596088800000 end
        if(startTime <= nowTime && nowTime <= endTime){
            String md5URL = getMD5URL(itemId);
            seckillURL = new SeckillURL(true,itemId,md5URL,startTime,endTime,nowTime);
        }else{
            seckillURL = new SeckillURL(false,itemId,null,startTime,endTime,nowTime);
        }
        return seckillURL;
    }


    /**
     * 先从Redis中拿取SeckillItem
     * 如果拿不到，就从数据库中拿，拿到后将SeckillItem存入Reids中
     * 方便下次可以直接从Redis中拿，提高效率
     * @param itemId 商品id
     * @return SeckillItem
     */
    private SeckillItem getItemToRedis(Integer itemId){
        if(ObjectUtil.isEmpty(itemId)){
            throw new SeckillItemException("前端的item的id参数有误");
        }
        SeckillItem seckillItem = redisDao.getValue(itemId.toString());
        if(ObjectUtil.isEmpty(seckillItem)){//Redis中没有拿到SekillItem对象
            System.out.println("从数据库拿数据--------------------------------------");
            //从数据库中拿SekillItem对象
            seckillItem = getItemById(itemId);
            //将SekillItem对象存入Redis中
            redisDao.setObj(itemId.toString(),seckillItem);
            //将该商品的库存存入Redis中 key --> stock_id
            redisDao.setObj("stock_" + seckillItem.getId(),seckillItem.getNo());
        }
        return seckillItem;
    }
    //MD5加密URL的秘钥
    private static final String URL_PASSWORD = "#%*(fDfDgf)$%$*($)";
    //根据itemId生成一个md5URL
    private String getMD5URL(String itemId){
        return DigestUtil.md5Hex(URL_PASSWORD + "|||" + itemId);
    }

}
