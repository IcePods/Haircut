package com.example.lu.thebarbershop.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.lu.thebarbershop.R;
import com.hyphenate.easeui.ui.EaseChatFragment;

public class ECChatActivity extends AppCompatActivity {
    // 当前聊天的 ID
    private String mChatId;
    private EaseChatFragment chatFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ecchat);
        // 这里直接使用EaseUI封装好的聊天界面
        chatFragment = new EaseChatFragment();
        // 将参数传递给聊天界面
        chatFragment.setArguments(getIntent().getExtras());
        Log.i("ztk","执行了chatfragment?");
        getSupportFragmentManager().beginTransaction().add(R.id.ec_layout_container, chatFragment).commit();
    }
}
