package com.example.lu.thebarbershop.Entity;

import java.io.Serializable;
import java.util.List;
import java.util.Set;

/**
 * Created by lu on 2018/5/11 0011.
 */

public class Dynamic implements Serializable {
    private int id;
    private Users user;//用户
    private String DynamicContent;  // 动态文字
    private Set<DynamicPicture> DynamicImagePathSet; //动态图片列表
    private String DynamicTime;
    //用户状态是否正确（登录是否过期）
    private boolean DynamicCondition = true;

    public Dynamic(){}
    public Dynamic(String text, Set<DynamicPicture> str){
        this.DynamicContent = text;
        this.DynamicImagePathSet = str;
    }

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

    public Set<DynamicPicture> getDynamicImagePathSet() {
        return DynamicImagePathSet;
    }

    public String getDynamicTime() {
        return DynamicTime;
    }

    public void setDynamicTime(String dynamicTime) {
        DynamicTime = dynamicTime;
    }

    public void setDynamicImagePathSet(Set<DynamicPicture> dynamicImagePathSet) {
        DynamicImagePathSet = dynamicImagePathSet;
    }

    public boolean isDynamicCondition() {
        return DynamicCondition;
    }

    public void setDynamicCondition(boolean dynamicCondition) {
        DynamicCondition = dynamicCondition;
    }
}