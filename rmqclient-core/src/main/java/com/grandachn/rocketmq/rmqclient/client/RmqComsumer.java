package com.grandachn.rocketmq.rmqclient.client;

import com.grandachn.rocketmq.rmqclient.core.RmqClientContext;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author by guanda
 * @Date 2018/12/20 15:07
 */
public class RmqComsumer {
    private static Logger LOG = LoggerFactory.getLogger(RmqComsumer.class);

    public void init() {
        initGracefullyShutdown();
    }

    private void initGracefullyShutdown() {
        Runtime.getRuntime().addShutdownHook(new Thread(this::shutdownGracefully));
    }

    private void shutdownGracefully() {

    }
}
