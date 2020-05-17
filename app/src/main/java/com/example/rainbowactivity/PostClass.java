package com.example.rainbowactivity;

public class PostClass {
    String name, imageUrl, profileUrl, message, uid, imageName;
    Long time;

    public PostClass()
    {

    }

    public PostClass(String name, String imageUrl, String profileUrl, String message, String uid, String imageName, Long time) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.profileUrl = profileUrl;
        this.message = message;
        this.uid = uid;
        this.imageName = imageName;
        this.time = time;
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

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getImageName() {
        return imageName;
    }

    public void setImageName(String imageName) {
        this.imageName = imageName;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }
}


