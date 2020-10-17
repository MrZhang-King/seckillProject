import com.zb.seckill.dao.RedisDao;
import com.zb.seckill.dto.SeckillURL;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import redis.clients.jedis.Jedis;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-*.xml" })
public class TestRedis {
    @Test
    public void testKeyValue(){
        Jedis jedis = new Jedis();
        jedis.set("age2","22");
        String age = jedis.get("age");
        String name = jedis.get("name");
        System.out.println(name + "---" + age);
    }

    @Autowired
    private RedisDao redisDao;
    @Test
    public void test1(){
//        redisDao.setObj("1",new SeckillURL(true,"1","ads",12L,12L,12L));
//        SeckillURL url = redisDao.getValue("1");
//        System.out.println("redis中拿到的：" + url);
        redisDao.executeLuaAddStock("stock_2");
    }
}
