package online.k12code.consumer.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.annotation.SelectorType;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

/**
 * @author Carl
 * @date 2023/8/22
 **/
@Component
@RocketMQMessageListener(
        topic = "syncTag-topic",
        consumerGroup = "syncTag-consumer",
        selectorType = SelectorType.TAG, // 消息过滤类型
        selectorExpression = "tag1 || tag2" // 消息过滤

)
public class syncTagListener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        System.out.println("syncTag-topic: " + new String(message.getBody()));
    }
}
