package com.zb.seckill.domain;

/**
 * 用户表对应实体类
 */
public class User {
    private Integer uid;
    private String uname;
    private String uphone;
    private String upassword;

    public User() {
    }

    public User(String uphone, String upassword) {
        this.uphone = uphone;
        this.upassword = upassword;
    }

    public User(String uname, String uphone, String upassword) {
        this.uname = uname;
        this.uphone = uphone;
        this.upassword = upassword;
    }

    public Integer getUid() {
        return uid;
    }

    public void setUid(Integer uid) {
        this.uid = uid;
    }

    public String getUname() {
        return uname;
    }

    public void setUname(String uname) {
        this.uname = uname;
    }

    public String getUphone() {
        return uphone;
    }

    public void setUphone(String uphone) {
        this.uphone = uphone;
    }

    public String getUpassword() {
        return upassword;
    }

    public void setUpassword(String upassword) {
        this.upassword = upassword;
    }
}
