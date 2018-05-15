package com.example.lu.thebarbershop.Fragment;

import android.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;

import com.example.lu.thebarbershop.Activity.UserPersonAboutsUsActivity;
import com.example.lu.thebarbershop.Activity.UserPersonCollectionActivity;
import com.example.lu.thebarbershop.Activity.UserPersonInformationActivity;
import com.example.lu.thebarbershop.MyTools.GetRoundedCornerBitmap;
import com.example.lu.thebarbershop.R;

/**
 * Created by lu on 2018/5/9 0009.
 *"我的" fragment
 */

public class PersonFragment extends Fragment {
    private ImageView imageView;//头像
    private Button infobutton;//个人信息按钮 id=user_person_information_btn
    private Button collectionbutton;//我的收藏按钮 id=user_person_collection_btn
    private Button aboutusbutton;//关于我们按钮 id=user_person_abouts_us_btn
    private Mylistener mylistener;//监听器
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.fragment_person,container,false);

        imageView = view.findViewById(R.id.user_person_head_portrait_img);//头像
        infobutton = view.findViewById(R.id.user_person_information_btn);//个人信息按钮
        collectionbutton = view.findViewById(R.id.user_person_collection_btn);//我的收藏按钮
        aboutusbutton = view.findViewById(R.id.user_person_abouts_us_btn);//关于我们按钮

        mylistener = new Mylistener();

        imageView.setImageBitmap(GetRoundedCornerBitmap.getRoundedCornerBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap(),2));
        infobutton.setOnClickListener(mylistener);
        collectionbutton.setOnClickListener(mylistener);
        aboutusbutton.setOnClickListener(mylistener);

        return view;
    }

    //监听器
    private class Mylistener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //1. 创建Intent对象
            Intent intent = new Intent();
            switch (v.getId()){
                //个人信息按钮
                case R.id.user_person_information_btn:
                    //只实现跳转，跳转到个人信息详情页面UserPersonInformationActivity
                    //2. 指定跳转路线
                    intent.setClass(getActivity().getApplicationContext(),UserPersonInformationActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                //我的预约按钮
                case R.id.user_person_appointment_btn:
                    break;
                //我的收藏按钮
                case R.id.user_person_collection_btn:
                    //只实现跳转，跳转到我的收藏页面UserPersonCollectionActivity
                    //2. 指定跳转路线
                    intent.setClass(getActivity().getApplicationContext(),UserPersonCollectionActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                //预约人信息按钮
                case R.id.user_person_appoint_information_btn:
                    break;
                //关于我们按钮
                case R.id.user_person_abouts_us_btn:
                    //2. 指定跳转路线
                    intent.setClass(getActivity().getApplicationContext(),UserPersonAboutsUsActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
            }
        }
    }
}