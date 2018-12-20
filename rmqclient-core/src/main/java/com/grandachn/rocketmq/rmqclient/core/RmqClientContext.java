package com.grandachn.rocketmq.rmqclient.core;

import com.grandachn.rocketmq.rmqclient.annotation.InputConsumer;
import com.grandachn.rocketmq.rmqclient.annotation.OutputProducer;
import com.grandachn.rocketmq.rmqclient.annotation.RocketMqHandler;
import com.grandachn.rocketmq.rmqclient.bean.RmqHandlerMeta;
import com.grandachn.rocketmq.rmqclient.client.RmqProducer;
import com.grandachn.rocketmq.rmqclient.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import javax.annotation.PostConstruct;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @Author by guanda
 * @Date 2018/12/20 11:46
 */
public class RmqClientContext implements ApplicationContextAware {
    private static Logger LOG = LoggerFactory.getLogger(RmqClientContext.class);

    private static ApplicationContext context;

    private List<RmqHandlerMeta> meta = new ArrayList<>();

    private Map<Method, RmqProducer> producers = new HashMap<>();

    private Map<Method, RmqHandlerMeta> producersMeta = new HashMap<>();

    @PostConstruct
    public void init() {
        this.meta = getRmqHandlerMeta();
        if (meta.size() == 0){
            throw new IllegalArgumentException("No handler method is declared in this spring context.");
        }
        meta.forEach(this::createConsumerOrProducer);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    private void createConsumerOrProducer(RmqHandlerMeta rmqHandlerMeta) {
        if (rmqHandlerMeta.getOutputProducer() != null){
            RmqProducer rmqProducer = new RmqProducer(rmqHandlerMeta.getOutputProducer().propertiesFile(), rmqHandlerMeta.getOutputProducer().topic());
            producers.put(rmqHandlerMeta.getMethod(), rmqProducer);
        }
    }

    private List<RmqHandlerMeta> getRmqHandlerMeta() {
        List<RmqHandlerMeta> metaList = new ArrayList<>();
        System.out.println(context.getBeanNamesForAnnotation(RocketMqHandler.class)[0]);
        System.out.println(context.getBean(context.getBeanNamesForAnnotation(RocketMqHandler.class)[0]));
        Arrays.stream(context.getBeanNamesForAnnotation(RocketMqHandler.class))
                .map(context::getBean)
                .forEach(bean -> getAnnotationMethodFromClass(bean.getClass()).forEach((method, annotation) -> {
                    RmqHandlerMeta rmqHandlerMeta = new RmqHandlerMeta();
                    rmqHandlerMeta.setBean(bean);
                    rmqHandlerMeta.setMethod(method);
                    if (annotation instanceof InputConsumer){
                        rmqHandlerMeta.setInputConsumer((InputConsumer) annotation);
                        if (method.getParameterTypes().length != 1){
                            throw new RuntimeException("method with @InputConsumer should have and only have one parameter");
                        }
                        rmqHandlerMeta.setParameterType(method.getParameterTypes()[0]);
                    }else if (annotation instanceof OutputProducer){
                        rmqHandlerMeta.setOutputProducer((OutputProducer) annotation);
                        producersMeta.put(method, rmqHandlerMeta);
                    }
                    metaList.add(rmqHandlerMeta);
                }));
        return metaList;
    }

    public static Map<Method, Annotation> getAnnotationMethodFromClass(Class clazz){
        Map<Method, Annotation> hashMap = new HashMap<>();
        System.out.println(clazz.getMethods().length);
        for (Annotation annotation: clazz.getAnnotations()){
            System.out.println(clazz.getName() + ":" +annotation);
        }

        for (Method method : clazz.getMethods()) {
            System.out.println(method.getName() + method.isAnnotationPresent(OutputProducer.class));
            for (Annotation annotation: method.getAnnotations()){
                System.out.println(method.getName() + ":" +annotation);
            }
        }

        Arrays.stream(clazz.getMethods())
                .forEach(method -> Arrays.stream(method.getDeclaredAnnotations())
                        .forEach(annotation -> {
                            System.out.println(annotation);
                            if (annotation instanceof InputConsumer){
                                hashMap.put(method, annotation);
                            }
                            else if (annotation instanceof OutputProducer) {
                                hashMap.put(method, annotation);
                            }
                        })
                );
        return hashMap;
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
}
