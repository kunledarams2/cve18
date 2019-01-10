package com.kunledarams.cve2018.Model;

import java.io.Serializable;

/**
 * Created by ok on 10/28/2018.
 */

public class Comment implements Serializable {

    private String CommentId;
    private String postComment;
    private String userName;
    private  String userPhoto;
    private long timeCreated;
    private String comment;

    public Comment(String commentId, String postComment, String userName, String userPhoto, long timeCreated, String comment) {
        CommentId = commentId;
        this.postComment = postComment;
        this.userName = userName;
        this.userPhoto = userPhoto;
        this.timeCreated = timeCreated;
        this.comment = comment;
    }

    public Comment() {
    }

    public String getCommentId() {
        return CommentId;
    }

    public void setCommentId(String commentId) {
        CommentId = commentId;
    }

    public String getPostComment() {
        return postComment;
    }

    public void setPostComment(String postComment) {
        this.postComment = postComment;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserPhoto() {
        return userPhoto;
    }

    public void setUserPhoto(String userPhoto) {
        this.userPhoto = userPhoto;
    }

    public long getTimeCreated() {
        return timeCreated;
    }

    public void setTimeCreated(long timeCreated) {
        this.timeCreated = timeCreated;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
}
