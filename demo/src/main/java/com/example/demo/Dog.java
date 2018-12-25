package com.example.demo;

import java.io.Serializable;

/**
 * @Author by guanda
 * @Date 2018/12/21 16:34
 */
public class Dog implements Serializable {

    private String name;
    private String type;

    public Dog(){

    }

    public Dog(String name, String type){
        this.name = name;
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
