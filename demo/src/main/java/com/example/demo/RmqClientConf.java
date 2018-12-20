package com.example.demo;

import com.grandachn.rocketmq.rmqclient.aspect.ProducerAspect;
import com.grandachn.rocketmq.rmqclient.core.RmqClientContext;
import com.grandachn.rocketmq.rmqclient.util.SpringContextUtils;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Author by guanda
 * @Date 2018/12/20 17:37
 */
@Configuration
public class RmqClientConf {

    @Bean
    public SpringContextUtils getSpringContextUtils(){
        return new SpringContextUtils();
    }

    @Bean
    public RmqClientContext getRmqClientContext(){
        return new RmqClientContext();
    }

//    @Bean
//    public ProducerAspect getProducerAspect(){
//        return new ProducerAspect();
//    }
}
