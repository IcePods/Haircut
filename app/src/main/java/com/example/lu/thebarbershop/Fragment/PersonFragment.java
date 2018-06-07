package com.example.lu.thebarbershop.Fragment;

import android.animation.Animator;
import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.lu.thebarbershop.Activity.UserPersonAboutsUsActivity;
import com.example.lu.thebarbershop.Activity.UserPersonAppointmentActivity;
import com.example.lu.thebarbershop.Activity.UserPersonCollectionActivity;
import com.example.lu.thebarbershop.Activity.UserPersonInformationActivity;
import com.example.lu.thebarbershop.Activity.UsersLoginActivity;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.Users;
import com.example.lu.thebarbershop.MyTools.GetRoundedCornerBitmap;
import com.example.lu.thebarbershop.MyTools.UserTokenSql;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by lu on 2018/5/9 0009.
 *"我的" fragment
 */

@SuppressLint("ValidFragment")
public class PersonFragment extends Fragment {
    private Context mContext;
    private ImageView imageView;//头像
    private TextView name;
    private Button infobutton;//个人信息按钮 id=user_person_information_btn
    private Button appointmentbutton;//我的预约
    private Button collectionbutton;//我的收藏按钮 id=user_person_collection_btn
    private Button aboutusbutton;//关于我们按钮 id=user_person_abouts_us_btn
    private Mylistener mylistener;//监听器
    private File f;
    private UserTokenSql userTokenSql;
    private String username;
    private String userheader;
    private String Token;
    private SQLiteDatabase database;
    public static Users users;



    @SuppressLint("ValidFragment")
    public PersonFragment(Context mContext) {

        this.mContext = mContext;
    }

