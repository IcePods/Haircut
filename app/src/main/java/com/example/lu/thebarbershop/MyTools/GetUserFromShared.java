package com.example.lu.thebarbershop.MyTools;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import com.example.lu.thebarbershop.Activity.UsersLoginActivity;

import java.io.File;

/**
 * Created by sweet on 2018/6/8.
 */

public class GetUserFromShared {
    private Context mContext;
    private File file;

    public GetUserFromShared(Context mContext) {
        this.mContext = mContext;
    }

    //得到用户token
    public String getUserTokenFromShared(){
        file =new File(mContext.getApplicationContext().getFilesDir().getParent()+"/shared_prefs/usertoken.xml");
        if(file.exists()){
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
            String token = sharedPreferences.getString("token","");
            return token;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, UsersLoginActivity.class);
        mContext.startActivity(intent);
        return "";
    }
    //得到用户账号
    public  String getUserAccountFromShared(){
        file =new File(mContext.getApplicationContext().getFilesDir().getParent()+"/shared_prefs/usertoken.xml");
        if(file.exists()){
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
            String userAccount = sharedPreferences.getString("userAccount","");
            return userAccount;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, UsersLoginActivity.class);
        mContext.startActivity(intent);
        return "";
    }
    //得到用户密码
    public  String getUserPasswordFromShared(){
        file =new File(mContext.getApplicationContext().getFilesDir().getParent()+"/shared_prefs/usertoken.xml");
        if(file.exists()){
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
            String userPassword = sharedPreferences.getString("userPassword","");
            return userPassword;
        }
        Intent intent = new Intent();
        intent.setClass(mContext, UsersLoginActivity.class);
        mContext.startActivity(intent);
        return "";
    }
}
