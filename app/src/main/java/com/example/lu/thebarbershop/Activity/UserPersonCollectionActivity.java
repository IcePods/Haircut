package com.example.lu.thebarbershop.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;

import com.example.lu.thebarbershop.Adapter.CollectionAdapter;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.MyTools.PrepareIndexShopDetail;
import com.example.lu.thebarbershop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的收藏页面
 */
public class UserPersonCollectionActivity extends AppCompatActivity {
    private ImageButton backbutton;//返回按钮
    private Mylistener mylistener;//监听器

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_person_collection);

        ListView lv = findViewById(R.id.item_user_person_collection);
        backbutton = findViewById(R.id.user_person_collection_back);//返回按钮

        mylistener = new Mylistener();

        final CollectionAdapter adapter = new CollectionAdapter(this,R.layout.item_user_person_collection,prepareDatas());
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
        backbutton.setOnClickListener(mylistener);
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
    //静态数据模拟
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
    }
}