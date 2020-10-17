package com.zb.seckill.service.serviceimpl;

import cn.hutool.core.util.ObjectUtil;
import com.zb.seckill.dao.RedisDao;
import com.zb.seckill.dao.SeckillOrderDao;
import com.zb.seckill.domain.SeckillItem;
import com.zb.seckill.domain.SeckillOrder;
import com.zb.seckill.exception.SeckillItemException;
import com.zb.seckill.exception.SeckillOrderException;
import com.zb.seckill.service.SeckillOrderService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class SeckillOrderServiceImpl implements SeckillOrderService {

    @Autowired
    private SeckillOrderDao seckillOrderDao;
    @Autowired
    private RedisDao redisDao;
    @Override
    public SeckillOrder getOrderByCode(String orderCode) {
        SeckillOrder seckillOrder = seckillOrderDao.selectOrderByCode(orderCode);
        if(ObjectUtil.isEmpty(seckillOrder)){
            throw new SeckillOrderException("通过orderCode查询订单时没查到");
        }
        return seckillOrder;
    }

    public SeckillItem getItemById(String itemId){
        SeckillItem seckillItem = redisDao.getValue(itemId);
        if(ObjectUtil.isEmpty(seckillItem)){
            throw new SeckillItemException("redis中拿取商品信息没拿到");
        }
        return seckillItem;
    }

    /**
     * 完成支付的方法
     * @param orderCode
     * @return
     *  1  订单失效
     *  2  该订单是已支付的订单
     *  3  支付成功
     */
    @Override
    public Byte payMoney(String orderCode) {
        //从redis中拿取订单信息，能拿到说明订单是可支付状态，拿不到说明订单已过期
        Object obj = redisDao.getValue("order_" + orderCode);
        if(ObjectUtil.isEmpty(obj)){
            return 1;
        }
        //判断此订单是否是已支付的订单
        Object orderState = redisDao.getValue("order_state" + orderCode);
        if(!ObjectUtil.isEmpty(orderState)){
            return 2;
        }
        redisDao.setObj("order_state" + orderCode,"2");
        //修改订单状态
        seckillOrderDao.updateStatePay(orderCode);
        return 3;
    }

}
