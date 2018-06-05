package com.example.lu.thebarbershop.MyTools;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by asus on 2018/5/27.
 */

public class UploadPictureUtil {

    /**
     * 线程上传图片
     * @param url 请求地址
     */
    public void upload(final String url, final Bitmap bitmap) {
        new Thread(){
            @Override
            public void run() {
                super.run();
                // 将bitmap转为string，并使用BASE64加密
                String photo = BitmapToString(bitmap);
                //参数的类型
                MediaType type = MediaType.parse("text/plain;charset=UTF-8");
                //创建RequestBody对象
                RequestBody body = RequestBody.create(type, photo);
                // 创建Request.Builder对象
                Request.Builder builder = new Request.Builder();
                //设置参数
                builder.url(url);
                //设置请求方式为POST
                builder.post(body);
                //创建Request请求对象
                Request request = builder.build();
                OkHttpClient okHttpClient = new OkHttpClient();
                //3. 创建Call对象
                Call call = okHttpClient.newCall(request);

                //4. 提交请求并获取返回的响应数据
                call.enqueue(new Callback() {//2）异步请求
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //发生错误时执行的回调
                        Log.i("李垚:::::::::","上传失败");
                    }
                    @Override
                    public void onResponse(Call call, Response r) throws IOException{
                        //正确执行，获取返回数据的回调
                        Log.i("李垚:::::::::","上传成功");
                    }
                });
            }
        }.start();
    }

    /**
     * 剪裁图片
     * @param uri 图片文件的Uri
     * @return 返回一个请求意图Intent
     */
    public Intent cropPhoto(Uri uri) {
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");

        // 设置裁剪
        intent.putExtra("crop", "true");

        // aspectX , aspectY :宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);

        // outputX , outputY : 裁剪图片宽高
        intent.putExtra("outputX", 400);
        intent.putExtra("outputY", 400);
        intent.putExtra("outputFormat", Bitmap.CompressFormat.PNG);
        intent.putExtra("return-data", true);

        return intent;
    }

    // 将bitmap转成string类型通过Base64
    private String BitmapToString(Bitmap bitmap) {

        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        // 将bitmap压缩成30%
        bitmap.compress(Bitmap.CompressFormat.PNG, 30, bao);
        // 将bitmap转化为一个byte数组
        byte[] bs = bao.toByteArray();
        // 将byte数组用BASE64加密
        String photoStr = Base64.encodeToString(bs, Base64.DEFAULT);
        // 返回String
        return photoStr;
    }
}