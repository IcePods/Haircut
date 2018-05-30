package com.example.lu.thebarbershop.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lu.thebarbershop.Entity.HairStyle;
import com.example.lu.thebarbershop.Entity.HairStyleDetail;
import com.example.lu.thebarbershop.MyTools.PrepareHairStylePicture;
import com.example.lu.thebarbershop.MyTools.ViewPagerTools;
import com.example.lu.thebarbershop.R;

import java.util.ArrayList;
import java.util.List;

public class HairStyleDetailActivity extends AppCompatActivity {

    private ViewPager vp;
    private LinearLayout ll_point;
    private Context mContext;
    private ImageButton backbutton;//返回按钮
    private TextView appointmentbutton;//底部预约
    private ImageView imgAppointment;//底部预约箭头

    private HairStyle hairStyle;//hairstyle对象

    private TextView hairStyleName;//发型名
    private TextView hairStyleIntroduction;//发型描述

    private List<String> hairstyleImgs = new ArrayList<String>();//发型图片URL

    private ArrayList<ImageView> imageViewArrayList = new ArrayList<ImageView>(); //放轮播图片的集合
    private int lastPosition;//轮播图下边点的位置
    private boolean isRunning = false; //viewpager是否在自动轮询

    private onClickListener onClickListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mContext = getApplicationContext();
        setContentView(R.layout.activity_hair_style_detail);

        //初始化控件
        init();
        //绑定点击方法
        onClickListener = new onClickListener();
        backbutton.setOnClickListener(onClickListener);
        appointmentbutton.setOnClickListener(onClickListener);
        imgAppointment.setOnClickListener(onClickListener);

        //获取传过来的intent 获取参数 并更改对应控件
        getIntentAndData();
        //得到轮播图片集合
        addViewPagerImages();
        //开启图片轮训
        viewPagerThread();

    }

    /**
     * 初始化控件
     */
    public void init() {
        vp = findViewById(R.id.hair_style_viewpager_content);
        hairStyleName = findViewById(R.id.hair_style_name);
        hairStyleIntroduction = findViewById(R.id.hair_style_introductions);
        ll_point = findViewById(R.id.ll_point);
        backbutton = findViewById(R.id.hair_style_back_imgbtn);
        appointmentbutton = findViewById(R.id.hair_style_appointment);
        imgAppointment = findViewById(R.id.image_appointment);
    }

    //获取传过来的intent 获取参数
    private void getIntentAndData() {
        //获取intent对象传递的参数
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        hairStyle = (HairStyle)bundle.getSerializable("hairStyle");
        for(HairStyleDetail i:hairStyle.getHairStyleDetailSet()){
            hairstyleImgs.add(i.getHairstyle_detail_picture());
            Log.i("hzl",hairstyleImgs.size()+"");
        }

        //更改控件内容
        hairStyleName.setText(hairStyle.getHairstyleName());
        hairStyleIntroduction.setText(hairStyle.getHairstyleIntroduce());

    }
    class onClickListener implements View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.hair_style_back_imgbtn://返回
                    finish();
                    break;
                case R.id.hair_style_appointment://预约
                    //携带数据跳转到另一个Activity，进行数据的更新操作
                    Intent intent = new Intent();
                    //指定跳转路线
                    intent.setClass(getApplicationContext(),MainActivity.class);//跳到预约界面
                    //把点击的对象添加到intent对象中去
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("hairStyleDetail",hairStyle);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    break;
                case R.id.image_appointment://预约
                    //携带数据跳转到另一个Activity，进行数据的更新操作
                    Intent intent1 = new Intent();
                    //指定跳转路线
                    intent1.setClass(getApplicationContext(),MainActivity.class);
                    //把点击的对象添加到intent对象中去
                    Bundle bundle1 = new Bundle();
                    bundle1.putSerializable("hairStyleDetail",hairStyle);
                    intent1.putExtras(bundle1);
                    startActivity(intent1);
                    break;
            }
        }
    }

    public void onPageSelected(int position) {
        //当前的位置可能很大，为了防止下标越界，对要显示的图片的总数进行取余
        int a = imageViewArrayList.size();
        int newPosition = position % a;

        //设置小圆点为高亮或暗色
        ll_point.getChildAt(lastPosition).setEnabled(false);
        ll_point.getChildAt(newPosition).setEnabled(true);
        lastPosition = newPosition; //记录之前的点

    }

    public void addViewPagerImages() {
        List<String> list = hairstyleImgs;
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        //requestOptions.placeholder(R.mipmap.user_index_nurse);
        for (int i = 0; i < list.size(); i++) {
            ImageView imageView = new ImageView(mContext);
            Glide.with(mContext)
                    .load(list.get(i))
                    .apply(requestOptions)
                    .into(imageView);
            imageViewArrayList.add(imageView);
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
                    if(this!=null){
                        runOnUiThread(new Runnable() {
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

    public void onDestroy() {

        super.onDestroy();
        isRunning=false;
    }
}
