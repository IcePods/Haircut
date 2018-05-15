package com.example.lu.thebarbershop.Activity;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import com.example.lu.thebarbershop.Adapter.CollectionAdapter;
import com.example.lu.thebarbershop.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
                Toast.makeText(getApplicationContext(),"a",Toast.LENGTH_SHORT).show();
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
    private List<Map<String,Object>> prepareDatas() {
        List<Map<String,Object>> shops = new ArrayList<>();
        //第一个商品
        Map<String,Object> shopa = new HashMap<>();
        shopa.put("img",R.mipmap.user_person_headimg);
        shopa.put("name","第一个收藏的店铺");
        shopa.put("address","万达广场 火锅");
        shops.add(shopa);
        Map<String,Object> shopb = new HashMap<>();
        shopb.put("img",R.mipmap.user_person_headimg);
        shopb.put("name","第一个收藏的店铺");
        shopb.put("address","万达广场 火锅");
        shops.add(shopb);

        return shops;
    }
}