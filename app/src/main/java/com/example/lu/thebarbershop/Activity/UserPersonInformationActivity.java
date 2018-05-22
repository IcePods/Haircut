package com.example.lu.thebarbershop.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
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
import android.widget.Toast;

import com.example.lu.thebarbershop.MyTools.GetRoundedCornerBitmap;
import com.example.lu.thebarbershop.R;

import java.io.File;

public class UserPersonInformationActivity extends AppCompatActivity {
    private ImageView imageView;//头像
    private ImageButton backbutton;//返回按钮
    private ImageButton nicknamebutton;//修改昵称按钮
    private ImageButton sexbutton;//修改性别按钮
    private ImageButton phnebutton;//修改电话按钮
    private Mylistener mylistener;//监听器
    private Button exittologin;//退出登录

    private PopupWindow popupWindow;
    //------------------------------------------------
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    /* 请求识别码 */
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    private static final int CODE_RESULT_REQUEST = 0xa2;
    // 裁剪后图片的宽(X)和高(Y),480 X 480的正方形。
    private static int output_X = 480;
    private static int output_Y = 480;
    //-------------------------------------------------------
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
        btn_choose.setOnClickListener(new OnClickListener());
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
                    choseHeadImageFromCameraCapture();
                    popupWindow.dismiss();
                    break;
                case R.id.chooseFrom:
                    choseHeadImageFromGallery();
                    popupWindow.dismiss();
                    break;
                case R.id.cancel:
                    popupWindow.dismiss();
                    break;


            }
        }
    }
    // 从本地相册选取图片作为头像
    private void choseHeadImageFromGallery() {
        Intent intentFromGallery = new Intent();
        // 设置文件类型
        intentFromGallery.setType("image/*");
        intentFromGallery.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intentFromGallery, CODE_GALLERY_REQUEST);
    }

    // 启动手机相机拍摄照片作为头像
    private void choseHeadImageFromCameraCapture() {
        Intent intentFromCapture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        // 判断存储卡是否可用，存储照片文件
        if (hasSdcard()) {
            intentFromCapture.putExtra(MediaStore.EXTRA_OUTPUT, Uri
                    .fromFile(new File(Environment
                            .getExternalStorageDirectory(), IMAGE_FILE_NAME)));
        }

        startActivityForResult(intentFromCapture, CODE_CAMERA_REQUEST);
    }
    /**
     * 裁剪原始的图片
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent intent) {

        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
            return;
        }

        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                cropRawPhoto(intent.getData());
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(
                            Environment.getExternalStorageDirectory(),
                            IMAGE_FILE_NAME);
                    cropRawPhoto(Uri.fromFile(tempFile));
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }

                break;

            case CODE_RESULT_REQUEST:
                if (intent != null) {
                    setImageToHeadView(intent);
                }

                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }
    public void cropRawPhoto(Uri uri) {

        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", output_X);
        intent.putExtra("outputY", output_Y);
        intent.putExtra("return-data", true);

        startActivityForResult(intent, CODE_RESULT_REQUEST);
    }

    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     */
    private void setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        if (extras != null) {
            Bitmap photo = extras.getParcelable("data");
            imageView.setImageBitmap(GetRoundedCornerBitmap.getRoundedCornerBitmap(photo,2));
        }
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
}