    OkHttpClient okHttpClient = new OkHttpClient();
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle b = msg.getData();
                    String userfromToken = b.getString("userFromToken");
                    Gson gson = new Gson();
                    users = gson.fromJson(userfromToken,Users.class);
                    Log.i("lhy",userfromToken);
                    if(users.getUserCondition()==true){
                        selectUser();
                        name.setText(users.getUserName());
                        RequestOptions requestOptions = new RequestOptions()
                                .centerCrop()
                                .transform(new CircleCrop());

                        requestOptions.placeholder(R.mipmap.user_index_nurse);
                        Glide.with(getActivity())
                                .load(users.getUserHeader())
                                .apply(requestOptions)
                                .into(imageView);
                    }else{

                    }
                   /* selectUser();
                    name.setText(username);
                    RequestOptions requestOptions = new RequestOptions().centerCrop();
                    requestOptions.placeholder(R.mipmap.user_index_nurse);
                    Glide.with(getActivity())
                            .load(userheader)
                            .apply(requestOptions)
                            .into(imageView);*/
                 /*case 2:
                     Bundle c = msg.getData();
                     String userfromKeep = c.getString("userFromToken");
                     Gson gson1 = new Gson();
                     users = gson1.fromJson(userfromKeep,Users.class);
                     Log.i("lhy",userfromKeep);
                     if(users.getUserCondition()==true){
                         selectUser();
                         name.setText(users.getUserName());
                         RequestOptions requestOptions = new RequestOptions().centerCrop();
                         requestOptions.placeholder(R.mipmap.user_index_nurse);
                         Glide.with(getActivity())
                                 .load(users.getUserHeader())
                                 .apply(requestOptions)
                                 .into(imageView);
                     }else{
                         Intent intent = new Intent();

                     }*/


            }
            super.handleMessage(msg);
        }
    };


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.fragment_person,container,false);


        f = new File(getActivity().getApplication().getFilesDir().getParent()+"/shared_prefs/usertoken.xml");
        Log.i("hzl",getActivity().getApplication().getFilesDir().getParent()+"");


        imageView = view.findViewById(R.id.user_person_head_portrait_img);//头像
        infobutton = view.findViewById(R.id.user_person_information_btn);//个人信息按钮
        appointmentbutton = view.findViewById(R.id.user_person_appointment_btn);//我的预约按钮
        collectionbutton = view.findViewById(R.id.user_person_collection_btn);//我的收藏按钮
        aboutusbutton = view.findViewById(R.id.user_person_abouts_us_btn);//关于我们按钮
        name = view.findViewById(R.id.user_person_username_tv);

        //更新UI



        mylistener = new Mylistener();
        if(f.exists()){
            getUserInformation(1);
        }


        imageView.setImageBitmap(GetRoundedCornerBitmap.getRoundedCornerBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap(),2));
        infobutton.setOnClickListener(mylistener);
        appointmentbutton.setOnClickListener(mylistener);
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
                    if(f.exists()){
                        intent.setClass(getActivity().getApplicationContext(),UserPersonInformationActivity.class);
                        //3. 进行跳转
                        startActivity(intent);
                    }else {
                        intent.setClass(getActivity().getApplicationContext(),UsersLoginActivity.class);
                        startActivity(intent);
                    }

                    break;
                //我的预约按钮
                case R.id.user_person_appointment_btn:
                    //只实现跳转，跳转到预约页面UserPersonAppointmentActivity
                    //2. 指定跳转路线
                    intent.setClass(getActivity().getApplicationContext(), UserPersonAppointmentActivity.class);
                    //3. 进行跳转
                    startActivity(intent);
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
    public void getUserInformation(final int num){
        //如果有token
                if(getActivity().getSharedPreferences("usertoken", Context.MODE_PRIVATE)!=null){
                    SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usertoken", Context.MODE_PRIVATE);
                    final String token = sharedPreferences.getString("token","");
                    Log.i("hzl",token);
                    final String userAccount = sharedPreferences.getString("userAccount","");
                    final String userPassword = sharedPreferences.getString("userPassword","");
                    new Thread(){
                        @Override
                        public void run() {
                            Request.Builder builder = new Request.Builder();
                            FormBody.Builder builder1 = new FormBody.Builder();
                            builder1.add("UserAccount",userAccount);
                            builder1.add("UserPassword",userPassword);
                            FormBody body = builder1.build();
                            final Request request = builder.header("UserTokenSql",token).post(body).url(UrlAddress.url+"keepLogin.action").build();
                            Log.i("qqt",request+"");
                            Call call = okHttpClient.newCall(request);
                            try {
                                Response response = call.execute();
                                String a = response.body().string();
                                Log.i("lhy",a);
                                Message message = Message.obtain();
                                message.what = num;
                                Bundle bundle = new Bundle();
                                bundle.putString("userFromToken",a);
                                message.setData(bundle);
                                handler.sendMessage(message);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }.start();

                }else{
                    //不做处理

                }
            }
    private void selectUser(){
        //第二次登录从sharendpreference拿到token取数据库查询用户信息
        if(getActivity().getSharedPreferences("usertoken", Context.MODE_PRIVATE)!=null){
            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usertoken", Context.MODE_PRIVATE);
            final String token = sharedPreferences.getString("token","");
            //拿到token去sqlite数据库查询用户信息
            database =new UserTokenSql(mContext).getReadableDatabase();
            Cursor cursor = database.query("user",null,"usertoken"+"=?",new String[]{token},null,null,null);

            if(cursor.moveToFirst()){
                username = cursor.getString(cursor.getColumnIndex("username"));
                userheader = cursor.getString(cursor.getColumnIndex("userheader"));
                if(userheader == null){
                    userheader = "";
                }
            }
        }


    }

    @Override
    public void onStart() {

        super.onStart();
      /*  getUserInformation(2);*/

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("usertoken", Context.MODE_PRIVATE);
        final String token = sharedPreferences.getString("token","");
        //拿到token去sqlite数据库查询用户信息
        database =new UserTokenSql(mContext).getReadableDatabase();
        Cursor cursor = database.query("user",null,"usertoken"+"=?",new String[]{token},null,null,null);

        if(cursor.moveToFirst()){
            username = cursor.getString(cursor.getColumnIndex("username"));
            userheader = cursor.getString(cursor.getColumnIndex("userheader"));
            if(userheader == null){
                userheader = "";
            }
            name.setText(username);
            RequestOptions requestOptions = new RequestOptions().centerCrop();
            requestOptions.placeholder(R.mipmap.user_index_nurse);
            Glide.with(getActivity())
                    .load(userheader)
                    .apply(requestOptions)
                    .into(imageView);

        }
    }
}