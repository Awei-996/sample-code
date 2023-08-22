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
@RocketMQMessageListener(topic = "oneway-topic", consumerGroup = "oneway-topic-consumer")
public class OnewaySendListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        System.out.println("oneway-topic: " + new String(message.getBody()));
    }
}
