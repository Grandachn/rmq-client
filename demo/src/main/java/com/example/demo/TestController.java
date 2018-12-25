package com.example.demo;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;


/**
 * @Author by guanda
 * @Date 2018/12/20 17:21
 */
@RestController
public class TestController {

    @Autowired
    MessageHandler messageHandler;


    @GetMapping("/test")
    public String test() throws InterruptedException {
        Dog dog = new Dog();
        dog.setName("kit");
        dog.setType("hashiki");
        messageHandler.createDog("t", dog, "haha");
        return "hello";
    }
}
