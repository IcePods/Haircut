package com.example.lu.thebarbershop.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lu.thebarbershop.Entity.HairStyle;
import com.example.lu.thebarbershop.MyTools.DateTimePickDialogUtil;
import com.example.lu.thebarbershop.R;

public class UserAppointmentActivity extends AppCompatActivity {
    private ImageView HeaderPic;
    private ImageButton Back;
    private TextView Title;
    private TextView Price;
    private TextView Description;
    private EditText username;
    private EditText phone;
    private TextView Selecttime;
    private Spinner master;
    private Button Commit;
    private String initTime = "2018年6月8日 00:00"; // 初始化开始时间
    private static final String[] masterName={"不选择","张","王","李","赵"};
    private ArrayAdapter<String> arrayAdapter;
    private HairStyle hairStyle;
    private MyOnclickListener myOnclickListener;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment);

        //获取控件
        findview();
        //设置监听器
        Back.setOnClickListener(myOnclickListener);
        Selecttime.setOnClickListener(myOnclickListener);
        Commit.setOnClickListener(myOnclickListener);
        //spinner
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.item_spinner,masterName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        master.setAdapter(arrayAdapter);
        master.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 String str=parent.getItemAtPosition(position).toString();
                 Log.i("ztl","你选择了"+str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                Log.i("ztl","你爱的还是我");
            }
        });
        //获取传过来的bundle参数
        getInentData();
    }

    private void findview() {
        HeaderPic = findViewById(R.id.user_appointment_img);
        Back = findViewById(R.id.user_appointment_back);
        Title = findViewById(R.id.user_appointment_title);
        Price = findViewById(R.id.user_appointment_price);
        Description = findViewById(R.id.user_appointment_description);
        username = findViewById(R.id.user_appointment_username);
        phone = findViewById(R.id.user_appointment_phone);
        master = findViewById(R.id.user_appointment_master);
        Selecttime = findViewById(R.id.user_appointment_time);
        Commit = findViewById(R.id.user_appointment_commit);
        myOnclickListener = new MyOnclickListener();
    }
    private void getInentData(){
        //获取Intent对象传递的参数
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        hairStyle = (HairStyle)bundle.getSerializable("hairStyleDetail");
        Glide.with(getApplicationContext())
                .load(hairStyle.getHairstylePicture())
                .into(HeaderPic);
        Title.setText(hairStyle.getHairstyleName());
        Description.setText(hairStyle.getHairstyleIntroduce());
    }
    private class MyOnclickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.user_appointment_back:
                    finish();
                    break;
                case R.id.user_appointment_time:
                    DateTimePickDialogUtil dateTimePickDialogUtil = new DateTimePickDialogUtil(
                            UserAppointmentActivity.this,initTime);
                    dateTimePickDialogUtil.dateTimePicKDialog(Selecttime);
                    break;
                case R.id.user_appointment_commit:
                    Intent i = new Intent();
                    i.setClass(getApplicationContext(),MainActivity.class);
                    startActivity(i);
                    break;
            }
        }
    }
}
