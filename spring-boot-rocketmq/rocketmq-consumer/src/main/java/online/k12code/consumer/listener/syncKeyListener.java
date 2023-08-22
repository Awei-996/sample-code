package online.k12code.consumer.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Carl
 * @date 2023/8/22
 **/
@Component
@RocketMQMessageListener(
        topic = "sync-topic",
        consumerGroup = "sync-consumer")
public class syncKeyListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        System.out.println("sync-topic: " + new String(message.getBody()));
        // key
        System.out.println("sync-topic-key: " + message.getKeys());
    }
}
