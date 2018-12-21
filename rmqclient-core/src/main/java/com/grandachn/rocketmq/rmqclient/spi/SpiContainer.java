package com.grandachn.rocketmq.rmqclient.spi;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author by guanda
 * @Date 2018/12/21 17:18
 */
public class SpiContainer {

    private static Logger LOG = LoggerFactory.getLogger(SpiContainer.class);

    private static Map<String, Object> containers = new ConcurrentHashMap<>();

    private static Properties properties = new Properties();

    static {
        try {
            properties.load(Thread.currentThread().getContextClassLoader()
                    .getResourceAsStream("spi.properties"));
        } catch (IOException e) {
            LOG.error("can't open the spi.properties.", e);
        }
    }

    public static Object getBean(String beanName){
        if(containers.containsKey(beanName)){
            return containers.get(beanName);
        }
        initBean(beanName);
        Object bean = containers.get(beanName);
        if(bean == null){
            throw new RuntimeException("SpiContainer is not have a bean that name: " + beanName);
        }
        return containers.get(beanName);
    }

    private static void initBean(String beanName) {
        String clazzName = properties.getProperty(beanName);
        try {
            containers.put(beanName, Class.forName(clazzName).newInstance());
        } catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) {
            LOG.error("spi bean create fail", e);
        }
    }
}
