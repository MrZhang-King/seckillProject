package com.zb.seckill.service.serviceimpl;

import cn.hutool.crypto.digest.DigestUtil;
import com.zb.seckill.dao.UserDao;
import com.zb.seckill.domain.User;
import com.zb.seckill.exception.UserException;
import com.zb.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserDao userDao;
    @Override
    public User selectUserByUId(Integer uid) {
        System.out.println("service执行了"+uid);
        return userDao.selectUserByUId(uid);
    }

    @Override
    public User login(String phone, String password) {
        User user = userDao.selectUserByPhone(phone);
        if(user == null){
            return null;
        }
        if(phone == null || phone.length() != 11 || !phone.equals(user.getUphone())){
            return null;
        }
//        System.out.println("数据库查到的：" + user.getUpassword());
        if(password == null || !DigestUtil.md5Hex(password).equals(user.getUpassword())){
            return null;
        }
        return user;
    }

    public String register(String name,String phone,String password1,String password2){
        if(name == null || "".equals(name)){
            return "用户名必填";
        }
        if(phone == null || "".equals(phone) || phone.length() != 11){
         return "手机号输入格式不正确";
        }
        if(password1 == null || password2 == null ||
                password1.equals("") || password2.equals("") ||
                !password1.equals(password2)) {
            return "密码输入格式有误";
        }
        User user = new User(name,phone,DigestUtil.md5Hex(password1));
        userDao.addUser(user);
        return "true";
    }
}
