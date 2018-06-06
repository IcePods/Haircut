package com.example.lu.thebarbershop.Entity;

import java.io.Serializable;
import java.util.Set;

/**
 * Created by lu on 2018/5/16 0016.
 */

public class Barber implements Serializable {
    private int barberId;
    private int BarberImg; //理发师头像
    private String BarberName;//理发师姓名
    //用户与理发师主键关联映射
    private Users user;

    public int getBarberId() {
        return barberId;
    }

    public void setBarberId(int barberId) {
        this.barberId = barberId;
    }

    public int getBarberImg() {
        return BarberImg;
    }

    public void setBarberImg(int barberImg) {
        BarberImg = barberImg;
    }

    public String getBarberName() {
        return BarberName;
    }

    public void setBarberName(String barberName) {
        BarberName = barberName;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }
}
