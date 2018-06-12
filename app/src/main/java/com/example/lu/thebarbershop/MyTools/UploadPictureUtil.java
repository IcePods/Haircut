package com.example.lu.thebarbershop.MyTools;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;

import java.io.ByteArrayOutputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

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
     * 线程里向服务器上传、请求数据
     * @param url 请求地址
     * @param obj 对象的json串
     * @param handler 接收响应信息的handler对象，在主线程里创建
     */
    public void requestServer(final String url, final String obj, final String token, final Handler handler) {
        new Thread(){
            @Override
            public void run() {
                super.run();

                // 获取到图片的名字
                //String name = photoPath.substring(photoPath.lastIndexOf("/")).substring(1);

                //参数的类型
                MediaType type = MediaType.parse("text/plain;charset=UTF-8");
                //创建RequestBody对象
                RequestBody body = RequestBody.create(type, obj);
                // 创建Request.Builder对象
                Request.Builder builder = new Request.Builder();
                //设置参数
                builder.url(url);
                //设置请求方式为POST
                builder.post(body);
                //创建Request请求对象
                Request request = builder.build();
                request.header(token);
                OkHttpClient okHttpClient = new OkHttpClient();
                //3. 创建Call对象
                Call call = okHttpClient.newCall(request);

                //4. 提交请求并获取返回的响应数据
                call.enqueue(new Callback() {//2）异步请求
                    @Override
                    public void onFailure(Call call, IOException e) {
                        //发生错误时执行的回调
                        Log.i("李垚:::::::::","请求失败");
                    }
                    @Override
                    public void onResponse(Call call, Response r) throws IOException{
                        //正确执行，获取返回数据的回调
                        Log.i("李垚:::::::::","请求成功");
                        String str = r.body().string();
                        Message message = Message.obtain();
                        Bundle bundle = new Bundle();
                        bundle.putString("string", str);
                        message.setData(bundle);
                        handler.sendMessage(message);
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

    /**
     * 从图片的Uri获取图片，并进行压缩，加密，转成字符串
     * @param ac 所在的Activity
     * @param uri 图片的uri
     * @return 图片转成的字符串
     */
    public String getStringFromUri(Activity ac, Uri uri){
        String photoStr = new String();
        try {
            Bitmap bitmap = getBitmapFormUri(ac, uri);
            photoStr = getStringFromBitmap(bitmap);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return photoStr;
    }

    /**
     * 将bitmap进行质量压缩，并加密，转成json串
     * @param bitmap 图片
     * @return 返回字符串
     */
    public String getStringFromBitmap(Bitmap bitmap) {
        ByteArrayOutputStream bao = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, bao);//质量压缩方法，这里100表示不压缩，把压缩后的数据存放到bao中
        int options = 100;
        while (bao.toByteArray().length / 1024 > 100 && options>10) {  //循环判断如果压缩后图片是否大于100kb,大于继续压缩
            bao.reset();//重置baos即清空baos
            options -= 10;//每次都减少10
            //第一个参数 ：图片格式 ，第二个参数： 图片质量，100为最高，0为最差  ，第三个参数：保存压缩后的数据的流
            bitmap.compress(Bitmap.CompressFormat.PNG, options, bao);//这里压缩options%，把压缩后的数据存放到baos中
        }
        // 将bitmap转化为一个byte数组
        byte[] bs = bao.toByteArray();
        // 将byte数组用BASE64加密
        String photoStr = Base64.encodeToString(bs, Base64.DEFAULT);
        // 返回String
        Log.i("李垚：：：：：：","质量压缩完成");
        return photoStr;
    }

    /**
     * 通过uri获取图片并进行压缩
     * @param uri
     */
    public Bitmap getBitmapFormUri(Activity ac, Uri uri) throws FileNotFoundException, IOException {
        InputStream input = ac.getContentResolver().openInputStream(uri);
        BitmapFactory.Options onlyBoundsOptions = new BitmapFactory.Options();
        onlyBoundsOptions.inJustDecodeBounds = true;
        onlyBoundsOptions.inDither = true;//optional
        onlyBoundsOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
        BitmapFactory.decodeStream(input, null, onlyBoundsOptions);
        input.close();
        int originalWidth = onlyBoundsOptions.outWidth;
        int originalHeight = onlyBoundsOptions.outHeight;
        if ((originalWidth == -1) || (originalHeight == -1))
            return null;
        //图片分辨率以480x800为标准
        float hh = 480f;//这里设置高度为800f
        float ww = 480f;//这里设置宽度为480f
        //缩放比。由于是固定比例缩放，只用高或者宽其中一个数据进行计算即可
        int be = 1;//be=1表示不缩放
        //计算压缩倍数
        if (originalWidth >= originalHeight && originalWidth > ww) {//如果宽度大的话根据宽度固定大小缩放
            be = (int) (Math.ceil(originalWidth / ww));
        } else if (originalWidth < originalHeight && originalHeight > hh) {//如果高度高的话根据宽度固定大小缩放
            be = (int) (Math.ceil(originalHeight / hh));
        }
        //比例压缩
        BitmapFactory.Options bitmapOptions = new BitmapFactory.Options();
        bitmapOptions.inSampleSize = be;//设置缩放比例
        bitmapOptions.inDither = true;//optional
        bitmapOptions.inPreferredConfig = Bitmap.Config.RGB_565;//optional
        input = ac.getContentResolver().openInputStream(uri);
        Bitmap bitmap = BitmapFactory.decodeStream(input, null, bitmapOptions);
        input.close();
        Log.i("李垚：：：：：：","根据Uri压缩完成");
        return bitmap;
    }
}