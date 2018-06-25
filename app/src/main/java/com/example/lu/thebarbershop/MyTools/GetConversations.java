package com.example.lu.thebarbershop.MyTools;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.Users;
import com.google.gson.Gson;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import okhttp3.Call;
import okhttp3.FormBody;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by 天龙 on 2018/6/24.
 */

public class GetConversations {
    private OkHttpClient okHttpClient = new OkHttpClient();
    private String userName;
    private Gson gson = new Gson();
    private Map<String,String> nMap ;
    private String token;
    private SQLiteDatabase sqLiteDatabase;
    public List<String> accountList;
    private Context context;

    public GetConversations(Context context) {
        this.context = context;
    }

    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    String st = bundle.getString("UserNameMap");
                    nMap = gson.fromJson(st,Map.class);
                    //Log.i("ztl",nMap.toString());
                    setUserProfileProvider();
            }
            super.handleMessage(msg);
        }
    };

    public Map<String, String> getnMap() {
        return nMap;
    }

    public void setnMap(Map<String, String> nMap) {
        this.nMap = nMap;
    }

    public void getAccountList(){
        accountList = new ArrayList<>();
        Map<String,EMConversation> conversations = EMClient.getInstance().chatManager().getAllConversations();
        for (EMConversation conversation : conversations.values()) {
            String userid = conversation.conversationId();
            accountList.add(userid);
        }
    }
    public void postUserNameList(final String str){
        new Thread(){
            @Override
            public void run() {
                //参数的类型
                MediaType type = MediaType.parse("text/plain;charset=UTF-8");
                //创建RequestBody对象
                RequestBody body;
                body = RequestBody.create(type,str);
                // 创建Request.Builder对象
                Request.Builder builder = new Request.Builder();
                //设置参数
                builder.url(UrlAddress.url+"QueryUserListNameByUserAccount.action");
                //设置请求方式为POST
                builder.post(body);
                Request request = builder.build();
                Call call = okHttpClient.newCall(request);
                try {
                    Response response = call.execute();
                    String s = response.body().string();
                    Message message = Message.obtain();
                    message.what = 1;
                    Bundle bundle = new Bundle();
                    bundle.putString("UserNameMap",s);
                    message.setData(bundle);
                    handler.sendMessage(message);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }
    private void setUserProfileProvider(){
        EaseUI easeUI = EaseUI.getInstance();
        easeUI.setUserProfileProvider(new EaseUI.EaseUserProfileProvider() {
            @Override
            public EaseUser getUser(String username) {
                return getUserInfo(username);
            }
        });
    }

    private EaseUser getUserInfo(String username) {
        EaseUser easeUser = new EaseUser(username);

        SharedPreferences sharedPreferences = context.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        sqLiteDatabase =new UserTokenSql(context).getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("user",null,"usertoken"+"=?",new String[]{token},null,null,null);
        if(cursor.moveToLast()){
            if (username.equals(EMClient.getInstance().getCurrentUser())){
                String usernamefromLocal = cursor.getString(cursor.getColumnIndex("username"));
                String userheaderfromLocal = cursor.getString(cursor.getColumnIndex("userheader"));
                easeUser.setAvatar(userheaderfromLocal);
                easeUser.setNickname(usernamefromLocal);
            }else {
                easeUser.setNickname(nMap.get(username));

            }
        }
        return easeUser;
    }//即可正常显示头像昵称


}
