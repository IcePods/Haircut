package com.example.lu.thebarbershop;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.example.lu.thebarbershop.MyTools.UserTokenSql;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.domain.EaseUser;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by lu on 2018/6/19 0019.
 */

public class MyApplication extends Application {
    // 上下文菜单
    private Context mContext;
    private String token;
    private SQLiteDatabase sqLiteDatabase;

    // 记录是否已经初始化
    private boolean isInit = false;
    @Override
    public void onCreate() {
        super.onCreate();
        mContext = this;

        // 初始化环信SDK
        initEasemob();
        JPushInterface.setDebugMode(true);
        JPushInterface.init(this);
    }
    /**
     * 初始化环信SDK
     */
    private void initEasemob() {

        if (EaseUI.getInstance().init(mContext, initOptions())) {

            // 设置开启debug模式
            EMClient.getInstance().setDebugMode(true);

            // 设置初始化已经完成
            isInit = true;
        }


    }

    /**
     * SDK初始化的一些配置
     * 关于 EMOptions 可以参考官方的 API 文档
     * http://www.easemob.com/apidoc/android/chat3.0/classcom_1_1hyphenate_1_1chat_1_1_e_m_options.html
     */
    private EMOptions initOptions() {

        EMOptions options = new EMOptions();
        // 设置Appkey，如果配置文件已经配置，这里可以不用设置
        // options.setAppKey("lzan13#hxsdkdemo");
        // 设置自动登录
        options.setAutoLogin(true);
        // 设置是否需要发送已读回执
        options.setRequireAck(true);
        // 设置是否需要发送回执，
        options.setRequireDeliveryAck(true);
        // 设置是否需要服务器收到消息确认
        options.setRequireDeliveryAck(true);
        // 设置是否根据服务器时间排序，默认是true
        options.setSortMessageByServerTime(false);
        // 收到好友申请是否自动同意，如果是自动同意就不会收到好友请求的回调，因为sdk会自动处理，默认为true
        options.setAcceptInvitationAlways(false);
        // 设置是否自动接收加群邀请，如果设置了当收到群邀请会自动同意加入
        options.setAutoAcceptGroupInvitation(false);
        // 设置（主动或被动）退出群组时，是否删除群聊聊天记录
        options.setDeleteMessagesAsExitGroup(false);
        // 设置是否允许聊天室的Owner 离开并删除聊天室的会话
        options.allowChatroomOwnerLeave(true);
        // 设置google GCM推送id，国内可以不用设置
        // options.setGCMNumber(MLConstants.ML_GCM_NUMBER);
        // 设置集成小米推送的appid和appkey
        // options.setMipushConfig(MLConstants.ML_MI_APP_ID, MLConstants.ML_MI_APP_KEY);

        return options;
    }
    private void setEaseUser() {
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
        SharedPreferences sharedPreferences = this.getSharedPreferences("usertoken", Context.MODE_PRIVATE);
        token = sharedPreferences.getString("token","");
        sqLiteDatabase =new UserTokenSql(this).getReadableDatabase();
        Cursor cursor = sqLiteDatabase.query("user",null,"usertoken"+"=?",new String[]{token},null,null,null);
        if(cursor.moveToLast()){
            String usernamefromLocal = cursor.getString(cursor.getColumnIndex("username"));
            String userheaderfromLocal = cursor.getString(cursor.getColumnIndex("userheader"));
            easeUser.setAvatar(userheaderfromLocal);
            easeUser.setNickname(usernamefromLocal);
        }
        return easeUser;
    }//即可正常显示头像昵称
}
