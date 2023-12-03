package com.example.myapplication.bean;

public class ChatBean {
    private int type; //该条消息的类型，0是(本地)发送的，1是接受的
    private String message;

    public ChatBean(int type, String msg){
        this.type = type;
        this.message = msg;
    }

    public int getType() {
        return type;
    }

    public String getMessage() {
        return message;
    }
}
