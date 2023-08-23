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
        topic = "syncSQL92-topic",
        consumerGroup = "syncSQL92-consumer",
        selectorType = SelectorType.SQL92, // 消息过滤类型 需要在broker.conf中添加 enablePropertyFilter=true,
        selectorExpression = "a > 3" // 消息过滤
)
public class syncSQL92Listener implements RocketMQListener<MessageExt> {
    @Override
    public void onMessage(MessageExt message) {
        System.out.println("syncSQL92-topic: " + new String(message.getBody()));
    }
}
