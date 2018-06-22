package com.example.lu.thebarbershop.Fragment;


import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.app.Fragment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.example.lu.thebarbershop.Activity.UserPersonAppointmentActivity;
import com.example.lu.thebarbershop.Adapter.InvalidAppointmentAdapter;
import com.example.lu.thebarbershop.Entity.Appointment;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressLint("ValidFragment")
public class UserPersonAppointmentInvalidFragment extends Fragment {
    private ListView appointmentInvalidList;//失效预约列表
    private Context mContext;
    OkHttpClient okHttpClient = new OkHttpClient();
    private List<Appointment> appointInvaList;

    @SuppressLint("ValidFragment")
    public UserPersonAppointmentInvalidFragment(Context mContext) {
        this.mContext = mContext;
    }
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    String appointlist = bundle.getString("appoint");
                    Log.i("appoint",appointlist);
                    Gson gson = new Gson();
                    appointInvaList = gson.fromJson(appointlist,new TypeToken<List<Appointment>>(){}.getType());
                    Log.i("appoint",appointInvaList.toString());
                    initAdapter();
                    break;

            }
            super.handleMessage(msg);
        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_user_person_appointment_invalid, container, false);
        appointmentInvalidList = view.findViewById(R.id.lv_appointment_invalid);


        //失效预约item的点击方法
        appointmentInvalidList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });

        getInvaAppointment();

        return view;
    }

    private void initAdapter() {
        final InvalidAppointmentAdapter adapter = new InvalidAppointmentAdapter(getActivity().getApplicationContext(),appointInvaList, R.layout.item_user_appointment_invalid);
        appointmentInvalidList.setAdapter(adapter);
    }

    /*//静态数据模拟
    private List<Map<String,Object>> prepareDatas() {
        List<Map<String,Object>> appointments = new ArrayList<>();
        //第一个商品
        Map<String,Object> appointments1 = new HashMap<>();
        appointments1.put("img",R.mipmap.user_person_headimg);
        appointments1.put("merchantName","美轮美奂理发店");
        appointments1.put("barber","John");
        appointments1.put("tel","123456789");
        appointments1.put("time","2017-5-6 7:00");
        appointments1.put("person","张三123");
        appointments1.put("hairstyle","经典烫发");
        appointments.add(appointments1);

        Map<String,Object> appointments2 = new HashMap<>();
        appointments2.put("img",R.mipmap.user_person_headimg);
        appointments2.put("merchantName","美轮美奂理发店");
        appointments2.put("barber","John");
        appointments2.put("tel","123456789");
        appointments2.put("time","2017-5-6 7:00");
        appointments2.put("person","张三123");
        appointments2.put("hairstyle","经典烫发");
        appointments.add(appointments2);

        return appointments;
    }*/
    public void getInvaAppointment(){
        SharedPreferences sharedPreferences = mContext.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("token","");
        Log.i("hzl",token);
        final String userAccount = sharedPreferences.getString("userAccount","");
        final String userPassword = sharedPreferences.getString("userPassword","");
        new Thread(){
            @Override
            public void run() {
                Request.Builder builder = new Request.Builder();
                FormBody.Builder builder1 = new FormBody.Builder();
                builder1.add("Appointment_state","无效");
                FormBody body = builder1.build();
                final Request request = builder.header("UserTokenSql",token).post(body).url(UrlAddress.url+"showAllAppointment.action").build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String a = response.body().string();
                    Log.i("appoint",a);
                    Message message = Message.obtain();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("appoint",a);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    @Override
    public void onResume() {
        super.onResume();
        getInvaAppointment();
    }
}
