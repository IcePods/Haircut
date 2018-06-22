package com.example.lu.thebarbershop.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.Users;
import com.example.lu.thebarbershop.MyTools.UserTokenSql;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import cn.jpush.android.api.JPushInterface;

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
                        JPushInterface.setAlias(getApplicationContext(),0,users.getUserToken());
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
                signIn();
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
    //插入用户信息
    private void insertUserToSql(Users users){
        //如果存在更新用户信息
        if(queryUserToSql(users.getUserAccount())){
            sqLiteDatabase = userTokenSql.getWritableDatabase();
            ContentValues cv = new ContentValues();
            cv.put("userpassword",users.getUserPassword());
            cv.put("username",users.getUserName());
            cv.put("usersex",users.getUserSex());
            cv.put("userphone",users.getUserPhone());
            cv.put("userheader",users.getUserHeader());
            cv.put("usertoken",users.getUserToken());
            long update = sqLiteDatabase.update("user",cv,"useraccount"+"=?",new String[]{users.getUserAccount()});
            Log.i("hzl","user更新成功");
            sqLiteDatabase.close();
        }else{//如果不存在插入
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
            sqLiteDatabase.close();
        }

    }
    //判断本地数据库用户是否已经存在
    private boolean queryUserToSql(String useraccount){
        sqLiteDatabase =new UserTokenSql(this).getReadableDatabase();
        /*Cursor cursor = database.rawQuery("select * from user where usertoken = "+token,null);*/
        Cursor cursor = sqLiteDatabase.query("user",null,"useraccount"+"=?",new String[]{useraccount},null,null,null);
        if(cursor.moveToFirst()){
            sqLiteDatabase.close();
            return true;
        }else {
            sqLiteDatabase.close();
            return false;
        }

    }
    /**
     * 登录方法
     */
    private void signIn() {
        String username = UserLoginUsername.getText().toString().trim();
        String password = UserLoginPwd.getText().toString().trim();
        if (TextUtils.isEmpty(username) || TextUtils.isEmpty(password)) {
            Toast.makeText(UsersLoginActivity.this, "用户名和密码不能为空", Toast.LENGTH_LONG).show();
            return;
        }
        EMClient.getInstance().login(username, password, new EMCallBack() {
            /**
             * 登陆成功的回调
             */
            @Override
            public void onSuccess() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        // 加载所有会话到内存
                        EMClient.getInstance().chatManager().loadAllConversations();
                        // 加载所有群组到内存，如果使用了群组的话
                        // EMClient.getInstance().groupManager().loadAllGroups();
                    }
                });
            }

            /**
             * 登陆错误的回调
             * @param i
             * @param s
             */
            @Override
            public void onError(final int i, final String s) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Log.d("lzan13", "登录失败 Error code:" + i + ", message:" + s);
                        /**
                         * 关于错误码可以参考官方api详细说明
                         * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                         */
                        switch (i) {
                            // 网络异常 2
                            case EMError.NETWORK_ERROR:
                                Toast.makeText(UsersLoginActivity.this, "网络错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的用户名 101
                            case EMError.INVALID_USER_NAME:
                                Toast.makeText(UsersLoginActivity.this, "无效的用户名 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无效的密码 102
                            case EMError.INVALID_PASSWORD:
                                Toast.makeText(UsersLoginActivity.this, "无效的密码 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户认证失败，用户名或密码错误 202
                            case EMError.USER_AUTHENTICATION_FAILED:
                                Toast.makeText(UsersLoginActivity.this, "用户认证失败，用户名或密码错误 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 用户不存在 204
                            case EMError.USER_NOT_FOUND:
                                Toast.makeText(UsersLoginActivity.this, "用户不存在 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 无法访问到服务器 300
                            case EMError.SERVER_NOT_REACHABLE:
                                Toast.makeText(UsersLoginActivity.this, "无法访问到服务器 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 等待服务器响应超时 301
                            case EMError.SERVER_TIMEOUT:
                                Toast.makeText(UsersLoginActivity.this, "等待服务器响应超时 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 服务器繁忙 302
                            case EMError.SERVER_BUSY:
                                Toast.makeText(UsersLoginActivity.this, "服务器繁忙 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            // 未知 Server 异常 303 一般断网会出现这个错误
                            case EMError.SERVER_UNKNOWN_ERROR:
                                Toast.makeText(UsersLoginActivity.this, "未知的服务器异常 code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                            default:
                                Toast.makeText(UsersLoginActivity.this, "ml_sign_in_failed code: " + i + ", message:" + s, Toast.LENGTH_LONG).show();
                                break;
                        }
                    }
                });
            }

            @Override
            public void onProgress(int i, String s) {

            }
        });
    }
}
