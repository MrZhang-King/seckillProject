package com.zb.seckill.dto;

public class SeckillURL {

    private boolean isUsed;  //url是否可用  true:可用  false:不可用
    private String itemId;   //商品id
    private String md5url;   //md5加密后的url
    private Long startTime;
    private Long endTime;
    private Long nowTime;

    public SeckillURL() {
    }

    public SeckillURL(boolean isUsed, String itemId, Long startTime, Long endTime, Long nowTime) {
        this.isUsed = isUsed;
        this.itemId = itemId;
        this.startTime = startTime;
        this.endTime = endTime;
        this.nowTime = nowTime;
    }

    public SeckillURL(boolean isUsed, String itemId, String md5url, Long startTime, Long endTime, Long nowTime) {
        this.isUsed = isUsed;
        this.itemId = itemId;
        this.md5url = md5url;
        this.startTime = startTime;
        this.endTime = endTime;
        this.nowTime = nowTime;
    }

    @Override
    public String toString() {
        return "SeckillURL{" +
                "isUsed=" + isUsed +
                ", itemId='" + itemId + '\'' +
                ", md5url='" + md5url + '\'' +
                ", startTime=" + startTime +
                ", endTime=" + endTime +
                ", nowTime=" + nowTime +
                '}';
    }

    public boolean isUsed() {
        return isUsed;
    }

    public void setUsed(boolean used) {
        isUsed = used;
    }

    public String getItemId() {
        return itemId;
    }

    public void setItemId(String itemId) {
        this.itemId = itemId;
    }

    public String getMd5url() {
        return md5url;
    }

    public void setMd5url(String md5url) {
        this.md5url = md5url;
    }

    public Long getStartTime() {
        return startTime;
    }

    public void setStartTime(Long startTime) {
        this.startTime = startTime;
    }

    public Long getEndTime() {
        return endTime;
    }

    public void setEndTime(Long endTime) {
        this.endTime = endTime;
    }

    public Long getNowTime() {
        return nowTime;
    }

    public void setNowTime(Long nowTime) {
        this.nowTime = nowTime;
    }
}
