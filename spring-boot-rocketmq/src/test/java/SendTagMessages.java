import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.List;

/**
 * @author Carl
 * @date 2023/8/21
 **/
public class SendTagMessages {

    @Test
    public void p1() throws Exception {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("tag-producer-group");
        defaultMQProducer.setNamesrvAddr("47.95.115.74:9876");
        defaultMQProducer.start();
        Message message = new Message("tagTopic", "tagA", "我是tagA消息".getBytes());
        defaultMQProducer.send(message);
        defaultMQProducer.shutdown();
    }
    @Test
    public void p2() throws Exception {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("tag-producer-group");
        defaultMQProducer.setNamesrvAddr("47.95.115.74:9876");
        defaultMQProducer.start();
        Message message = new Message("tagTopic", "tagB", "我是tagB消息".getBytes());
        defaultMQProducer.send(message);
        defaultMQProducer.shutdown();
    }

    @Test
    public void c1() throws Exception {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("tag-consumer-group");
        defaultMQPushConsumer.setNamesrvAddr("47.95.115.74:9876");
        defaultMQPushConsumer.subscribe("tagTopic", "tagB || tagA");
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {

                    for (MessageExt msg : msgs) {
                        System.out.println("消费者1消费消息："+new String(msg.getBody()));
                    }
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        defaultMQPushConsumer.start();
        System.in.read();
    }
}
