package com.example.lu.thebarbershop.Entity;

/**
 * Created by 天龙 on 2018/5/24.
 */

public class Users {
    private int UserId;
    private String UserAccount;
    private String UserPassword;
    private String UserName;
    private String UserSex;
    private String UserPhone;
    private String UserHeader;

    public int getUserId() {
        return UserId;
    }

    public void setUserId(int userId) {
        UserId = userId;
    }

    public String getUserAccount() {
        return UserAccount;
    }

    public void setUserAccount(String userAccount) {
        UserAccount = userAccount;
    }

    public String getUserPassword() {
        return UserPassword;
    }

    public void setUserPassword(String userPassword) {
        UserPassword = userPassword;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getUserSex() {
        return UserSex;
    }

    public void setUserSex(String userSex) {
        UserSex = userSex;
    }

    public String getUserPhone() {
        return UserPhone;
    }

    public void setUserPhone(String userPhone) {
        UserPhone = userPhone;
    }

    public String getUserHeader() {
        return UserHeader;
    }

    public void setUserHeader(String userHeader) {
        UserHeader = userHeader;
    }
}
