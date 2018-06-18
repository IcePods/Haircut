package com.example.lu.thebarbershop.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.TintContextWrapper;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.Toast;

import com.example.lu.thebarbershop.Adapter.DynamicListAdapter;
import com.example.lu.thebarbershop.Entity.Dynamic;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.MyTools.GetUserFromShared;
import com.example.lu.thebarbershop.MyTools.UploadPictureUtil;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;

public class MyDynamicActivity extends AppCompatActivity {
    //数据显示控件
    private RecyclerView myDynamicList;
    private DynamicListAdapter dynamicListAdapter;
    //数据源
    private List<Dynamic> data;
    //当前显示的数据量
    private int showDataNum=0;
    //页面刷新控件
    private RefreshLayout refreshLayout;
    //返回控件
    private ImageButton back;
    //动态请求地址
    final private String URL = UrlAddress.url + "getUserDynamic.action";
    UploadPictureUtil util = new UploadPictureUtil();
    private String token;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_dynamic);
        //初始化控件对象
        initControl();
        //返回按钮绑定事件监听器
        setBackControlOnClickListener();
        //初始化显示数据
        initData();
        //设置下拉刷新和上滑加载功能
        setRefreshAndLoadMore();
    }

    /**
     * 初始化控件对象
     */
    private void initControl(){
        Log.i("李垚：：：：：","初始化控件对象");
        myDynamicList = findViewById(R.id.my_dynamic_list);
        data = new ArrayList<>();
        refreshLayout  = findViewById(R.id.my_dynamic_refreshLayout);
        back = findViewById(R.id.my_dynamic_back);
        token = new GetUserFromShared(this).getUserTokenFromShared();
        if(token == null){
            Intent intent = new Intent();
            intent.setClass(getApplicationContext(),UsersLoginActivity.class);
            startActivity(intent);
            finish();
        }

    }

    /**
     * 给返回按钮绑定点击事件监听器
     */
    private void setBackControlOnClickListener(){
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 服务器请求用户动态数据
     */
    private void initData(){
        Log.i("李垚：：：：：","初始化数据源");
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String dynamicList = bundle.getString("string");
                Gson gson = new Gson();
                List<Dynamic> list = gson.fromJson(dynamicList, new TypeToken<List<Dynamic>>(){}.getType());
                if(list.size() == 0){
                    Toast.makeText(getApplicationContext(),
                            "您还没有发布过动态呦！",
                            Toast.LENGTH_SHORT).show();
                }else {
                    data.addAll(list);
                    if(data.size()>5){
                        list.removeAll(list);
                        for (int i=0;i<5;i++){
                            list.add(data.get(i));
                        }
                    }
                }
                //首次显示的数据
                showDataNum = list.size();
                dynamicListAdapter = new DynamicListAdapter(getApplicationContext(),list);
                LinearLayoutManager manager = new LinearLayoutManager(getApplicationContext());
                myDynamicList.setLayoutManager(manager);
                myDynamicList.setAdapter(dynamicListAdapter);
            }

        };
        util.requestServer(URL, null,token,handler);
    }

    private void setRefreshAndLoadMore(){
        Log.i("李垚：：：：：","绑定刷新加载的监听器");
        refreshLayout.finishRefresh(2000);
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Bundle bundle = msg.getData();
                        String dynamicList = bundle.getString("string");
                        Gson gson = new Gson();
                        List<Dynamic> list = gson.fromJson(dynamicList, new TypeToken<List<Dynamic>>(){}.getType());
                        if(list.size() == 0){
                            Toast.makeText(getApplicationContext(),
                                    "您还没有发布动态呦！",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            data.removeAll(data);
                            data.addAll(list);
                            if(list.size()>5){
                                list.removeAll(list);
                                for (int i=0;i<5;i++){
                                    list.add(data.get(i));
                                }
                            }
                        }
                        showDataNum = list.size();
                        dynamicListAdapter.refresh(list);
                    }
                };
                //重新向服务器获取数据
                util.requestServer(URL,null,token,handler);
            }
        });

        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishLoadMore(2000);
                //在这里执行上滑加载时的具体操作
                List<Dynamic> add = new ArrayList<>();
                if(showDataNum < data.size()){
                    if(showDataNum+5 <= data.size()){
                        for(int i=0; i<5; i++){
                            add.add(data.get(showDataNum + i));
                        }
                        showDataNum+=5;
                    }else {
                        for(;showDataNum<data.size(); showDataNum++){
                            add.add(data.get(showDataNum));
                            showDataNum = data.size();
                        }
                    }
                }else {
                    Toast.makeText(getApplicationContext(),
                            "没有更多啦",
                            Toast.LENGTH_SHORT).show();
                }
                dynamicListAdapter.add(add);
            }
        });
    }


}
