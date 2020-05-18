package com.example.rainbowactivity.model;

public class ChatLastMessageClass
{
    String name,imageUrl,uid,message,from;

    public ChatLastMessageClass(String name, String imageUrl, String uid, String message, String from) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.uid = uid;
        this.message = message;
        this.from = from;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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
}
