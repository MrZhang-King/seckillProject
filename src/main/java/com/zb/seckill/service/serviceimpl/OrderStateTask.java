package com.zb.seckill.service.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import com.zb.seckill.dao.RedisDao;
import com.zb.seckill.dao.SeckillItemDao;
import com.zb.seckill.dao.SeckillOrderDao;
import com.zb.seckill.domain.SeckillOrder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@EnableScheduling
public class OrderStateTask {

    @Autowired
    SeckillOrderDao seckillOrderDao;
    @Autowired
    RedisDao redisDao;
    @Autowired
    SeckillItemDao seckillItemDao;

    @Transactional
    @Scheduled(fixedRate = 1000)
    public void setOrderState(){
        //查询数据库中的所有的未支付订单
        List<SeckillOrder> orderList = seckillOrderDao.queryNoPayOrder();
        if(ObjectUtil.isEmpty(orderList) || orderList.size() == 0){
            return;
        }
        for(SeckillOrder order : orderList){
            //通过order_uuid拿存入redis中的值，能拿到说明订单未失效，拿不到订单失效
            SeckillOrder seckillOrder = redisDao.getValue("order_" + order.getOrderCode());
            //订单失效了
            if(ObjectUtil.isEmpty(seckillOrder)){
                //修改数据库中的订单状态
                System.out.println("=========================" + order.getId());
                seckillOrderDao.updateStateFailure(order.getId());
                //redis库存加1
                redisDao.executeLuaAddStock("stock_" + order.getItemId());
                //数据库库存加1
                seckillItemDao.addStock(order.getItemId());
            }
        }
    }
}
