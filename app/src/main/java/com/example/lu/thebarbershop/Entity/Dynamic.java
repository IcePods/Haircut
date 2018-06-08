package com.example.lu.thebarbershop.Entity;

import java.util.List;

/**
 * Created by lu on 2018/5/11 0011.
 */

public class Dynamic {
    private int id;
    private Users user;//用户
    private String DynamicContent;  // 动态文字
    private List<String> DynamicImagePathList; //动态图片列表

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getDynamicContent() {
        return DynamicContent;
    }

    public void setDynamicContent(String dynamicContent) {
        DynamicContent = dynamicContent;
    }

    public Users getUser() {
        return user;
    }

    public void setUser(Users user) {
        this.user = user;
    }

    public List<String> getDynamicImagePathList() {
        return DynamicImagePathList;
    }

    public void setDynamicImagePathList(List<String> dynamicImagePathList) {
        DynamicImagePathList = dynamicImagePathList;
    }
}
