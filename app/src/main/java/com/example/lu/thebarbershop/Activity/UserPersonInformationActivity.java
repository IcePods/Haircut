package com.example.lu.thebarbershop.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.example.lu.thebarbershop.MyTools.CenterSquareScaleBitmap;
import com.example.lu.thebarbershop.MyTools.GetRoundedCornerBitmap;
import com.example.lu.thebarbershop.R;

public class UserPersonInformationActivity extends AppCompatActivity {
    private ImageView imageView;//头像
    private ImageButton backbutton;//返回按钮
    private ImageButton nicknamebutton;//修改昵称按钮
    private ImageButton sexbutton;//修改性别按钮
    private ImageButton phnebutton;//修改电话按钮
    private Mylistener mylistener;//监听器
    private Button exittologin;//退出登录

    private PopupWindow popupWindow;
    private final int CAMERA_REQUEST = 8888;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_person_information);

        imageView = findViewById(R.id.user_person_informatin_header_img);
        backbutton = findViewById(R.id.user_person_informatin_back);
        nicknamebutton = findViewById(R.id.arrowtip48_1);
        sexbutton = findViewById(R.id.arrowtip48_2);
        phnebutton = findViewById(R.id.arrowtip48_3);
        exittologin = findViewById(R.id.user_person_information_exit);

        mylistener = new Mylistener();

        imageView.setImageBitmap(GetRoundedCornerBitmap.getRoundedCornerBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap(),2));
        backbutton.setOnClickListener(mylistener);
        nicknamebutton.setOnClickListener(mylistener);
        sexbutton.setOnClickListener(mylistener);
        phnebutton.setOnClickListener(mylistener);
        exittologin.setOnClickListener(mylistener);
        imageView.setOnClickListener(mylistener);
    }
    private class Mylistener implements View.OnClickListener{

        @Override
        public void onClick(View v) {
            //1. 创建Intent对象
            Intent intent = new Intent();
            switch (v.getId()){
                //返回按钮
                case R.id.user_person_informatin_back:
                    //只销毁当前页面
                    finish();
                    break;
                //修改昵称
                case R.id.arrowtip48_1:
                    //只实现跳转
                    //2. 指定跳转路线
                    intent.setClass(getApplicationContext(),UserPersonInformationChangeNicknameActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                //修改性别
                case R.id.arrowtip48_2:
                    //只实现跳转
                    //2. 指定跳转路线
                    intent.setClass(getApplicationContext(),UserPersonInformationChangeSexActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                //修改电话
                case R.id.arrowtip48_3:
                    //只实现跳转
                    //2. 指定跳转路线
                    intent.setClass(getApplicationContext(),UserPersonInformationChangePhneActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                case R.id.user_person_information_exit:
                    //只实现跳转，跳转到登录页面
                    //跳转路线
                    intent.setClass(getApplicationContext(),UsersLoginActivity.class);
                    //进行跳转
                    startActivity(intent);
                    finish();
                    break;
                case R.id.user_person_informatin_header_img:
                    showPop(imageView);

                    break;
            }
        }
    }
    /**
     * 弹出拍照的弹出框
     * */
    private void showPop(ImageView imageButton){
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.user_person_profil_popupwindow,null);
        Button btn_take = v.findViewById((R.id.takePicture));
        Button btn_choose = v.findViewById((R.id.chooseFrom));
        Button btn_cancel = v.findViewById(R.id.cancel);
        btn_take.setOnClickListener(new OnClickListener());

        btn_cancel.setOnClickListener(new OnClickListener() );

        popupWindow=new PopupWindow(this);
        darkenBackground(0.5f);
        popupWindow.setContentView(v);
        popupWindow.setWidth(RelativeLayout.LayoutParams.MATCH_PARENT);
        popupWindow.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        popupWindow.showAsDropDown(imageButton);
        popupWindow.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                WindowManager.LayoutParams lp = getWindow()
                        .getAttributes();
                lp.alpha = 1f;
                getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
                getWindow().setAttributes(lp);
            }
        });

    }
    //设置弹出框背景黑色半透明
    private void darkenBackground(Float bgcolor){
        WindowManager.LayoutParams lp = getWindow().getAttributes();
        lp.alpha = bgcolor;
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_DIM_BEHIND);
        getWindow().setAttributes(lp);


    }
    //弹出框内容的监听器
    class OnClickListener implements View.OnClickListener{
        @Override
        public void onClick(View view) {
            switch (view.getId()){
                case R.id.takePicture:
                    Intent cameraIntent = new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
                    startActivityForResult(cameraIntent, CAMERA_REQUEST);
                    popupWindow.dismiss();
                    break;
                case R.id.cancel:
                    popupWindow.dismiss();
                    break;


            }
        }
    }
    //返回拍的照片并设置给imageview

    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == CAMERA_REQUEST && resultCode == RESULT_OK) {
            Bitmap photo = (Bitmap) data.getExtras().get("data");
            Bitmap result = CenterSquareScaleBitmap.centerSquareScaleBitmap(photo,80);
            imageView.setImageBitmap(GetRoundedCornerBitmap.getRoundedCornerBitmap(result,2));
        }
    }
}