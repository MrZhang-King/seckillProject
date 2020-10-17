package com.zb.seckill.service;

import com.zb.seckill.domain.User;

public interface UserService {
    User selectUserByUId(Integer uid);

    User login(String phone, String password);

    String register(String name,String phone,String password1,String password2);
}
