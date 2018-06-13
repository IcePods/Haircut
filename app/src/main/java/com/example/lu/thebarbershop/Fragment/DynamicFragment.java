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
import android.util.Log;
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
import com.example.lu.thebarbershop.Entity.UrlAddress;
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
    //当前显示的数据量
    private int showDataNum=0;

    RefreshLayout refreshLayout;
    private Button createNewDynamic;
    //动态请求地址
    final private String URL = UrlAddress.url + "showAllDynamic.action";


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
                intent.setClass(getActivity().getApplicationContext(), CreateDynamicActivity.class);
                startActivity(intent);
            }
        });
    }

    private void initData(){
        //服务器请求动态数据
        data = new ArrayList<>();
        UploadPictureUtil util = new UploadPictureUtil();
        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle bundle = msg.getData();
                String dynamicStrList = bundle.getString("string");
                Gson gson = new Gson();
                List<Dynamic> list = gson.fromJson(dynamicStrList, new TypeToken<List<Dynamic>>(){}.getType());
                if(list.size() == 0){
                    Toast.makeText(context,
                            "还没有人发布动态呦！",
                            Toast.LENGTH_SHORT).show();
                }else {
                    data.addAll(list);
                    list.removeAll(list);
                    //设置显示的数据
                    if(data.size()<=5){
                        list.addAll(data);
                    }else {
                        for (int i = 0; i < 5; i++) {
                            list.add(data.get(i));
                        }
                    }
                }
                //首次显示的数据量
                showDataNum = list.size();
                dynamicListAdapter = new DynamicListAdapter(context,list);
                LinearLayoutManager linearLayoutManager = new LinearLayoutManager(context);
                DynamicList.setLayoutManager(linearLayoutManager);
                DynamicList.setAdapter(dynamicListAdapter);
            }
        };
        util.requestServer(URL,null,null,handler);

    }

    private void setPullRefresher(){
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {
                refreshLayout.finishRefresh(2000/*,false*/);
                //不传时间则立即停止刷新    传入false表示刷新失败
                //服务器请求动态数据
                UploadPictureUtil util = new UploadPictureUtil();
                Handler handler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Bundle bundle = msg.getData();
                        String dynamicStrList = bundle.getString("string");
                        Gson gson = new Gson();
                        List<Dynamic> list = gson.fromJson(dynamicStrList, new TypeToken<List<Dynamic>>(){}.getType());
                        if(list.size() == 0){
                            Toast.makeText(context,
                                    "还没有人发布动态呦！",
                                    Toast.LENGTH_SHORT).show();
                        }else {
                            data.removeAll(data);
                            data.addAll(list);
                            //设置显示的数据
                            list.removeAll(list);
                            if(data.size()<=5){
                                list.addAll(data);
                            }else {
                                for (int i = 0; i < 5; i++) {
                                    list.add(data.get(i));
                                }
                            }
                        }

                        showDataNum = list.size();
                        Log.i("李垚：：：：","当前显示的数据量："+list.size());
                        dynamicListAdapter.refresh(list);
                    }
                };

                util.requestServer(URL,null,null,handler);
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
                    Toast.makeText(context,
                            "没有更多啦",
                            Toast.LENGTH_SHORT).show();
                }
                dynamicListAdapter.add(add);
            }
        });
    }
}