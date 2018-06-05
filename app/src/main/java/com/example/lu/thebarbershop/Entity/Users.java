package com.example.lu.thebarbershop.Entity;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by 天龙 on 2018/5/24.
 */

public class Users implements Serializable{
    private int UserId;
    private String UserAccount;
    private String UserPassword;
    private String UserName;
    private String UserSex;
    private String UserPhone;
    private String UserHeader;
    private String UserToken;
    private Boolean UserCondition;
    private Set<UserShopDetail> shopSet = new HashSet<UserShopDetail>();

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

    public Set<UserShopDetail> getShopSet() {
        return shopSet;
    }

    public void setShopSet(Set<UserShopDetail> shopSet) {
        this.shopSet = shopSet;
    }

    public String getUserToken() {
        return UserToken;
    }

    public void setUserToken(String userToken) {
        UserToken = userToken;
    }

    public Boolean getUserCondition() {
        return UserCondition;
    }

    public void setUserCondition(Boolean userCondition) {
        UserCondition = userCondition;
    }

    public String toString(Users u) {
        String a= u.UserId+u.UserAccount+u.UserPassword;
        return a;
    }
}
