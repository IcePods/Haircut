package com.example.lu.thebarbershop.Entity;

/**
 * Created by lu on 2018/5/16 0016.
 */

public class Barber {
    private int id;
    private int BarberImg; //理发师头像
    private String BarberName;//理发师姓名

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
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
}
