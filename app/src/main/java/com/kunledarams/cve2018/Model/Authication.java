package com.kunledarams.cve2018.Model;

public class Authication {

    private String UserName;
    private String UserPassword;
    private String userMail;

    public Authication(String userName, String userPassword, String userMail) {
        UserName = userName;
        UserPassword = userPassword;
        this.userMail = userMail;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getUserMail() {
        return userMail;
    }

    public void setUserMail(String userMail) {
        this.userMail = userMail;
    }
}
