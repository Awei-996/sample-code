import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeOrderlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerOrderly;
import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.MessageQueueSelector;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.common.message.MessageQueue;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

/**
 * @author Carl
 * @date 2023/8/18
 **/
public class SequentialMessages {

    private List<MsgModel> msgModels = Arrays.asList(
            new MsgModel("1", 1, "下单"),
            new MsgModel("1", 1, "短信"),
            new MsgModel("1", 1, "物流"),
            new MsgModel("2", 2, "下单"),
            new MsgModel("2", 2, "短信"),
            new MsgModel("2", 2, "物流")
    );

    @Test
    public void p1() throws  Exception{
        DefaultMQProducer producer_group = new DefaultMQProducer("sequential_messages_producer_group");
        producer_group.setNamesrvAddr("47.95.115.74:9876");
        producer_group.start();

        msgModels.stream().forEach(msgModel -> {
            Message message = new Message("orderlyTopic", msgModel.toString().getBytes());
            try {
                // 发送消息
                producer_group.send(message, new MessageQueueSelector() {
                    @Override
                    public MessageQueue select(List<MessageQueue> list, Message message, Object o) {
                       int i = o.toString().hashCode() % list.size();
                        return list.get(i);
                    }
                }, msgModel.getOrderSn());
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        });

    }


    @Test
    public void c1() throws Exception{
        DefaultMQPushConsumer sequential_messages_consumer_group = new DefaultMQPushConsumer("sequential_messages_consumer_group");
        sequential_messages_consumer_group.setNamesrvAddr("47.95.115.74:9876");
        sequential_messages_consumer_group.subscribe("orderlyTopic", "*");
        sequential_messages_consumer_group.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> msgs, ConsumeOrderlyContext context) {
                System.out.println("线程id：" + Thread.currentThread().getId());
                System.out.println(new String(msgs.get(0).getBody()));
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        sequential_messages_consumer_group.start();
        System.in.read();
    }
}
