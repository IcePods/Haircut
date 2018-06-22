package com.example.lu.thebarbershop.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.MyTools.GetRoundedCornerBitmap;
import com.example.lu.thebarbershop.MyTools.GetUserFromShared;
import com.example.lu.thebarbershop.MyTools.UploadPictureUtil;
import com.example.lu.thebarbershop.MyTools.UserTokenSql;
import com.example.lu.thebarbershop.R;

import java.io.File;

import cn.jpush.android.api.JPushInterface;


public class UserPersonInformationActivity extends AppCompatActivity {
    private ImageView imageView;//头像
    private ImageButton backbutton;//返回按钮
    private RelativeLayout nicknamerl;//修改昵称RelativeLayout
    private ImageButton nicknamebutton;//修改昵称按钮
    private RelativeLayout sexrl;//修改性别RelativeLayout
    private ImageButton sexbutton;//修改性别按钮
    private RelativeLayout phnerl;//修改电话RelativeLayout
    private ImageButton phnebutton;//修改电话按钮
    private Mylistener mylistener;//监听器
    private Button exittologin;//退出登录
    private TextView name;
    private TextView sex;
    private TextView phone;
    private String username;
    private String userheader;
    private String usersex;
    private String userphone;
    private String token = "";
    /*private Users users = (Users)getIntent().getSerializableExtra("users");*/
    //上传剪裁后的图片
    final private String uploadPicUrl = UrlAddress.url + "uploadHead.action";

    private SQLiteDatabase database;//查询数据库


