package com.example.lu.thebarbershop.Adapter;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lu.thebarbershop.R;

import java.util.List;
import java.util.Map;

/**
 * Created by shan on 2018/5/11.
 */

public class CollectionAdapter extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<Map<String, Object>> favouriteshops;

    public CollectionAdapter(Context mContext, int mLayout, List<Map<String, Object>> favouriteshops) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.favouriteshops = favouriteshops;
    }

    //数据源的数量
    @Override
    public int getCount() {
        return favouriteshops.size();
    }

    //返回选择的item项的数据
    @Override
    public Object getItem(int position) {
        return favouriteshops.get(position);
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
        ImageView img = convertView.findViewById(R.id.user_person_collection_shop_headr_img);
        TextView name = convertView.findViewById(R.id.user_person_collection_shop_name);
        TextView address = convertView.findViewById(R.id.user_person_collection_shop_address);
        //设置删除按钮
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(1, Color.BLACK); // 边框粗细及颜色
        drawable.setColor(0x22ffffff); // 边框内部颜色
        drawable.setCornerRadius(45);

        Button button = convertView.findViewById(R.id.user_person_collection_shop_Fdelete);

        button.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        //
        //利用传递的数据源给相应的控件对象赋值
        Map<String, Object> shops = favouriteshops.get(position);
        img.setImageResource((Integer) shops.get("img"));
        name.setText((String) shops.get("name"));
        address.setText((String) shops.get("address"));
        //返回子项目布局视图
        return convertView;
    }
}
