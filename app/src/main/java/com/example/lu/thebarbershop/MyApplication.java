package com.example.lu.thebarbershop;

import android.app.Application;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lu on 2018/6/19 0019.
 */

public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
}
