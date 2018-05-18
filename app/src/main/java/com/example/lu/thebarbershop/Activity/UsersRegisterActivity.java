package com.example.lu.thebarbershop.Activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.lu.thebarbershop.MyTools.GetRoundedCornerBitmap;
import com.example.lu.thebarbershop.R;

public class UsersRegisterActivity extends AppCompatActivity {
    private Button ToLogin;
    private ImageView RegisterLogo;
    private EditText UserRegisterUsername;
    private EditText UserRegisterPwd;
    private EditText UserRegisterPwd2;
    private CheckBox checkBox;
    private Button UserRegister;
    private Mylistener mylistener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_register_activity);
        //获取控件
        getview();
        //控件监听
        mylistener = new Mylistener();
        ToLogin.setOnClickListener(mylistener);
        UserRegister.setOnClickListener(mylistener);



    }
//获取控件
    private void getview() {
        ToLogin = findViewById(R.id.user_register_login);
        RegisterLogo = findViewById(R.id.user_register_logo);
        UserRegisterUsername = findViewById(R.id.user_register_username);
        UserRegisterPwd = findViewById(R.id.user_register_pwd);
        UserRegisterPwd2 = findViewById(R.id.user_register_pwd2);
        checkBox = findViewById(R.id.user_register_box);
        UserRegister = findViewById(R.id.user_register);
    }
    private class Mylistener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.user_register_login:
                    intent.setClass(getApplicationContext(),UsersLoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
                case R.id.user_register:
                    intent.setClass(getApplicationContext(),UsersLoginActivity.class);
                    startActivity(intent);
                    finish();
                    break;
            }
        }
    }



}