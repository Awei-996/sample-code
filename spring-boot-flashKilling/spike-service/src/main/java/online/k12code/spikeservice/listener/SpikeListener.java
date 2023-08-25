package online.k12code.spikeservice.listener;

import com.alibaba.fastjson2.JSON;
import com.alibaba.fastjson2.JSONObject;
import online.k12code.spikeservice.service.GoodService;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.concurrent.TimeUnit;

/**
 * 默认负载均衡模式
 * 默认多线程消费
 * @author Carl
 * @date 2023/8/25
 **/
@Component
@RocketMQMessageListener(topic = "seckillTopic3", consumerGroup = "seckill-consumer-group")
public class SpikeListener implements RocketMQListener<MessageExt> {
    @Resource
    private GoodService goodService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    // 20s
    int time = 20000;

    @Override
    public void onMessage(MessageExt message) {
        String s = new String(message.getBody());
        JSONObject jsonObject = JSON.parseObject(s);
        Integer goodsId = jsonObject.getInteger("goodsId");
        Integer userId = jsonObject.getInteger("userId");
        // 做真实的抢购业务  减库存 写订单表    todo 答案2  但是不符合分布式
//        synchronized (SeckillMsgListener.class) {
//            goodsService.realDoSeckill(goodsId, userId);
//        }

        // 自旋锁  一般 mysql 每秒1500/s写   看数量 合理的设置自旋时间  todo 答案3
        int current = 0;
        while (current <= time) {
            // 一般在做分布式锁的情况下  会给锁一个过期时间 防止出现死锁的问题
            Boolean flag = redisTemplate.opsForValue().setIfAbsent("goods_lock:" + goodsId, "", 10, TimeUnit.SECONDS);
            if (flag) {
                try {
                    goodService.realDoSeckill(goodsId, userId);
                    return;
                } finally {
                    redisTemplate.delete("goods_lock:" + goodsId);
                }
            } else {
                current += 200;
                try {
                    Thread.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
