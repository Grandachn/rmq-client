package com.grandachn.rocketmq.rmqclient.aspect;

import com.grandachn.rocketmq.rmqclient.annotation.TransactionMethod;
import com.grandachn.rocketmq.rmqclient.bean.RmqHandlerMeta;
import com.grandachn.rocketmq.rmqclient.bean.TransactionParam;
import com.grandachn.rocketmq.rmqclient.client.RmqTransactionProducer;
import com.grandachn.rocketmq.rmqclient.core.RmqClientContext;
import com.grandachn.rocketmq.rmqclient.util.SpringContextUtils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.LinkedList;

/**
 * @Author by guanda
 * @Date 2018/12/20 16:58
 */
@Aspect
public class TransactionProducerAspect {

    private static Logger LOG = LoggerFactory.getLogger(TransactionProducerAspect.class);

    @Pointcut("@annotation(com.grandachn.rocketmq.rmqclient.annotation.TransactionMethod)")
    public void annotationPoint(){}

    @Around("annotationPoint()")
    public void handle(ProceedingJoinPoint joinPoint) throws Throwable {
//        Object returnObject = joinPoint.proceed();
        RmqClientContext context = (RmqClientContext) SpringContextUtils.getBeanByClass(RmqClientContext.class);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(),
                signature.getMethod().getParameterTypes());

        RmqTransactionProducer producer = context.getTransactionProducers().get(realMethod);
        RmqHandlerMeta rmqHandlerMeta = context.getProducersMeta().get(realMethod);

        TransactionMethod outputMessage = rmqHandlerMeta.getTransactionMethod();

        Object[] args = joinPoint.getArgs();
//        Object transactionMessage = new Object();
        TransactionParam transactionParam = new TransactionParam(new LinkedList<>());
        for (Object arg : args) {
            if(arg.getClass().equals(rmqHandlerMeta.getTransactionMessageClass())){
//                transactionMessage = arg;
                transactionParam.setMessages(arg);
            }
            transactionParam.getParams().add(arg);
        }
        producer.sendTransactionBeanToTopic(rmqHandlerMeta.getTransactionMethod().topic(), outputMessage.tags(), transactionParam);
//        LOG.info("send message to topic:{} , message:{}", outputMessage.topic(), JSON.toJSONString(transactionMessage));
    }

}
