package com.kunledarams.cve2018.Model;

import java.io.Serializable;

/**
 * Created by ok on 10/19/2018.
 */

public class User implements Serializable {
    private String Userid;
    private String userName;
    private long userPhone;
    private String userEmail;
    private String userAspect;
    private String userHobbies;
    private String userlikes;
    private String userFood;
    private String usercolor;

    private String userPlan;
    private String userBirthday;
    private String userNickName;

    private String userImage;
    private String userSocialmedia;

    public User(String userid, String userName, long userPhone, String userEmail, String userAspect, String userHobbies, String userlikes, String userFood,
                String usercolor, String userPlan, String userBirthday, String userNickName, String userImage, String userSocialmedia) {
        Userid = userid;
        this.userName = userName;
        this.userPhone = userPhone;
        this.userEmail = userEmail;
        this.userAspect = userAspect;
        this.userHobbies = userHobbies;
        this.userlikes = userlikes;
        this.userFood = userFood;
        this.usercolor = usercolor;
        this.userPlan = userPlan;
        this.userBirthday = userBirthday;
        this.userNickName = userNickName;
        this.userImage = userImage;
        this.userSocialmedia = userSocialmedia;
    }

    public User() {


    }

    public String getUserid() {
        return Userid;
    }

    public void setUserid(String userid) {
        Userid = userid;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getUserPhone() {
        return userPhone;
    }

    public void setUserPhone(long userPhone) {
        this.userPhone = userPhone;
    }

    public String getUserEmail() {
        return userEmail;
    }

    public void setUserEmail(String userEmail) {
        this.userEmail = userEmail;
    }

    public String getUserAspect() {
        return userAspect;
    }

    public void setUserAspect(String userAspect) {
        this.userAspect = userAspect;
    }

    public String getUserHobbies() {
        return userHobbies;
    }

    public void setUserHobbies(String userHobbies) {
        this.userHobbies = userHobbies;
    }

    public String getUserlikes() {
        return userlikes;
    }

    public void setUserlikes(String userlikes) {
        this.userlikes = userlikes;
    }

    public String getUserFood() {
        return userFood;
    }

    public void setUserFood(String userFood) {
        this.userFood = userFood;
    }

    public String getUsercolor() {
        return usercolor;
    }

    public void setUsercolor(String usercolor) {
        this.usercolor = usercolor;
    }

    public String getUserPlan() {
        return userPlan;
    }

    public void setUserPlan(String userPlan) {
        this.userPlan = userPlan;
    }

    public String getUserBirthday() {
        return userBirthday;
    }

    public void setUserBirthday(String userBirthday) {
        this.userBirthday = userBirthday;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getUserImage() {
        return userImage;
    }

    public void setUserImage(String userImage) {
        this.userImage = userImage;
    }

    public String getUserSocialmedia() {
        return userSocialmedia;
    }

    public void setUserSocialmedia(String userSocialmedia) {
        this.userSocialmedia = userSocialmedia;
    }
}
