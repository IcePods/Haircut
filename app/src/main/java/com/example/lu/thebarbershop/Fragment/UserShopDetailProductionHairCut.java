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
import com.example.lu.thebarbershop.Adapter.HaircutRecyclerviewAdapter;
import com.example.lu.thebarbershop.Entity.HairStyle;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserShopDetail;

import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by lu on 2018/5/23 0023.
 */

@SuppressLint("ValidFragment")
public class UserShopDetailProductionHairCut extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private HaircolorRecyclerviewAdapter mAdapter;
    private UserShopDetail userShopDetail;
    private List<HairStyle> cutList;

    OkHttpClient okHttpClient = new OkHttpClient();

    private static final int HAIRCUT = 1;

    @SuppressLint("ValidFragment")
    public UserShopDetailProductionHairCut(UserShopDetail userShopDetail) {
        this.userShopDetail = userShopDetail;
    }
Handler handler = new Handler(){
    @Override
    public void handleMessage(Message msg) {
        switch (msg.what){
            case HAIRCUT:
                Bundle bundle = msg.getData();
                String back = bundle.getString("productList");
                Gson gson = new Gson();
                cutList = gson.fromJson(back,new TypeToken<List<HairStyle>>(){}.getType());
                initAdapter();
                break;

        }
        super.handleMessage(msg);
    }
};
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.fragment_user_shop_detail_production_haicut,container,false);

        //创建静态数据

        mRecyclerView = view.findViewById(R.id.fragment_shop_detail_haircut_recyclerview);
        //调用查询
        Log.i("shopId",String.valueOf(userShopDetail.getShopId()));
        selectCut("剪发",String.valueOf(userShopDetail.getShopId()));

        return view;
    }

    private void initAdapter() {
        //设置布局管理器为2列，纵向

        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new HaircolorRecyclerviewAdapter(getActivity().getApplicationContext(), R.layout.item_user_main_haircolor_recyclerview,cutList);
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
        mAdapter.setItemClickListener(new HaircolorRecyclerviewAdapter.MyItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                Intent intent = new Intent();
                //指定跳转路线
                intent.setClass(getActivity().getApplicationContext(),HairStyleDetailActivity.class);
                //把点击的商品对象添加到intent对象中去
                Bundle bundle = new Bundle();
                HairStyle hairStyle =cutList.get(position);
                bundle.putString("fromIndex","2");
                bundle.putSerializable("userShopDetail",userShopDetail);
                bundle.putSerializable("hairStyle",hairStyle);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });




    }

    public void selectCut(final String name,final String id){
        new Thread(){
            @Override
            public void run() {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("shopId",id);
                builder.add("HairStyleType",name);
                FormBody body = builder.build();
                Request request = new Request.Builder().post(body).url(UrlAddress.url+"getHairStyleByShopInShopDetail.action").build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String back = response.body().string();
                        Message message = Message.obtain();
                        message.what=HAIRCUT;
                        Bundle bundle = new Bundle();
                        bundle.putString("productList",back);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                });
            }
        }.start();
    }
}
