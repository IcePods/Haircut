package com.example.lu.thebarbershop.Fragment;

import android.app.Fragment;
import android.content.Context;

import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.graphics.drawable.BitmapDrawable;

import android.content.Intent;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Dimension;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ScrollView;
import android.widget.TextView;


import com.bumptech.glide.Glide;
import com.dou361.dialogui.DialogUIUtils;
import com.dou361.dialogui.listener.DialogUIListener;
import com.example.lu.thebarbershop.Activity.UserSearchActivity;
import com.example.lu.thebarbershop.Activity.UserMainHaircolorActivity;
import com.example.lu.thebarbershop.Activity.UserMainHaircutActivity;
import com.example.lu.thebarbershop.Activity.UserMainNurseActivity;
import com.example.lu.thebarbershop.Activity.UserMainPermActivity;
import com.example.lu.thebarbershop.Activity.UserShopDetailActivity;
import com.example.lu.thebarbershop.Activity.UsersLoginActivity;
import com.example.lu.thebarbershop.Adapter.IndexShopDetailAdapter;
import com.example.lu.thebarbershop.Entity.HairStyle;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.Entity.Users;
import com.example.lu.thebarbershop.MyTools.GetRoundedCornerBitmap;
import com.example.lu.thebarbershop.MyTools.GetUserFromShared;
import com.example.lu.thebarbershop.MyTools.LooperTextView;
import com.example.lu.thebarbershop.MyTools.PrepareIndexShopDetail;
import com.example.lu.thebarbershop.MyTools.PrepareIndexViewPagerDate;
import com.example.lu.thebarbershop.MyTools.PrepareLooperTextDate;
import com.example.lu.thebarbershop.MyTools.ViewPagerTools;

import com.example.lu.thebarbershop.Activity.UsersRegisterActivity;

import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.reflect.TypeToken;

import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lu on 2018/5/9 0009.
 * 主页面fragment
 */

public class MainFragment extends Fragment implements ViewPager.OnPageChangeListener{
    private Context mContext;
    private ImageView haircut;//剪发 id=user_index_haircut
    private TextView haircuttxt;
    private ImageView haircolor;//染发 id=user_index_haircolor
    private TextView haircolortxt;
    private ImageView perm;//烫发 id=user_index_perm
    private TextView permtxt;
    private ImageView nurse;//护理 id=user_index_nurse
    private TextView nursetxt;
    private TextView address;
    private LinearLayout search;
    private ImageView plus;
    private ScrollView scrollView;
    private LooperTextView looperTextView;
    private ListView lv_shop;
    private ViewPager vp;
    private LinearLayout ll_point;
    private List<UserShopDetail> allShopList;

    RefreshLayout refreshLayout;

    private List<HairStyle> permsList;

    private ArrayList<ImageView> imageViewArrayList = new ArrayList<ImageView>(); //存放轮播图片图片的集合
    private int lastPosition;//轮播图下边点的位置
    private boolean isRunning = false; //viewpager是否在自动轮询

    private IndexShopDetailAdapter indexShopDetailAdapter;//主页店铺展示的adapter

    private Mylistener mylistener;//点击事件监听器

    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/plain;charset=UTF-8");

