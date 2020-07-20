package com.example.rainbowactivity.ui.home.mypost;

public class ProfileClass {
    String name,profileUrl,uid;

    public ProfileClass(){

    }

    public ProfileClass(String name, String profileUrl, String uid) {
        this.name = name;
        this.profileUrl = profileUrl;
        this.uid = uid;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getProfileUrl() {
        return profileUrl;
    }

    public void setProfileUrl(String profileUrl) {
        this.profileUrl = profileUrl;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
