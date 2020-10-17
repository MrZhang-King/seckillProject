package com.zb.seckill.service;

import com.zb.seckill.domain.SeckillItem;
import com.zb.seckill.domain.SeckillOrder;

public interface SeckillOrderService {
    SeckillOrder getOrderByCode(String orderCode);

   SeckillItem getItemById(String itemId);

   Byte payMoney(String orderCode);
}
