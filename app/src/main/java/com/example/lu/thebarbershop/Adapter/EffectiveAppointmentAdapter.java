package com.example.lu.thebarbershop.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.mapapi.map.TextOptions;
import com.bumptech.glide.Glide;
import com.example.lu.thebarbershop.Entity.Appointment;
import com.example.lu.thebarbershop.Entity.Dynamic;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.R;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/5/15 0015.
 */

public class EffectiveAppointmentAdapter extends BaseAdapter {
    //上下文环境
    private Context mContext;
    //声明数据源
    private List<Appointment> appointments;
    //声明列表项的布局itemID
    private int item_layout_id;
    private Button btn_finish;
    private Button btn_cancle;
    OkHttpClient okHttpClient = new OkHttpClient();


    public EffectiveAppointmentAdapter(Context mContext, List<Appointment> appointments, int item_layout_id) {
        this.mContext = mContext;
        this.appointments = appointments;
        this.item_layout_id = item_layout_id;
    }

    //数据源的数量
    @Override
    public int getCount() {
        return appointments.size();
    }

    //返回选择的item项的数据
    @Override
    public Object getItem(int position) {
        return appointments.get(position);
    }

    //返回当前选择了第几个item项
    @Override
    public long getItemId(int position) {
        return position;
    }

    //返回item的布局视图
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView && null != mContext) {
            //获取子项目item的布局文件
            LayoutInflater mInflater = LayoutInflater.from(mContext);
            //布局文件赋值给convertView参数
            convertView = mInflater.inflate(item_layout_id, null);
        }
        //获取布局文件中的控件对象
        ImageView img = convertView.findViewById(R.id.merchant_img);
        TextView merchantName = convertView.findViewById(R.id.appointment_merchant);
        TextView barber = convertView.findViewById(R.id.appointment_barber);
        TextView tel = convertView.findViewById(R.id.appointment_phone);
        TextView time = convertView.findViewById(R.id.appointment_time);
        TextView person = convertView.findViewById(R.id.appointment_person);
        TextView hairstyle = convertView.findViewById(R.id.appointment_hairstyle);
        btn_finish = convertView.findViewById(R.id.btn_appointment_finish);
        btn_cancle = convertView.findViewById(R.id.btn_appointment_cancle);
        ///////////////////////////
        //确认的点击事件--


        //利用传递的数据源给相应的控件对象赋值
        final Appointment effectiveAppointments = appointments.get(position);
        Glide.with(mContext).load(UrlAddress.url+effectiveAppointments.getAppoint_hairStyle().getHairstylePicture()).into(img);
        merchantName.setText(effectiveAppointments.getAppoint_userShopDetail().getShopName());
        barber.setText(effectiveAppointments.getAppoint_barber());
        tel.setText(effectiveAppointments.getAppoint_phone());
        time.setText(effectiveAppointments.getAppoint_time());
        person.setText(effectiveAppointments.getAppoint_username());
        hairstyle.setText(effectiveAppointments.getAppoint_hairStyle().getHairstyleName());

        btn_finish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFinish(effectiveAppointments,"已完成");
                Log.i("effect_id",String.valueOf(effectiveAppointments.getAppoint_id()));
                Toast.makeText(mContext,"完成订单",Toast.LENGTH_SHORT).show();
                appointments.remove(effectiveAppointments);
                notifyDataSetChanged();
            }
        });
        btn_cancle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                clickFinish(effectiveAppointments,"已取消");
                Log.i("effect_id",String.valueOf(effectiveAppointments.getAppoint_id()));
                Toast.makeText(mContext,"取消订单",Toast.LENGTH_SHORT).show();
                appointments.remove(effectiveAppointments);
                notifyDataSetChanged();
            }
        });

        return convertView;
    }
    //确认按钮的单击事件

    public void clickFinish(final Appointment effectiveAppointments,final String state){
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
                builder1.add("Appointment_state",state);
                builder1.add("Appointment_id",String.valueOf(effectiveAppointments.getAppoint_id()));
                FormBody body = builder1.build();
                final Request request = builder.header("UserTokenSql",token).post(body).url(UrlAddress.url+"UpdateAppointmentState.action").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String appointment_finash = response.body().string();
                        Log.i("appointment_finash",appointment_finash);

                    }
                });
            }
        }.start();
    }
}
