package com.kunledarams.cve2018.Model;

/**
 * Created by ok on 10/19/2018.
 */

public class Gallerydata {
    private String userId;
    private String GImage;
    private String userComment;
    private String userImage;
    private String userNickname;
    private long postTime;

    public Gallerydata(String userId, String GImage, String userComment, String userImage, String userNickname ,long postTime) {
        this.userId = userId;
        this.GImage = GImage;
        this.userComment = userComment;
        this.userImage = userImage;
        this.userNickname = userNickname;
        this.postTime= postTime;
    }

    public Gallerydata() {
    }

    public String getUserId() {
        return userId;
    }

    public long getPostTime() {
        return postTime;
    }

    public void setPostTime(long postTime) {
        this.postTime = postTime;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getGImage() {
        return GImage;
    }

    public void setGImage(String GImage) {
        this.GImage = GImage;
    }

    public String getUserComment() {
        return userComment;
    }

    public void setUserComment(String userComment) {
        this.userComment = userComment;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserNickname() {
        return userNickname;
    }

    public void setUserNickname(String userNickname) {
        this.userNickname = userNickname;
    }
}
