package com.example.lu.thebarbershop.Activity;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lu.thebarbershop.MyTools.GetRoundedCornerBitmap;
import com.example.lu.thebarbershop.R;

public class UserPersonInformationActivity extends AppCompatActivity {
    private ImageView imageView;//头像
    private ImageButton backbutton;//返回按钮
    private ImageButton nicknamebutton;//修改昵称按钮
    private ImageButton sexbutton;//修改性别按钮
    private ImageButton phnebutton;//修改电话按钮
    private Mylistener mylistener;//监听器
    private Button exittologin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_person_information);

        imageView = findViewById(R.id.user_person_informatin_header_img);
        backbutton = findViewById(R.id.user_person_informatin_back);
        nicknamebutton = findViewById(R.id.arrowtip48_1);
        sexbutton = findViewById(R.id.arrowtip48_2);
        phnebutton = findViewById(R.id.arrowtip48_3);
        exittologin = findViewById(R.id.user_person_information_exit);

        mylistener = new Mylistener();

        imageView.setImageBitmap(GetRoundedCornerBitmap.getRoundedCornerBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap(),2));
        backbutton.setOnClickListener(mylistener);
        nicknamebutton.setOnClickListener(mylistener);
        sexbutton.setOnClickListener(mylistener);
        phnebutton.setOnClickListener(mylistener);
        exittologin.setOnClickListener(mylistener);
    }
    private class Mylistener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //1. 创建Intent对象
            Intent intent = new Intent();
            switch (v.getId()){
                //返回按钮
                case R.id.user_person_informatin_back:
                    //只销毁当前页面
                    finish();
                    break;
                //修改昵称
                case R.id.arrowtip48_1:
                    //只实现跳转
                    //2. 指定跳转路线
                    intent.setClass(getApplicationContext(),UserPersonInformationChangeNicknameActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                //修改性别
                case R.id.arrowtip48_2:
                    //只实现跳转
                    //2. 指定跳转路线
                    intent.setClass(getApplicationContext(),UserPersonInformationChangeSexActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                //修改电话
                case R.id.arrowtip48_3:
                    //只实现跳转
                    //2. 指定跳转路线
                    intent.setClass(getApplicationContext(),UserPersonInformationChangePhneActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                case R.id.user_person_information_exit:
                    //只实现跳转，跳转到登录页面
                    //跳转路线
                    intent.setClass(getApplicationContext(),UsersLoginActivity.class);
                    //进行跳转
                    startActivity(intent);
                    break;
            }
        }
    }
}