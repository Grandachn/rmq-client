package com.grandachn.rocketmq.rmqclient.serialize;

/**
 * @Author by guanda
 * @Date 2018/12/21 16:58
 */
public enum SerializeType {
    FASTJSON("fastjson") ,
    PROTOBUF("protobuf");

    private String name;

    SerializeType(String name) {
        this.name = name;
    }
}
