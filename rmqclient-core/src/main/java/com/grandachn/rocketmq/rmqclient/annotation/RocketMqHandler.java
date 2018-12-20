package com.grandachn.rocketmq.rmqclient.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.*;

/**
 * @Author by guanda
 * @Date 2018/12/20 11:32
 */
@Target({ ElementType.TYPE })
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RocketMqHandler {
}
