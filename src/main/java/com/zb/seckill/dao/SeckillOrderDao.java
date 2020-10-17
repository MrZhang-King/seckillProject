package com.zb.seckill.dao;

import com.zb.seckill.domain.SeckillOrder;

import java.util.List;

public interface SeckillOrderDao {

    void insert(SeckillOrder seckillOrder);

    List<SeckillOrder> queryNoPayOrder();

    void updateStateFailure(Integer id);

    SeckillOrder selectOrderByCode(String orderCode);

    void updateStatePay(String orderCode);
}
