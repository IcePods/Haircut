package com.example.lu.thebarbershop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lu.thebarbershop.Entity.Appointment;
import com.example.lu.thebarbershop.R;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/16 0016.
 */

public class InvalidAppointmentAdapter extends BaseAdapter {
    //上下文环境
    private Context mContext;
    //声明数据源
    private List<Appointment> appointments;
    //声明列表项的布局itemID
    private int item_layout_id;

    public InvalidAppointmentAdapter(Context mContext, List<Appointment> appointments, int item_layout_id) {
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
        ImageView img = convertView.findViewById(R.id.invalid_merchant_img);
        TextView merchantName = convertView.findViewById(R.id.appointment_invalid_merchant);
        TextView barber = convertView.findViewById(R.id.appointment_invalid_barber);
        TextView tel = convertView.findViewById(R.id.appointment_invalid_phone);
        TextView time = convertView.findViewById(R.id.appointment_invalid_time);
        TextView person = convertView.findViewById(R.id.appointment_invalid_person);
        TextView hairstyle = convertView.findViewById(R.id.appointment_invalid_hairstyle);
        TextView state = convertView.findViewById(R.id.state);

        //利用传递的数据源给相应的控件对象赋值
        Appointment invalidAppointments = appointments.get(position);
       /* img.setImageResource((Integer) invalidAppointments.get("img"));
        merchantName.setText((String) invalidAppointments.get("merchantName"));
        barber.setText((String) invalidAppointments.get("barber"));
        tel.setText((String) invalidAppointments.get("tel"));
        time.setText((String) invalidAppointments.get("time"));
        person.setText((String) invalidAppointments.get("person"));
        hairstyle.setText((String) invalidAppointments.get("hairstyle"));*/
        Glide.with(mContext).load(invalidAppointments.getAppoint_hairStyle().getHairstylePicture()).into(img);
        merchantName.setText(invalidAppointments.getAppoint_userShopDetail().getShopName());
        barber.setText(invalidAppointments.getAppoint_barber());
        tel.setText(invalidAppointments.getAppoint_phone());
        time.setText(invalidAppointments.getAppoint_time());
        person.setText(invalidAppointments.getAppoint_username());
        hairstyle.setText(invalidAppointments.getAppoint_hairStyle().getHairstyleName());
        state.setText(invalidAppointments.getAppoint_state());

        return convertView;
    }

}
