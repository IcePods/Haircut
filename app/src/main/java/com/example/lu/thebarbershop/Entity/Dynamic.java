package com.example.lu.thebarbershop.Entity;

/**
 * Created by lu on 2018/5/11 0011.
 */

public class Dynamic {
    private int id;
    private int ImgName; //图片地址 不清楚怎么保存 Glide框架 可以通过String类型保存图片 也可以通过int类型 保存本地图片
    private String UserName; //用户姓名
    private String DynamicContent;  // 动态文字
    private int DynamicImg; //动态图片

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getImgName() {
        return ImgName;
    }

    public void setImgName(int imgName) {
        ImgName = imgName;
    }

    public String getUserName() {
        return UserName;
    }

    public void setUserName(String userName) {
        UserName = userName;
    }

    public String getDynamicContent() {
        return DynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        DynamicContent = dynamicContent;
    }

    public int getDynamicImg() {
        return DynamicImg;
    }

    public void setDynamicImg(int dynamicImg) {
        DynamicImg = dynamicImg;
    }
}
