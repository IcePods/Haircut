package com.example.lu.thebarbershop.Activity;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.example.lu.thebarbershop.MyTools.RecordSQLiteOpenHelper;
import com.example.lu.thebarbershop.R;

import java.util.Date;

public class UserSearchActivity extends AppCompatActivity {
    private ImageView userSearchBack;//返回键
    private EditText userSearchEdit;//输入框
    private TextView userSearchSouSuo;//搜索内个字
    private ListView lv_searchHistory;//搜索历史listview
    private SQLiteDatabase database;
    private Button userSearchClean;
    private TextView userSearchResult;
    private SimpleCursorAdapter simpleCursorAdapter;
    private RecordSQLiteOpenHelper recordSQLiteOpenHelper = new RecordSQLiteOpenHelper(this);//实例化SQLOpenhelper

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_search);
        //初始化控件
        initView();

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
        onKeyDownListener();


        //点击历史记录进行搜索
        lv_searchHistory.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView textView = (TextView) view.findViewById(android.R.id.text1);
                String name = textView.getText().toString();
                userSearchEdit.setText(name);
                Toast.makeText(getApplicationContext(), name, Toast.LENGTH_SHORT).show();
                // TODO 获取到item上面的文字，根据该关键字跳转到另一个页面查询
                Intent intent = new Intent();

            }
        });

        // 插入数据，便于测试，否则第一次进入没有数据怎么测试呀？
      /*  Date date = new Date();
        long time = date.getTime();
        insertData("hzl" + time);
        Log.i("hzl","第一次数据插入成功");
        // 第一次进入查询所有的历史记录*/
        queryData("");
      /*  Log.i("hzl","第一次显示所有的记录");*/

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
        lv_searchHistory =findViewById(R.id.user_search_listview);
        userSearchClean = findViewById(R.id.user_search_clean);
        userSearchResult =findViewById(R.id.user_search_result);
    }
    /**
     * 插入数据
     * @param name 搜索的内容
     * */
    private void insertData(String name){
        database = recordSQLiteOpenHelper.getWritableDatabase();
       /* ContentValues values = new ContentValues();
        values.put("history",name);
        long lInsert = database.insert("searchhistory",null,values);
        Log.i("hzl","记录插入成功");*/
        database.execSQL("insert into searchhistory(history) values('" + name + "')");
        database.close();
    }
    /**
     * 模糊查询数据
     * */
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
     * 检查数据库中是否已经有该条记录
     */
    private boolean hasData(String name) {
        Cursor cursor = recordSQLiteOpenHelper.getReadableDatabase().query(
                "searchhistory",
                null,
                "history=?",
                 new String[]{name},
                null,
                null,
                null);
       /* Cursor cursor = recordSQLiteOpenHelper.getReadableDatabase().rawQuery(
                "select * from searchhistory where history=?",
                new String[]{name}*/

        //判断是否有下一个
        return cursor.moveToNext();
    }

    /**
     * 清空数据
     */
    private void deleteData() {
        database = recordSQLiteOpenHelper.getWritableDatabase();
        database.execSQL("delete from searchhistory");
        database.close();
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
                    Toast.makeText(getApplicationContext(), "clicked!", Toast.LENGTH_SHORT).show();
                    userSearchEdit.setText("");
                }
                return false;
            }
        });
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
