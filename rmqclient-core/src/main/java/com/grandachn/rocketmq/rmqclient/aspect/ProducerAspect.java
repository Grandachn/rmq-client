package com.grandachn.rocketmq.rmqclient.aspect;

import com.grandachn.rocketmq.rmqclient.bean.RmqHandlerMeta;
import com.grandachn.rocketmq.rmqclient.client.RmqProducer;
import com.grandachn.rocketmq.rmqclient.core.RmqClientContext;
import com.grandachn.rocketmq.rmqclient.util.SpringContextUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;

import java.lang.reflect.Method;

/**
 * @Author by guanda
 * @Date 2018/12/20 16:58
 */
@Aspect
public class ProducerAspect {

    @Pointcut("@annotation(com.grandachn.rocketmq.rmqclient.annotation.OutputProducer)")
    public void annotationPoint(){}

    @Around("annotationPoint()")
    public void handle(ProceedingJoinPoint joinPoint) throws Throwable {
        System.out.println("1111111111111------------aspect");
        Object result = joinPoint.proceed();
        RmqClientContext context = (RmqClientContext) SpringContextUtils.getBeanByClass(RmqClientContext.class);
//        context.init();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();

        RmqProducer producer = context.getProducers().get(signature.getMethod());
        RmqHandlerMeta rmqHandlerMeta = context.getProducersMeta().get(signature.getMethod());

        producer.sendBeanToTopic(rmqHandlerMeta.getOutputProducer().topic(), "", result);
    }

    @Before("annotationPoint()")
    public void BeforeAnnotation(JoinPoint joinPoint){
        MethodSignature signature=(MethodSignature) joinPoint.getSignature();
        Method method=signature.getMethod();
        System.out.println("注解的拦截方法名注解内容前：" + method.getName());
    }
}
