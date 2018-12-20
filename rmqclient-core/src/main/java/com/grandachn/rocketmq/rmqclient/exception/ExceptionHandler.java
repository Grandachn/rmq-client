package com.grandachn.rocketmq.rmqclient.exception;

/**
 * @Author by guanda
 * @Date 2018/12/20 16:13
 */
@FunctionalInterface
public interface ExceptionHandler {
    void handle(Throwable t, String message);
}
