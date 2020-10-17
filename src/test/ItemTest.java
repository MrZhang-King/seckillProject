import com.zb.seckill.dao.SeckillItemDao;
import com.zb.seckill.dao.SeckillOrderDao;
import com.zb.seckill.domain.SeckillItem;
import com.zb.seckill.domain.SeckillOrder;
import com.zb.seckill.dto.SeckillURL;
import com.zb.seckill.service.SeckillItemService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(locations = { "classpath:spring/spring-*.xml" })
public class ItemTest {

    @Autowired
    private SeckillItemService seckillItemService;
    @Autowired
    private SeckillOrderDao seckillOrderDao;

    @Autowired
    private SeckillItemDao seckillItemDao;
    @Test
    public void test1(){
//    List<SeckillItem> seckillItemList = seckillItemService.getAllItem();
        List<SeckillItem> seckillItemList = seckillItemDao.selectAllItem();
        for(SeckillItem seckillItem : seckillItemList){
            System.out.println("查询到的信息" + seckillItem);
        }
    }

    @Test
    public void test2(){
        seckillItemDao.addStock(2);
//        Date date = new Date();
//        System.out.println("日期：" + date);
//        System.out.println("long:" + date.getTime());
    }

    @Test
    public void test3(){

        String str = (String)null;
        System.out.println("==================================================" + str);
//        SeckillURL url = seckillItemService.getSeckillURL(2);
//        System.out.println("拿到的url:" + url);
    }

    @Test
    public void testOrderDao(){
        SeckillOrder seckillOrder = new SeckillOrder();
        seckillOrder.setItemId(22);
        seckillOrder.setUserId(22);
        seckillOrder.setState(22);
        seckillOrder.setCreateTime(new Date());
        seckillOrderDao.insert(seckillOrder);
    }

}
