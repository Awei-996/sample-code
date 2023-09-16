package online.k12code.consumer.listener;

import org.apache.rocketmq.common.message.MessageExt;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.springframework.stereotype.Component;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author Carl
 * @date 2023/8/22
 **/
@Component
@RocketMQMessageListener(
        topic = "oneway-topic",
        consumerGroup = "oneway-topic-consumer",
        consumeThreadNumber = 20
)
public class OnewaySendListener implements RocketMQListener<MessageExt> {

    @Override
    public void onMessage(MessageExt message) {
        ThreadPoolExecutor threadPoolExecutor = new ThreadPoolExecutor(
                10, // 核心线程数
                Runtime.getRuntime().availableProcessors() * 2, // 最大线程
                10,
                TimeUnit.SECONDS,
            new ArrayBlockingQueue<>(20)
        );


        System.out.println("oneway-topic: " + new String(message.getBody()));
    }
}
