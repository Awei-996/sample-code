package online.k12code.consumer.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.ConsumeMode;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Carl
 * @date 2023/8/22
 **/
@Component
@RocketMQMessageListener(
        topic = "syncSendOrderly-topic",
        consumerGroup = "syncSendOrderly-consumer",
        consumeMode = ConsumeMode.ORDERLY, // 消费模式-顺序消费
        maxReconsumeTimes = 3 // 最大重试次数
)
public class syncSendOrderlyListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        System.out.println("syncSendOrderly-topic: " + new String(message.getBody()));
    }
}
