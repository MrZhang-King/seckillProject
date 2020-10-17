package com.zb.seckill.domain;

import java.util.Date;

/**
 * 订单表对应实体类
 */
public class SeckillOrder {
    private Integer id;
    private String orderCode;
    private Integer itemId;
    private Integer userId;
    // 1  生成订单未支付
    // 2  生成订单已支付
    // 4  已失效的订单
    private Integer state;
    private Date createTime;

    public SeckillOrder() {
    }

    public SeckillOrder(Integer id, Integer itemId, Integer userId, Integer state, Date createTime) {
        this.id = id;
        this.itemId = itemId;
        this.userId = userId;
        this.state = state;
        this.createTime = createTime;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getOrderCode() {
        return orderCode;
    }

    public void setOrderCode(String orderCode) {
        this.orderCode = orderCode;
    }

    public Integer getItemId() {
        return itemId;
    }

    public void setItemId(Integer itemId) {
        this.itemId = itemId;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getState() {
        return state;
    }

    public void setState(Integer state) {
        this.state = state;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }
}
