package com.example.demo;

import com.grandachn.rocketmq.rmqclient.annotation.InputMessage;
import com.grandachn.rocketmq.rmqclient.annotation.OutputMessage;
import com.grandachn.rocketmq.rmqclient.annotation.OutputTransactionMessage;
import com.grandachn.rocketmq.rmqclient.annotation.RocketMq;

/**
 * @Author by guanda
 * @Date 2018/12/20 17:25
 */
@RocketMq
public class MessageHandler {

    @OutputTransactionMessage(topic = "TestHandler", tags = "Dog", producerGroup = "Dog")
    public String createDog() throws InterruptedException {
//        for(int i = 0 ; i < 60 ; i++){
//            Thread.sleep(1000);
//            System.out.println(i);
//        }
//
        return "tranaction";
    }

    @InputMessage(topic = "TestHandler", subExpression = "Dog", consumerGroup = "receiveDog")
//    @InputMessage(topic = "TestHandler", subExpression = "TagA || TagB", consumerGroup = "receiveMessage0")
    public void receiveMessage(String message){
        System.out.println("[name reveive]:" + message);
    }


    @OutputMessage(topic = "TestHandler", tags = "TagA", producerGroup = "createMessage")
//    @InputMessage(topic = "TestHandler", subExpression = "Dog", consumerGroup = "receiveDog")
    public String receiveDog(Dog dog){
        System.out.println("[dog reveive]:" + dog);
        return dog.getName();
    }


}
