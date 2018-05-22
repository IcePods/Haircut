package com.example.lu.thebarbershop.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import com.example.lu.thebarbershop.Adapter.NurseRecyclerviewAdapter;
import com.example.lu.thebarbershop.Entity.HairStyle;
import com.example.lu.thebarbershop.MyTools.PrepareHairStylePicture;
import com.example.lu.thebarbershop.R;

public class UserMainNurseActivity extends AppCompatActivity {
    private RecyclerView mRecyclerView;
    private RecyclerView.LayoutManager mLayoutManager;
    private NurseRecyclerviewAdapter mAdapter;
    private ImageButton backbutton;//返回按钮 id=user_main_nurse_back_imgbtn
    private TextView hairnametxt;//顶部发型类型名称 id=user_main_nurse_name

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_main_nurse);

        //获取控件id
        backbutton = findViewById(R.id.user_main_nurse_back_imgbtn);
        hairnametxt = findViewById(R.id.user_main_nurse_name);
        //绑定事件监听器
        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        hairnametxt.setText("护理");

        //创建静态数据
        PrepareHairStylePicture hairStyles = new PrepareHairStylePicture();
        mRecyclerView = findViewById(R.id.user_main_nurse_recyclerview);
        //设置布局管理器为2列，纵向
        mLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mAdapter = new NurseRecyclerviewAdapter(this, R.layout.item_user_main_nurse_recyclerview, hairStyles.prepareHairStylePicture()); mRecyclerView.setLayoutManager(mLayoutManager);

        mRecyclerView.setAdapter(mAdapter);
        //给发型展示页面的子item绑定点击事件监听器 跳转到对应的详情页面
        mAdapter.setItemClickListener(new NurseRecyclerviewAdapter.MyItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
//                Toast.makeText(getApplicationContext(),"aaa"+position, Toast.LENGTH_SHORT).show();
                //携带数据跳转到另一个Activity，进行数据的更新操作
                Intent intent = new Intent();
                //指定跳转路线
                intent.setClass(getApplicationContext(),MainActivity.class);
                //把点击的商品对象添加到intent对象中去
                Bundle bundle = new Bundle();

                HairStyle hairStyle = PrepareHairStylePicture.prepareHairStylePicture().get(position);
                bundle.putSerializable("hairStyle",hairStyle);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
