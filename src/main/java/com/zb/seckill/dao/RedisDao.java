package com.zb.seckill.dao;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.zb.seckill.exception.RedisException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.connection.RedisConnection;
import org.springframework.data.redis.core.RedisCallback;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;
import redis.clients.jedis.Jedis;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

@Component
public class RedisDao {
    @Autowired
    private RedisTemplate redisTemplate;

    //存值
    public void setObj(String key, Object value){
        try {
            if (StrUtil.isEmpty(key) || ObjectUtil.isEmpty(value)) {
                throw new RedisException("存入Redis时key或value为null");
            }
            redisTemplate.opsForValue().set(key, value);
        }catch (Exception e){
            e.printStackTrace();
            throw new RedisException("redis存入数据出错");
        }
    }

    //取值
    public <T> T getValue(String key){
        try {

            if (StrUtil.isEmpty(key)) {
                throw new RedisException("取Redis中的值时key为null");
            }
            return (T) redisTemplate.opsForValue().get(key);
        }catch (Exception e){
            e.printStackTrace();
            throw new RedisException("获取Redis中的数据出错");
        }
    }

    //存有存活时间的值
    public void setEx(String key, Object value, Integer time){
        try {
            if (StrUtil.isEmpty(key) || ObjectUtil.isEmpty(value) || ObjectUtil.isEmpty(time)) {
                throw new RedisException("存入Redis时key--value或存活时间为null");
            }
            redisTemplate.opsForValue().set(key, value, time, TimeUnit.SECONDS);
        }catch (Exception e){
            e.printStackTrace();
            throw new RedisException("Redis存入固定时间失效的数据时出错");
        }
    }

    //减库存的LUA脚本
    private static final String LUA_SCRIPT_DOWNSTOCK;
    //加库存的LUA脚本
    private static final String LUA_SCRIPT_ADDSTOCK;
    //拼接LUA脚本的静态块
    static {
        StringBuilder sb = new StringBuilder();
        // 初始化减库存lua脚本
        // -1 库存不足
        // -2 不存在
        // 整数是正常操作，减库存成功
        sb.append("if (redis.call('exists', KEYS[1]) == 1) then");
        sb.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        sb.append("    if (stock == -1) then");
        sb.append("        return -1");
        sb.append("    end;");
        sb.append("    if (stock > 0) then");
        sb.append("        redis.call('incrby', KEYS[1], -1);");
        sb.append("        return stock - 1;");
        sb.append("    end;");
        sb.append("    return -1;");
        sb.append("end;");
        sb.append("return -2;");
        LUA_SCRIPT_DOWNSTOCK = sb.toString();
        //----------------------------------------------------
        StringBuilder sb2 = new StringBuilder();
        // 初始化加库存lua脚本
        // -2 不存在
        // 整数是正常操作，加库存成功
        sb2.append("if (redis.call('exists', KEYS[1]) == 1) then");
        sb2.append("    local stock = tonumber(redis.call('get', KEYS[1]));");
        sb2.append("    if (stock == -1) then");
        sb2.append("        redis.call('set', KEYS[1],1)");
        sb2.append("    end;");
        sb2.append("    if (stock == 0) then");
        sb2.append("        redis.call('set', KEYS[1],1)");
        sb2.append("    end;");
        sb2.append("    if (stock > 0) then");
        sb2.append("        redis.call('incrby', KEYS[1], 1);");
        sb2.append("        return stock + 1;");
        sb2.append("    end;");
        sb2.append("    end;");
        sb2.append("return -2;");
        LUA_SCRIPT_ADDSTOCK = sb2.toString();


    }

    //执行减库存lua脚本
    public Integer executeLuaDownStock(String key){
        // 初始化减库存lua脚本
        // -1 库存不足
        // -2 不存在
        // 整数是正常操作，减库存成功

        // 脚本里的KEYS参数
        final List<String> keys = new ArrayList<String>();
        keys.add(key);
        // 脚本里的ARGV参数
        final List<String> args = new ArrayList<String>();

        Integer result = (Integer) redisTemplate.execute(new RedisCallback<Integer>() {

            @Override
            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // redis集群模式，执行脚本
//                if (nativeConnection instanceof JedisCluster) {
//                    return (Integer) ((JedisCluster) nativeConnection).eval(LUA_SCRIPT, keys, args);
//                }
                // redis单机模式，执行脚本
//                else if (nativeConnection instanceof Jedis) {
                if (nativeConnection instanceof Jedis) {
                    Object temp = ((Jedis) nativeConnection).eval(LUA_SCRIPT_DOWNSTOCK, keys, args);
                    return Integer.valueOf(String.valueOf(temp));
                }
                return null;
            }
            });
            return result;
    }


    //执行加库存lua脚本
    public Integer executeLuaAddStock(String key){
        // 初始化减库存lua脚本
        // -2 不存在
        // 整数是正常操作，加库存成功

        // 脚本里的KEYS参数
        final List<String> keys = new ArrayList<String>();
        keys.add(key);
        // 脚本里的ARGV参数
        final List<String> args = new ArrayList<String>();

        Integer result = (Integer) redisTemplate.execute(new RedisCallback<Integer>() {

            @Override
            public Integer doInRedis(RedisConnection connection) throws DataAccessException {
                Object nativeConnection = connection.getNativeConnection();
                // 集群模式和单机模式虽然执行脚本的方法一样，但是没有共同的接口，所以只能分开执行
                // redis集群模式，执行脚本
//                if (nativeConnection instanceof JedisCluster) {
//                    return (Integer) ((JedisCluster) nativeConnection).eval(LUA_SCRIPT, keys, args);
//                }
                // redis单机模式，执行脚本
//                else if (nativeConnection instanceof Jedis) {
                if (nativeConnection instanceof Jedis) {
                    Object temp = ((Jedis) nativeConnection).eval(LUA_SCRIPT_ADDSTOCK, keys, args);
                    return Integer.valueOf(String.valueOf(temp));
                }
                return null;
            }
        });
        return result;
    }

}
