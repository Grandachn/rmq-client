package com.grandachn.rocketmq.rmqclient.serialize;

/**
 * @Author by guanda
 * @Date 2018/12/21 17:00
 */
public interface ISerialize {
    <T> byte[] serialize(T t,Class<T> clazz);
    <T> T deSerialize(byte[] data,Class<T> clazz);
}
