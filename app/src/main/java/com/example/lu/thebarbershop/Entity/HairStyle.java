package com.example.lu.thebarbershop.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by shan on 2018/5/18.
 * 发型实体类
 */

public class HairStyle implements Serializable{
    private int hairstyleId;//发型id
    private String hairstylePicture;//发型图片地址
    private Set<HairStyleDetail> hairStyleDetailSet;//发型详情
    private String hairstyleName;//发型名称
    private String hairstyleIntroduce;//发型简介
//    private int imgHeight;//发型图片高度


    public HairStyle() {
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

    public Set<HairStyleDetail> getHairStyleDetailSet() {
        return hairStyleDetailSet;
    }

    public void setHairStyleDetailSet(Set<HairStyleDetail> hairStyleDetailSet) {
        this.hairStyleDetailSet = hairStyleDetailSet;
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
