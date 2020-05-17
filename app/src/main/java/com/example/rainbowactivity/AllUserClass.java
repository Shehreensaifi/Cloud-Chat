package com.example.rainbowactivity;

public class AllUserClass
{
    String name,imageUrl,uid;

    public AllUserClass()
    {

    }
    public AllUserClass(String name, String imageUrl, String uid) {
        this.name = name;
        this.imageUrl = imageUrl;
        this.uid = uid;
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
}
