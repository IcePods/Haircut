package com.example.lu.thebarbershop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.lu.thebarbershop.Entity.Barber;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.R;

import java.util.ArrayList;

/**
 * Created by lu on 2018/5/16 0016.
 */

public class UserShopDetailBaberRecyclerAdapter  extends RecyclerView.Adapter<UserShopDetailBaberRecyclerAdapter.ViewHolder> {
    private ArrayList<Barber> mData;
    private Context mContext;

    public UserShopDetailBaberRecyclerAdapter(ArrayList<Barber> data,Context mContext) {
        this.mData = data;
        this.mContext = mContext;
    }

    public void updateData(ArrayList<Barber> data) {
        this.mData = data;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // 实例化展示的view
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_user_shop_detail_baber, parent, false);
        // 实例化viewholder
        ViewHolder viewHolder = new ViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        holder.baberName.setText(mData.get(position).getUser().getUserName());
        RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .transform(new CircleCrop());
        Glide.with(mContext)
                .load(UrlAddress.url+mData.get(position).getUser().getUserHeader())
                .apply(requestOptions)
                .into(holder.header);
        //holder.header.setImageResource(mData.get(position).getBarberImg());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        ImageView header; //recyclerView 中的理发师
        TextView baberName;//理发师姓名

        public ViewHolder(View itemView) {
            super(itemView);
            //理发师头像
            header = itemView.findViewById(R.id.user_shop_detail_baber_item_img);
            //理发师姓名
            baberName = (TextView) itemView.findViewById(R.id.user_shop_detail_baber_name);
        }
    }
}
