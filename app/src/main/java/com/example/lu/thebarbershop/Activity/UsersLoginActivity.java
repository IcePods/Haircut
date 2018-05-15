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
    private Button ToRegister;
    private ImageView UserLoginLogo;
    private EditText UserLoginUsername;
    private EditText UserLoginPwd;
    private Button UserLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.users_login_activity);
        getview();
        ToRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(),UsersRegisterActivity.class);
                startActivity(intent);
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
    }
}
