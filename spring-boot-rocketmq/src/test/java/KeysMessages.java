import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.List;
import java.util.UUID;

/**
 * @author Carl
 * @date 2023/8/21
 **/
public class KeysMessages {

    @Test
    public void p1() throws Exception {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("keys-producer-group");
        defaultMQProducer.setNamesrvAddr("47.95.115.74:9876");
        defaultMQProducer.start();
        String s = UUID.randomUUID().toString();
        Message message = new Message("keysTopic", "tagA", s, "我是tagA消息".getBytes());
        defaultMQProducer.send(message);
        defaultMQProducer.shutdown();
    }

    @Test
    public void c1() throws Exception {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("keys-consumer-group");
        defaultMQPushConsumer.setNamesrvAddr("47.95.115.74:9876");
        defaultMQPushConsumer.subscribe("keysTopic", "*");
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                    for (MessageExt msg : msgs) {
                        System.out.println("消费者1消费消息："+new String(msg.getBody()));
                        System.out.println("消费者1消费消息业务标识："+msg.getKeys());
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        defaultMQPushConsumer.start();
        System.in.read();
    }
}
