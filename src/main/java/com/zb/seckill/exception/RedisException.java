package com.zb.seckill.exception;

public class RedisException extends RuntimeException {
    public RedisException(){}
    public RedisException(String message){
        super(message);
    }
}
