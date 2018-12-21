package com.grandachn.rocketmq.rmqclient.client;

import com.grandachn.rocketmq.rmqclient.bean.ConsumerConfig;
import com.grandachn.rocketmq.rmqclient.bean.RmqHandlerMeta;
import com.grandachn.rocketmq.rmqclient.handler.BeanMessageHandler;
import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Properties;

/**
 * @Author by guanda
 * @Date 2018/12/20 15:07
 */
public class RmqConsumer {
    private static Logger LOG = LoggerFactory.getLogger(RmqConsumer.class);

    private DefaultMQPushConsumer consumer;
    private String propertiesFile;
    private Properties properties;
    private Object bean;

    private String topic;
    private String subExpression;
    private RmqHandlerMeta rmqHandlerMeta;

    public RmqConsumer(RmqHandlerMeta rmqHandlerMeta){
        this.propertiesFile = rmqHandlerMeta.getInputMessage().propertiesFile();
        this.subExpression = rmqHandlerMeta.getInputMessage().subExpression();
        this.topic = rmqHandlerMeta.getInputMessage().topic();
        this.bean = rmqHandlerMeta.getBean();
        this.rmqHandlerMeta = rmqHandlerMeta;
        init();
    }

    public void init() {
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
        ConsumerConfig config = new ConsumerConfig(properties);
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(this.rmqHandlerMeta.getInputMessage().consumerGroup());
        consumer.setNamesrvAddr(config.getNamesrvAddr());

        BeanMessageHandler beanMessageHandler = new BeanMessageHandler(this.bean, this.rmqHandlerMeta.getMethod());


        consumer.registerMessageListener((MessageListenerConcurrently) (msgs, context) -> {
            for(MessageExt msg : msgs){
                try {
                    beanMessageHandler.execute(msg.getBody(), this.rmqHandlerMeta.getParameterType());
                } catch (UnsupportedEncodingException e) {
                    LOG.error("message to String Exception.", e);
                } catch (Exception e) {
                    return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
                }
            }
            return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
        });

        try {
            consumer.subscribe(this.topic, this.subExpression);
            consumer.start();
            this.consumer = consumer;
            LOG.info("consumer is start, method:{}", this.rmqHandlerMeta.getMethod());
        } catch (MQClientException e) {
            LOG.error("The DefaultMQProducer start fail.", e);
        }
        initGracefullyShutdown();
    }

    private void initGracefullyShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownGracefully));
    }

    private void shutdownGracefully() {
        if (consumer != null) {
            consumer.shutdown();
        }
    }
}
