package com.example.lu.thebarbershop.Fragment;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lu.thebarbershop.Activity.UsersRegisterActivity;
import com.example.lu.thebarbershop.R;

/**
 * Created by lu on 2018/5/9 0009.
 * 主页面fragment
 */

public class MainFragment extends Fragment {
    private ImageView hirecut;
    private ImageView hirecolor;
    private ImageView perm;
    private ImageView nurse;
    private TextView address;
    private LinearLayout search;
    private ImageView plus;
    private ViewPager vp;
    private ListView lv_shop;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.fragment_index,container,false);
        //初始化控件
        init(view);

        return view;
    }
    /**
     * 初始化控件
     * 参数;view
     * */
    public void init(View view){
        address = view.findViewById(R.id.user_index_address);
        search = view.findViewById(R.id.user_index_search);
        plus = view.findViewById(R.id.user_index_plus);
        perm = view.findViewById(R.id.user_index_perm);
        hirecut = view.findViewById(R.id.user_index_hirecut);
        hirecolor = view.findViewById(R.id.user_index_hirecolor);
        nurse = view.findViewById(R.id.user_index_nurse);
        vp = view.findViewById(R.id.user_index_viewpager_content);
        lv_shop = view.findViewById(R.id.user_person_index_lvshop_content);


    }

    /**
     *
     * */
}
