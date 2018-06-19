package com.example.lu.thebarbershop.Activity;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;

import com.example.lu.thebarbershop.Adapter.IndexShopDetailAdapter;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.MyTools.RecordSQLiteOpenHelper;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class UserSearchActivityCopy extends AppCompatActivity {
    private ImageView userSearchBack;//返回键
    private EditText userSearchEdit;//输入框
    private TextView userSearchSouSuo;//搜索内个字
   /* private Fragment searchHistoryFragment;//搜索历史的Fragemnt
    private Fragment searchResultFragment;//搜索结果的Fragment
    private FrameLayout userSearch;
    private FragmentManager fragmentManager;
    private Fragment currentFragment;*/
    OkHttpClient okHttpClient = new OkHttpClient();
    private List<UserShopDetail> userShopDetails;
    private ListView lv_searchHistory;//搜索历史listview
    private SQLiteDatabase database;
    private Button userSearchClean;
    private TextView userSearchResult;
    private SimpleCursorAdapter simpleCursorAdapter;
    private IndexShopDetailAdapter indexShopDetailAdapter;
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
                   indexShopDetailAdapter.notifyDataSetChanged();
                   break;
           }
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        //初始化控件以及Fragment
        initView();

        //默认显示第一个页面
      /*  changeFragment(searchHistoryFragment);*/



        //清空记录
        userSearchClean.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteData();
                queryData("");
                Log.i("hzl","点击了清空");
            }
        });
        //设置搜索点击回调函数
        /*onKeyDownListener();*/


       /* lv_searchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                userSearchEdit.setText(name);
                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                // TODO 获取到item上面的文字，根据该关键字跳转到另一个页面查询
                Intent intent = new Intent();

            }
        });*/
        //点击历史记录进行搜索


        // 第一次进入查询所有的历史记录
        queryData("");
        Log.i("hzl","第一次显示所有的记录");

        userSearchBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        //文本框监听
        userSearchEdit.addTextChangedListener(watcher);



    }

    //初始化控件
    public void initView(){
        userSearchBack =findViewById(R.id.user_search_back);
        userSearchEdit = findViewById(R.id.user_search_edit);
        userSearchSouSuo = findViewById(R.id.user_search_sousuo);
     /*   searchHistoryFragment = new SearchHistoryFragment(this);
        searchResultFragment = new SearchResultFragment(this);
        userSearch = findViewById(R.id.search_fragment);*/
        lv_searchHistory =findViewById(R.id.user_search_listview);
        /*userSearchClean = findViewById(R.id.user_search_clean);*/
        userSearchResult =findViewById(R.id.user_search_result);
    }

    //切换页面函数
   /* public void changeFragment(Fragment fragment){
        if(null == fragmentManager){
            fragmentManager = getFragmentManager();
        }
        if(currentFragment!= fragment){
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            transaction.hide(currentFragment);
            if(!fragment.isAdded()){
                transaction.add(R.id.search_fragment,fragment);
            }
            transaction.show(fragment);
            transaction.commit();
            currentFragment = fragment;
        }
    }*/

    //get得到搜索结果
    public void postSearchResult(final String searchEdit){
        new Thread(){
            @Override
            public void run() {
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("",searchEdit);
                FormBody body = formBody.build();
                Request request = new Request.Builder().url(UrlAddress.url+"").post(body).build();
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
                    }
                });

            }
        }.start();
    }
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
        database.execSQL("insert into searchhistory(history) values('" + name + "')");
        database.close();
    }
    /*
     * 模糊查询数据
     */
    private void queryData(String name){
        Cursor cursor= recordSQLiteOpenHelper.getReadableDatabase().rawQuery(
                "select id as _id,history from searchhistory where history like '%" + name + "%' order by id desc ", null);

                simpleCursorAdapter = new SimpleCursorAdapter(
                        this,
                        R.layout.item_user_search_history_list,
                        cursor,
                        new String[] { "history" },
                        new int[] { R.id.item_user_search },
                        CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER);
                // 设置适配器
                lv_searchHistory.setAdapter(simpleCursorAdapter);
                simpleCursorAdapter.notifyDataSetChanged();




    }
    /**
     * 从服务器获取的数据后展示的方法
     * */
    private void searchResuleAdapter(){
        indexShopDetailAdapter = new IndexShopDetailAdapter(this,userShopDetails,R.layout.item_user_index_shop);
        lv_searchHistory.setAdapter(indexShopDetailAdapter);
        lv_searchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
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
    /**
     * 检查数据库中是否已经有该条记录
     * */
    /*private boolean hasData(String name) {
        Cursor cursor = recordSQLiteOpenHelper.getReadableDatabase().query(
                "searchhistory",
                null,
                "history=?",
                 new String[]{name},
                null,
                null,
                null);
       *//* Cursor cursor = recordSQLiteOpenHelper.getReadableDatabase().rawQuery(
                "select * from searchhistory where history=?",
                new String[]{name});*//*

        //判断是否有下一个
        return cursor.moveToNext();
    }*/

   /**
     * 清空数据*/
    private void deleteData() {
        database = recordSQLiteOpenHelper.getWritableDatabase();
        database.execSQL("delete from searchhistory");
        database.close();
    }

   //按下搜索触发事件
    /*public void onKeyDownListener(){
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
                    Toast.makeText(getApplicationContext(), "clicked!", Toast.LENGTH_SHORT).show();
                    userSearchEdit.setText("");
                }
                return false;
            }
        });
    }*/
    //设置文本框实时监听
    private TextWatcher watcher = new TextWatcher() {
        @Override
        public void beforeTextChanged(CharSequence s, int start, int count, int after) {

        }

        @Override
        public void onTextChanged(CharSequence s, int start, int before, int count) {

            postSearchResult(s.toString());
        }

        @Override
        public void afterTextChanged(Editable s) {
            if (s.toString().trim().length() == 0) {
                userSearchResult.setText("搜索历史");
            } else {
                userSearchResult.setText("搜索结果");
            }
            String tempName = userSearchEdit.getText().toString();
            // 根据tempName去模糊查询数据库中有没有数据
            queryData(tempName);


        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(null!= recordSQLiteOpenHelper){
            recordSQLiteOpenHelper.close();
        }
    }
}
