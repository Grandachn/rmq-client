package com.example.demo;

import com.grandachn.rocketmq.rmqclient.annotation.InputConsumer;
import com.grandachn.rocketmq.rmqclient.annotation.OutputProducer;
import com.grandachn.rocketmq.rmqclient.annotation.RocketMqHandler;
import org.springframework.stereotype.Component;

import java.lang.annotation.Annotation;

/**
 * @Author by guanda
 * @Date 2018/12/20 17:25
 */
@RocketMqHandler
public class MessageHandler {

    @OutputProducer(topic = "TopicTest")
    public String createMessage(){
        System.out.println("test ------");
        for (Annotation annotation: MessageHandler.class.getAnnotations()){
            System.out.println(MessageHandler.class.getName() + ":" +annotation);
        }
        return "this is from RocketMqHandler";
    }
}
