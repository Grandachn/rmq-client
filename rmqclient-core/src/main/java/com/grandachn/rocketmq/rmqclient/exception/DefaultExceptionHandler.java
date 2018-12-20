package com.grandachn.rocketmq.rmqclient.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author by guanda
 * @Date 2018/12/20 16:13
 */
public class DefaultExceptionHandler implements ExceptionHandler{
    private static Logger LOG = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    public void handle(Throwable t, String message) {
        if (t instanceof InterruptedException){
            LOG.error("Maybe it is shutting down. Or interruped when handing the message:" + message, t);
        }else{
            LOG.error("Failed to handle the message: " + message, t);
        }
    }
}
