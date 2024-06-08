package com.example.myapplication.models;

public class Message {
    private String text;
    String senderId;
    String recieverId;
    long timemills;

    public long getTimemills() {
        return timemills;
    }

    public void setTimemills(long timemills) {
        this.timemills = timemills;
    }

    public Message(String text, String senderId, String recieverId) {
        this.timemills =  System.currentTimeMillis();
        this.text = text;
        this.senderId = senderId;
        this.recieverId = recieverId;
    }


    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public String getRecieverId() {
        return recieverId;
    }

    public void setRecieverId(String recieverId) {
        this.recieverId = recieverId;
    }

    public void setText(String text) {
        this.text = text;
    }
    public Message() {

    }
    public String getText() {
        return text;
    }
}
