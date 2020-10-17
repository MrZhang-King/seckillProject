package com.zb.seckill.domain;

import java.util.Date;

/**
 * 秒杀商品表对应实体类
 */
public class SeckillItem {
    private Integer id;
    private String name;
    private Double price;
    private Integer no;
    private Date createTime;
    private Date startTime;
    private Date endTime;

    public SeckillItem() {
    }

    public SeckillItem(Integer id, String name, Double price, Integer no, Date createTime, Date startTime, Date endTime) {
        this.id = id;
        this.name = name;
        this.price = price;
        this.no = no;
        this.createTime = createTime;
        this.startTime = startTime;
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "SeckillItem{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", price=" + price +
                ", no=" + no +
                ", createTime=" + createTime +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                '}';
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Integer getNo() {
        return no;
    }

    public void setNo(Integer no) {
        this.no = no;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }
}
