package com.grandachn.rocketmq.rmqclient.handler;

/**
 * @Author by guanda
 * @Date 2018/12/20 16:10
 */
@FunctionalInterface
public interface MessageHandler {
    void execute(String message);
}
