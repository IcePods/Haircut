package com.example.lu.thebarbershop.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.media.session.MediaSession;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lu.thebarbershop.Activity.CreateDynamicActivity;
import com.example.lu.thebarbershop.Adapter.DynamicListAdapter;
import com.example.lu.thebarbershop.Entity.Dynamic;
import com.example.lu.thebarbershop.MyTools.UploadPictureUtil;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

/**
 * Created by lu on 2018/5/9 0009.
 * 动态fragment
 */

public class DynamicFragment extends Fragment{
    private RecyclerView DynamicList;
    private Context context;
    private DynamicListAdapter dynamicListAdapter;
    //数据源
    private List<Dynamic> data;
    //当前显示的动态数据
    private List<Dynamic> showData;

    RefreshLayout refreshLayout;
    private Button createNewDynamic;
    //动态请求地址
    final private String URL = "http://192.168.3.2:8080/theBarberShopServers/dynamic.action";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.fragment_dynamic_page,container,false);
        DynamicList  = view.findViewById(R.id.dynamic_list);
        refreshLayout = view.findViewById(R.id.refreshLayout);
        context = getActivity().getApplicationContext();
        //点击跳转到动态发布界面
        gotoCreateNewDynamic(view);
        //初始化冬天页面数据
        initData();
        //下拉刷新和上滑加载的动作绑定
        setPullRefresher();

        return view;
    }

    /**
     * 点击发布按钮，跳转到发布新动态页面
     */
    private void gotoCreateNewDynamic(View view){
        createNewDynamic = view.findViewById(R.id.create_new_dynamic);
        createNewDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(context, CreateDynamicActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData(){
        //服务器请求动态数据
        UploadPictureUtil util = new UploadPictureUtil();
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String dynamicStrList = bundle.getString("string");
                Gson gson = new Gson();
                data = gson.fromJson(dynamicStrList, new TypeToken<List<Dynamic>>(){}.getType());

            }
        };
        util.requestServer(URL,null,null,handler);
        for(int i=0; i<5; i++){
            showData.add(data.get(i));
        }

        dynamicListAdapter = new DynamicListAdapter(context,showData);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
        DynamicList.setLayoutManager(linearLayoutManager);
        DynamicList.setAdapter(dynamicListAdapter);
    }

    private void setPullRefresher(){
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                //在这里执行下拉刷新时的具体操作(网络请求、更新UI等)
                //服务器请求动态数据
                UploadPictureUtil util = new UploadPictureUtil();
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Bundle bundle = msg.getData();
                        String dynamicStrList = bundle.getString("string");
                        Gson gson = new Gson();
                        data = gson.fromJson(dynamicStrList, new TypeToken<List<Dynamic>>(){}.getType());

                    }
                };
                util.requestServer(URL,null,null,handler);
                showData.clear();
                for(int i=0; i<5; i++){
                    showData.add(data.get(i));
                }
                dynamicListAdapter.refresh(data);
                refreshLayout.finishRefresh(2000/*,false*/);
                //不传时间则立即停止刷新    传入false表示刷新失败
            }
        });
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
                //在这里执行上滑加载时的具体操作
                if(showData.size()<data.size()){
                    if(showData.size()+5 <= data.size()){
                        for(int i=0; i<5; i++){
                            showData.add(data.get(showData.size()+i));
                        }
                    }else {
                        for(int i=showData.size(); i<data.size(); i++){
                            showData.add(data.get(i));
                        }
                    }
                }else {
                    Toast.makeText(context,
                            "没有更多啦",
                            Toast.LENGTH_SHORT).show();
                }
                dynamicListAdapter.add(showData);
                refreshLayout.finishLoadMore(2000);
            }
        });
    }
}