package com.example.lu.thebarbershop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.lu.thebarbershop.Entity.ShopActivity;
import com.example.lu.thebarbershop.R;
import java.util.List;

/**
 * Created by lu on 2018/6/22 0022.
 */

public class ShopActivityAdapter extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<ShopActivity> shopActivities;

    public ShopActivityAdapter(Context mContext, int mLayout, List<ShopActivity> shopActivities) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.shopActivities = shopActivities;
    }

    //数据源的数量
    @Override
    public int getCount() {
        return shopActivities.size();
    }

    //返回选择的item项的数据
    @Override
    public Object getItem(int position) {
        return shopActivities.get(position);
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
            convertView = mInflater.inflate(mLayout, null);
        }
        //获取布局文件中的控件对象

        TextView Activity_Name = convertView.findViewById(R.id.item_user_activity_name);
        TextView Activity_Content = convertView.findViewById(R.id.item_user_activity_content);
        TextView Activity_Time = convertView.findViewById(R.id.item_user_activity_time);

       /* RequestOptions requestOptions = new RequestOptions().centerCrop();
        requestOptions.placeholder(R.mipmap.user_index_perm);
        Glide.with(mContext)
                .load(UrlAddress.url+shops.getShopPicture())
                .apply(requestOptions)
                .into(img);*/

        Activity_Name.setText(shopActivities.get(position).getActivityName());
        Activity_Content.setText(shopActivities.get(position).getActivityContent());
        Activity_Time.setText(shopActivities.get(position).getActivityStartTime()+"至"+shopActivities.get(position).getActivityEndTime());
        //返回子项目布局视图
        return convertView;
    }
}
