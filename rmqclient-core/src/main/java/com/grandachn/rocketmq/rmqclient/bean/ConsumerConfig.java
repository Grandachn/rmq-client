package com.grandachn.rocketmq.rmqclient.bean;

import java.util.Properties;

/**
 * @Author by guanda
 * @Date 2018/12/21 11:50
 */
public class ConsumerConfig {
    private String consumerGroup;
    private String namesrvAddr;

    public ConsumerConfig(Properties properties){
        this.consumerGroup = properties.getProperty("consumerGroup");
        this.namesrvAddr = properties.getProperty("namesrvAddr");
    }

    public String getConsumerGroup() {
        return consumerGroup;
    }

    public void setConsumerGroup(String consumerGroup) {
        this.consumerGroup = consumerGroup;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }
}