    private PopupWindow popupWindow;
    //------------------------------------------------
    /* 头像文件 */
    private static final String IMAGE_FILE_NAME = "temp_head_image.jpg";
    /* 请求识别码 */
    //从相册选取图片
    private static final int CODE_GALLERY_REQUEST = 0xa0;
    //手机拍照
    private static final int CODE_CAMERA_REQUEST = 0xa1;
    //图片剪裁完成后的请求码
    private static final int CODE_RESULT_REQUEST = 0xa2;
    //-------------------------------------------------------
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_person_information);

        imageView = findViewById(R.id.user_person_informatin_header_img);
        backbutton = findViewById(R.id.user_person_informatin_back);
        //修改昵称
        nicknamerl = findViewById(R.id.user_person_information_nickname_rl);
        nicknamebutton = findViewById(R.id.arrowtip48_nickname);
        //修改性别
        sexrl = findViewById(R.id.user_person_information_sex_rl);
        sexbutton = findViewById(R.id.arrowtip48_sex);
        //修改电话
        phnerl = findViewById(R.id.user_person_information_phne_rl);
        phnebutton = findViewById(R.id.arrowtip48_phne);
        exittologin = findViewById(R.id.user_person_information_exit);

        name = findViewById(R.id.user_person_informatin_nickname_tv);
        sex  =findViewById(R.id.user_person_informatin_sex_tv);
        phone = findViewById(R.id.user_person_informatin_phne_tv);
        mylistener = new Mylistener();

        selectUser();

        imageView.setImageBitmap(GetRoundedCornerBitmap.getRoundedCornerBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap(),2));
        name.setText(username);
        sex.setText(usersex);
        phone.setText(userphone);
        backbutton.setOnClickListener(mylistener);
        nicknamerl.setOnClickListener(mylistener);
        nicknamebutton.setOnClickListener(mylistener);
        sexrl.setOnClickListener(mylistener);
        sexbutton.setOnClickListener(mylistener);
        phnerl.setOnClickListener(mylistener);
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
                case R.id.user_person_information_nickname_rl:
                    //只实现跳转
                    //2. 指定跳转路线

                    intent.setClass(getApplicationContext(),UserPersonInformationChangeNicknameActivity.class);
                    intent.putExtra("token",token);
                    /*intent.putExtra("users",users);*/
                    //3. 进行跳转
                    startActivity(intent);

                    break;
                case R.id.arrowtip48_nickname:
                    intent.setClass(getApplicationContext(),UserPersonInformationChangeNicknameActivity.class);
                    intent.putExtra("token",token);
                    /*intent.putExtra("users",users);*/
                    startActivity(intent);
                    break;
                //修改性别
                case R.id.user_person_information_sex_rl:
                    //只实现跳转
                    //2. 指定跳转路线
                    intent.setClass(getApplicationContext(),UserPersonInformationChangeSexActivity.class);
                    intent.putExtra("token",token);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                case R.id.arrowtip48_sex:
                    intent.setClass(getApplicationContext(),UserPersonInformationChangeSexActivity.class);
                    intent.putExtra("token",token);
                    startActivity(intent);
                    break;
                //修改电话
                case R.id.user_person_information_phne_rl:
                    //只实现跳转
                    //2. 指定跳转路线
                    intent.setClass(getApplicationContext(),UserPersonInformationChangePhneActivity.class);
                    intent.putExtra("token",token);
                    //3. 进行跳转
                    startActivity(intent);
                    break;
                case R.id.arrowtip48_phne:
                    intent.setClass(getApplicationContext(),UserPersonInformationChangePhneActivity.class);
                    intent.putExtra("token",token);
                    startActivity(intent);
                    break;
                case R.id.user_person_information_exit:
                    //只实现跳转，跳转到登录页面
                    //跳转路线
                    //删除shared文件
                    File file = new File(getApplication().getFilesDir().getParent()+"/shared_prefs/usertoken.xml");
                    file.delete();
                    Log.i("hzl","文件删除成功");
                  /*  intent.setClass(getApplicationContext(),PersonFragment.class);
                    //进行跳转
                    startActivity(intent);*/
                    JPushInterface.setAlias(getApplicationContext(),0,"exitLogin");
                    finish();
                    break;
                case R.id.user_person_informatin_header_img:
                    intent.putExtra("token",token);
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
     * 重写方法，在调用选择图片完成后系统回调
     * startActivityForResult()的回调方法
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
        // 用户没有进行有效的设置操作，返回
        if (resultCode == RESULT_CANCELED) {
            Toast.makeText(getApplication(), "取消", Toast.LENGTH_LONG).show();
        }
        //图片剪裁后的请求意图
        Intent mIntent;
        UploadPictureUtil uploadPictureUtil = new UploadPictureUtil();
        switch (requestCode) {
            case CODE_GALLERY_REQUEST:
                //剪裁图片
                mIntent = uploadPictureUtil.cropPhoto(intent.getData());
                startActivityForResult(mIntent, CODE_RESULT_REQUEST);
                break;

            case CODE_CAMERA_REQUEST:
                if (hasSdcard()) {
                    File tempFile = new File(Environment.getExternalStorageDirectory(), IMAGE_FILE_NAME);
                    //剪裁图片并获得剪裁后的Intent
                    mIntent = uploadPictureUtil.cropPhoto(Uri.fromFile(tempFile));
                    //删除拍照图片：：未完成

                    startActivityForResult(mIntent, CODE_RESULT_REQUEST);
                } else {
                    Toast.makeText(getApplication(), "没有SDCard!", Toast.LENGTH_LONG)
                            .show();
                }
                break;

            case CODE_RESULT_REQUEST:
                //剪裁后的图片
                Bitmap bitmap = setImageToHeadView(intent);

                String picStr = uploadPictureUtil.getStringFromBitmap(bitmap);
                String token = new GetUserFromShared(this).getUserTokenFromShared();
                uploadPictureUtil.requestServer(uploadPicUrl,picStr,token,null);
                break;
        }

        super.onActivityResult(requestCode, resultCode, intent);
    }


    /**
     * 提取保存裁剪之后的图片数据，并设置头像部分的View
     * @param intent 裁剪图片后的Intent
     * @return 返回选取区域图片的bitmap
     */
    private Bitmap setImageToHeadView(Intent intent) {
        Bundle extras = intent.getExtras();
        Bitmap photo = extras.getParcelable("data");
        imageView.setImageBitmap(GetRoundedCornerBitmap.getRoundedCornerBitmap(photo,2));
        return photo;
    }

    /**
     * 检查设备是否存在SDCard的工具方法
     */
    private static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            // 有存储的SDCard
            return true;
        } else {
            return false;
        }
    }
    private void selectUser(){
        SharedPreferences sharedPreferences = this.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        database =new UserTokenSql(this).getReadableDatabase();
        /*Cursor cursor = database.rawQuery("select * from user where usertoken = "+token,null);*/
        Cursor cursor = database.query("user",null,"usertoken"+"=?",new String[]{token},null,null,null);
        if(cursor.moveToLast()){
            username = cursor.getString(cursor.getColumnIndex("username"));
            userheader = cursor.getString(cursor.getColumnIndex("userheader"));
            usersex = cursor.getString(cursor.getColumnIndex("usersex"));
            userphone = cursor.getString(cursor.getColumnIndex("userphone"));
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        //每次进入该activity查询用户信息
        selectUser();
        //如果用户头像为空则加载默认的
        if(userheader!=null){
            RequestOptions requestOptions = new RequestOptions().centerCrop().transform(new CircleCrop());
            Glide.with(this).load(UrlAddress.url+userheader).apply(requestOptions)
                    .into(imageView);
        }else{
            RequestOptions requestOptions = new RequestOptions().centerCrop().transform(new CircleCrop());
            Glide.with(this).load(R.mipmap.default_header_img).apply(requestOptions)
                  .into(imageView);
        }
        name.setText(username);
        sex.setText(usersex);
        phone.setText(userphone);

    }
}