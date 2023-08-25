package online.k12code.spikeweb.controller;

import com.alibaba.fastjson2.JSON;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Carl
 * @date 2023/8/25
 **/
@RestController
public class SpikeController {
    @Autowired
    private StringRedisTemplate redisTemplate;

    @Autowired
    private RocketMQTemplate rocketMQTemplate;

    /**
     * 压测时自动是生成用户id
     */
    AtomicInteger ai = new AtomicInteger(0);

    /**
     * 1.一个用户针对一种商品只能抢购一次
     * 2.做库存的预扣减  拦截掉大量无效请求
     * 3.放入mq 异步化处理订单
     *
     * @return
     */
    @GetMapping("doSeckill")
    public String doSeckill(Integer goodsId /*, Integer userId*/) {
        int userId = ai.incrementAndGet();
        // unique key 唯一标记 去重
        String uk = userId + "-" + goodsId;
        // set nx  set if not exist
        Boolean flag = redisTemplate.opsForValue().setIfAbsent("seckillUk:" + uk, "");
        if (!flag) {
            return "您以及参与过该商品的抢购，请参与其他商品抢购!";
        }
        // 假设库存已经同步了  key:goods_stock:1  val:10
        Long count = redisTemplate.opsForValue().decrement("goods_stock:" + goodsId);
        // getkey  java  setkey    先查再写 再更新 有并发安全问题
        if (count < 0) {
            return "该商品已经被抢完，请下次早点来哦O(∩_∩)O";
        }
        // 放入mq
        HashMap<String, Integer> map = new HashMap<>(4);
        map.put("goodsId", goodsId);
        map.put("userId", userId);
        rocketMQTemplate.asyncSend("seckillTopic3", JSON.toJSONString(map), new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功" + sendResult.getSendStatus());
            }

            @Override
            public void onException(Throwable throwable) {
                System.err.println("发送失败" + throwable);
            }
        });
        return "拼命抢购中,请稍后去订单中心查看";
    }

}
