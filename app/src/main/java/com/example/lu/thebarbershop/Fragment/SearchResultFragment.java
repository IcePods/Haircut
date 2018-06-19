package com.example.lu.thebarbershop.Fragment;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by sweet on 2018/6/18.
 */

@SuppressLint("ValidFragment")
public class SearchResultFragment extends Fragment {
    private Context mContext;
    OkHttpClient okHttpClient = new OkHttpClient();
    private List<UserShopDetail> userShopDetails;

    @SuppressLint("ValidFragment")
    public SearchResultFragment(Context mContext) {
        this.mContext = mContext;
    }


    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what){
                case 1:
                    Bundle bundle = msg.getData();
                    String searchList = bundle.getString("searchList");
                    Log.i("searchList",searchList);
                    Gson gson =new Gson();
                    userShopDetails =gson.fromJson(searchList,new TypeToken<List<UserShopDetail>>(){}.getType());
                    Log.i("searchList",userShopDetails.size()+"");
                   /*initShopAdapter();*/
                    break;
            }
        }
    };
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }
    //get得到搜索结果
    public void getSearchResult(final String searchEdit){
        new Thread(){
            @Override
            public void run() {
                FormBody.Builder formBody = new FormBody.Builder();
                formBody.add("",searchEdit);
                FormBody body = formBody.build();
                Request request = new Request.Builder().url(UrlAddress.url+"").post(body).build();
                Call call = okHttpClient.newCall(request);
                call.enqueue(new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String searchList = response.body().string();
                        Message message = Message.obtain();
                        message.what=1;
                        Bundle bundle = new Bundle();
                        bundle.putString("searchList",searchList);
                        message.setData(bundle);
                    }
                });

            }
        }.start();
    }
}
