package com.example.lu.thebarbershop.Adapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.R;

import java.io.IOException;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by shan on 2018/5/11.
 */

public class SearchResultAdapter extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<UserShopDetail> favouriteshops;
    private UserShopDetail shops;

    public SearchResultAdapter(Context mContext, int mLayout, List<UserShopDetail> favouriteshops) {
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
        final UserShopDetail userShopDetail = favouriteshops.get(position);


        //利用传递的数据源给相应的控件对象赋值
        shops = favouriteshops.get(position);
        /*RequestOptions requestOptions = new RequestOptions().centerCrop();
        requestOptions.placeholder(R.mipmap.user_index_perm);*/
        Glide.with(mContext)
                .load(shops.getShopPicture())
                .placeholder(R.mipmap.user_index_perm)
                .centerCrop()
                .into(img);
        String str = shops.getShopAddress();
        if(str.length() > 10){
            str = str.substring(0,10)+"...";
        }
        name.setText(shops.getShopName());
        address.setText(str);
        //返回子项目布局视图
        return convertView;
    }

}