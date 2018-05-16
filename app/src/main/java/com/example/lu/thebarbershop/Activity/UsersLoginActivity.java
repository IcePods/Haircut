package com.example.lu.thebarbershop.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.lu.thebarbershop.R;

public class UsersLoginActivity extends AppCompatActivity {
    private Button ToRegister;//跳转注册button
    private ImageView UserLoginLogo;//登录页面logo
    private EditText UserLoginUsername;//登录用户名
    private EditText UserLoginPwd;//登录密码
    private Button UserLogin;//登录按钮
    private Mylistener mylistener;//监听器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_login_activity);
        //获取控件
        getview();
        //实现监听控件
        mylistener = new Mylistener();
        ToRegister.setOnClickListener(mylistener);
        UserLogin.setOnClickListener(mylistener);


    }
//获取控件
    private void getview() {
        ToRegister = findViewById(R.id.users_login_register);
        UserLoginLogo = findViewById(R.id.users_login_logo);
        UserLoginUsername = findViewById(R.id.users_login_username);
        UserLoginPwd = findViewById(R.id.users_login_pwd);
        UserLogin = findViewById(R.id.users_login_login);
    }
//实现监听器类
    private class Mylistener implements View.OnClickListener{
    @Override
    public void onClick(View v) {
        Intent intent = new Intent();
        switch (v.getId()){
            case R.id.users_login_register:
                intent.setClass(getApplicationContext(),UsersRegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.users_login_login:
                intent.setClass(getApplicationContext(),MainActivity.class);
                startActivity(intent);
                break;
        }
    }
}
}
