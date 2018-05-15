package com.example.lu.thebarbershop.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.Switch;
import android.widget.Toast;

import com.example.lu.thebarbershop.Fragment.DynamicFragment;
import com.example.lu.thebarbershop.Fragment.MainFragment;
import com.example.lu.thebarbershop.Fragment.NewsFragment;
import com.example.lu.thebarbershop.Fragment.PersonFragment;
import com.example.lu.thebarbershop.R;

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
    private Fragment FragmentNews;   //消息
    private Fragment FragmentDynameic; //动态
    private Fragment FragmentPerson; //我的
    private long fistKeyDownTime;//记录第一次按下返回的时间（毫秒数）
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //获取控件
        getView();
        //给选项卡绑定事件监听器
        onClickListenerImpl listener = new onClickListenerImpl();
        BtnFragmentIndex.setOnClickListener(listener);
        BtnFragmentNews.setOnClickListener(listener);
        BtnFragmentDynameic.setOnClickListener(listener);
        BtnFragmentPerson.setOnClickListener(listener);
        //初始化页面对象
            FragmentIndex = new MainFragment();
            FragmentNews = new NewsFragment();
            FragmentDynameic = new DynamicFragment();
            FragmentPerson = new PersonFragment();
        //默认显示第一个页面
        ChangeFragment(FragmentIndex);
    }
    //获取布局文件中的控件对象
    private void getView(){
        BtnFragmentIndex =  findViewById(R.id.btn_fragment_main);
        BtnFragmentNews = findViewById(R.id.btn_fragment_news);
        BtnFragmentDynameic = findViewById(R.id.btn_fragment_dynamic);
        BtnFragmentPerson = findViewById(R.id.btn_fragment_mine);
    }

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
            transaction.hide(currentFragment);
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
    //FragmentManage的监听器 点击切换页面
    private  class onClickListenerImpl implements View.OnClickListener{
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
    }
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
}
