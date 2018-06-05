package com.example.lu.thebarbershop.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.Users;
import com.example.lu.thebarbershop.MyTools.UserTokenSql;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 个人信息详情-修改昵称
 */
public class UserPersonInformationChangeNicknameActivity extends AppCompatActivity {
    private ImageButton backbutton;//返回按钮
    private Mylistener mylistener;//监听器
    private EditText nickname;
    private String token;
    private Users users;
    private UserTokenSql userTokenSql = new UserTokenSql(this);
    OkHttpClient okHttpClient = new OkHttpClient();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_person_information_change_nickname);

        backbutton = findViewById(R.id.user_person_information_change_nickname_back);
        nickname = findViewById(R.id.user_person_information_nickname_edt);

        //获取User对象
        Intent intent = getIntent();
        token = intent.getStringExtra("token");
      /*  users = (Users) getIntent().getSerializableExtra("users");*/

        mylistener = new Mylistener();

        backbutton.setOnClickListener(mylistener);
    }

    private class Mylistener implements View.OnClickListener {

        @Override
        public void onClick(View v) {
            switch (v.getId()) {
                //返回按钮
                case R.id.user_person_information_change_nickname_back:
                    //只销毁当前页面
                    String name = nickname.getText().toString();
                   /* users.setUserName(name);
                    Gson gson = new Gson();
                    String gsonChangeName = gson.toJson(users);
                    postchangeUserAttribute(gsonChangeName);
*/
                    finish();
                    break;
            }
        }
    }

    private void changeNickName(String name) {
       /* Intent intent = getIntent();
        String token = intent.getStringExtra("token");
        Users users = (Users) getIntent().getSerializableExtra("users");
        users.setUserName(name);*/
        ContentValues contentValues = new ContentValues();
        contentValues.put("username", name);
        userTokenSql.getWritableDatabase().update("user", contentValues, "usertoken=?", new String[]{token});

    }

    private void postchangeUserAttribute(final String gsonUser) {
        new Thread() {
            @Override
            public void run() {

                FormBody.Builder builder = new FormBody.Builder();
                builder.add("changeUserAttribute", gsonUser);
                FormBody body = builder.build();
                Request request = new Request.Builder().post(body).url(UrlAddress.url + ".action").build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String a = response.body().string();
                    Gson gson = new Gson();
                    Users users = gson.fromJson(a,Users.class);
                    changeNickName(users.getUserName());
                  /*  Message message =Message.obtain();
                    message.what =1;
                    Bundle bundle =new Bundle();
                    bundle.putString("user",a);
                    message.setData(bundle);
                    handler.sendMessage(message);*/

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
