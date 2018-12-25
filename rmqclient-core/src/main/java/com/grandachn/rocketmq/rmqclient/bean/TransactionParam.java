package com.grandachn.rocketmq.rmqclient.bean;

import java.util.List;

/**
 * @Author by guanda
 * @Date 2018/12/25 14:02
 */
public class TransactionParam {
    Object messages;
    List<Object> params;

    public TransactionParam(){

    }

    public TransactionParam(List<Object> params){
        this.params = params;
    }

    public Object getMessages() {
        return messages;
    }

    public void setMessages(Object messages) {
        this.messages = messages;
    }

    public List<Object> getParams() {
        return params;
    }

    public void setParams(List<Object> params) {
        this.params = params;
    }
}
