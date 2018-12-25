package com.grandachn.rocketmq.rmqclient.aspect;

import com.grandachn.rocketmq.rmqclient.annotation.OutputMessage;
import com.grandachn.rocketmq.rmqclient.bean.RmqHandlerMeta;
import com.grandachn.rocketmq.rmqclient.client.RmqProducer;
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

/**
 * @Author by guanda
 * @Date 2018/12/20 16:58
 */
@Aspect
public class ProducerAspect {

    private static Logger LOG = LoggerFactory.getLogger(ProducerAspect.class);


    @Pointcut("@annotation(com.grandachn.rocketmq.rmqclient.annotation.OutputMessage)")
    public void annotationPoint(){}

    @Around("annotationPoint()")
    public void handle(ProceedingJoinPoint joinPoint) throws Throwable {
        Object returnObject = joinPoint.proceed();
        RmqClientContext context = (RmqClientContext) SpringContextUtils.getBeanByClass(RmqClientContext.class);
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method realMethod = joinPoint.getTarget().getClass().getDeclaredMethod(signature.getName(),
                signature.getMethod().getParameterTypes());

        RmqProducer producer = context.getProducers().get(realMethod);
        RmqHandlerMeta rmqHandlerMeta = context.getProducersMeta().get(realMethod);

        OutputMessage outputMessage = rmqHandlerMeta.getOutputMessage();

        producer.sendBeanToTopic(rmqHandlerMeta.getOutputMessage().topic(), outputMessage.tags(),  returnObject);
//        LOG.info("send message to topic:{} , message:{}", outputMessage.topic(), JSON.toJSONString(returnObject));
    }

}
