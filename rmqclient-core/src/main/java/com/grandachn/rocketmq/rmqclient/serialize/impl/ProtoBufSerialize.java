package com.grandachn.rocketmq.rmqclient.serialize.impl;

import com.dyuproject.protostuff.LinkedBuffer;
import com.dyuproject.protostuff.ProtobufIOUtil;
import com.dyuproject.protostuff.runtime.RuntimeSchema;
import com.grandachn.rocketmq.rmqclient.serialize.ISerialize;

/**
 * @Author by guanda
 * @Date 2018/12/21 17:01
 */
public class ProtoBufSerialize implements ISerialize {
    @Override
    public <T> byte[] serialize(T t, Class<T> clazz) {
        return ProtobufIOUtil.toByteArray(t, RuntimeSchema.createFrom(clazz),
                LinkedBuffer.allocate(LinkedBuffer.DEFAULT_BUFFER_SIZE));
    }

    @Override
    public <T> T deSerialize(byte[] data, Class<T> clazz) {
        RuntimeSchema<T> runtimeSchema = RuntimeSchema.createFrom(clazz);
        T t = runtimeSchema.newMessage();
        ProtobufIOUtil.mergeFrom(data, t, runtimeSchema);
        return t;
    }
}
