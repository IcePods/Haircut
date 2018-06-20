package com.example.lu.thebarbershop.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.style.TtsSpan;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lu.thebarbershop.Adapter.CollectionAdapter;
import com.example.lu.thebarbershop.Adapter.IndexShopDetailAdapter;
import com.example.lu.thebarbershop.Adapter.SearchResultAdapter;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.Fragment.SearchHistoryFragment;
import com.example.lu.thebarbershop.Fragment.SearchResultFragment;
import com.example.lu.thebarbershop.MyTools.RecordSQLiteOpenHelper;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserSearchActivity extends AppCompatActivity {
    private ImageView userSearchBack;//返回键
    private EditText userSearchEdit;//输入框
    private TextView userSearchSouSuo;//搜索内个字
    OkHttpClient okHttpClient = new OkHttpClient();
    private List<UserShopDetail> userShopDetails;
    private ListView lv_searchHistory;//搜索历史listview
    private SQLiteDatabase database;
    private Button userSearchClean;
    private TextView userSearchResult;//显示搜索历史和搜索结果的内个文字
    private ImageView searchError;
    private SimpleCursorAdapter simpleCursorAdapter;
    private SearchResultAdapter searchResultAdapter;
    private RecordSQLiteOpenHelper recordSQLiteOpenHelper = new RecordSQLiteOpenHelper(this);//实例化SQLOpenhelper

Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
           switch (msg.what){
               case 1:
                   Bundle bundle = msg.getData();
                   String searchList = bundle.getString("searchList");
                   Log.i("searchList",searchList);
                   Gson gson =new Gson();
                   userShopDetails =gson.fromJson(searchList,new TypeToken<List<UserShopDetail>>(){}.getType());
                   Log.i("searchList",userShopDetails.size()+"");
                   searchResuleAdapter();
                   searchResultAdapter.notifyDataSetChanged();
                   break;
           }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        /**
         * 初始化控件
         * */
        initView();
        //设置文本框监听
        userSearchEdit.addTextChangedListener(watcher);

        //返回键
        userSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        //第一次进入显示全部
        queryData("");

        //键盘搜索按钮点击事件
        onKeyDownListener();


    }

    /**
     * 初始化控件
     * */
    public void initView(){
        userSearchBack =findViewById(R.id.user_search_back);
        userSearchEdit = findViewById(R.id.user_search_edit);
        userSearchSouSuo = findViewById(R.id.user_search_sousuo);
        lv_searchHistory =findViewById(R.id.user_search_listview);
       /* userSearchClean = findViewById(R.id.user_search_clean);*/
       userSearchResult = findViewById(R.id.user_search_result);
       searchError = findViewById(R.id.search_error);


    }

    /**
     * post从服务器获取数据
     * */
    public void postSearchResult(final String searchEdit){
        new Thread(){
            @Override
            public void run() {
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("ShopName",searchEdit);
                FormBody body = formBody.build();
                Request request = new Request.Builder().url(UrlAddress.url+"SelectShopFuzzyMatching.action").post(body).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String searchList = response.body().string();
                        Message message = Message.obtain();
                        message.what=1;
                        Bundle bundle = new Bundle();
                        bundle.putString("searchList",searchList);
                        message.setData(bundle);
                        handler.sendMessage(message);

                    }
                });

            }
        }.start();
    }
    /**
     * 从服务器获取的数据后展示的方法
     * */
    private void searchResuleAdapter(){
        searchResultAdapter = new SearchResultAdapter(this, R.layout.item_user_search_result,userShopDetails);
       /* if(userShopDetails.size()==0){
            *//*headerView = getLayoutInflater().inflate(R.layout.search_error_headerview,null);
            lv_searchHistory.addHeaderView(headerView);*//*
            searchError.setVisibility(View.VISIBLE);
        }else{*/

            lv_searchHistory.setAdapter(searchResultAdapter);
            lv_searchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    TextView textView = (TextView)view.findViewById(R.id.user_person_collection_shop_name);
                    String name = textView.getText().toString();
                    //把点击的店铺的名字插入到数据库中
                    if(!hasData(name)){
                        insertData(name);
                    }

                    //携带数据跳转到另一个Activity，进行数据的更新操作
                    Intent intent = new Intent();
                    //指定跳转路线
                    intent.setClass(getApplicationContext(),UserShopDetailActivity.class);
                    //把点击的商品对象添加到intent对象中去
                    Bundle bundle = new Bundle();
                    UserShopDetail userShopDetail = (UserShopDetail)searchResultAdapter.getItem(position);
                    bundle.putSerializable("userShopDetail",userShopDetail);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
            });
       /* }*/


    }

    //设置文本框实时监听
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

        }

        @Override
        public void afterTextChanged(Editable s) {
            if(s.toString().trim().length() ==0){

                userSearchResult.setText("搜索历史");
                queryData("");
            }else{
                userSearchResult.setText("搜索结果");
                postSearchResult(s.toString());
            }

        }
    };

    /**
     * 插入数据
     * @param name 搜索的内容
     */
    private void insertData(String name){
        database = recordSQLiteOpenHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("history",name);
        long lInsert = database.insert("searchhistory",null,values);
        Log.i("hzl","记录插入成功");
        database.close();
    }

    /*
     * 模糊查询数据
     */
    private void queryData(String name){
        Cursor cursor= recordSQLiteOpenHelper.getReadableDatabase().rawQuery(
                "select id as _id,history from searchhistory where history like '%" + name + "%' order by id desc", null);

        simpleCursorAdapter = new SimpleCursorAdapter(
                this,
                R.layout.item_user_search_history_list,
                cursor,
                new String[] { "history" },
                new int[] { R.id.item_user_search },
                CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
        // 设置适配器
        lv_searchHistory.setAdapter(simpleCursorAdapter);
        lv_searchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView)view.findViewById(R.id.item_user_search);
                String name = textView.getText().toString();
                queryData(name);
                //TODO
                userSearchEdit.setText(name);
            }
        });
        simpleCursorAdapter.notifyDataSetChanged();

    }
    //按下搜索触发事件
    public void onKeyDownListener(){
        userSearchEdit.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                if (keyCode == KeyEvent.KEYCODE_ENTER && event.getAction() == KeyEvent.ACTION_DOWN) {// 修改回车键功能
                    // 先隐藏键盘
                    ((InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE)).hideSoftInputFromWindow(
                            getCurrentFocus().getWindowToken(), InputMethodManager.HIDE_NOT_ALWAYS);
                    // 按完搜索键后将当前查询的关键字保存起来,如果该关键字已经存在就不执行保存
                    boolean hasData = hasData(userSearchEdit.getText().toString().trim());
                    if (!hasData) {
                        insertData(userSearchEdit.getText().toString().trim());
                        queryData("");
                        Log.i("hzl","不相同搜索插入了");
                    }
                    // TODO 根据输入的内容模糊查询商品，并跳转到另一个界面，由你自己去实现
                    Toast.makeText(getApplicationContext(), "点击了搜索", Toast.LENGTH_SHORT).show();
                    if(userShopDetails.size()==0){
                        Intent intent = new Intent();
                        intent.setClass(getApplicationContext(),SearchErrorActivity.class);
                        startActivity(intent);

                    }else{
                        Intent intent1= new Intent();
                        intent1.setClass(getApplicationContext(),SearchResultShopShowActivity.class);
                        intent1.putExtra("searchShopShowList",(Serializable)userShopDetails);
                        startActivity(intent1);

                    }
                    userSearchEdit.setText("");
                }
                return false;
            }
        });
    }

    /**
     * 检查数据库中是否已经有该条记录
     * */
    private boolean hasData(String name) {
        Cursor cursor = recordSQLiteOpenHelper.getReadableDatabase().query(
                "searchhistory",
                null,
                "history=?",
                 new String[]{name},
                null,
                null,
                null);

        //判断是否有下一个
        return cursor.moveToNext();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!= recordSQLiteOpenHelper){
            recordSQLiteOpenHelper.close();
        }
    }
}
