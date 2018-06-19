package com.example.lu.thebarbershop.Activity;

import android.app.Activity;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.example.lu.thebarbershop.Fragment.UserPersonAppointmentEffectiveFragment;
import com.example.lu.thebarbershop.Fragment.UserPersonAppointmentInvalidFragment;
import com.example.lu.thebarbershop.R;

public class UserPersonAppointmentActivity extends AppCompatActivity {
    private TextView tvFragmentEffective;//有效预约标签
    private TextView tvFragmentInvalid;//失效预约标签
    private ImageButton imageButtonBack;//顶部返回箭头
    //定义Fragment的管理器
    private FragmentManager fragmentManager;
    //定义当前页面fragment
    private Fragment currentFragment = new Fragment();
    //定义页面
    private Fragment EffectiveFragment; //有效预约页面
    private Fragment InvalidFragment;   //失效预约页面

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_person_appointment);
        //获取控件
        getView();
        //给选项卡绑定事件监听器
        onClickListenerImpl listener = new onClickListenerImpl();
        tvFragmentEffective.setOnClickListener(listener);
        tvFragmentInvalid.setOnClickListener(listener);
        imageButtonBack.setOnClickListener(listener);

        //初始化页面对象
        EffectiveFragment = new UserPersonAppointmentEffectiveFragment(this);
        InvalidFragment = new UserPersonAppointmentInvalidFragment(this);
        //默认显示第一个页面
        ChangeFragment(EffectiveFragment);

    }

    //获取布局文件中的控件对象
    private void getView() {
        tvFragmentEffective = findViewById(R.id.tv_appointment_effective);
        tvFragmentInvalid = findViewById(R.id.tv_appointment_invalid);
        imageButtonBack = findViewById(R.id.user_person_collection_back);
    }

    //切换页面的选项卡
    private void ChangeFragment(Fragment fragment) {
        //借助FragmentManage 和 FragmentTransac
        //判断FragmentManager 是否为空
        if (null == fragmentManager) {
            fragmentManager = getFragmentManager();
        }
        //切换页面每次切换页面，进行页面切换事物
        if (currentFragment != fragment) {//如果当前显示的页面和目标要显示的页面不同
            //创建事务
            FragmentTransaction transaction = fragmentManager.beginTransaction();
            //隐藏当前页面
            transaction.hide(currentFragment);
            //判断待显示的页面是是否已经添加过
            if (!fragment.isAdded()) {//待显示的页面没有被添加过
                transaction.add(R.id.appointment_content, fragment);
            }
            //显示目标页面
            transaction.show(fragment);
            //提交事务
            transaction.commit();
            //赋值给当前页面
            currentFragment = fragment;
        }
    }

    // ragmentManage的监听器 点击切换页面
    private class onClickListenerImpl implements View.OnClickListener {
        @Override
        public void onClick(View view) {
            switch (view.getId()) {
                case R.id.tv_appointment_effective://显示“有效预约”
                    tvFragmentEffective.setTextColor(0xFFF7A113);//更改标签颜色
                    //更改页面
                    ChangeFragment(EffectiveFragment);
                    tvFragmentInvalid.setTextColor(Color.GRAY);
                break;
                case R.id.tv_appointment_invalid://显示“失效预约”
                    tvFragmentInvalid.setTextColor(0xFFF7A113);//更改标签颜色
                    //更改页面
                    ChangeFragment(InvalidFragment);
                    tvFragmentEffective.setTextColor(Color.GRAY);
                break;
                case R.id.user_person_collection_back:
                    finish();
                break;

            }
        }

    }
}
