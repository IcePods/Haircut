package com.example.lu.thebarbershop.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lu.thebarbershop.Adapter.DynamicListAdapter;
import com.example.lu.thebarbershop.Entity.Dynamic;
import com.example.lu.thebarbershop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lu on 2018/5/9 0009.
 * 动态fragment
 */

public class DynamicFragment extends Fragment{
    private ListView DynamicList;
    private Context context;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.fragment_dynamic_page,container,false);
        DynamicList  = view.findViewById(R.id.dynamic_list);
        context = getActivity().getApplicationContext();
        ////通过listview展示动态列表
        ShowDynamicByListView(dataSource(),context);
        return view;


    }
    //实现通过listview展示动态列表
    private void ShowDynamicByListView(List<Dynamic> dataSource, Context context){
        //实例化DynamicListAdapter对象
        final DynamicListAdapter adapter = new DynamicListAdapter(context,dataSource,R.layout.item_user_dynamic_list);
        //绑定适配器
        DynamicList.setAdapter(adapter);
    }
    //假设的数据源  后期通过数据库返回用户的动态
    private List<Dynamic> dataSource(){
        List<Dynamic> dataSource = new ArrayList<Dynamic>();
        //定义第一个数据项
        Dynamic d1 =  new Dynamic();
        d1.setImgName(R.mipmap.default_header_img);
        d1.setUserName("用户名1");
        d1.setDynamicContent("巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙" +
                "、巴拉巴拉吧小魔仙、");
        d1.setDynamicImg(R.mipmap.default_header_img);
        dataSource.add(d1);
        //定义第2个数据项
        Dynamic d2 =  new Dynamic();
        d2.setImgName(R.mipmap.default_header_img);
        d2.setUserName("用户名2");
        d2.setDynamicContent("巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙" +
                "、巴拉巴拉吧小魔仙、");
        d2.setDynamicImg(R.mipmap.default_header_img);
        dataSource.add(d2);
        //定义第一个数据项
        Dynamic d3 =  new Dynamic();
        d3.setImgName(R.mipmap.default_header_img);
        d3.setUserName("用户名3");
        d3.setDynamicContent("巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙" +
                "、巴拉巴拉吧小魔仙、");
        d3.setDynamicImg(R.mipmap.default_header_img);
        dataSource.add(d3);
        //定义第一个数据项
        Dynamic d4 =  new Dynamic();
        d4.setImgName(R.mipmap.default_header_img);
        d4.setUserName("用户名4");
        d4.setDynamicContent("巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙" +
                "、巴拉巴拉吧小魔仙、");
        d4.setDynamicImg(R.mipmap.default_header_img);
        dataSource.add(d4);
        //定义第一个数据项
        Dynamic d5 =  new Dynamic();
        d5.setImgName(R.mipmap.default_header_img);
        d5.setUserName("用户名5");
        d5.setDynamicContent("巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙" +
                "、巴拉巴拉吧小魔仙、");
        d5.setDynamicImg(R.mipmap.default_header_img);
        dataSource.add(d5);
        //定义第一个数据项
        Dynamic d6 =  new Dynamic();
        d6.setImgName(R.mipmap.default_header_img);
        d6.setUserName("用户名6");
        d6.setDynamicContent("巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙、巴拉巴拉吧小魔仙" +
                "、巴拉巴拉吧小魔仙、");
        d6.setDynamicImg(R.mipmap.default_header_img);
        dataSource.add(d6);

        return dataSource;
    }
}
