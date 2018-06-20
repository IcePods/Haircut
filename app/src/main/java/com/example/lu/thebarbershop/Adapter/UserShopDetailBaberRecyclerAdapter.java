package com.example.lu.thebarbershop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lu.thebarbershop.Entity.Barber;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.R;

import java.util.ArrayList;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by lu on 2018/5/16 0016.
 */

public class UserShopDetailBaberRecyclerAdapter  extends RecyclerView.Adapter<UserShopDetailBaberRecyclerAdapter.ViewHolder> {
    private ArrayList<Barber> mData;
    private Context mContext;
    private MyItemClickListener myItemClickListener;

    public UserShopDetailBaberRecyclerAdapter(ArrayList<Barber> data, Context mContext) {
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
        ViewHolder viewHolder = new ViewHolder(v,myItemClickListener);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // 绑定数据
        holder.baberName.setText(mData.get(position).getUser().getUserName());
        /*RequestOptions requestOptions = new RequestOptions()
                .centerCrop()
                .transform(new CircleCrop());*/
        Glide.with(mContext)
                .load(UrlAddress.url+mData.get(position).getUser().getUserHeader())
                .centerCrop()
                .bitmapTransform(new CropCircleTransformation(mContext))
                .into(holder.header);
        //holder.header.setImageResource(mData.get(position).getBarberImg());
    }

    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
        ImageView header; //recyclerView 中的理发师
        TextView baberName;//理发师姓名

        public ViewHolder(View itemView,MyItemClickListener myItemClickListener) {
            super(itemView);
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
            //理发师头像
            header = itemView.findViewById(R.id.user_shop_detail_baber_item_img);
            //理发师姓名
            baberName = (TextView) itemView.findViewById(R.id.user_shop_detail_baber_name);

        }

        @Override
        public void onClick(View v) {
            if (mListener!=null){
                mListener.onItemClick(v,getPosition());
            }
        }
    }
    /**
     * 创建一个回调接口
     */
    public interface MyItemClickListener {
        void onItemClick(View view, int position);
    }
    /**
     * 在activity里面adapter就是调用的这个方法,将点击事件监听传递过来,并赋值给全局的监听
     *
     * @param myItemClickListener
     */
    public void setMyItemClickListener(MyItemClickListener myItemClickListener) {
        this.myItemClickListener = myItemClickListener;
    }
}
