package com.example.rainbowactivity.model;

public class MessageClass
{
    private String message,from,type,key;
    private Long time;
    private boolean seen;
    public  MessageClass()
    {

    }

    public MessageClass(String message, String from, String type, Long time, boolean seen,String key) {
        this.message = message;
        this.from = from;
        this.type = type;
        this.time = time;
        this.seen = seen;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getFrom() {
        return from;
    }

    public void setFrom(String from) {
        this.from = from;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public boolean isSeen() {
        return seen;
    }

    public void setSeen(boolean seen) {
        this.seen = seen;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
