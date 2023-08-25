package online.k12code.spikeservice.config;

import com.baomidou.mybatisplus.core.toolkit.CollectionUtils;
import online.k12code.spikeservice.entity.Good;
import online.k12code.spikeservice.mapper.GoodMapper;
import online.k12code.spikeservice.service.GoodService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.annotation.Resource;
import java.util.List;

/**
 * @author Carl
 * @date 2023/8/25
 **/
@Component
public class DataSyncConfig {
    @Resource
    private GoodService goodService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 业务场景是搞一个定时任务 每天10点开启
    // 为了 测试方便 项目已启动就执行一次

    /**
     * spring bean的生命周期
     * 在当前对象 实例化完以后
     * 属性注入以后
     * 执行 PostConstruct 注解的方法
     */
    @PostConstruct
    @Scheduled(cron = "0 10 0 0 0 ?")
    public void initData() {
        // 查询所有的秒杀商品
        List<Good> goodsList = goodService.lambdaQuery().eq(Good::getStatus, 2).list();
        if (CollectionUtils.isEmpty(goodsList)) {
            return;
        }
        goodsList.forEach(goods -> redisTemplate.opsForValue().set("goods_stock:" + goods.getId(), goods.getStocks().toString()));
    }
}
