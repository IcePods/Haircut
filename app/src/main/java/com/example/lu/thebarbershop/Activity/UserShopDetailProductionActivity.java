package com.example.lu.thebarbershop.Activity;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;

import com.example.lu.thebarbershop.Fragment.UserShopDetailProductionHairColor;
import com.example.lu.thebarbershop.Fragment.UserShopDetailProductionHairCut;
import com.example.lu.thebarbershop.Fragment.UserShopDetailProductionNurse;
import com.example.lu.thebarbershop.Fragment.UserShopDetailProductionPerm;
import com.example.lu.thebarbershop.R;

public class UserShopDetailProductionActivity extends AppCompatActivity {
    //剪发选项卡
    private Button user_shop_detail_production_haircut_btn;
    //染发选项卡
    private Button user_shop_detail_production_haircolor_btn;
    //烫发选项卡
    private Button user_shop_detail_production_perm_btn;
    //护理选项卡
    private Button user_shop_detail_production_nurse_btn;

    //返回按钮
    private ImageButton user_shop_detail_production_back_imgbtn;

    //定义Fragment的管理器
    private FragmentManager fragmentManager;
    //定义当前页面fragment
    private Fragment currentFragment = new Fragment();

    //定义页面
    private Fragment FragmentHairCut; //剪发
    private Fragment FragmentHaiColor;   //染发
    private Fragment FragmentPerm; //烫发
    private Fragment FragmentNurse; //护理
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_shop_detail_production);

        getView();

        onClickListenerImpl listener = new onClickListenerImpl();
        user_shop_detail_production_haircut_btn.setOnClickListener(listener);
        user_shop_detail_production_haircolor_btn.setOnClickListener(listener);
        user_shop_detail_production_perm_btn.setOnClickListener(listener);
        user_shop_detail_production_nurse_btn.setOnClickListener(listener);
        //初始化fragment
        FragmentHairCut = new UserShopDetailProductionHairCut();
        FragmentHaiColor = new UserShopDetailProductionHairColor();
        FragmentPerm = new UserShopDetailProductionPerm();
        FragmentNurse = new UserShopDetailProductionNurse();
        //首次
        ChangeFragment(FragmentHairCut);

        user_shop_detail_production_back_imgbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }
    private void getView(){
        user_shop_detail_production_haircut_btn = findViewById(R.id.user_shop_detail_production_haircut);
        user_shop_detail_production_haircolor_btn = findViewById(R.id.user_shop_detail_production_haircolor);
        user_shop_detail_production_perm_btn = findViewById(R.id.user_shop_detail_production_perm);
        user_shop_detail_production_nurse_btn = findViewById(R.id.user_shop_detail_production_nurse);

        user_shop_detail_production_back_imgbtn = findViewById(R.id.user_shop_detail_production_back_imgbtn);
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
                transaction.add(R.id.user_shop_detail_production_fl_content,fragment);
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
                case R.id.user_shop_detail_production_haircut: //显示“剪发”
                    //更改页面
                    ChangeFragment(FragmentHairCut);

                    //更该选项卡字体颜色
                    user_shop_detail_production_haircut_btn.setTextColor(getResources().getColor(R.color.onSelected));
                    user_shop_detail_production_haircolor_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    user_shop_detail_production_perm_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    user_shop_detail_production_nurse_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    break;
                case R.id.user_shop_detail_production_haircolor: //显示”染发“页面
                    //更改页面
                    ChangeFragment(FragmentHaiColor);
                    //更该选项卡字体颜色
                    user_shop_detail_production_haircut_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    user_shop_detail_production_haircolor_btn.setTextColor(getResources().getColor(R.color.onSelected));
                    user_shop_detail_production_perm_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    user_shop_detail_production_nurse_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    break;
                case R.id.user_shop_detail_production_perm : // 显示“烫发”页面
                    //更改页面
                    ChangeFragment(FragmentPerm);
                    //更该选项卡字体颜色
                    user_shop_detail_production_haircut_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    user_shop_detail_production_haircolor_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    user_shop_detail_production_perm_btn.setTextColor(getResources().getColor(R.color.onSelected));
                    user_shop_detail_production_nurse_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    break;
                case R.id.user_shop_detail_production_nurse: //显示“护理”页面
                    //更改页面
                    ChangeFragment(FragmentNurse);
                    //更该选项卡字体颜色
                    user_shop_detail_production_haircut_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    user_shop_detail_production_haircolor_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    user_shop_detail_production_perm_btn.setTextColor(getResources().getColor(R.color.unSelected));
                    user_shop_detail_production_nurse_btn.setTextColor(getResources().getColor(R.color.onSelected));
                    break;
            }
        }
    }

}
