package com.grandachn.rocketmq.rmqclient.serialize.impl;

import com.alibaba.fastjson.JSON;
import com.grandachn.rocketmq.rmqclient.serialize.ISerialize;

/**
 * @Author by guanda
 * @Date 2018/12/21 17:03
 */
public class FastJsonSerialize implements ISerialize {
    @Override
    public <T> byte[] serialize(T t, Class<T> clazz) {
        return JSON.toJSONBytes(t);
    }

    @Override
    public <T> T deSerialize(byte[] data, Class<T> clazz) {
        return JSON.parseObject(data, clazz);
    }
}
