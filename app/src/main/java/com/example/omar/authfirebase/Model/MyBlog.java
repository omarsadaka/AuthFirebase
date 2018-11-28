package com.example.omar.authfirebase.Model;

/**
 * Created by OMAR on 11/22/2018.
 */

public class MyBlog {
    private String title;
    private String image;
    private String descr;
    private String timesStamp;
    private String userId;

    public MyBlog() {
    }

    public String getTitle() {
        return title;
    }

    public MyBlog(String title, String image, String descr, String timesStamp, String userId) {
        this.title = title;
        this.image = image;
        this.descr = descr;
        this.timesStamp = timesStamp;
        this.userId = userId;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescr() {
        return descr;
    }

    public void setDescr(String descr) {
        this.descr = descr;
    }

    public String getTimesStamp() {
        return timesStamp;
    }

    public void setTimesStamp(String timesStamp) {
        this.timesStamp = timesStamp;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }
}
