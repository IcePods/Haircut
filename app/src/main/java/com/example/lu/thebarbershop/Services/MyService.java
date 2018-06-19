package com.example.lu.thebarbershop.Services;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.example.lu.thebarbershop.Activity.MainActivity;

import cn.jpush.android.api.JPushInterface;
import cn.jpush.android.service.JPushMessageReceiver;

/**
 * Created by lu on 2018/6/19 0019.
 */

public class MyService extends BroadcastReceiver {

    private final String TAG = "lhy";
    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle bundle = intent.getExtras();
        //获取广播类型
        String action = intent.getAction();
        switch (action){
            case JPushInterface.ACTION_MESSAGE_RECEIVED://自定义消息
                String title = bundle.getString(JPushInterface.EXTRA_TITLE);
                String message = bundle.getString(JPushInterface.EXTRA_MESSAGE);
                String extras = bundle.getString(JPushInterface.EXTRA_EXTRA);

                break;
            case  JPushInterface.ACTION_NOTIFICATION_OPENED: //普通的通知消息
                //获取通知内容， 并在另一个Activity中展示
                //获取通知标题
                String title2 = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                //获取通知内容
                String content2 = bundle.getString(JPushInterface.EXTRA_ALERT);
                //获取附加消息的JSon数据
                String extraJson2 = bundle.getString(JPushInterface.EXTRA_EXTRA);
                Log.i(TAG,"title= " + title2+"content= "+ content2+"extraJson= "+extraJson2);
                //跳转页面
                Intent conIntent = new Intent(context,MainActivity.class);
                conIntent.putExtra("content",content2);
                conIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(conIntent);
                Log.i(TAG ,"收到普通的通知");
                break;
            case JPushInterface.ACTION_NOTIFICATION_RECEIVED://富媒体消息

                String title3 = bundle.getString(JPushInterface.EXTRA_NOTIFICATION_TITLE);
                String content3 = bundle.getString(JPushInterface.EXTRA_ALERT);
                String extras3 = bundle.getString(JPushInterface.EXTRA_EXTRA);
                break;
        }
    }
}
