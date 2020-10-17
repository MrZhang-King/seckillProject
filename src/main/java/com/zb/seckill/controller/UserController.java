package com.zb.seckill.controller;

import cn.hutool.core.util.ObjectUtil;
import com.zb.seckill.domain.User;
import com.zb.seckill.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpSession;

@Controller
public class UserController {
    @Autowired
    private UserService userService;

    @RequestMapping("/test/{uid}")
    @ResponseBody
    public User selectUserByUId(@PathVariable("uid") Integer uid){
        System.out.println("controller执行"+uid);
        return userService.selectUserByUId(uid);
    }

    @RequestMapping("/test1")
    @ResponseBody
    public User test1(){
        System.out.println("执行了");
        return new User();
    }


    //登录
    @RequestMapping("/login-c")
    public String login(Model model, String uphone, String upassword, HttpSession session) {
//        System.out.println("登录controller执行");
//        System.out.println("controller接收到的密码：" + upassword);
        User user = userService.login(uphone,upassword);
        if(!ObjectUtil.isEmpty(user)){
//          登录成功
//        前缀prefix value="/WEB-INF/jsp/"
//        后缀suffix value=".jsp"
            session.setAttribute("user",user);
          return "forward:getAllItem";
        }
        model.addAttribute("loginResult","手机号或密码输入有误");
        return "login";
    }


    //辅助注册 做跳转
    @RequestMapping("/jumpRegister")
    public String jumpRegister(){
        return "register";
    }
    //注册
    @RequestMapping("/register-c")
    public String register(Model model,String name,String phone,String password1,String password2){
//        System.out.println("controller层接到的name：" + name);
        String registerResult = userService.register(name,phone,password1,password2);
        if("true".equals(registerResult)){
            model.addAttribute("registerResult","注册成功，请登录");
            return "login";
        }
        model.addAttribute("registerResult",registerResult);
        return "forward:jumpRegister";
    }

}
