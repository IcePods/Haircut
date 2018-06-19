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
import android.widget.Toast;

import com.example.lu.thebarbershop.Activity.UserPersonAppointmentActivity;
import com.example.lu.thebarbershop.Adapter.EffectiveAppointmentAdapter;
import com.example.lu.thebarbershop.Entity.Appointment;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserCollections;
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
public class UserPersonAppointmentEffectiveFragment extends Fragment {
    private ListView appointmentEffectiveList;//有效预约列表
    private Context mContext;
    OkHttpClient okHttpClient = new OkHttpClient();
    private List<Appointment> appointList;

    @SuppressLint("ValidFragment")
    public UserPersonAppointmentEffectiveFragment(Context mContext) {
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
                    appointList = gson.fromJson(appointlist,new TypeToken<List<Appointment>>(){}.getType());

                    Log.i("appoint",appointList.toString());
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
        View view = inflater.inflate(R.layout.fragment_user_person_appointment_effective, container, false);
        appointmentEffectiveList = view.findViewById(R.id.lv_appointment_effective);
        mContext = getActivity().getApplicationContext();


        getAppointment();
        //有效预约item点击方法
        appointmentEffectiveList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                /*Toast.makeText(getActivity().getApplicationContext(),"test",Toast.LENGTH_SHORT).show();*/
            }
        });

        return view;
    }

    private void initAdapter() {
        final EffectiveAppointmentAdapter adapter = new EffectiveAppointmentAdapter(getActivity().getApplicationContext(),appointList, R.layout.item_user_appointment_effective);
        appointmentEffectiveList.setAdapter(adapter);
        appointmentEffectiveList.setDivider(null);
    }


    public void getAppointment(){
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
                builder1.add("UserAccount",userAccount);
                builder1.add("UserPassword",userPassword);
                builder1.add("Appointment_state","进行中");
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

}
