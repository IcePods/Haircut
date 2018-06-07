package com.example.lu.thebarbershop.Activity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lu.thebarbershop.Adapter.UserShopDetailBaberRecyclerAdapter;
import com.example.lu.thebarbershop.Entity.Barber;
import com.example.lu.thebarbershop.Entity.ShopPicture;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.MyTools.PrepareIndexViewPagerDate;
import com.example.lu.thebarbershop.MyTools.ViewPagerTools;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserShopDetailActivity extends AppCompatActivity implements ViewPager.OnPageChangeListener{
    //RecyclerView 控件
    private RecyclerView mRecyclerView;
    //RecycleAdapter
    private RecyclerView.Adapter mAdapter;
    //RecycleView 管理器
    private RecyclerView.LayoutManager mLayoutManager;
    //图片轮训控件
    private ViewPager vp;
    //图片轮训的点
    private LinearLayout ll_point;
    //百度定位
    private RelativeLayout LocationMap;
    //电话控件
    private RelativeLayout TelephoneLayout;
    //作品展示
    private RelativeLayout productionLayout;

    private List<String> shopPictureList=new ArrayList<String>();//服务器获取的店铺轮播图片集合
    private ArrayList<ImageView> imageViewArrayList = new ArrayList<ImageView>(); //存放轮播图片图片的集合
    //从服务器获取理发师列表
    private ArrayList<Barber> barberList = new ArrayList<Barber>();
    private int lastPosition;//轮播图下边点的位置
    private boolean isRunning = false; //viewpager是否在自动轮询

    private  UserShopDetail userShopDetail;

    private ImageButton user_shop_detail_back_imgbtn ;//店铺详情页面返回按钮
    private ImageButton user_shopdetail_collect;//店铺详情 收藏按钮
    private TextView user_shopdetail_header_shopname; //店铺名称
    private Button user_shop_detail_address; //店铺地址
    private Button user_shop_detail_phone_btn; //店铺电话
    private TextView user_shopdetail_describe; //店铺简介
    OkHttpClient okHttpClient = new OkHttpClient();

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    String isCollection =  bundle.getString("isCollection");
                    Gson gson = new Gson();
                    UserShopDetail userShopDetail = gson.fromJson(isCollection, UserShopDetail.class);

            }
            super.handleMessage(msg);
        }
    };



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shop_detail);
        //绑定控件
        getView();
        //获取传过来的intent 获取参数 并更改对应控件
        getIntentAndData();
        //实例化监听器
        OnClickImpListener listener = new OnClickImpListener();
        //点击返回 finish 本activity
        user_shop_detail_back_imgbtn.setOnClickListener(listener);
        //点击跳转百度地图
        LocationMap.setOnClickListener(listener);
        //点击跳转拨号盘 填入电话号 但不拨打
        TelephoneLayout.setOnClickListener(listener);
        //点击跳转作品展示页面
        productionLayout.setOnClickListener(listener);
        initData();
        initView();
        //得到轮播图片集合
        addViewPagerImages();
        ///
        changeViewPagerPoint();
        //开启图片轮训
        viewPagerThread();
        //设置最开头显示
        vp.setFocusable(true);
        vp.setFocusableInTouchMode(true);
        vp.requestFocus();


    }
    private class OnClickImpListener implements  View.OnClickListener{
        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.user_shop_detail_back_imgbtn:
                    finish();
                    break;
                case R.id.user_shop_detail_address_content:
                    Intent intent =  new Intent();
                    //把点击的商品对象添加到intent对象中去
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("userShopDetail",userShopDetail);
                    intent.putExtras(bundle);
                    intent.setClass(getApplicationContext(),ShopDetailBaiduMap.class);
                    startActivity(intent);
                    break;
                case R.id.user_shop_detail_phone_content:
                    //拉取拨号盘 填入电话号 不拨打
                    Intent intent1 = new Intent(Intent.ACTION_DIAL, Uri.parse("tel:"+userShopDetail.getShopPhone()));
                    intent1.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent1);
                    break;
                case R.id.user_shop_detail_production:
                    //跳转到作品展示页面
                    Intent intent2 =  new Intent();
                    intent2.setClass(getApplicationContext(),UserShopDetailProductionActivity.class);
                    intent2.putExtra("shop",userShopDetail);
                    startActivity(intent2);

            }
        }
    }
    //获取控件
    private void getView(){
        user_shopdetail_header_shopname = findViewById(R.id.user_shopdetail_header_shopname);
        user_shop_detail_address  = findViewById(R.id.user_shop_detail_address);
        user_shop_detail_phone_btn = findViewById(R.id.user_shop_detail_phone_btn);
        user_shopdetail_describe = findViewById(R.id.user_shopdetail_describe);

        user_shop_detail_back_imgbtn= findViewById(R.id.user_shop_detail_back_imgbtn);
        user_shopdetail_collect = findViewById(R.id.user_shopdetail_collect);
        vp =findViewById(R.id.user_shop_detail_viewpager_content);
        ll_point =findViewById(R.id.user_shop_detail_ll_point);
        LocationMap = findViewById(R.id.user_shop_detail_address_content);
        TelephoneLayout = findViewById(R.id.user_shop_detail_phone_content);
        productionLayout = findViewById(R.id.user_shop_detail_production);
    }
    //获取传过来的intent 获取参数
    private void getIntentAndData(){
        //获取intent对象传递的参数
        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        userShopDetail = (UserShopDetail) bundle.getSerializable("userShopDetail");
        Log.i("allshoppicture",userShopDetail.getShopPictureSet().size()+"");
        //获取店铺图片set 填入到list中
        for(ShopPicture i:userShopDetail.getShopPictureSet()){

            shopPictureList.add(i.getShoppicture_picture());
            Log.i("allshoppicturestring",i.toString());
        }
        //获取店铺理发师 Set 填入到list中
        for (Barber barber:userShopDetail.getBarberSet()){
            barberList.add(barber);
        }
        //更改控件内容
        user_shopdetail_header_shopname.setText(userShopDetail.getShopName());
        user_shop_detail_address.setText(userShopDetail.getShopAddress());
        user_shop_detail_phone_btn.setText(userShopDetail.getShopPhone());
        user_shopdetail_describe.setText(userShopDetail.getShopIntroduce());
    }

    //RecyclerView Barber 理发师构建数据
    private void initData() {
        mLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false);
        mAdapter = new UserShopDetailBaberRecyclerAdapter(barberList,this);
    }
    //ReclerView 理发师绑定View
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

    /**
     *将轮播的图片添加到集合中
     * */
    public void addViewPagerImages(){
        //得到图片集合
       /* PrepareIndexViewPagerDate prepareIndexViewPagerDate = new PrepareIndexViewPagerDate();
        List<String> list = prepareIndexViewPagerDate.date();*/
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        requestOptions.placeholder(R.mipmap.user_index_nurse);
        for(int i=0;i<shopPictureList.size();i++){
            ImageView imageView =new ImageView(this);
            Glide.with(this)
                    .load(shopPictureList.get(i))
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
       /* ViewPagerTools tools = new ViewPagerTools(this,vp,ll_point,imageViewArrayList);
        tools.initDate(imageViewArrayList);
        tools.initAdapter();*/
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
    public void changeViewPagerPoint(){
        ViewPagerTools tools = new ViewPagerTools(this,vp,ll_point,imageViewArrayList);
        tools.initDate(imageViewArrayList);
        tools.initAdapter();
        vp.setOnPageChangeListener(this);
    }
    /**
     * 判断店铺是否加入收藏
     * */
    public void getIsCollection(){
        File file = new File(this.getFilesDir().getParent()+"/shared_prefs/usertoken.xml");
        if(file.exists()){
            SharedPreferences sharedPreferences =this.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
            String userToken = sharedPreferences.getString("token","");
            String userAccount = sharedPreferences.getString("userAccount","");
            String userPassword = sharedPreferences.getString("userPassword","");
            String shopId = String.valueOf(userShopDetail.getShopId());
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("userAccount",userAccount);
            builder.add("userPassword",userPassword);
            builder.add("shopId",shopId);
            final FormBody body = builder.build();
            Request request = new Request.Builder().addHeader("userToken",userToken).post(body).url(UrlAddress.url+".action").build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String isCollection = response.body().toString();
                    Log.i("collection",isCollection);
                    Message message = Message.obtain();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("isCollection",isCollection);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            });

        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        isRunning=false;
    }

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
}
