package com.example.lu.thebarbershop.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
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
import android.widget.Toast;

import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.Users;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hyphenate.EMError;
import com.hyphenate.chat.EMClient;
import com.hyphenate.exceptions.HyphenateException;

import net.qiujuer.genius.ui.widget.Loading;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UsersRegisterActivity extends AppCompatActivity {

    // 弹出框
    private ProgressDialog mDialog;
    private TextView ToLogin;

    private EditText UserRegisterUsername;
    private EditText UserRegisterPwd;
    private EditText UserRegisterPwd2;

    private Button UserRegister;
    private Mylistener mylistener;


    private Loading loading;
    OkHttpClient okHttpClient;
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/plain;charset=UTF-8");

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    String u = bundle.getString("user");
                    Log.i("ztlhandler1",u);
                    Users users = new Users();
                    Gson gson = new Gson();
                    users = gson.fromJson(u,Users.class);
                    if(users.getUserCondition() == null){
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(),UsersLoginActivity.class);
                        startActivity(intent);
                        finish();

                    }else if(users.getUserCondition()==false){
                        showError();
                        Toast.makeText(getApplicationContext(),"该账户已注册！！！",Toast.LENGTH_SHORT).show();
                    }

                   /* Log.i("ztlhandler2",users.getUserAccount());*/

                break;
            }
            super.handleMessage(msg);
        }
    };
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
                        Toast.makeText(getApplicationContext(),"账户不能为空",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
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
                         Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                    }else if (!str1.equals(str2)){
                         Toast.makeText(getApplicationContext(),"两次密码输入不一致",Toast.LENGTH_SHORT).show();
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

        UserRegisterUsername = findViewById(R.id.user_register_username);
        UserRegisterPwd = findViewById(R.id.user_register_pwd);
        UserRegisterPwd2 = findViewById(R.id.user_register_pwd2);

        UserRegister = findViewById(R.id.user_register);

        loading = findViewById(R.id.register_loading);
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
                        Toast.makeText(getApplicationContext(),"账户不能为空",Toast.LENGTH_SHORT).show();
                    }else if (registerPwd.isEmpty()){
                        Toast.makeText(getApplicationContext(),"密码不能为空",Toast.LENGTH_SHORT).show();
                    }else if (!registerPwd.equals(registerPwd2)){
                        Toast.makeText(getApplicationContext(),"两次密码输入不一致",Toast.LENGTH_SHORT).show();
                    }else {
                        showLoading();
                        signUp();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                        Users user = new Users();
                        user.setUserAccount(registerUsername);
                        user.setUserPassword(registerPwd);
                        String RegisterJson = gson.toJson(user);
                        Log.i("ztlto",RegisterJson);
                        postRegisterUser(RegisterJson);
                       /* intent.setClass(getApplicationContext(),UsersLoginActivity.class);
                        startActivity(intent);
                        finish();*/
                    }
                    break;
            }
        }
    }

    //POST请求，带有封装请求参数的请求方式
    private void postRegisterUser(final String a){
        new Thread(){
            @Override
            public void run() {

                RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN,a);
                Request.Builder builder = new Request.Builder();
                builder.url(UrlAddress.url+"register.action");
                builder.post(body);
                Request request = builder.build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String a = response.body().string();
                        Users users =new Users();
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
    /**
     * 注册方法
     */
    private void signUp() {
        // 注册是耗时过程，所以要显示一个dialog来提示下用户
        mDialog = new ProgressDialog(this);
        mDialog.setMessage("注册中，请稍后...");
        mDialog.show();

        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String username = UserRegisterUsername.getText().toString().trim();
                    String password = UserRegisterPwd.getText().toString().trim();
                    EMClient.getInstance().createAccount(username, password);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!UsersRegisterActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                        }
                    });
                } catch (final HyphenateException e) {
                    e.printStackTrace();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!UsersRegisterActivity.this.isFinishing()) {
                                mDialog.dismiss();
                            }
                            /**
                             * 关于错误码可以参考官方api详细说明
                             * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1_e_m_error.html
                             */
                            int errorCode = e.getErrorCode();
                            String message = e.getMessage();
                            Log.d("lzan13", String.format("sign up - errorCode:%d, errorMsg:%s", errorCode, e.getMessage()));
                            switch (errorCode) {
                                // 网络错误
                                case EMError.NETWORK_ERROR:
                                    Toast.makeText(UsersRegisterActivity.this, "网络错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 用户已存在
                                case EMError.USER_ALREADY_EXIST:
                                    Toast.makeText(UsersRegisterActivity.this, "用户已存在 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册
                                case EMError.USER_ILLEGAL_ARGUMENT:
                                    Toast.makeText(UsersRegisterActivity.this, "参数不合法，一般情况是username 使用了uuid导致，不能使用uuid注册 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                // 服务器未知错误
                                case EMError.SERVER_UNKNOWN_ERROR:
                                    Toast.makeText(UsersRegisterActivity.this, "服务器未知错误 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                case EMError.USER_REG_FAILED:
                                    Toast.makeText(UsersRegisterActivity.this, "账户注册失败 code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                                default:
                                    Toast.makeText(UsersRegisterActivity.this, "ml_sign_up_failed code: " + errorCode + ", message:" + message, Toast.LENGTH_LONG).show();
                                    break;
                            }
                        }
                    });
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }).start();
    }

    private void showLoading(){
        loading.start();
        UserRegisterUsername.setEnabled(false);
        UserRegisterPwd.setEnabled(false);
        UserRegisterPwd2.setEnabled(false);
        UserRegister.setEnabled(false);
    }

    private void showError(){
        loading.stop();
        UserRegisterUsername.setEnabled(true);
        UserRegisterPwd.setEnabled(true);
        UserRegisterPwd2.setEnabled(true);
        UserRegister.setEnabled(true);
    }
}