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
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.lu.thebarbershop.Entity.Appointment;
import com.example.lu.thebarbershop.Entity.Barber;
import com.example.lu.thebarbershop.Entity.HairStyle;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.MyTools.DateTimePickDialogUtil;
import com.example.lu.thebarbershop.MyTools.GetUserFromShared;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserAppointmentActivity extends AppCompatActivity {
    //全部都是控件
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
    private String masterString;
    private static String[] masterName={};//理发师列表
    private ArrayAdapter<String> arrayAdapter;
    private HairStyle hairStyle;//发型对象
    private UserShopDetail userShopDetail;
    private Appointment appointment;
    private MyOnclickListener myOnclickListener;
    private OkHttpClient okHttpClient;
    private GetUserFromShared getUserFromShared;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_appointment);

        okHttpClient = new OkHttpClient();
        //获取控件
        findview();
        //获取传过来的bundle参数
        getInentData();
        //spinner
        arrayAdapter = new ArrayAdapter<String>(this,R.layout.item_spinner,masterName);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        master.setAdapter(arrayAdapter);
        master.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                 String str=parent.getItemAtPosition(position).toString();
                 masterString=str;
                 Log.i("ztl","你选择了"+str);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        //设置监听器
        Back.setOnClickListener(myOnclickListener);
        Selecttime.setOnClickListener(myOnclickListener);
        Commit.setOnClickListener(myOnclickListener);

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
        getUserFromShared = new GetUserFromShared(getApplicationContext());
        myOnclickListener = new MyOnclickListener();
    }
    private void getInentData(){
        //获取Intent对象传递的参数
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userShopDetail = (UserShopDetail)bundle.getSerializable("shopDetail");
        String[] str = new String[userShopDetail.getBarberSet().size()+1];
        int i=1;
        str[0] = "不选择";
        for (Barber barber:userShopDetail.getBarberSet()){
            str[i] = barber.getUser().getUserName();
            i++;
        }
        masterName = str;
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
                        setAppointment();
                        Gson gson = new GsonBuilder().serializeNulls().create();
                    /*appointment.setAppoint_time(Selecttime.getText().toString().trim());*/
                        String a = gson.toJson(appointment);
                        Log.i("ztl",a);
                        postAppointment(a);
                       /* Intent intent = new Intent();
                        intent.setClass(getApplicationContext(),UserShopDetail.class);
                        startActivity(intent);*/
                        Toast.makeText(getApplicationContext(),"预约成功",Toast.LENGTH_SHORT).show();
                        finish();
                        break;


            }
        }
    }
    private void setAppointment(){
        appointment = new Appointment();
        appointment.setAppoint_username(username.getText().toString().trim());
        appointment.setAppoint_phone(phone.getText().toString().trim());
        appointment.setAppoint_hairStyle(hairStyle);
        appointment.setAppoint_userShopDetail(userShopDetail);
        appointment.setAppoint_barber(masterString);
        appointment.setAppoint_users(null);
        appointment.setAppoint_time(Selecttime.getText().toString().trim());

    }

    private void postAppointment(final String a){
        new Thread(){
            @Override
            public void run() {
                RequestBody body = RequestBody.create(MediaType.parse("text/plain;charset=UTF-8"),a);
                Request.Builder builder = new Request.Builder();
                builder.url(UrlAddress.url+"saveAppointment.action");
                builder.post(body);
                builder.header("UserTokenSQL",getUserFromShared.getUserTokenFromShared());
                Request request = builder.build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {


                    }
                });



            }
        }.start();
    }
}
