import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.util.List;

/**
 * @author Carl
 * @date 2023/8/17
 **/
public class NormalMessages {

    @Test
    public void p1() throws Exception {
        // 创建一个生产者 （制定一个组名）
        DefaultMQProducer producer = new DefaultMQProducer("test-producer-group");
        // 连接namesrv
        producer.setNamesrvAddr("47.95.115.74:9876");
        // 启动
        producer.start();
        // 创建消息 (主题,消息)
        Message testTopic = new Message("testTopic", "一条新消息24823".getBytes());
        // 发送结果
        SendResult sendResult = producer.send(testTopic);
        System.err.println("sendResult,"+sendResult);
        // 关闭
        producer.shutdown();

    }

    @Test
    public void c1() throws Exception{
        //创建一个消费者
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer("test-consumer-group");
        //连接namesrv
        consumer.setNamesrvAddr("47.95.115.74:9876");
        //订阅一个主题 * 标识订阅这个主题中所有消息，后期会有消息过滤
        consumer.subscribe("testTopic", "*");
        //设置一个监听器
        consumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> list, ConsumeConcurrentlyContext consumeConcurrentlyContext) {
                //这个就是消费的方法（业务处理）
                System.out.println("我是消费者");
                System.out.println(list.get(0).toString());
                System.out.println("消息内容：" + new String(list.get(0).getBody()));
                System.out.println("消费上下文" + consumeConcurrentlyContext);
                //返回值 CONSUME_SUCCESS 成功，消息会从mq出队
                // RECONSUME_LATER(报错/null) 失败，消息会重新回到队列，过一会重新投递出来，给当前消费者或者其他消费者消费
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        //启动
        consumer.start();
        //TimeUnit.SECONDS.sleep(100);
        //挂起当前jvm
        System.in.read();

    }
}
