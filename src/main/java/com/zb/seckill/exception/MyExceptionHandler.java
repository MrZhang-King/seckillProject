package com.zb.seckill.exception;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerExceptionResolver;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class MyExceptionHandler implements HandlerExceptionResolver {
    @Override
    public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        ex.printStackTrace();
        ModelAndView mv = new ModelAndView();
        if(ex instanceof UserException){
            System.out.println("异常信息：" + ex.getMessage());
        }else if(ex instanceof SeckillItemException){
            System.out.println("异常信息：" + ex.getMessage());
        }else if(ex instanceof SeckillOrderException){
            System.out.println("异常信息：" + ex.getMessage());
        }
        //该异常不应该展示给客户
        else if(ex instanceof RedisException){
            System.out.println("异常信息：" + ex.getMessage());
            return null;
        }
        mv.addObject("exception",ex.getMessage());
        mv.setViewName("exception");
        return mv;
    }
}
