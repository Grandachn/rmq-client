package com.example.demo;

import com.grandachn.rocketmq.rmqclient.annotation.*;

/**
 * @Author by guanda
 * @Date 2018/12/20 17:25
 */
@RocketMq
public class MessageHandler {

    @TransactionMethod(topic = "TestHandler", tags = "Dog1", producerGroup = "Dog")
    public String createDog(String t, @TransactionMessage Dog dog, String s) throws InterruptedException {
        System.out.println("doing create dog:" + s);
        return "tranaction";
    }

    @InputMessage(topic = "TestHandler", subExpression = "Dog1", consumerGroup = "receiveDog")
    @TransactionMethod(topic = "TestHandler", tags = "Dog2", producerGroup = "Dog22")
    public void receiveMessage(@TransactionMessage Dog message){
        System.out.println("[dog reveive]:" + message);
    }

    @InputMessage(topic = "TestHandler", subExpression = "Dog2", consumerGroup = "receiveDog2")
    @OutputMessage(topic = "TestHandler", tags = "TagA", producerGroup = "createMessage")
    public String receiveMessage2(Dog message){
        System.out.println("[dog2 reveive]:" + message);
        return  message.getName();
    }

    @InputMessage(topic = "TestHandler", subExpression = "TagA", consumerGroup = "receiveDog3")
    public void receiveMessage3(String message){
        System.out.println("[message reveive]:" + message);
    }

}
