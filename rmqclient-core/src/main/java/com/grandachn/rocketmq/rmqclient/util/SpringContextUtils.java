package com.grandachn.rocketmq.rmqclient.util;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

import java.lang.annotation.Annotation;

/**
 * @Author by guanda
 * @Date 2018/12/20 11:42
 */
public class SpringContextUtils implements ApplicationContextAware {
    private static ApplicationContext context;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        context = applicationContext;
    }

    public static ApplicationContext getContext(){
        return context;
    }

    public static String[] getBeanNamesForAnnotation(Class<? extends Annotation> clazz){
        return context.getBeanNamesForAnnotation(clazz);
    }

    public static Object getBean(String name){
        return context.getBean(name);
    }

    public static Object getBeanByClass(Class clazz){
        return context.getBean(clazz);
    }

}