    //获取店铺信息
    /*private final String url="";*/
    OkHttpClient okHttpClient= new OkHttpClient();
   /* private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/plain;charset=UTF-8");*/

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    String allShop = bundle.getString("allshop");
                    Log.i("allshop",allShop);
                    Gson gson =new Gson();
                    allShopList =gson.fromJson(allShop,new TypeToken<List<UserShopDetail>>(){}.getType());
                    Log.i("allshop",allShopList.size()+"");
                    initShopAdapter();
                    break;
                case 2:
                    Bundle bundle1 = msg.getData();
                    String fromServiceUser = bundle1.getString("fromServiceToken");
                    Log.i("fromServiceToken",fromServiceUser);
                    Gson gson1 =new Gson();
                    Users users = gson1.fromJson(fromServiceUser,Users.class);
                    //得到服务器返回的user的token
                    String fromServiceToken = users.getUserToken();
                    String localUserToken = new GetUserFromShared(mContext).getUserTokenFromShared();
                    SharedPreferences sharedPreferences= mContext.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
                    if(!fromServiceToken.equals(localUserToken)){
                       //登录失效跳转到登录界面
                       alertDialog();

                    }

            }
            super.handleMessage(msg);
        }
    };

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        mContext =  getActivity().getApplicationContext();
        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.fragment_index,container,false);
        //初始化控件
        init(view);

        //绑定事件监听器
        mylistener = new Mylistener();
        perm.setOnClickListener(mylistener);
        permtxt.setOnClickListener(mylistener);
        haircut.setOnClickListener(mylistener);
        haircuttxt.setOnClickListener(mylistener);
        haircolor.setOnClickListener(mylistener);
        haircolortxt.setOnClickListener(mylistener);
        nurse.setOnClickListener(mylistener);
        nursetxt.setOnClickListener(mylistener);
        //设置刷新

        //设置图片为圆形
        setRounded();
        //得到轮播图片集合
        addViewPagerImages();
        //开启图片轮训
        viewPagerThread();
        //消息上下轮播
        looperTextView.setTipList(new PrepareLooperTextDate().getLooperList());
        //从服务器获取usershopdetail
        getUserShopDetail();
        //店铺listview

        //设置scroll开始从头显示
       /* scrollView.smoothScrollTo(0,0);*/
       vp.setFocusable(true);
       vp.setFocusableInTouchMode(true);
       vp.requestFocus();
        //设置监听器
        MianFragmentListener mianFragmentListener = new MianFragmentListener();
        search.setOnClickListener(mianFragmentListener);

        postJudgeUserIsLogin();

        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);
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
        perm = view.findViewById(R.id.user_index_perm);//烫发
        permtxt = view.findViewById(R.id.user_index_perm_txt);
        haircut = view.findViewById(R.id.user_index_haircut);//剪发
        haircuttxt = view.findViewById(R.id.user_index_haircut_txt);
        haircolor = view.findViewById(R.id.user_index_haircolor);//染发
        haircolortxt = view.findViewById(R.id.user_index_haircolor_txt);
        nurse = view.findViewById(R.id.user_index_nurse);//护理
        nursetxt = view.findViewById(R.id.user_index_nurse_txt);
        vp = view.findViewById(R.id.user_index_viewpager_content);
        lv_shop = view.findViewById(R.id.user_person_index_lvshop_content);
        ll_point = view.findViewById(R.id.ll_point);
        looperTextView = view.findViewById(R.id.user_index_loopertextview);
        scrollView =view.findViewById(R.id.user_index_scroll);
        refreshLayout = view.findViewById(R.id.refreshLayout);

    }

    /**7
     *设置为圆形
     * */
    public void setRounded(){
        GetRoundedCornerBitmap GetRounded = new GetRoundedCornerBitmap();
        haircut.setImageBitmap(GetRounded.getRoundedCornerBitmap(((BitmapDrawable)haircut.getDrawable()).getBitmap(),2));
        haircolor.setImageBitmap(GetRounded.getRoundedCornerBitmap(((BitmapDrawable)haircolor.getDrawable()).getBitmap(),2));
        perm.setImageBitmap(GetRounded.getRoundedCornerBitmap(((BitmapDrawable)perm.getDrawable()).getBitmap(),2));
        nurse.setImageBitmap(GetRounded.getRoundedCornerBitmap(((BitmapDrawable)nurse.getDrawable()).getBitmap(),2));

    }

    /**
     * 点击事件监听器类
     */
    private class Mylistener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            Intent intent = new Intent();
            switch (v.getId()){
                //点击烫发图片 跳转到对应发型页面
                case R.id.user_index_perm:
                    intent.setClass(getActivity().getApplicationContext(),UserMainPermActivity.class);
                    startActivity(intent);
                    break;
                //点击烫发文字 跳转到对应发型界面
                case R.id.user_index_perm_txt:
                    intent.setClass(getActivity().getApplicationContext(),UserMainPermActivity.class);
                    startActivity(intent);
                    break;
                //剪发
                case R.id.user_index_haircut:

                    intent.setClass(getActivity().getApplicationContext(),UserMainHaircutActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_index_haircut_txt:
                    intent.setClass(getActivity().getApplicationContext(),UserMainHaircutActivity.class);
                    startActivity(intent);
                    break;
                //染发
                case R.id.user_index_haircolor:

                    intent.setClass(getActivity().getApplicationContext(),UserMainHaircolorActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_index_haircolor_txt:
                    intent.setClass(getActivity().getApplicationContext(),UserMainHaircolorActivity.class);
                    startActivity(intent);
                    break;
                //护理
                case R.id.user_index_nurse:

                    intent.setClass(getActivity().getApplicationContext(),UserMainNurseActivity.class);
                    startActivity(intent);
                    break;
                case R.id.user_index_nurse_txt:
                    intent.setClass(getActivity().getApplicationContext(),UserMainNurseActivity.class);
                    startActivity(intent);
                    break;
            }
        }
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
        //RequestOptions requestOptions = new RequestOptions().centerCrop();
        //requestOptions.placeholder(R.mipmap.user_index_nurse);
        for(int i=0;i<list.size();i++){
            ImageView imageView =new ImageView(mContext);
            Glide.with(mContext)
                    .load(list.get(i))
                    .placeholder(R.mipmap.user_index_nurse)
                    .centerCrop()
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
        vp.setOnPageChangeListener(this);
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
        indexShopDetailAdapter = new IndexShopDetailAdapter(mContext,allShopList,R.layout.item_user_index_shop);
        lv_shop.setAdapter(indexShopDetailAdapter);
        lv_shop.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //携带数据跳转到另一个Activity，进行数据的更新操作
                Intent intent = new Intent();
                //指定跳转路线
                intent.setClass(getActivity().getApplicationContext(),UserShopDetailActivity.class);
                //把点击的商品对象添加到intent对象中去
                Bundle bundle = new Bundle();
                UserShopDetail userShopDetail = (UserShopDetail)indexShopDetailAdapter.getItem(position);
                bundle.putSerializable("userShopDetail",userShopDetail);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
    //点击事件监听器
    class MianFragmentListener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                case R.id.user_index_search:
                    Intent intent = new Intent();
                    intent.setClass(mContext, UserSearchActivity.class);
                    getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                    startActivity(intent);
            }
        }
    }

    //Get请求，带有封装请求参数的请求方式
    private void getUserShopDetail(){
        Request request = new Request.Builder().url(UrlAddress.url+"AllShop.action").build();
        final Call call = okHttpClient.newCall(request);
        new Thread(){
            @Override
            public void run() {

                try {
                    Response response = call.execute();
                    String a = response.body().string();
                    Message message =Message.obtain();
                    message.what =1;
                    Bundle bundle =new Bundle();
                    bundle.putString("allshop",a);
                    message.setData(bundle);
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }.start();


    }

    public void postJudgeUserIsLogin(){
        //判断usertoken是否为空
        if(new File(mContext.getApplicationContext().getFilesDir().getParent()+"/shared_prefs/usertoken.xml").exists()){

            if(mContext.getSharedPreferences("usertoken",Context.MODE_PRIVATE)!=null){
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("UserAccount", new GetUserFromShared(mContext).getUserAccountFromShared());
                builder.add("UserPassword",new GetUserFromShared(mContext).getUserPasswordFromShared());
                FormBody body = builder.build();
                Request request = new Request.Builder().url(UrlAddress.url+"FindTokenByAccountAndPwd.action").post(body).build();
                Call call  = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String fromServiceToken = response.body().string();
                        Message message = Message.obtain();
                        message.what = 2;
                        Bundle bundle = new Bundle();
                        bundle.putString("fromServiceToken",fromServiceToken);
                        message.setData(bundle);
                        handler.sendMessage(message);
                    }
                });

            }
        }


    }

    //下拉刷新上滑加载的点击事件
    public void indexPullRefresher(){
        //下拉
        refreshLayout.setOnRefreshListener(new OnRefreshListener() {
            @Override
            public void onRefresh(@NonNull RefreshLayout refreshLayout) {

            }
        });
        //上滑加载的点击事件
        refreshLayout.setOnLoadMoreListener(new OnLoadMoreListener() {
            @Override
            public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

            }
        });
    }


    /**
     * 重写ondestory
     * */
    @Override
    public void onDestroy() {

        super.onDestroy();
        isRunning=false;
    }

    public void alertDialog(){
        DialogUIUtils.showMdAlert(getActivity(), "登录失效", "请重新登录",new DialogUIListener() {
            @Override
            public void onPositive() {
                Intent intent = new Intent();
                intent.setClass(mContext,UsersLoginActivity.class);
                startActivity(intent);
            }

            @Override
            public void onNegative() {

            }

        }).show();

    }
}
