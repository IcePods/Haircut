package com.example.lu.thebarbershop.Entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by shan on 2018/5/18.
 * 发型实体类
 */

public class HairStyle implements Serializable{
    private int hairstyleId;//发型id
    private String hairstylePicture;//发型图片地址
    private List<String> hairstyleDetailPicture;//发型图片集合
    private String hairstyleName;//发型名称
    private String hairstyleIntroduce;//发型简介
//    private int imgHeight;//发型图片高度

    public HairStyle() {
    }

    public HairStyle(int hairstyleId, String hairstylePicture, List<String> hairstyleDetailPicture, String hairstyleName, String hairstyleIntroduce) {
        this.hairstyleId = hairstyleId;
        this.hairstylePicture = hairstylePicture;
        this.hairstyleDetailPicture = hairstyleDetailPicture;
        this.hairstyleName = hairstyleName;
        this.hairstyleIntroduce = hairstyleIntroduce;
    }

    public int getHairstyleId() {
        return hairstyleId;
    }

    public void setHairstyleId(int hairstyleId) {
        this.hairstyleId = hairstyleId;
    }

    public String getHairstylePicture() {
        return hairstylePicture;
    }

    public void setHairstylePicture(String hairstylePicture) {
        this.hairstylePicture = hairstylePicture;
    }

    public List<String> getHairstyleDetailPicture() {
        return hairstyleDetailPicture;
    }

    public void setHairstyleDetailPicture(List<String> hairstyleDetailPicture) {
        this.hairstyleDetailPicture = hairstyleDetailPicture;
    }

    public String getHairstyleName() {
        return hairstyleName;
    }

    public void setHairstyleName(String hairstyleName) {
        this.hairstyleName = hairstyleName;
    }

    public String getHairstyleIntroduce() {
        return hairstyleIntroduce;
    }

    public void setHairstyleIntroduce(String hairstyleIntroduce) {
        this.hairstyleIntroduce = hairstyleIntroduce;
    }
}
