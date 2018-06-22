package com.example.lu.thebarbershop.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;


import com.ashokvarma.bottomnavigation.BottomNavigationBar;
import com.ashokvarma.bottomnavigation.BottomNavigationItem;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.Users;
import com.example.lu.thebarbershop.Fragment.DynamicFragment;
import com.example.lu.thebarbershop.Fragment.MainFragment;
import com.example.lu.thebarbershop.Fragment.PersonFragment;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.jaeger.library.StatusBarUtil;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Field;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {
    private Button BtnFragmentIndex;
    private Button BtnFragmentNews;
    private Button BtnFragmentDynameic;
    private Button BtnFragmentPerson;
    //定义Fragment的管理器
    private FragmentManager fragmentManager;
    //定义当前页面fragment
    private Fragment currentFragment = new Fragment();
    //定义页面
    private Fragment FragmentIndex; //首页
    private Fragment FragmentDynameic; //动态
    private Fragment FragmentPerson; //我的
    private Users users;
    private String userName="";
    private String userAccount="";
    private long fistKeyDownTime;//记录第一次按下返回的时间（毫秒数）
    private OkHttpClient okHttpClient;
    private EaseConversationListFragment conversationListFragment;//环信会话列表页面
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    String st = bundle.getString("UserName");
                    users = new Users();
                    Gson gson = new Gson();
                    users = gson.fromJson(st,Users.class);
                    userName = users.getUserName();
                    startActivity(new Intent(MainActivity.this, ECChatActivity.class)
                            .putExtra(EaseConstant.EXTRA_USER_ID,userAccount)
                            .putExtra(EaseConstant.EXTRA_USER_NAME,userName));



            }
            super.handleMessage(msg);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        StatusBarUtil.setTranslucent(this,50);
        //设置底部栏
        BottomNavigationBar bottomNavigationBar = (BottomNavigationBar) findViewById(R.id.bottom_navigation_bar);
        bottomNavigationBar
                .addItem(new BottomNavigationItem(R.mipmap.index_on, "首页"))
                .addItem(new BottomNavigationItem(R.mipmap.news_on, "消息"))
                .addItem(new BottomNavigationItem(R.mipmap.dynamic_un, "互动"))
                .addItem(new BottomNavigationItem(R.mipmap.person_un, "我的"))
                .initialise();
        okHttpClient = new OkHttpClient();
        conversationListFragment = new EaseConversationListFragment();
        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {
            @Override
            public void onListItemClicked(EMConversation conversation) {
                userAccount = conversation.conversationId();
                postUserName(conversation.conversationId());
            }
        });
        //获取控件
        /*getView();*/
        /*changeSPLocation();*/
        //给选项卡绑定事件监听器
        /*onClickListenerImpl listener = new onClickListenerImpl();
        BtnFragmentIndex.setOnClickListener(listener);
        BtnFragmentNews.setOnClickListener(listener);
        BtnFragmentDynameic.setOnClickListener(listener);
        BtnFragmentPerson.setOnClickListener(listener);
*/
        //初始化页面对象
        FragmentIndex = new MainFragment();
        FragmentDynameic = new DynamicFragment();
        FragmentPerson = new PersonFragment(this);

        //默认显示第一个页面
        ChangeFragment(FragmentIndex);

        bottomNavigationBar.setTabSelectedListener(new BottomNavigationBar.OnTabSelectedListener(){
            @Override
            public void onTabSelected(int position) {
                switch (position) {
                    case 0: //显示“主页”
                        if (currentFragment == null){
                            getSupportFragmentManager().beginTransaction().hide(conversationListFragment).commit();
                        }
                        ChangeFragment(FragmentIndex);
                        break;
                    case 1:
                        //更改页面
                        if (currentFragment!=null){
                            ChangeFragment();
                            if (!conversationListFragment.isAdded()){
                                getSupportFragmentManager().beginTransaction().add(R.id.activity_main_fl_content, conversationListFragment).commit();
                            }
                            getSupportFragmentManager().beginTransaction().show(conversationListFragment).commit();
                            currentFragment = null;
                        }
                        break;
                    case 2:
                        //更改页面
                        if (currentFragment == null){
                            getSupportFragmentManager().beginTransaction().hide(conversationListFragment).commit();
                        }
                        ChangeFragment(FragmentDynameic);
                        break;
                    case 3:
                        //更改页面
                        if (currentFragment == null){
                            getSupportFragmentManager().beginTransaction().hide(conversationListFragment).commit();
                        }
                        ChangeFragment(FragmentPerson);
                        break;
                }
            }
            @Override
            public void onTabUnselected(int position) {
            }
            @Override
            public void onTabReselected(int position) {
            }
        });
    }
    private void postUserName(final String UserAccount){
        new Thread(){
            @Override
            public void run() {
                FormBody.Builder builder = new FormBody.Builder();
                builder.add("BarberAccount",UserAccount);
                FormBody body = builder.build();
                Request request = new Request.Builder().post(body).url(UrlAddress.url+"queryUserName.action").build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String s = response.body().string();
                    Message message = Message.obtain();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("UserName",s);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    //获取布局文件中的控件对象
 /*   private void getView(){
        BtnFragmentIndex =  findViewById(R.id.btn_fragment_main);
        BtnFragmentNews = findViewById(R.id.btn_fragment_news);
        BtnFragmentDynameic = findViewById(R.id.btn_fragment_dynamic);
        BtnFragmentPerson = findViewById(R.id.btn_fragment_mine);
    }*/

    //切换页面的选项卡
    private void ChangeFragment(Fragment fragment){
        //借助FragmentManage 和 FragmentTransac
        //判断FragmentManager 是否为空
        if(null == fragmentManager){
            fragmentManager =getFragmentManager();
        }
        //切换页面每次切换页面，进行页面切换事物
        if(currentFragment != fragment){//如果当前显示的页面和目标要显示的页面不同
            //创建事务
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //隐藏当前页面
            if (currentFragment!=null){
                transaction.hide(currentFragment);
            }
            //判断待显示的页面是是否已经添加过
            if(!fragment.isAdded()){//待显示的页面没有被添加过
                transaction.add(R.id.activity_main_fl_content,fragment);
            }
            //显示目标页面
            transaction.show(fragment);
            //提交事务
            transaction.commit();
            //赋值给当前页面
            currentFragment = fragment;
        }
    }
    private void ChangeFragment(){
        //切换页面每次切换页面，进行页面切换事物
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        //隐藏当前页面
        transaction.hide(currentFragment);
        transaction.commit();
        //赋值给当前页面
    }
    //FragmentManage的监听器 点击切换页面
    /*private  class onClickListenerImpl implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.btn_fragment_main: //显示“主页”
                    //更改页面
                    ChangeFragment(FragmentIndex);
                    //更改选项卡图片颜色
                    BtnFragmentIndex.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.index_on,0,0);
                    BtnFragmentNews.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.news_un,0,0);
                    BtnFragmentDynameic.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.dynamic_un,0,0);
                    BtnFragmentPerson.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.person_un,0,0);
                    //更该选项卡字体颜色
                    BtnFragmentIndex.setTextColor(getResources().getColor(R.color.onSelected));
                    BtnFragmentNews.setTextColor(getResources().getColor(R.color.unSelected));
                    BtnFragmentDynameic.setTextColor(getResources().getColor(R.color.unSelected));
                    BtnFragmentPerson.setTextColor(getResources().getColor(R.color.unSelected));
                    break;
                case R.id.btn_fragment_news: //显示”消息“页面
                    //更改页面
                    ChangeFragment(FragmentNews);
                    //更改选项卡图片颜色
                    BtnFragmentIndex.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.index_un,0,0);
                    BtnFragmentNews.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.news_on,0,0);
                    BtnFragmentDynameic.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.dynamic_un,0,0);
                    BtnFragmentPerson.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.person_un,0,0);
                    //更该选项卡字体颜色
                    BtnFragmentIndex.setTextColor(getResources().getColor(R.color.unSelected));
                    BtnFragmentNews.setTextColor(getResources().getColor(R.color.onSelected));
                    BtnFragmentDynameic.setTextColor(getResources().getColor(R.color.unSelected));
                    BtnFragmentPerson.setTextColor(getResources().getColor(R.color.unSelected));
                    break;
                case R.id.btn_fragment_dynamic : // 显示“动态”页面
                    //更改页面
                    ChangeFragment(FragmentDynameic);
                    //更改选项卡图片颜色
                    BtnFragmentIndex.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.index_un,0,0);
                    BtnFragmentNews.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.news_un,0,0);
                    BtnFragmentDynameic.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.dynamic_on,0,0);
                    BtnFragmentPerson.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.person_un,0,0);
                    //更该选项卡字体颜色
                    BtnFragmentIndex.setTextColor(getResources().getColor(R.color.unSelected));
                    BtnFragmentNews.setTextColor(getResources().getColor(R.color.unSelected));
                    BtnFragmentDynameic.setTextColor(getResources().getColor(R.color.onSelected));
                    BtnFragmentPerson.setTextColor(getResources().getColor(R.color.unSelected));
                    break;
                case R.id.btn_fragment_mine : //显示“我的”页面
                    SharedPreferences sharedPreferences = getSharedPreferences("token", Context.MODE_PRIVATE);
                    sharedPreferences.getString("UserAccount","王大锤");
                    //更改页面
                    ChangeFragment(FragmentPerson);
                    //更改选项卡图片颜色
                    BtnFragmentIndex.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.index_un,0,0);
                    BtnFragmentNews.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.news_un,0,0);
                    BtnFragmentDynameic.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.dynamic_un,0,0);
                    BtnFragmentPerson.setCompoundDrawablesWithIntrinsicBounds(0,R.mipmap.person_no,0,0);
                    //更该选项卡字体颜色
                    BtnFragmentIndex.setTextColor(getResources().getColor(R.color.unSelected));
                    BtnFragmentNews.setTextColor(getResources().getColor(R.color.unSelected));
                    BtnFragmentDynameic.setTextColor(getResources().getColor(R.color.unSelected));
                    BtnFragmentPerson.setTextColor(getResources().getColor(R.color.onSelected));
                    break;
            }
        }
    }*/
    /**
     * 摁下两次返回键退出程序
     * @param keyCode
     * @param event
     * @return
     */
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK){
            if (System.currentTimeMillis() - fistKeyDownTime>2000) {//第一次按键
                //提示再按一次退出系统
                Toast.makeText(MainActivity.this,
                        "再按一次，退出系统",
                        Toast.LENGTH_SHORT).show();
                //记录下当前按键的时间
                fistKeyDownTime = System.currentTimeMillis();
            }else {//第二次按键
                //退出系统
                finish();
            }
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }

    private void changeSPLocation() {
        try {
            Field field;
            // 获取ContextWrapper对象中的mBase变量。该变量保存了ContextImpl对象
            field = ContextWrapper.class.getDeclaredField("mBase");
            field.setAccessible(true);
            // 获取mBase变量
            Object obj = field.get(this);
            // 获取ContextImpl。mPreferencesDir变量，该变量保存了数据文件的保存路径
            field = obj.getClass().getDeclaredField("mPreferencesDir");
            field.setAccessible(true);
            // 创建自定义路径
            File file = new File(android.os.Environment.getExternalStorageDirectory().getPath());
            // 修改mPreferencesDir变量的值
            field.set(obj, file);
        } catch (NoSuchFieldException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }


}
