package online.k12code.spikeservice.config;

import online.k12code.spikeservice.entity.Good;
import online.k12code.spikeservice.mapper.GoodMapper;
import online.k12code.spikeservice.service.GoodService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author Carl
 * @date 2023/8/25
 **/
@Component
public class MySqlToRedis2 implements CommandLineRunner {


    @Resource
    private GoodService goodService;

    @Resource
    private StringRedisTemplate stringRedisTemplate;

    @Override
    public void run(String... args) throws Exception {
        initData();
    }

    private void initData() {
        //1,查询数据库中需要参于秒杀的商品数据
        List<Good> goodsList = goodService.lambdaQuery().eq(Good::getStatus, 2).list();
        ValueOperations<String, String> operations = stringRedisTemplate.opsForValue();
//        //2,把数据同步到Redis
        for (Good goods : goodsList) {
            operations.set("goods:" + goods.getId(), goods.getStocks().toString());
        }
    }
}
