package com.example.lu.thebarbershop.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lu.thebarbershop.Adapter.HaircolorRecyclerviewAdapter;
import com.example.lu.thebarbershop.MyTools.PrepareHairStylePicture;
import com.example.lu.thebarbershop.R;

/**
 * Created by lu on 2018/5/23 0023.
 */

public class UserShopDetailProductionHairCut extends Fragment {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private HaircolorRecyclerviewAdapter mAdapter;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {

        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.fragment_user_shop_detail_production_haicut,container,false);

     /*   //创建静态数据
        PrepareHairStylePicture hairStyles = new PrepareHairStylePicture();
        mRecyclerView = view.findViewById(R.id.fragment_shop_detail_haircut_recyclerview);
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new HaircolorRecyclerviewAdapter(getActivity().getApplicationContext(), R.layout.item_user_main_haircolor_recyclerview, hairStyles.prepareHairStylePicture());
        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);*/
        return view;
    }
}
