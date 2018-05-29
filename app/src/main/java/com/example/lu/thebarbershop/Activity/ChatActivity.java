package com.example.lu.thebarbershop.Activity;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.lu.thebarbershop.Fragment.ChatFragment;
import com.example.lu.thebarbershop.R;

public class ChatActivity extends AppCompatActivity {

    public static void show(Context context){
        Intent intent = new Intent(context,ChatActivity.class);
        context.startActivity(intent);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        getSupportFragmentManager().beginTransaction()
                .add(R.id.chat_fragment,new ChatFragment())
                .commit();
    }
}
