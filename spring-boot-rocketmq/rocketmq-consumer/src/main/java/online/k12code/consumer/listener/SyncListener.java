package online.k12code.consumer.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * 同步监听
 * @author Carl
 * @date 2023/8/22
 **/
@Component
@RocketMQMessageListener(topic = "sync-topic", consumerGroup = "sync-consumer")
public class SyncListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        System.out.println("sync-topic: " + new String(message.getBody()));
    }
}
