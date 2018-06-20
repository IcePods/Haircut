package com.example.lu.thebarbershop.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lu.thebarbershop.Adapter.IndexShopDetailAdapter;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.R;


import java.util.List;

public class SearchResultShopShowActivity extends AppCompatActivity {
    private List<UserShopDetail> shopList;
    private IndexShopDetailAdapter indexShopDetailAdapter;//主页店铺展示的adapter
    private ListView shopListView;
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result_shop_show);

        shopListView=findViewById(R.id.user_search_result_shop_show);
        back = findViewById(R.id.search_shop_show_back_imgbtn);
        shopList = (List<UserShopDetail>)getIntent().getSerializableExtra("searchShopShowList");

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        initShopAdapter();
    }
    public void initShopAdapter(){
        //实例化数据
        indexShopDetailAdapter = new IndexShopDetailAdapter(this,shopList,R.layout.item_user_index_shop);
        shopListView.setAdapter(indexShopDetailAdapter);
        shopListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //携带数据跳转到另一个Activity，进行数据的更新操作
                Intent intent = new Intent();
                //指定跳转路线
                intent.setClass(getApplicationContext(),UserShopDetailActivity.class);
                //把点击的商品对象添加到intent对象中去
                Bundle bundle = new Bundle();
                UserShopDetail userShopDetail = (UserShopDetail)indexShopDetailAdapter.getItem(position);
                bundle.putSerializable("userShopDetail",userShopDetail);
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}
