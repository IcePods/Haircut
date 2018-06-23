package com.example.lu.thebarbershop.Activity;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;


import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lu.thebarbershop.Adapter.ShopActivityAdapter;
import com.example.lu.thebarbershop.Adapter.UserShopDetailBaberRecyclerAdapter;
import com.example.lu.thebarbershop.Entity.Barber;
import com.example.lu.thebarbershop.Entity.ShopActivity;
import com.example.lu.thebarbershop.Entity.ShopPicture;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserCollections;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.MyTools.PrepareIndexViewPagerDate;
import com.example.lu.thebarbershop.MyTools.ViewPagerTools;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import jp.wasabeef.glide.transformations.CropCircleTransformation;
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
    private UserShopDetailBaberRecyclerAdapter mAdapter;
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
    private static UserCollections userCollections;
    private boolean isColl=false;
    OkHttpClient okHttpClient = new OkHttpClient();
    ///////下拉活动所需要的控件
    private  TextView shop_not_activity_tv;
    private LinearLayout mHiddenLayout;
    private float mDensity;
    private int mHiddenViewMeasuredHeight;
    private ImageView mIv;
    private ListView user_shop_activity_lv;
    List<ShopActivity> shopActivityList = new ArrayList<ShopActivity>();
    ///////////////////
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    String isCollection =  bundle.getString("isCollection");
                    Gson gson = new Gson();
                    Log.i("collection",isCollection);
                    userCollections = gson.fromJson(isCollection, UserCollections.class);
                    if(userCollections.isCollectionCondition()){
                        user_shopdetail_collect.setBackgroundResource(R.mipmap.user_person_collection2);
                        isColl = true;
                    }
                    break;

                case 2:



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
        //收藏点击事件
        user_shopdetail_collect.setOnClickListener(listener);
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
        //判断是否收藏
        if(new File(getApplication().getFilesDir().getParent()+"/shared_prefs/usertoken.xml").exists()){
            getIsCollection();
        }
        if (shopActivityList.size()==0){
            shop_not_activity_tv.setVisibility(View.VISIBLE);
            user_shop_activity_lv.setVisibility(View.GONE);
            mDensity = getResources().getDisplayMetrics().density;
            mHiddenViewMeasuredHeight = (int) (mDensity * 100 + 0.5);
        }else{
            ShopActivityAdapter adapter = new ShopActivityAdapter(getApplicationContext(),R.layout.item_shop_activity,shopActivityList);
            user_shop_activity_lv.setAdapter(adapter);
            //活动下拉
            mDensity = getResources().getDisplayMetrics().density;
            mHiddenViewMeasuredHeight = (int) (mDensity * 75*shopActivityList.size() + 0.5);
        }



    }
    private class OnClickImpListener implements  View.OnClickListener{
        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()){
                case R.id.user_shop_detail_back_imgbtn:
                    finish();
                    break;
                case R.id.user_shop_detail_address_content:
                   /* Intent intent =  new Intent();*/
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
                   /* Intent intent2 =  new Intent();*/
                    intent.setClass(getApplicationContext(),UserShopDetailProductionActivity.class);
                    intent.putExtra("shop",userShopDetail);
                    startActivity(intent);
                    break;
                case R.id.user_shopdetail_collect:
                    if(new File(getApplication().getFilesDir().getParent()+"/shared_prefs/usertoken.xml").exists()){
                        if(isColl){
                            user_shopdetail_collect.setBackgroundResource(R.mipmap.user_person_collection1);
                            Toast.makeText(getApplicationContext(),"取消收藏成功",Toast.LENGTH_SHORT).show();
                            //向服务器提交数据用户取消哦收藏了这个店铺
                            addCollection();
                            isColl=false;

                        }else {
                            user_shopdetail_collect.setBackgroundResource(R.mipmap.user_person_collection2);
                            Toast.makeText(getApplicationContext(),"添加收藏成功",Toast.LENGTH_SHORT).show();
                            //向服务器提交数据用户收藏了这个店铺
                            addCollection();
                            isColl = true;

                        }
                    }else {
                       /* Intent intent = new Intent();*/
                        intent.setClass(getApplicationContext(),UsersLoginActivity.class);
                        startActivity(intent);

                    }

                    break;


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
        //活动下拉
        mHiddenLayout = (LinearLayout) findViewById(R.id.linear_hidden);
        mIv = (ImageView) findViewById(R.id.my_iv);
        user_shop_activity_lv = findViewById(R.id.user_shop_activity_lv);
        shop_not_activity_tv = findViewById(R.id.shop_not_activity_tv);
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
        //获取店铺活动
        for (ShopActivity shopActivity :userShopDetail.getActivitySet()) {
            shopActivityList.add(shopActivity);
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
        mAdapter.setMyItemClickListener(new UserShopDetailBaberRecyclerAdapter.MyItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Barber barber = barberList.get(position);
                String chatId = barber.getUser().getUserAccount();
                String chatName = barber.getUser().getUserName();
                Log.i("ztl","理发师账户名"+chatId);
                String currUsername = EMClient.getInstance().getCurrentUser();
                if (chatId.equals(currUsername)) {
                    Toast.makeText(UserShopDetailActivity.this, "不能和自己聊天", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent = new Intent(UserShopDetailActivity.this, ECChatActivity.class);
                // EaseUI封装的聊天界面需要这两个参数，聊天者的username，以及聊天类型，单聊还是群聊
                intent.putExtra("userId", chatId);
                intent.putExtra("chatType", EMMessage.ChatType.Chat);
                intent.putExtra("username",chatName);
                startActivity(intent);
            }
        });
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
                    .load(UrlAddress.url+shopPictureList.get(i))
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

        if(true){
            SharedPreferences sharedPreferences =this.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
            String userToken = sharedPreferences.getString("token","");
            String userAccount = sharedPreferences.getString("userAccount","");
            String userPassword = sharedPreferences.getString("userPassword","");
            String shopId = String.valueOf(userShopDetail.getShopId());
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("UserAccount",userAccount);
            builder.add("UserPassword",userPassword);
            builder.add("shopId",shopId);
            final FormBody body = builder.build();
            Request request = new Request.Builder().header("UserTokenSQL",userToken).post(body).url(UrlAddress.url+"CheckIsCollected.action").build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String isCollection = response.body().string();
                    Log.i("collection",isCollection);
                    Message message = Message.obtain();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("isCollection",isCollection);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            });
            //未登陆跳转到登陆界面
        }else {
          /*  Intent intent = new Intent();
            intent.setClass(this,UsersLoginActivity.class);
            startActivity(intent);*/
        }
    }

    /**
     * 添加收藏
     * */
    public void addCollection(){

        if(true){
            SharedPreferences sharedPreferences =this.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
            String userToken = sharedPreferences.getString("token","");
            String userAccount = sharedPreferences.getString("userAccount","");
            String userPassword = sharedPreferences.getString("userPassword","");
            String shopId = String.valueOf(userShopDetail.getShopId());
            FormBody.Builder builder = new FormBody.Builder();
            builder.add("UserAccount",userAccount);
            builder.add("UserPassword",userPassword);
            builder.add("shopId",shopId);
            final FormBody body = builder.build();
            Request request = new Request.Builder().header("UserTokenSQL",userToken).post(body).url(UrlAddress.url+"SaveOrDeleteCollection.action").build();
            Call call = okHttpClient.newCall(request);
            call.enqueue(new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {

                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    String isCollection = response.body().string();
                    Log.i("addcollection",isCollection);
                    Message message = Message.obtain();
                    message.what = 2;
                    Bundle bundle = new Bundle();
                    bundle.putString("isCollection",isCollection);
                    message.setData(bundle);
                    handler.sendMessage(message);
                }
            });
            //未登陆跳转到登陆界面
        }
    }
    //活动下拉
    public void onClick(View v) {
        if (mHiddenLayout.getVisibility() == View.GONE) {
            animateOpen(mHiddenLayout);
            animationIvOpen();
        } else {
            animateClose(mHiddenLayout);
            animationIvClose();
        }
    }

    private void animateOpen(View v) {
        v.setVisibility(View.VISIBLE);
        ValueAnimator animator = createDropAnimator(v, 0,
                mHiddenViewMeasuredHeight);
        animator.start();

    }

    private void animationIvOpen() {
        RotateAnimation animation = new RotateAnimation(0, 180,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        mIv.startAnimation(animation);
    }

    private void animationIvClose() {
        RotateAnimation animation = new RotateAnimation(180, 0,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF,
                0.5f);
        animation.setFillAfter(true);
        animation.setDuration(100);
        mIv.startAnimation(animation);
    }

    private void animateClose(final View view) {
        int origHeight = view.getHeight();
        ValueAnimator animator = createDropAnimator(view, origHeight, 0);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }

        });
        animator.start();
    }

    private ValueAnimator createDropAnimator(final View v, int start, int end) {
        ValueAnimator animator = ValueAnimator.ofInt(start, end);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {

            @Override
            public void onAnimationUpdate(ValueAnimator arg0) {
                int value = (int) arg0.getAnimatedValue();
                ViewGroup.LayoutParams layoutParams = v.getLayoutParams();
                layoutParams.height = value;
                v.setLayoutParams(layoutParams);

            }
        });
        return animator;
    }


    //////////活动下啦//////
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
