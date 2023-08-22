package online.k12code.producer.send;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.spring.core.RocketMQTemplate;
import org.apache.rocketmq.spring.support.RocketMQHeaders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.Message;
import org.springframework.messaging.support.MessageBuilder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * 消息测试
 * @author Carl
 * @date 2023/8/22
 **/
@RestController
public class SendController {

    @Resource
    private  RocketMQTemplate rocketMQTemplate;

    @GetMapping("/syncSend")
    @ApiOperation(value = "同步发送消息")
    public void syncSend() {
        // 同步消息
        SendResult sendResult = rocketMQTemplate.syncSend("sync-topic", "Hello, World!");
        // 输出发送结果
        System.out.printf("syncSend1 to topic %s sendResult=%s %n", "sync-topic", sendResult);
    }

    @GetMapping("/asyncSend")
    @ApiOperation(value = "异步发送消息")
    public void asyncSend() {
        // 异步消息
        rocketMQTemplate.asyncSend("async-topic", "Hello, World!", new SendCallback() {
            @Override
            public void onSuccess(SendResult sendResult) {
                // 成功处理
                System.out.printf("asyncSend1 to topic %s sendResult=%s %n", "async-topic", sendResult);
            }

            @Override
            public void onException(Throwable e) {
                // 失败处理
                e.printStackTrace();
            }
        });
        // 执行其他操作
        System.err.println("asyncSend1 to topic async-topic");
    }

    @GetMapping("/onewaySend")
    @ApiOperation(value = "单向发送消息")
    public void onewaySend() {
        // 单向消息
        rocketMQTemplate.sendOneWay("oneway-topic", "oneway Hello, World!");
        // 执行其他操作
        System.err.println("onewaySend1 to topic oneway-topic");
    }

    @GetMapping("/syncSendOrderly")
    @ApiOperation(value = "顺序发送消息")
    public void syncSendOrderly() {
        // 顺序消息
        for (int i = 0; i < 10; i++) {
            SendResult sendResult = rocketMQTemplate.syncSendOrderly("orderly-topic", "orderly Hello, World!", String.valueOf(i));
            // 输出发送结果
            System.out.printf("syncSendOrderly1 to topic %s sendResult=%s %n", "orderly-topic", sendResult);
        }
    }

    @GetMapping("/syncSendDelay")
    @ApiOperation(value = "延时发送消息")
    public void syncSendDelay() {
        // 延时消息
        Message<String> build = MessageBuilder.withPayload("delay Hello, World!").build();
        SendResult sendResult = rocketMQTemplate.syncSend("delay-topic", build, 1000, 3);
        // 输出发送结果
        System.out.printf("syncSendDelay1 to topic %s sendResult=%s %n", "delay-topic", sendResult);
    }

    @GetMapping("/syncSendTag")
    @ApiOperation(value = "同步发送消息代标签")
    public void syncSendTag() {
        // 同步消息
        SendResult sendResult = rocketMQTemplate.syncSend("sync-topic:tag1", "Hello, World!");
        // 输出发送结果
        System.out.printf("syncSend1 to topic %s sendResult=%s %n", "sync-topic:tag1", sendResult);
    }

    @GetMapping("/syncSendSQL92")
    @ApiOperation(value = "同步发送消息代SQL92")
    public void syncSendSQL92() {
        // 同步消息
        SendResult sendResult = rocketMQTemplate.syncSend("sync-topic:10", "Hello, World!");
        // 输出发送结果
        System.out.printf("syncSend1 to topic %s sendResult=%s %n", "sync-topic:SQL92", sendResult);
    }

    @GetMapping("/syncSendKey")
    @ApiOperation(value = "同步发送消息代Key")
    public void syncSendKey() {
        // 同步消息
        Message<String> message = MessageBuilder.withPayload("key Hello, World!")
                .setHeader(RocketMQHeaders.KEYS, "key")
                .build();
        SendResult sendResult = rocketMQTemplate.syncSend("sync-topic", message);
        // 输出发送结果
        System.out.printf("syncSend1 to topic %s sendResult=%s %n", "sync-topic", sendResult);
    }

}
