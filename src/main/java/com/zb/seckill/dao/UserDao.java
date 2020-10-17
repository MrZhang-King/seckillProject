package com.zb.seckill.dao;

import com.zb.seckill.domain.User;
import org.springframework.stereotype.Repository;

@Repository
public interface UserDao {

    User selectUserByUId(Integer uid);

    User selectUserByPhone(String phone);

    void addUser(User user);
}
