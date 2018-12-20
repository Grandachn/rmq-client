package com.grandachn.rocketmq.rmqclient.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @Author by guanda
 * @Date 2018/12/20 11:28
 */
@Target({ ElementType.METHOD })
@Retention(RetentionPolicy.RUNTIME)
public @interface InputConsumer {
    String propertiesFile() default "rocketmq.properties";
    String topic() default "";
}
