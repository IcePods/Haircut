package com.example.lu.thebarbershop.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.lu.thebarbershop.R;

/**
 * 关于我们页面
 */
public class UserPersonAboutsUsActivity extends AppCompatActivity {
    private ImageButton backbutton;//返回按钮
    private Mylistener mylistener;//监听器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_person_abouts_us);

        backbutton = findViewById(R.id.user_person_about_us_back);

        mylistener = new Mylistener();

        backbutton.setOnClickListener(mylistener);
    }
    private class Mylistener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //返回按钮
                case R.id.user_person_about_us_back:
                    //只销毁当前页面
                    finish();
                    break;
            }
        }
    }
}
