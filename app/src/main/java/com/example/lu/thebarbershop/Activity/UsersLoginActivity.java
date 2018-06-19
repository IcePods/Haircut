package com.example.lu.thebarbershop.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.Users;
import com.example.lu.thebarbershop.Fragment.PersonFragment;
import com.example.lu.thebarbershop.MyTools.RecordSQLiteOpenHelper;
import com.example.lu.thebarbershop.MyTools.UserTokenSql;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UsersLoginActivity extends AppCompatActivity {
    private Button ToRegister;//跳转注册button
    private ImageView UserLoginLogo;//登录页面logo
    private EditText UserLoginUsername;//登录用户名
    private EditText UserLoginPwd;//登录密码
    private Button UserLogin;//登录按钮
    private Mylistener mylistener;//监听器
    private TextView errorMessage;
    private UserTokenSql userTokenSql = new UserTokenSql(this);//数据库连接
    private SQLiteDatabase sqLiteDatabase;

    /* private final String url = "http://192.168.155.3:8080/theBarberShopServers/loginCheck.action";*/
    OkHttpClient okHttpClient;
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/plain;charset=UTF-8");

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    String u = bundle.getString("user");
                    Log.i("loginback",u);
                    Users users = new Users();
                    Gson gson = new Gson();
                    users = gson.fromJson(u,Users.class);
                   /* Log.i("loginback",users.getUserAccount());*/
                    if(users.getUserCondition()==true){
                        SharedPreferences sharedPreferences= getSharedPreferences("usertoken", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor =sharedPreferences.edit();
                        editor.putString("token",users.getUserToken());
                        editor.putString("userAccount",users.getUserAccount());
                        editor.putString("userPassword",users.getUserPassword());
                        editor.commit();
                        //登录成功插入数据库
                        insertUserToSql(users);
                        setResult(2);
                        finish();


                    }else {
                        errorMessage.setText("用户名密码错误");
                        UserLoginUsername.setText("");
                        UserLoginPwd.setText("");
                    }

                    break;
            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_login_activity);
        //获取控件
        getview();
        //实现监听控件
        okHttpClient = new OkHttpClient();
        mylistener = new Mylistener();
        ToRegister.setOnClickListener(mylistener);
        UserLogin.setOnClickListener(mylistener);
        UserLoginUsername.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                }else{
                    String str =UserLoginUsername.getText().toString().trim();
                    if (str.isEmpty()){
                        errorMessage.setText("请输入用户名");
                    }else if (!str.isEmpty()){
                        errorMessage.setText("");
                    }
                }
            }
        });
        UserLoginPwd.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus){

                }else{
                    String str =UserLoginPwd.getText().toString().trim();
                    if (str.isEmpty()){
                        errorMessage.setText("请输入密码");
                    }else if (!str.isEmpty()){
                        errorMessage.setText("");
                    }
                }
            }
        });


    }
//获取控件
    private void getview() {
        ToRegister = findViewById(R.id.users_login_register);
        UserLoginLogo = findViewById(R.id.users_login_logo);
        UserLoginUsername = findViewById(R.id.users_login_username);
        UserLoginPwd = findViewById(R.id.users_login_pwd);
        UserLogin = findViewById(R.id.users_login_login);
        errorMessage = findViewById(R.id.login_error_message);
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
                finish();
                break;
            case R.id.users_login_login:
                postLoginUser(UserLoginUsername.getText().toString().trim(),UserLoginPwd.getText().toString().trim());
               /* intent.setClass(getApplicationContext(),UserPersonInformationActivity.class);
                startActivity(intent);
                finish();*/
                break;
        }
    }
}
    //POST请求，带有封装请求参数的请求方式
    private void postLoginUser(final String UserAccount, final String UserPassword){
        new Thread(){
            @Override
            public void run() {

               /* RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN,a);
                Request.Builder builder = new Request.Builder();
                builder.url(url);
                builder.post(body);
                Request request = builder.build();*/
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("UserAccount",UserAccount);
                builder.add("UserPassword",UserPassword);
                FormBody body = builder.build();
                Request request = new Request.Builder().post(body).url(UrlAddress.url+"loginCheck.action").build();
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
    private void insertUserToSql(Users users){
        sqLiteDatabase = userTokenSql.getReadableDatabase();
        Log.i("hzl","logininsert"+users.toString());

        ContentValues cv = new ContentValues();
        cv.put("useraccount",users.getUserAccount());
        cv.put("userpassword",users.getUserPassword());
        cv.put("username",users.getUserName());
        cv.put("usersex",users.getUserSex());
        cv.put("userphone",users.getUserPhone());
        cv.put("userheader",users.getUserHeader());
        cv.put("usertoken",users.getUserToken());
        long insert =  sqLiteDatabase.insert("user",null,cv);
        Log.i("insert111",insert+"");
        Log.i("hzl","user插入成功");
    }
}
