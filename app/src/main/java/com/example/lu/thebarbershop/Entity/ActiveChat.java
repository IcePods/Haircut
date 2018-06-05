package com.example.lu.thebarbershop.Entity;

/**
 * Created by lenovo on 2018/5/24.
 */

public class ActiveChat {
    //头像
    private Object mPortrait;
    //名称
    private String mName;
    //内容
    private String mContent;
    //时间
    private String mTime;
    public ActiveChat(){}

    public ActiveChat(Object mPortrait, String mName, String mContent, String mTime) {
        this.mPortrait = mPortrait;
        this.mName = mName;
        this.mContent = mContent;
        this.mTime = mTime;
    }

    public Object getmPortrait() {
        return mPortrait;
    }

    public void setmPortrait(Object mPortrait) {
        this.mPortrait = mPortrait;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getmContent() {
        return mContent;
    }

    public void setmContent(String mContent) {
        this.mContent = mContent;
    }

    public String getmTime() {
        return mTime;
    }

    public void setmTime(String mTime) {
        this.mTime = mTime;
    }
}
