package com.grandachn.rocketmq.rmqclient.client;

import com.grandachn.rocketmq.rmqclient.bean.ProducerConfig;
import com.grandachn.rocketmq.rmqclient.bean.RmqHandlerMeta;
import com.grandachn.rocketmq.rmqclient.core.RmqClientContext;
import com.grandachn.rocketmq.rmqclient.util.SpringContextUtils;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.LocalTransactionState;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.client.producer.TransactionListener;
import org.apache.rocketmq.client.producer.TransactionMQProducer;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Method;
import java.util.Properties;
import java.util.concurrent.*;

/**
 * @Author by guanda
 * @Date 2018/12/21 18:03
 */
public class RmqTransactionProducer {
    private static Logger LOG = LoggerFactory.getLogger(RmqTransactionProducer.class);

    private TransactionMQProducer producer;
    private String propertiesFile;
    private Properties properties;

    private RmqHandlerMeta rmqHandlerMeta;
    private Object bean;
    private Method method;
    private Class beanClass;

    private RmqClientContext rmqClientContext;

    private ConcurrentHashMap<String, LocalTransactionState> localTrans = new ConcurrentHashMap<>();

    public RmqTransactionProducer(RmqHandlerMeta rmqHandlerMeta){
        this.rmqHandlerMeta = rmqHandlerMeta;
        this.bean = rmqHandlerMeta.getBean();
        this.method = rmqHandlerMeta.getMethod();
        this.propertiesFile = rmqHandlerMeta.getOutputTransactionMessage().propertiesFile();
        this.beanClass = rmqHandlerMeta.getBeanClass();
        init();
    }

    private void init() {
        if (properties == null) {
            properties = new Properties();
            try {
                properties.load(Thread.currentThread().getContextClassLoader()
                        .getResourceAsStream(propertiesFile));
            } catch (IOException e) {
                LOG.error("The properties file is not loaded.", e);
                throw new IllegalArgumentException(
                        "The properties file is not loaded.", e);
            }
        }
        LOG.info("Producer properties:" + properties);
        ProducerConfig config = new ProducerConfig(properties);

        TransactionMQProducer producer = new TransactionMQProducer("please_rename_unique_group_name");
        ExecutorService executorService = new ThreadPoolExecutor(2, 5, 100, TimeUnit.SECONDS, new ArrayBlockingQueue<Runnable>(2000), new ThreadFactory() {
            @Override
            public Thread newThread(Runnable r) {
                Thread thread = new Thread(r);
                thread.setName("client-transaction-msg-check-thread");
                return thread;
            }
        });

        TransactionListener transactionListener = new TransactionListener() {
            @Override
            public LocalTransactionState executeLocalTransaction(Message msg, Object arg) {
                try {
                    System.out.println("start: " + msg.getTransactionId());
                    localTrans.put(msg.getTransactionId(), LocalTransactionState.UNKNOW);
                    method.invoke(beanClass.newInstance());
                } catch (Exception e) {
                    LOG.error("transantion method running fail.", e);
                    localTrans.put(msg.getTransactionId(), LocalTransactionState.ROLLBACK_MESSAGE);
                    return LocalTransactionState.ROLLBACK_MESSAGE;
                }
                localTrans.put(msg.getTransactionId(), LocalTransactionState.COMMIT_MESSAGE);
                return LocalTransactionState.COMMIT_MESSAGE;
            }

            @Override
            public LocalTransactionState checkLocalTransaction(MessageExt msg) {
                System.out.println("check status: " + localTrans.get(msg.getTransactionId()));
                return localTrans.get(msg.getTransactionId());
            }
        };

        producer.setNamesrvAddr(config.getNamesrvAddr());
        producer.setExecutorService(executorService);
        producer.setTransactionListener(transactionListener);

        try {
            producer.start();
            this.producer = producer;
        } catch (MQClientException e) {
            LOG.error("The RmqTransactionProducer start fail.", e);
        }

        this.rmqClientContext = (RmqClientContext) SpringContextUtils.getBeanByClass(RmqClientContext.class);
    }

    public SendResult sendBeanToTopic(String topicName, String tags, Object bean){
        SendResult sendResult = null;
        try {
            byte[] bytes = rmqClientContext.getSerialize().serialize(bean, rmqHandlerMeta.getReturnType());
            Message msg = new Message(topicName , tags,  bytes);
            sendResult = producer.sendMessageInTransaction(msg, null);
        } catch (Exception e) {
            LOG.error("send message error.", e);
        }

        return sendResult;
    }

    public void close() {
        producer.shutdown();
    }
}
