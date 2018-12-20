package com.grandachn.rocketmq.rmqclient.bean;

import com.grandachn.rocketmq.rmqclient.annotation.InputConsumer;
import com.grandachn.rocketmq.rmqclient.annotation.OutputProducer;

import java.lang.reflect.Method;

/**
 * @Author by guanda
 * @Date 2018/12/20 11:51
 */
public class RmqHandlerMeta {
    private Object bean;

    private Method method;

    private Class parameterType;

    private InputConsumer inputConsumer;

    private OutputProducer outputProducer;

    public Object getBean() {
        return bean;
    }

    public void setBean(Object bean) {
        this.bean = bean;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }

    public Class getParameterType() {
        return parameterType;
    }

    public void setParameterType(Class parameterType) {
        this.parameterType = parameterType;
    }

    public InputConsumer getInputConsumer() {
        return inputConsumer;
    }

    public void setInputConsumer(InputConsumer inputConsumer) {
        this.inputConsumer = inputConsumer;
    }

    public OutputProducer getOutputProducer() {
        return outputProducer;
    }

    public void setOutputProducer(OutputProducer outputProducer) {
        this.outputProducer = outputProducer;
    }
}
