package com.grandachn.rocketmq.rmqclient.core;

import com.grandachn.rocketmq.rmqclient.annotation.*;
import com.grandachn.rocketmq.rmqclient.bean.RmqHandlerMeta;
import com.grandachn.rocketmq.rmqclient.client.RmqConsumer;
import com.grandachn.rocketmq.rmqclient.client.RmqProducer;
import com.grandachn.rocketmq.rmqclient.client.RmqTransactionProducer;
import com.grandachn.rocketmq.rmqclient.serialize.ISerialize;
import com.grandachn.rocketmq.rmqclient.spi.SpiContainer;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author by guanda
 * @Date 2018/12/20 11:46
 */
public class RmqClientContext implements ApplicationContextAware {
    private static Logger LOG = LoggerFactory.getLogger(RmqClientContext.class);

    private static String packageName;

    private static ApplicationContext context;

    private Map<Method, RmqProducer> producers = new HashMap<>();

    private Map<Method, RmqTransactionProducer> transactionProducers = new HashMap<>();

    private Map<Method, RmqHandlerMeta> producersMeta = new HashMap<>();

    private Map<Method, RmqHandlerMeta> consumerMeta = new HashMap<>();

    private String serializeType = "fastjson";

    private ISerialize serialize;

    public RmqClientContext(String packageName){
        RmqClientContext.packageName = packageName;
    }

    @PostConstruct
    public void init() {
        initMeta();
        initSerialize();
        producersMeta.forEach(((method, rmqHandlerMeta) -> createConsumerOrProducer(rmqHandlerMeta)));
        consumerMeta.forEach(((method, rmqHandlerMeta) -> createConsumerOrProducer(rmqHandlerMeta)));
    }

    private void initSerialize() {
        this.serialize = (ISerialize) SpiContainer.getBean(serializeType);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    private void createConsumerOrProducer(RmqHandlerMeta rmqHandlerMeta) {
        if (rmqHandlerMeta.getOutputMessage() != null){
            RmqProducer rmqProducer = new RmqProducer(rmqHandlerMeta);
            producers.put(rmqHandlerMeta.getMethod(), rmqProducer);
        }

        if (rmqHandlerMeta.getInputMessage() != null){
            new RmqConsumer(rmqHandlerMeta);
        }

        if (rmqHandlerMeta.getTransactionMethod() != null){
            RmqTransactionProducer transactionProducer = new RmqTransactionProducer(rmqHandlerMeta);
            transactionProducers.put(rmqHandlerMeta.getMethod(), transactionProducer);
        }
    }

    private void initMeta() {
        Reflections reflections = new Reflections(packageName);
        reflections.getTypesAnnotatedWith(RocketMq.class)
                .forEach(clazz -> Arrays.stream(clazz.getMethods())
                        .forEach(method -> Arrays.stream(method.getAnnotations())
                                .forEach(annotation -> {
                                    RmqHandlerMeta rmqHandlerMeta = new RmqHandlerMeta();
                                    rmqHandlerMeta.setMethod(method);
                                    rmqHandlerMeta.setBean(context.getBean(clazz));
                                    rmqHandlerMeta.setBeanClass(clazz);
                                    if (annotation instanceof InputMessage){
                                        rmqHandlerMeta.setInputMessage((InputMessage) annotation);
                                        if (method.getParameterTypes().length != 1){
                                            throw new RuntimeException("method with @InputMessage should have and only have one parameter");
                                        }
                                        rmqHandlerMeta.setParameterType(method.getParameterTypes()[0]);
                                        consumerMeta.put(method, rmqHandlerMeta);
                                    } else if (annotation instanceof OutputMessage){
                                        rmqHandlerMeta.setOutputMessage((OutputMessage) annotation);
                                        rmqHandlerMeta.setReturnType(method.getReturnType());
                                        producersMeta.put(method, rmqHandlerMeta);
                                    } else if (annotation instanceof TransactionMethod){
                                        rmqHandlerMeta.setTransactionMethod((TransactionMethod) annotation);
                                        rmqHandlerMeta.setReturnType(method.getReturnType());
                                        Arrays.asList(method.getParameters()).forEach(param -> {
                                            if (param.isAnnotationPresent(TransactionMessage.class)){
                                                rmqHandlerMeta.setTransactionMessageClass(param.getType());
                                            }
                                        });
                                        producersMeta.put(method, rmqHandlerMeta);
                                    }
                                })
                        )
                );
    }

    public Map<Method, RmqProducer> getProducers() {
        return producers;
    }

    public void setProducers(Map<Method, RmqProducer> producers) {
        this.producers = producers;
    }

    public Map<Method, RmqHandlerMeta> getProducersMeta() {
        return producersMeta;
    }

    public void setProducersMeta(Map<Method, RmqHandlerMeta> producersMeta) {
        this.producersMeta = producersMeta;
    }

    public Map<Method, RmqHandlerMeta> getConsumerMeta() {
        return consumerMeta;
    }

    public void setConsumerMeta(Map<Method, RmqHandlerMeta> consumerMeta) {
        this.consumerMeta = consumerMeta;
    }

    public String getSerializeType() {
        return serializeType;
    }

    public void setSerializeType(String serializeType) {
        this.serializeType = serializeType;
    }

    public ISerialize getSerialize() {
        return serialize;
    }

    public void setSerialize(ISerialize serialize) {
        this.serialize = serialize;
    }

    public Map<Method, RmqTransactionProducer> getTransactionProducers() {
        return transactionProducers;
    }

    public void setTransactionProducers(Map<Method, RmqTransactionProducer> transactionProducers) {
        this.transactionProducers = transactionProducers;
    }
}
