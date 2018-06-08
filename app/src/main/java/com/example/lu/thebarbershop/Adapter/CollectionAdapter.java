package com.example.lu.thebarbershop.Adapter;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
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
 * Created by shan on 2018/5/11.
 */

public class CollectionAdapter extends BaseAdapter {
    private Context mContext;
    private int mLayout;
    private List<UserShopDetail> favouriteshops;
    OkHttpClient okHttpClient = new OkHttpClient();
    private UserShopDetail shop;

    public CollectionAdapter(Context mContext, int mLayout, List<UserShopDetail> favouriteshops) {
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
        shop = favouriteshops.get(position);

        //设置删除按钮
        GradientDrawable drawable = new GradientDrawable();
        drawable.setShape(GradientDrawable.RECTANGLE); // 画框
        drawable.setStroke(1, Color.BLACK); // 边框粗细及颜色
        drawable.setColor(0x22ffffff); // 边框内部颜色
        drawable.setCornerRadius(45);

        Button button = convertView.findViewById(R.id.user_person_collection_shop_Fdelete);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /* Toast.makeText(mContext,"点击了第"+shop.getShopId()+"item",Toast.LENGTH_SHORT).show();*/
                favouriteshops.remove(userShopDetail);
                notifyDataSetChanged();
                addCollection();
            }
        });

        button.setBackground(drawable); // 设置背景（效果就是有边框及底色）
        //
        //利用传递的数据源给相应的控件对象赋值
        UserShopDetail shops = favouriteshops.get(position);
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        requestOptions.placeholder(R.mipmap.user_index_perm);
        Glide.with(mContext)
                .load(shops.getShopPicture())
                .apply(requestOptions)
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
    /**
     * 添加收藏
     * */
    public void addCollection(){

        if(true){
            SharedPreferences sharedPreferences =mContext.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
            String userToken = sharedPreferences.getString("token","");
            String userAccount = sharedPreferences.getString("userAccount","");
            String userPassword = sharedPreferences.getString("userPassword","");
            String shopId = String.valueOf(shop.getShopId());
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("UserAccount",userAccount);
            builder.add("UserPassword",userPassword);
            builder.add("shopId",shopId);
            final FormBody body = builder.build();
            Request request = new Request.Builder().header("UserTokenSQL",userToken).post(body).url(UrlAddress.url+"SaveOrDeleteCollection.action").build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String isCollection = response.body().string();
                    Log.i("addcollection",isCollection);
                    /*Message message = Message.obtain();
                    message.what = 2;
                    Bundle bundle = new Bundle();
                    bundle.putString("isCollection",isCollection);
                    message.setData(bundle);
                    handler.sendMessage(message);*/
                }
            });
            //未登陆跳转到登陆界面
        }
    }
}