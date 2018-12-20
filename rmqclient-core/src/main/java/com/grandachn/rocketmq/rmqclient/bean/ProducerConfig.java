package com.grandachn.rocketmq.rmqclient.bean;

import java.util.Properties;

/**
 * @Author by guanda
 * @Date 2018/12/20 13:41
 */
public class ProducerConfig {
    private String producerGroup;
    private String namesrvAddr;

    public ProducerConfig(Properties properties){
        this.producerGroup = properties.getProperty("producerGroup");
        this.namesrvAddr = properties.getProperty("namesrvAddr");
    }

    public String getProducerGroup() {
        return producerGroup;
    }

    public void setProducerGroup(String producerGroup) {
        this.producerGroup = producerGroup;
    }

    public String getNamesrvAddr() {
        return namesrvAddr;
    }

    public void setNamesrvAddr(String namesrvAddr) {
        this.namesrvAddr = namesrvAddr;
    }
}
