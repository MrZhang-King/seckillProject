package com.zb.seckill.dao;

import com.zb.seckill.domain.SeckillItem;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeckillItemDao {

    List<SeckillItem> selectAllItem();

    SeckillItem selectItemById(Integer id);

    void downStock(Integer id);

    void addStock(Integer id);
}
