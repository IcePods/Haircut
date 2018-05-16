package com.example.lu.thebarbershop.Fragment;

import android.app.Fragment;
import android.content.Context;

import android.graphics.drawable.BitmapDrawable;

import android.content.Intent;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lu.thebarbershop.Adapter.IndexShopDetailAdapter;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.MyTools.GetRoundedCornerBitmap;
import com.example.lu.thebarbershop.MyTools.LooperTextView;
import com.example.lu.thebarbershop.MyTools.PrepareIndexShopDetail;
import com.example.lu.thebarbershop.MyTools.PrepareIndexViewPagerDate;
import com.example.lu.thebarbershop.MyTools.PrepareLooperTextDate;
import com.example.lu.thebarbershop.MyTools.ViewPagerTools;

import com.example.lu.thebarbershop.Activity.UsersRegisterActivity;

import com.example.lu.thebarbershop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lu on 2018/5/9 0009.
 * 主页面fragment
 */

public class MainFragment extends Fragment implements ViewPager.OnPageChangeListener{
    private Context mContext;
    private ImageView hirecut;
    private ImageView hirecolor;
    private ImageView perm;
    private ImageView nurse;
    private TextView address;
    private LinearLayout search;
    private ImageView plus;
    private ViewPager vp;
    private LinearLayout ll_point;
    private ListView lv_shop;
    private LooperTextView looperTextView;

    private ArrayList<ImageView> imageViewArrayList = new ArrayList<ImageView>(); //存放轮播图片图片的集合
    private int lastPosition;//轮播图下边点的位置
    private boolean isRunning = false; //viewpager是否在自动轮询

    private IndexShopDetailAdapter indexShopDetailAdapter;//主页店铺展示的adapter



    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext =  getActivity().getApplicationContext();
        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.fragment_index,container,false);
        //初始化控件
        init(view);
        //设置图片为圆形
        setRounded();
        //得到轮播图片集合
        addViewPagerImages();
        //开启图片轮训
        viewPagerThread();
        //消息上下轮播
        looperTextView.setTipList(new PrepareLooperTextDate().getLooperList());
        //店铺listview
        initShopAdapter();




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
        ll_point = view.findViewById(R.id.ll_point);
        looperTextView = view.findViewById(R.id.user_index_loopertextview);


    }

    /**7
     *设置为圆形
     * */
    public void setRounded(){
        GetRoundedCornerBitmap GetRounded = new GetRoundedCornerBitmap();
        hirecut.setImageBitmap(GetRounded.getRoundedCornerBitmap(((BitmapDrawable)hirecut.getDrawable()).getBitmap(),2));
        hirecolor.setImageBitmap(GetRounded.getRoundedCornerBitmap(((BitmapDrawable)hirecolor.getDrawable()).getBitmap(),2));
        perm.setImageBitmap(GetRounded.getRoundedCornerBitmap(((BitmapDrawable)perm.getDrawable()).getBitmap(),2));
        nurse.setImageBitmap(GetRounded.getRoundedCornerBitmap(((BitmapDrawable)nurse.getDrawable()).getBitmap(),2));

    }

    //////////////////////
    /**
     * 实现onpagechangelistener
     * */
    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {
        //当前的位置可能很大，为了防止下标越界，对要显示的图片的总数进行取余
        int a = imageViewArrayList.size();
        int newPosition = position % a;

        //设置小圆点为高亮或暗色
        ll_point.getChildAt(lastPosition).setEnabled(false);
        ll_point.getChildAt(newPosition).setEnabled(true);
        lastPosition = newPosition; //记录之前的点

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }
    //////////////////////////
    /**
     *将轮播的图片添加到集合中
     * */
    public void addViewPagerImages(){
        //得到图片集合
        PrepareIndexViewPagerDate prepareIndexViewPagerDate = new PrepareIndexViewPagerDate();
        List<String> list = prepareIndexViewPagerDate.date();
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        requestOptions.placeholder(R.mipmap.user_index_nurse);
        for(int i=0;i<list.size();i++){
            ImageView imageView =new ImageView(mContext);
            Glide.with(mContext)
                    .load(list.get(i))
                    .apply(requestOptions)
                    .into(imageView);
            imageViewArrayList.add(imageView);
            Log.i("hzl",imageViewArrayList.size()+"");


        }
    }

    /**
     * 图片轮播线程
     * */
    public void viewPagerThread(){
        ViewPagerTools tools = new ViewPagerTools(mContext,vp,ll_point,imageViewArrayList);
        tools.initDate(imageViewArrayList);
        tools.initAdapter();
        new Thread(){
            @Override
            public void run() {
                isRunning =true;
                while (isRunning){
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    if(getActivity()!=null){
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                vp.setCurrentItem(vp.getCurrentItem()+1);
                            }
                        });
                    }
                }
            }
        }.start();
    }

    /**
     * 实例化主页展示shop的adapter以及数据
     * */
    public void initShopAdapter(){
        //实例化数据

        indexShopDetailAdapter = new IndexShopDetailAdapter(mContext,new PrepareIndexShopDetail().prepareIndexShopDetail(),R.layout.item_user_index_shop);
        lv_shop.setAdapter(indexShopDetailAdapter);
    }
    /**
     * 重写ondestory
     * */
    @Override
    public void onDestroy() {

        super.onDestroy();
        isRunning=false;
    }


}
