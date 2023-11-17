package online.k12code.consumer.listener;

import com.alibaba.fastjson.JSON;
import lombok.extern.slf4j.Slf4j;
import online.k12code.consumer.entity.CanalSynDto;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.UnsupportedEncodingException;

/**
 * 同步监听
 *
 * @author Carl
 * @date 2023/8/22
 **/
@Slf4j
@Component
@RocketMQMessageListener(topic = "example", consumerGroup = "canal-syn-consumer")
public class SyncListener implements RocketMQListener<MessageExt> {

    //注入Redis
    @Resource
    private RedisTemplate<String, Object> redisTemplate;

    @Override
    public void onMessage(MessageExt message) {
        try {
            String json = new String(message.getBody(), "utf-8");
            CanalSynDto canalSynDto = JSON.parseObject(json, CanalSynDto.class);
            log.info("canal同步 {}", canalSynDto);
            if (canalSynDto.getType().equals("INSERT") || canalSynDto.getType().equals("UPDATE")) {
                canalSynDto.getData().forEach(blacklist -> {
                    redisTemplate.opsForValue().set("ID:" + blacklist.hashCode(), blacklist);
                });
            } else if (canalSynDto.getType().equals("DELETE")) {
                canalSynDto.getData().forEach(blacklist -> {
                    redisTemplate.delete("ID:" + blacklist.hashCode());
                });

                System.out.println(json);
            }
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
    }
}
