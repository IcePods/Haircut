package com.example.lu.thebarbershop.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;

import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.Users;
import com.example.lu.thebarbershop.MyTools.GetUserFromShared;
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
 * 个人信息详情-修改电话
 */
public class UserPersonInformationChangePhneActivity extends AppCompatActivity {
    private ImageButton backbutton;//返回按钮
    private Mylistener mylistener;//监听器
    private Button save;
    private EditText phne;
    private String token;
    private Users users;
    private UserTokenSql userTokenSql = new UserTokenSql(this);
    private SQLiteDatabase sqLiteDatabase;


    OkHttpClient okHttpClient = new OkHttpClient();

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    String u= bundle.getString("user");
                    Gson gson = new Gson();
                    users = gson.fromJson(u,Users.class);
                    sqLiteDatabase = userTokenSql.getWritableDatabase();
                    ContentValues contentValues = new ContentValues();
                    contentValues.put("userphone",users.getUserPhone());
                    sqLiteDatabase.update("user",contentValues,"usertoken =?",new String[]{users.getUserToken()});
                    sqLiteDatabase.close();



            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_person_information_change_phne);

        backbutton = findViewById(R.id.user_person_information_change_phne_back);
        save = findViewById(R.id.user_person_information_save_phne_btn);
        phne = findViewById(R.id.user_person_information_phne_edt);

        mylistener = new Mylistener();

        backbutton.setOnClickListener(mylistener);
        save.setOnClickListener(mylistener);
    }
    private class Mylistener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //返回按钮
                case R.id.user_person_information_change_phne_back:
                    //只销毁当前页面
                    finish();
                    break;
                case R.id.user_person_information_save_phne_btn:
                    String p = phne.getText().toString().trim();
                    postchangeUserAttribute(p);
                    finish();
                    break;
            }
        }
    }

    private void postchangeUserAttribute(final String gsonUser) {
        new Thread() {
            @Override
            public void run() {

                FormBody.Builder builder = new FormBody.Builder();
                builder.add("userPhone", gsonUser);
                FormBody body = builder.build();
                Request request = new Request.Builder().header("UserTokenSQL",new GetUserFromShared(getApplicationContext()).getUserTokenFromShared()).post(body).url(UrlAddress.url + "changeUserAttribute.action").build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String a = response.body().string();
                    Message message =Message.obtain();
                    message.what =1;
                    Bundle bundle =new Bundle();
                    bundle.putString("user",a);
                    message.setData(bundle);
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
}
