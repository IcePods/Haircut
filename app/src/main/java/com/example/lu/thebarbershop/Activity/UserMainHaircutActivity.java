package com.example.lu.thebarbershop.Activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lu.thebarbershop.Adapter.HaircutRecyclerviewAdapter;
import com.example.lu.thebarbershop.Entity.HairStyle;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.MyTools.PrepareHairStylePicture;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class UserMainHaircutActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private HaircutRecyclerviewAdapter mAdapter;
    private ImageButton backbutton;//返回按钮 id=user_main_haircut_back_imgbtn
    private TextView hairnametxt;//顶部发型类型名称 id=user_main_haircut_name
    private List<HairStyle> cutlist;

    OkHttpClient okHttpClient= new OkHttpClient();
    private static final MediaType MEDIA_TYPE_MARKDOWN = MediaType.parse("text/plain;charset=UTF-8");

    Handler handler =new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle2 = msg.getData();
                    String perms = bundle2.getString("indexByType");
                    Log.i("index",perms);
                    Gson gson1 = new Gson();
                    cutlist=gson1.fromJson(perms,new TypeToken<List<HairStyle>>(){}.getType());
                    Log.i("index",cutlist.get(1).getHairstyleName()+"");
                    initAdapter();

                    break;
            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_haircut);
        postGetHairStyleByType("剪发",1);
        //获取控件id
        backbutton = findViewById(R.id.user_main_haircut_back_imgbtn);
        hairnametxt = findViewById(R.id.user_main_haircut_name);
        //绑定事件监听器
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hairnametxt.setText("剪发");

        //创建静态数据
        PrepareHairStylePicture hairStyles = new PrepareHairStylePicture();
        mRecyclerView = findViewById(R.id.user_main_haircut_recyclerview);
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);

    }

    private void initAdapter() {
        mAdapter = new HaircutRecyclerviewAdapter(this, R.layout.item_user_main_haircut_recyclerview, cutlist);

        mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
        //给发型展示页面的子item绑定点击事件监听器 跳转到对应的详情页面
        mAdapter.setItemClickListener(new HaircutRecyclerviewAdapter.MyItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(getApplicationContext(),"aaa"+position, Toast.LENGTH_SHORT).show();
                //携带数据跳转到另一个Activity，进行数据的更新操作
                Intent intent = new Intent();
                //指定跳转路线
                intent.setClass(getApplicationContext(),HairStyleDetailActivity.class);
                //把点击的商品对象添加到intent对象中去
                Bundle bundle = new Bundle();

                HairStyle hairStyle =cutlist.get(position);
                bundle.putSerializable("hairStyle",hairStyle);
                bundle.putString("fromIndex","1");
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }

    //POST请求，带有封装请求参数的请求方式
    private void postGetHairStyleByType(final String a,final int num){
        new Thread(){
            @Override
            public void run() {

                RequestBody body = RequestBody.create(MEDIA_TYPE_MARKDOWN,a);
                Request.Builder builder = new Request.Builder();
                builder.url(UrlAddress.url+"getHairStyleByTypeInHome.action");
                builder.post(body);
                Request request = builder.build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String a = response.body().string();
                    Message message =Message.obtain();
                    message.what =num;
                    Bundle bundle =new Bundle();
                    bundle.putString("indexByType",a);
                    message.setData(bundle);
                    handler.sendMessage(message);

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();


    }
}
