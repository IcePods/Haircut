package com.example.lu.thebarbershop.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lu.thebarbershop.Activity.HairStyleDetailActivity;
import com.example.lu.thebarbershop.Adapter.HaircolorRecyclerviewAdapter;
import com.example.lu.thebarbershop.Entity.Barber;
import com.example.lu.thebarbershop.Entity.HairStyle;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.MyTools.PrepareHairStylePicture;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lu on 2018/5/23 0023.
 */

@SuppressLint("ValidFragment")
public class UserShopDetailProductionHairColor extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private HaircolorRecyclerviewAdapter mAdapter;
    private UserShopDetail userShopDetail;
    OkHttpClient okHttpClient  = new OkHttpClient();
    private List<HairStyle> colorList;


    public UserShopDetailProductionHairColor(UserShopDetail userShopDetail) {
        this.userShopDetail = userShopDetail;
    }
Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case 1:
                Bundle bundle = msg.getData();
                String productList = bundle.getString("productList");
               // Log.i("colorList",productList);
                Gson gson = new Gson();
                colorList = gson.fromJson(productList,new TypeToken<List<HairStyle>>(){}.getType());
                initAdapter();
        }
        super.handleMessage(msg);
    }
};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.fragment_user_shop_detail_production_haicolor,container,false);

        //创建静态数据

        mRecyclerView = view.findViewById(R.id.fragment_shop_detail_haircolor_recyclerview);
        //设置布局管理器为2列，纵向
        String id =String.valueOf(userShopDetail.getShopId());

        selectHairColor(id,"染发");
        return view;
    }

    private void initAdapter() {
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new HaircolorRecyclerviewAdapter(getActivity().getApplicationContext(), R.layout.item_user_main_haircolor_recyclerview,colorList);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new HaircolorRecyclerviewAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                //指定跳转路线
                intent.setClass(getActivity().getApplicationContext(),HairStyleDetailActivity.class);
                //把点击的商品对象添加到intent对象中去
                Bundle bundle = new Bundle();
                HairStyle hairStyle =colorList.get(position);
                bundle.putSerializable("userShopDetail",userShopDetail);
                bundle.putSerializable("hairStyle",hairStyle);
                intent.putExtras(bundle);

                startActivity(intent);
            }
        });
    }

    //请求店铺染发产品
    public void selectHairColor(final String id,final String type){
        new Thread(){
            @Override
            public void run() {
                Request.Builder builder = new Request.Builder();
                FormBody.Builder builder1 = new FormBody.Builder();
                builder1.add("shopId",id);
                builder1.add("HairStyleType",type);
                FormBody body = builder1.build();
                Request request = new Request.Builder().post(body).url(UrlAddress.url+"getHairStyleByShopInShopDetail.action").build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String a = response.body().string();
                    Message message =Message.obtain();
                    message.what =1;
                    Bundle bundle =new Bundle();
                    bundle.putString("productList",a);
                    Log.i("colorList",a);
                    message.setData(bundle);
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }
}
