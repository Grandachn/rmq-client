package com.grandachn.rocketmq.rmqclient.handler;

import com.alibaba.fastjson.JSON;
import com.grandachn.rocketmq.rmqclient.core.RmqClientContext;
import com.grandachn.rocketmq.rmqclient.util.SpringContextUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @Author by guanda
 * @Date 2018/12/21 14:45
 */
public class BeanMessageHandler extends SafelyMessageHandler {
    private static Logger LOG = LoggerFactory.getLogger(BeanMessageHandler.class);

    private Method method;
    private Object bean;

    public BeanMessageHandler(Object bean, Method method) {
        super();
        this.method = method;
        this.bean = bean;
    }

    @Override
    protected void doExecute(byte[] message, Class clazz) throws Exception{
        RmqClientContext context = (RmqClientContext)SpringContextUtils.getBeanByClass(RmqClientContext.class);
        Object param = context.getSerialize().deSerialize(message, clazz);
        this.method.invoke(this.bean, param);
    }
}
