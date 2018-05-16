package com.example.lu.thebarbershop.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lu.thebarbershop.Adapter.UserShopDetailBaberRecyclerAdapter;
import com.example.lu.thebarbershop.Entity.Barber;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.R;

import java.util.ArrayList;

public class UserShopDetailActivity extends AppCompatActivity {
    //RecyclerView 控件
    private RecyclerView mRecyclerView;
    //RecycleAdapter
    private RecyclerView.Adapter mAdapter;
    //RecycleView 管理器
    private RecyclerView.LayoutManager mLayoutManager;

    private ImageButton user_shop_detail_back_imgbtn ;//店铺详情页面返回按钮
    private ImageButton user_shopdetail_collect;//店铺详情 收藏按钮
    private TextView user_shopdetail_header_shopname; //店铺名称
    private Button user_shop_detail_address; //店铺地址
    private Button user_shop_detail_phone_btn; //店铺电话
    private TextView user_shopdetail_describe; //店铺简介

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shop_detail);
        //绑定控件
        getView();
        //获取传过来的intent 获取参数 并更改对应控件
        getIntentAndData();
        //点击返回 finish 本activity
        user_shop_detail_back_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });




        initData();
        initView();
    }
    //获取控件
    private void getView(){
        user_shopdetail_header_shopname = findViewById(R.id.user_shopdetail_header_shopname);
        user_shop_detail_address  = findViewById(R.id.user_shop_detail_address);
        user_shop_detail_phone_btn = findViewById(R.id.user_shop_detail_phone_btn);
        user_shopdetail_describe = findViewById(R.id.user_shopdetail_describe);

        user_shop_detail_back_imgbtn= findViewById(R.id.user_shop_detail_back_imgbtn);
        user_shopdetail_collect = findViewById(R.id.user_shopdetail_collect);
    }
    //获取传过来的intent 获取参数
    private void getIntentAndData(){
        //获取intent对象传递的参数
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        final UserShopDetail userShopDetail = (UserShopDetail) bundle.getSerializable("userShopDetail");

        //更改控件内容
        user_shopdetail_header_shopname.setText(userShopDetail.getShopName());
        user_shop_detail_address.setText(userShopDetail.getShopAddress());
        user_shop_detail_phone_btn.setText(userShopDetail.getShopPhone());
        user_shopdetail_describe.setText(userShopDetail.getShopIntroduce());
    }
    //RecyclerView 构建数据
    private void initData() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new UserShopDetailBaberRecyclerAdapter(getData());
    }
    //ReclerView 绑定View
    private void initView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.user_shop_detail_barber_recyclerView);
        // 设置布局管理器
        mRecyclerView.setLayoutManager(mLayoutManager);
        // 设置adapter
        mRecyclerView.setAdapter(mAdapter);
    }
    //数据源
    private ArrayList<Barber> getData() {
        ArrayList<Barber> data = new ArrayList<>();
        Barber barber1 = new Barber();
        barber1.setBarberImg(R.mipmap.user_shop_detail_baber_header);
        barber1.setBarberName("发型师1");
        data.add(barber1);

        Barber barber2 = new Barber();
        barber2.setBarberImg(R.mipmap.user_shop_detail_baber_header);
        barber2.setBarberName("发型师2");
        data.add(barber2);

        Barber barber3 = new Barber();
        barber3.setBarberImg(R.mipmap.user_shop_detail_baber_header);
        barber3.setBarberName("发型师3");
        data.add(barber3);
        Barber barber4 = new Barber();
        barber4.setBarberImg(R.mipmap.user_shop_detail_baber_header);
        barber4.setBarberName("发型师4");
        data.add(barber4);
        Barber barber5 = new Barber();
        barber5.setBarberImg(R.mipmap.user_shop_detail_baber_header);
        barber5.setBarberName("发型师5");
        data.add(barber5);
        return data;
    }
}
