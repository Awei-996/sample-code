import org.apache.rocketmq.client.producer.DefaultMQProducer;

import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

/**
 * @author Carl
 * @date 2023/8/18
 **/
public class AsynchronousMessages {


    @Test // rocketmq异步消息
    public void p1() throws Exception {
        DefaultMQProducer defaultMQProducer = new DefaultMQProducer("async-producer-group");
        defaultMQProducer.setNamesrvAddr("47.95.115.74:9876");
        defaultMQProducer.start();
        Message message = new Message("async-topic", "async-tag", "async-key", "async-value".getBytes());
        defaultMQProducer.send(message, new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                System.out.println("发送成功");
            }

            @Override
            public void onException(Throwable throwable) {
                System.out.println("发送失败" + throwable.getMessage());
            }
        });
        System.out.println("我先执行");
//        System.in.read();
        defaultMQProducer.shutdown();
    }
}
