import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.junit.Test;

/**
 * @author Carl
 * @date 2023/8/18
 **/
public class SendOnewayMessages {

    @Test
    public void onewayProducer() throws Exception {
        DefaultMQProducer producer = new DefaultMQProducer("oneway-producer-group");
        producer.setNamesrvAddr("47.95.115.74:9876");
        producer.start();
        Message message = new Message("onewayTopic", "这是一条单向消息".getBytes());
        for (int i = 0; i <10; i++) {
            producer.sendOneway(message);
        }
        System.out.println("成功");
        producer.shutdown();
    }
}
