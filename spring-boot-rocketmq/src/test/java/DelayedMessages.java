import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

import java.util.Date;

/**
 * @author Carl
 * @date 2023/8/18
 **/
public class DelayedMessages {

    @Test
    public void msProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("ms-producer-group");
        producer.setNamesrvAddr("47.95.115.74:9876");
        producer.start();

        Message message = new Message("orderMsTopic", "订单消息".getBytes());
        //设置延迟等级
        //messageDelayLevel = "1s 5s 10s 30s 1m 2m 3m 4m 5m 6m 7m 8m 9m 10m 20m 30m 1h 2h"
        message.setDelayTimeLevel(3);//10s
        producer.send(message);
        System.out.println("发送事件：" + new Date());
        producer.shutdown();
    }
}
