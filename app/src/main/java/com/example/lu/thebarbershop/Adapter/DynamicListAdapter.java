package com.example.lu.thebarbershop.Adapter;

import android.content.Context;
import android.graphics.Point;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lu.thebarbershop.Entity.Dynamic;
import com.example.lu.thebarbershop.Entity.DynamicPicture;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.MyTools.CustomImageView;
import com.example.lu.thebarbershop.R;

import java.net.CookieStore;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by lu on 2018/5/11 0011.
 */

public class DynamicListAdapter extends RecyclerView.Adapter<DynamicListAdapter.Holder> {
    //上下文环境
    private Context context;
    //声明数据源
    private List<Dynamic> dataSource;
    //声明列表项的布局itemID
    //private int item_layout_id;

    //构造方法
    public DynamicListAdapter(){super();}
    public DynamicListAdapter(Context context, List<Dynamic> dataSource) {
        this.context = context;
        this.dataSource = dataSource;
        //this.item_layout_id = item_layout_id;
    }

    @Override
    public Holder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user_dynamic_list, parent, false);
        return new Holder(view);
    }

    /**
     * 给控件赋值
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(Holder holder, int position) {
        final Dynamic dynamic = dataSource.get(position);
        RequestOptions requestOptions = new RequestOptions();
        requestOptions.circleCrop();
        Glide.with(context).load(UrlAddress.url + dynamic.getUser().getUserHeader()).apply(requestOptions).into(holder.DynamicUserHead);
        holder.DynamicUserName.setText(dynamic.getUser().getUserName());
        holder.DynamicContent.setText(dynamic.getDynamicContent());
        holder.DynamicTime.setText(dynamic.getDynamicTime());
        Log.i("DynamicTime",dynamic.toString());
        List<String> imgUrlList = new ArrayList<>();
        for (DynamicPicture dp: dynamic.getDynamicImagePathSet()){
            imgUrlList.add(UrlAddress.url + dp.getDynamicPicture());
        }
        holder.imgView.setUrlList(imgUrlList);
    }

    /**
     * 很重要的一个方法。返回adapter中的包含多少个列表项
     * @return 返回数据源的数量
     */
    @Override
    public int getItemCount() {
        return dataSource.size();
    }

    //获取控件
    class Holder extends RecyclerView.ViewHolder {
        //获取用户头像
        ImageView  DynamicUserHead;
        //互获取动态用户昵称
        TextView DynamicUserName;
        //获取动态内容
        TextView DynamicContent;
        //动态图片列表
        CustomImageView imgView;
        //动态发布时间
        TextView DynamicTime;

        public Holder(View itemView) {
            super(itemView);
            DynamicUserHead = itemView.findViewById(R.id.dynamic_user_head);
            DynamicUserName = itemView.findViewById(R.id.dynamic_user_name);
            DynamicContent = itemView.findViewById(R.id.dynamic_content);
            DynamicTime = itemView.findViewById(R.id.dynamic_time);
            imgView = (CustomImageView) itemView.findViewById(R.id.custom_image);
        }
    }

    public void add(List<Dynamic> addMessageList){
        //增加数据
        int position = dataSource.size();
        dataSource.addAll(position,addMessageList);
        notifyItemInserted(position);
    }

    public void refresh(List<Dynamic> newList){
        //刷新数据
        dataSource = newList;
        notifyDataSetChanged();
    }

}
