package com.grandachn.rocketmq.rmqclient.handler;

import com.grandachn.rocketmq.rmqclient.exception.ExceptionHandler;
import org.reflections.Reflections;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @Author by guanda
 * @Date 2018/12/20 16:18
 */
public abstract class SafelyMessageHandler implements MessageHandler {
    private static Logger LOG = LoggerFactory.getLogger(SafelyMessageHandler.class);

    protected abstract void doExecute(String message);

    @Override
    public void execute(String message) {
        try {
            doExecute(message);
        } catch (Throwable t) {
            handleException(t, message);
        }
    }

   private void handleException(Throwable t, String message){
       Reflections reflections = new Reflections("com.grandachn.rocketmq.rmqclient");
       reflections.getSubTypesOf(ExceptionHandler.class)
               .forEach(clazz -> {
                   try {
                       clazz.newInstance().handle(t, message);
                   } catch (Exception e) {
                       LOG.error("can't find a proper handler to handle the exception.", e);
                   }
               });
   }
}
