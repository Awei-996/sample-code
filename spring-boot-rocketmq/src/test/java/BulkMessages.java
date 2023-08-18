import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Carl
 * @date 2023/8/18
 **/
public class BulkMessages {

    @Test
    public void p1() throws Exception{
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("batch-producer-group");
        defaultMQProducer.setNamesrvAddr("47.95.115.74:9876");
        defaultMQProducer.start();
        List<Message> messages = Arrays.asList(
                new Message("batchTopic", "我是一组消息的A消息".getBytes()),
                new Message("batchTopic", "我是一组消息的B消息".getBytes()),
                new Message("batchTopic", "我是一组消息的C消息".getBytes())
        );

        defaultMQProducer.send(messages);
        defaultMQProducer.shutdown();

    }

    @Test
    public void c1() throws Exception {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("batch-consumer-group");
        defaultMQPushConsumer.subscribe("batchTopic", "*");
        defaultMQPushConsumer.setNamesrvAddr("47.95.115.74:9876");
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                for (MessageExt messageExt : list) {
                    System.out.println(new String(messageExt.getBody()));
                }
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        defaultMQPushConsumer.start();
        System.in.read();
    }

}
