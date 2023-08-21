import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.junit.Test;

import java.sql.*;
import java.util.List;
import java.util.UUID;

/**
 * @author Carl
 * @date 2023/8/21
 **/
public class repeatMessages {

    @Test
    public void p1() throws Exception {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("repeat-producer-group");
        defaultMQProducer.setNamesrvAddr("47.95.115.74:9876");
        defaultMQProducer.start();
        String s = UUID.randomUUID().toString();
        Message m1 = new Message("repeatTopic", null, s, "一条新消息".getBytes());
        Message m2 = new Message("repeatTopic", null, s, "一条新消息".getBytes());
        defaultMQProducer.send(m1);
        defaultMQProducer.send(m2);
        defaultMQProducer.shutdown();
    }

    @Test
    public void c2() throws Exception {
        DefaultMQPushConsumer defaultMQPushConsumer = new DefaultMQPushConsumer("repeat-consumer-group");
        defaultMQPushConsumer.setNamesrvAddr("47.95.115.74:9876");
        defaultMQPushConsumer.subscribe("repeatTopic", "*");
        defaultMQPushConsumer.registerMessageListener(new MessageListenerConcurrently() {
            @Override
            public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
                // 先拿key
                MessageExt messageExt = msgs.get(0);
                String keys = messageExt.getKeys();
                // 原生方式操作
                Connection connection = null;
                try {
                    connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/test?serverTimezone=GMT%2B8&useSSL=false", "root", "123456");
                } catch (SQLException e) {
                    e.printStackTrace();
                }
                PreparedStatement statement = null;

                try {
                    // 插入数据库 因为我们 key做了唯一索引
                    statement = connection.prepareStatement("insert into order_log(`type`, `order_sn`, `user`) values (1,'" + keys + "','123')");
                } catch (SQLException e) {
                    e.printStackTrace();
                }

                try {
                    // 新增 要么成功 要么报错   修改 要么成功,要么返回0 要么报错
                    statement.executeUpdate();
                } catch (SQLException e) {
                    System.out.println("executeUpdate");
                    if (e instanceof SQLIntegrityConstraintViolationException) {
                        // 唯一索引冲突异常
                        // 说明消息来过了
                        System.out.println("该消息来过了");
                        return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                    }
                    e.printStackTrace();
                }

                // 处理业务逻辑
                // 如果业务报错 则删除掉这个去重表记录 delete order_log where order_sn = keys;
                System.out.println(new String(messageExt.getBody()));
                System.out.println(keys);
                return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
            }
        });
        defaultMQPushConsumer.start();
        System.in.read();
    }
    }
