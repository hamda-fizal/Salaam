package com.example.salaam.model;

public class User {
    private String uid;
    private String username;
    private String imageURL;
    private String status;

    public User(String uid,String username,String imageURL,String status) {
        this.username = username;
        this.uid=uid;
        this.imageURL=imageURL;
        this.status=status;
    }
    public User(){

    }

    public String getImageURL() {
        return imageURL;
    }

    public void setImageURL(String imageURL) {
        this.imageURL = imageURL;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
