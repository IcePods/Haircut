package com.example.lu.thebarbershop.Entity;

import java.io.Serializable;

/**
 * Created by 天龙 on 2018/6/8.
 */

public class Appointment implements Serializable{
    private int Appoint_id;
    private String Appoint_username;//预约人
    private String Appoint_phone;//预约人电话
    private String Appoint_time;//预约时间
    private String Appoint_barber;//预约理发师
    private String Appoint_state;//预约状态
    private HairStyle Appoint_hairStyle;//预约的发型对象
    private Users Appoint_users;//订单所属用户对象
    private UserShopDetail Appoint_userShopDetail;//预约的店铺对象

    public int getAppoint_id() {
        return Appoint_id;
    }

    public void setAppoint_id(int appoint_id) {
        Appoint_id = appoint_id;
    }

    public String getAppoint_username() {
        return Appoint_username;
    }

    public void setAppoint_username(String appoint_username) {
        Appoint_username = appoint_username;
    }

    public String getAppoint_state() {
        return Appoint_state;
    }

    public void setAppoint_state(String appoint_state) {
        Appoint_state = appoint_state;
    }

    public String getAppoint_phone() {
        return Appoint_phone;
    }

    public void setAppoint_phone(String appoint_phone) {
        Appoint_phone = appoint_phone;
    }

    public String getAppoint_time() {
        return Appoint_time;
    }

    public void setAppoint_time(String appoint_time) {
        Appoint_time = appoint_time;
    }

    public String getAppoint_barber() {
        return Appoint_barber;
    }

    public void setAppoint_barber(String appoint_barber) {
        Appoint_barber = appoint_barber;
    }

    public HairStyle getAppoint_hairStyle() {
        return Appoint_hairStyle;
    }

    public void setAppoint_hairStyle(HairStyle appoint_hairStyle) {
        Appoint_hairStyle = appoint_hairStyle;
    }

    public Users getAppoint_users() {
        return Appoint_users;
    }

    public void setAppoint_users(Users appoint_users) {
        Appoint_users = appoint_users;
    }

    public UserShopDetail getAppoint_userShopDetail() {
        return Appoint_userShopDetail;
    }

    public void setAppoint_userShopDetail(UserShopDetail appoint_userShopDetail) {
        Appoint_userShopDetail = appoint_userShopDetail;
    }

}
