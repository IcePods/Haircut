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
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lu.thebarbershop.Entity.Users;
import com.example.lu.thebarbershop.MyTools.GetRoundedCornerBitmap;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.nio.charset.Charset;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UsersRegisterActivity extends AppCompatActivity {
    private Button ToLogin;
    private ImageView RegisterLogo;
    private EditText UserRegisterUsername;
    private EditText UserRegisterPwd;
    private EditText UserRegisterPwd2;
    private CheckBox checkBox;
    private Button UserRegister;
    private Mylistener mylistener;
    private TextView errorMessage;
    private final String url = "http://192.168.155.2:8080/theBarberShopServers/register.action";
    OkHttpClient okHttpClient ;
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/plain;charset=UTF-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_register_activity);
        okHttpClient = new OkHttpClient();
        //获取控件
        getview();
        //控件监听
        mylistener = new Mylistener();
        ToLogin.setOnClickListener(mylistener);
        UserRegister.setOnClickListener(mylistener);
        UserRegisterUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                }else {
                    String str =UserRegisterUsername.getText().toString().trim();
                    if (str.isEmpty()){
                        errorMessage.setText("用户名不能为空");
                    }else if (!str.isEmpty()){
                        errorMessage.setText("");
                    }
                }
            }
        });
        UserRegisterPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                }else {
                    String str =UserRegisterPwd.getText().toString().trim();
                    if (str.isEmpty()){
                        errorMessage.setText("密码不能为空");
                    }else if (!str.isEmpty()){
                        errorMessage.setText("");
                    }
                }
            }
        });
        UserRegisterPwd2.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){
                }else {
                    String str1 =UserRegisterPwd.getText().toString().trim();
                    String str2 =UserRegisterPwd2.getText().toString().trim();
                     if (str2.isEmpty()){
                        errorMessage.setText("密码不能为空");
                    }else if (!str1.equals(str2)){
                        errorMessage.setText("密码不一致");
                    }else if (str1.equals(str2)){
                        errorMessage.setText("");
                    }
                }
            }
        });

        //初始化OKHTTPClient对象
        okHttpClient = new OkHttpClient();
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
        errorMessage = findViewById(R.id.register_error_message);
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
                    String registerUsername =UserRegisterUsername.getText().toString().trim();
                    String registerPwd =UserRegisterPwd.getText().toString().trim();
                    String registerPwd2 =UserRegisterPwd2.getText().toString().trim();
                    if (registerUsername.isEmpty()){
                        errorMessage.setText("用户名不能为空");
                    }else if (registerPwd.isEmpty()){
                        errorMessage.setText("密码不能为空");
                    }else if (!registerPwd.equals(registerPwd2)){
                        errorMessage.setText("两次密码不一致");
                    }else {
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        Users user = new Users();
                        user.setUserAccount(registerUsername);
                        user.setUserPassword(registerPwd);
                        String RegisterJson = gson.toJson(user);
                        postRegisterUser();
                        intent.setClass(getApplicationContext(),UsersLoginActivity.class);
                        startActivity(intent);
                        finish();
                    }
                    break;
            }
        }
    }
    //POST请求，带有封装请求参数的请求方式
    private void postRegisterUser(){
        new Thread(){
            @Override
            public void run() {
                String str = "{'uName':'张三','uPwd':'123'}";
                RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN,str);
                Request.Builder builder = new Request.Builder();
                builder.url(url);
                builder.post(body);
                Request request = builder.build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    Log.i("ztl",response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }


}