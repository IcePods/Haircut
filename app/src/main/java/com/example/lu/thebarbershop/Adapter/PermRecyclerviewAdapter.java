package com.example.lu.thebarbershop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lu.thebarbershop.Entity.HairStyle;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.R;

import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;

/**
 * Created by shan on 2018/5/18.
 */

public class PermRecyclerviewAdapter extends RecyclerView.Adapter{
    private Context mContext;
    private int mLayout;
    private List<HairStyle> hairs;
    private MyItemClickListener mItemClickListener;

    public PermRecyclerviewAdapter(Context mContext, int mLayout, List<HairStyle> hairs) {
        this.mContext = mContext;
        this.mLayout = mLayout;
        this.hairs = hairs;
    }

    @Override //将ItemView渲染进来，创建ViewHolder
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(mLayout,null);
        return new MyViewHolder(view,mItemClickListener);
    }

    //将数据源的数据绑定到相应控件上
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        MyViewHolder holder2 = (MyViewHolder) holder;
        HairStyle hairStyle = hairs.get(position);
//        Uri uri = Uri.parse(hairStyle.getHairstylePicture());
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        Glide.with(mContext)
                .load(UrlAddress.url+hairStyle.getHairstylePicture())
                .apply(requestOptions)
                .into(holder2.hairstyleimg);
        holder2.hairstylename.setText(hairStyle.getHairstyleName());
    }

    @Override
    public int getItemCount() {
        if (hairs != null) {
            return hairs.size();
        }
        return 0;
    }

    //定义自己的ViewHolder，将View的控件引用在成员变量上
    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private MyItemClickListener mListener;
//        public SimpleDraweeView simpleDraweeView;
        public ImageView hairstyleimg;
        public TextView hairstylename;

        public MyViewHolder(View itemView, MyItemClickListener myItemClickListener) {
            super(itemView);
            //将全局的监听赋值给接口
            this.mListener = myItemClickListener;
            itemView.setOnClickListener(this);
//            simpleDraweeView = (SimpleDraweeView) itemView.findViewById(R.id.user_main_perm_simpledraweeview);
            hairstyleimg = itemView.findViewById(R.id.user_main_perm_img);
            hairstylename = itemView.findViewById(R.id.user_main_perm_hairstylename);

        }
        /**
         * 实现OnClickListener接口重写的方法
         * @param v
         */
        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
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
    public void setItemClickListener(MyItemClickListener myItemClickListener) {
        this.mItemClickListener = myItemClickListener;
    }
}
