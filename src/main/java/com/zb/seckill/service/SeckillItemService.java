package com.zb.seckill.service;

import com.zb.seckill.domain.SeckillItem;
import com.zb.seckill.domain.SeckillOrder;
import com.zb.seckill.domain.User;
import com.zb.seckill.dto.SeckillURL;

import java.util.List;

public interface SeckillItemService {
    List<SeckillItem> getAllItem();

    SeckillItem getItemById(Integer id);

    SeckillURL getSeckillURL(Integer itemId);

    boolean judgeMD5URL(String itemId, String md5URL);

    boolean excludeRepeatedlyRequest(Integer itemId, User user);

    boolean downStock(Integer itemId);

    SeckillOrder flushDB(User user, Integer itemId);
}
