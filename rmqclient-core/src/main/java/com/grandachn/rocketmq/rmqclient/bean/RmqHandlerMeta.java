package com.grandachn.rocketmq.rmqclient.bean;

import com.grandachn.rocketmq.rmqclient.annotation.InputMessage;
import com.grandachn.rocketmq.rmqclient.annotation.OutputMessage;
import com.grandachn.rocketmq.rmqclient.annotation.TransactionMessage;
import com.grandachn.rocketmq.rmqclient.annotation.TransactionMethod;

import java.lang.reflect.Method;

/**
 * @Author by guanda
 * @Date 2018/12/20 11:51
 */
public class RmqHandlerMeta {
    private Object bean;

    private Class beanClass;

    private Method method;

    private Class parameterType;

    private Class returnType;

    private Class transactionMessageClass;

    private InputMessage inputMessage;

    private OutputMessage outputMessage;

    private TransactionMethod transactionMethod;

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

    public InputMessage getInputMessage() {
        return inputMessage;
    }

    public void setInputMessage(InputMessage inputMessage) {
        this.inputMessage = inputMessage;
    }

    public OutputMessage getOutputMessage() {
        return outputMessage;
    }

    public void setOutputMessage(OutputMessage outputMessage) {
        this.outputMessage = outputMessage;
    }

    public Class getReturnType() {
        return returnType;
    }

    public void setReturnType(Class returnType) {
        this.returnType = returnType;
    }

    public TransactionMethod getTransactionMethod() {
        return transactionMethod;
    }

    public void setTransactionMethod(TransactionMethod transactionMethod) {
        this.transactionMethod = transactionMethod;
    }

    public Class getBeanClass() {
        return beanClass;
    }

    public void setBeanClass(Class beanClass) {
        this.beanClass = beanClass;
    }

    public Class getTransactionMessageClass() {
        return transactionMessageClass;
    }

    public void setTransactionMessageClass(Class transactionMessageClass) {
        this.transactionMessageClass = transactionMessageClass;
    }
}
