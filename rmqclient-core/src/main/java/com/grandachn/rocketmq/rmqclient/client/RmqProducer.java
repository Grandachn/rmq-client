package com.grandachn.rocketmq.rmqclient.client;

import com.alibaba.fastjson.JSON;
import com.grandachn.rocketmq.rmqclient.bean.ProducerConfig;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @Author by guanda
 * @Date 2018/12/20 11:52
 */
public class RmqProducer {
    private static Logger LOG = LoggerFactory.getLogger(RmqProducer.class);

    private DefaultMQProducer producer;
    private String propertiesFile;
    private Properties properties;
    private String topic;

    public RmqProducer(){

    }

    public RmqProducer(String propertiesFile, String topic){
        this.propertiesFile = propertiesFile;
        this.topic = topic;
        init();
    }

    public RmqProducer(Properties properties, String topic){
        this.properties = properties;
        this.topic = topic;
        init();
    }

    private void init() {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(propertiesFile));
            } catch (IOException e) {
                LOG.error("The properties file is not loaded.", e);
                throw new IllegalArgumentException(
                        "The properties file is not loaded.", e);
            }
        }
        LOG.info("Producer properties:" + properties);
        ProducerConfig config = new ProducerConfig(properties);

        DefaultMQProducer producer = new DefaultMQProducer(config.getProducerGroup());
        producer.setNamesrvAddr(config.getNamesrvAddr());
        try {
            producer.start();
        } catch (MQClientException e) {
            LOG.error("The DefaultMQProducer start fail.", e);
        }
    }

    public SendResult sendBeanToTopic(String topicName, String tags, Object bean){
        SendResult sendResult = null;
        try {
            Message msg = new Message(topicName , tags,  JSON.toJSONString(bean).getBytes(RemotingHelper.DEFAULT_CHARSET));
            sendResult = producer.send(msg);
        } catch (UnsupportedEncodingException e) {
            LOG.error("message bean to json fail.", e);
        } catch (Exception e) {
            LOG.error("send message error.", e);
        }

        return sendResult;
    }

    public void close() {
        producer.shutdown();
    }
}
