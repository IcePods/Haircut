package com.example.lu.thebarbershop.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lu.thebarbershop.Adapter.CollectionAdapter;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserCollections;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.MyTools.PrepareIndexShopDetail;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * 我的收藏页面
 */
public class UserPersonCollectionActivity extends AppCompatActivity {
    private ImageButton backbutton;//返回按钮
    private Mylistener mylistener;//监听器
    private ListView lv;//listview
    private List<UserShopDetail> userShopDetailsLisk = new ArrayList<>();
    private List<UserCollections> collectionsList;
    OkHttpClient okHttpClient = new OkHttpClient();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    String getusercollections = bundle.getString("getusercollections");
                    Log.i("getusercollections",getusercollections);
                    Gson gson = new Gson();
                    collectionsList = gson.fromJson(getusercollections,new TypeToken<List<UserCollections>>(){}.getType());
                    for(UserCollections i:collectionsList){
                        userShopDetailsLisk.add(i.getShop());
                    }
                    Log.i("userShopDetailsLisk",userShopDetailsLisk.toString());
                    initAdapter();
                    break;

            }
            super.handleMessage(msg);
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_person_collection);

        lv = findViewById(R.id.item_user_person_collection);
        backbutton = findViewById(R.id.user_person_collection_back);//返回按钮

        mylistener = new Mylistener();

        //获取用户收藏
        getUserCollections();
        backbutton.setOnClickListener(mylistener);
    }

    private void initAdapter() {
        final CollectionAdapter adapter = new CollectionAdapter(this, R.layout.item_user_person_collection,userShopDetailsLisk);
        lv.setAdapter(adapter);
        lv.setDivider(null);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //携带数据跳转到另一个Activity，进行数据的更新操作
                Intent intent = new Intent();
                //指定跳转路线
                intent.setClass(getApplicationContext(),UserShopDetailActivity.class);
                //把点击的商品对象添加到intent对象中去
                Bundle bundle = new Bundle();
                UserShopDetail userShopDetail = (UserShopDetail)adapter.getItem(position);
                bundle.putSerializable("userShopDetail",userShopDetail);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    private class Mylistener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            switch (v.getId()){
                //返回按钮
                case R.id.user_person_collection_back:
                    //只销毁当前页面
                    finish();
                    break;
            }
        }
    }
   /* //静态数据模拟
    private List<UserShopDetail> prepareDatas() {
        PrepareIndexShopDetail pshopDetail = new PrepareIndexShopDetail();
        List<UserShopDetail> shops = new ArrayList<>();
        //第一个店铺 从PrepareIndexShopDetail类中获取前两个店铺信息
        UserShopDetail shopDetaila = pshopDetail.prepareIndexShopDetail().get(0);
        shops.add(shopDetaila);
        //第二个店铺 从PrepareIndexShopDetail类中获取前两个店铺信息
        UserShopDetail shopDetailb = pshopDetail.prepareIndexShopDetail().get(1);
        shops.add(shopDetailb);

        return shops;
    }*/
    //post方式得到用户的收藏
    public void getUserCollections(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("token","");
        Log.i("hzl",token);
        final String userAccount = sharedPreferences.getString("userAccount","");
        final String userPassword = sharedPreferences.getString("userPassword","");
        new Thread(){
            @Override
            public void run() {
                Request.Builder builder = new Request.Builder();
                FormBody.Builder builder1 = new FormBody.Builder();
                builder1.add("UserAccount",userAccount);
                builder1.add("UserPassword",userPassword);
                FormBody body = builder1.build();
                final Request request = builder.header("UserTokenSql",token).post(body).url(UrlAddress.url+"findCollectionByUser.action").build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String a = response.body().string();
                    Log.i("getusercollections",a);
                    Message message = Message.obtain();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("getusercollections",a);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

}