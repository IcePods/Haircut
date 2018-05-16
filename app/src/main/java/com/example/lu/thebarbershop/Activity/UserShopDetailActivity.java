package com.example.lu.thebarbershop.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.example.lu.thebarbershop.Adapter.UserShopDetailBaberRecyclerAdapter;
import com.example.lu.thebarbershop.Entity.Barber;
import com.example.lu.thebarbershop.R;

import java.util.ArrayList;

public class UserShopDetailActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;

    private RecyclerView.Adapter mAdapter;

    private RecyclerView.LayoutManager mLayoutManager;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shop_detail);
        initData();
        initView();
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
