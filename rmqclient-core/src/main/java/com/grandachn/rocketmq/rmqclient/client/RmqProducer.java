package com.grandachn.rocketmq.rmqclient.client;

import com.grandachn.rocketmq.rmqclient.bean.ProducerConfig;
import com.grandachn.rocketmq.rmqclient.bean.RmqHandlerMeta;
import com.grandachn.rocketmq.rmqclient.core.RmqClientContext;
import com.grandachn.rocketmq.rmqclient.util.SpringContextUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
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

    private RmqHandlerMeta rmqHandlerMeta;

    private RmqClientContext rmqClientContext;

    public RmqProducer(RmqHandlerMeta rmqHandlerMeta){
        this.rmqHandlerMeta = rmqHandlerMeta;
        this.propertiesFile = rmqHandlerMeta.getOutputMessage().propertiesFile();
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

        DefaultMQProducer producer = new DefaultMQProducer(rmqHandlerMeta.getOutputMessage().producerGroup());
        producer.setNamesrvAddr(config.getNamesrvAddr());
        try {
            producer.start();
            this.producer = producer;
        } catch (MQClientException e) {
            LOG.error("The DefaultMQProducer start fail.", e);
        }

        this.rmqClientContext = (RmqClientContext) SpringContextUtils.getBeanByClass(RmqClientContext.class);
    }

    public SendResult sendBeanToTopic(String topicName, String tags, Object bean){
        SendResult sendResult = null;
        try {
            byte[] bytes = rmqClientContext.getSerialize().serialize(bean, rmqHandlerMeta.getReturnType());
            Message msg = new Message(topicName , tags,  bytes);
            sendResult = producer.send(msg);
        } catch (Exception e) {
            LOG.error("send message error.", e);
        }

        return sendResult;
    }

    public void close() {
        producer.shutdown();
    }
}
