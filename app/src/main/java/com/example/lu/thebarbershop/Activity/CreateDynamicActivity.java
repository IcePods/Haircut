package com.example.lu.thebarbershop.Activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import android.text.method.HideReturnsTransformationMethod;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.baidu.mapapi.search.geocode.OnGetGeoCoderResultListener;
import com.example.lu.thebarbershop.Adapter.FullyGridLayoutManager;
import com.example.lu.thebarbershop.Adapter.GridImageAdapter;
import com.example.lu.thebarbershop.Entity.Dynamic;
import com.example.lu.thebarbershop.Entity.DynamicPicture;
import com.example.lu.thebarbershop.Entity.UrlAddress;
import com.example.lu.thebarbershop.MyTools.GetUserFromShared;
import com.example.lu.thebarbershop.MyTools.UploadPictureUtil;
import com.example.lu.thebarbershop.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.luck.picture.lib.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.entity.LocalMedia;

import java.io.File;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import okhttp3.internal.Util;

public class CreateDynamicActivity extends AppCompatActivity {
    //返回按钮
    private ImageButton newDynamicBack;
    //动态的文字内容
    private EditText textContent;
    //动态多图的显示控件
    private RecyclerView newDynamicImages;
    //发布动态按钮
    private Button publishNewDynamic;
    //图片适配器
    private GridImageAdapter adapter;
    //存储图片选择完成后的图片地址信息
    private List<LocalMedia> selectList = new ArrayList<>();
    //页面风格信息
    private int themeId;
    //上传动态图片的路径
    final String uploadPictureUrl = UrlAddress.url + "uploadPictureList.action";
    //发布动态路径
    final private String publishUrl = UrlAddress.url + "createDynamic.action";
    //动态对象
    private Dynamic dynamic;
    //标记用户的token
    private String token;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_dynamic);

        //初始化控件
        initControl();

        //给RecycleView设置管理器和适配器
        setNewDynamicImagesAdapter();

        //给导航栏返回图标绑定监听事件
        setNewDynamicBackListener();

        //给发布动态按钮绑定事件监听器
        setPublishNewDynamicListener();
    }

    /**
     * 获取控件
     */
    private void initControl(){
        //activaty_create_dynamic.xml中的控件
        newDynamicBack = findViewById(R.id.new_dynamic_back);
        textContent = findViewById(R.id.new_dynamic_content);
        newDynamicImages = findViewById(R.id.new_dynamic_images);
        publishNewDynamic = findViewById(R.id.publish_new_dynamic);
        //styles.xml中的默认样式
        themeId = R.style.picture_default_style;

        try {
            token = new GetUserFromShared(this).getUserTokenFromShared();
        }catch (RuntimeException e){
            Toast.makeText(this,
                    "您尚未登录",
                    Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * 给RecycleView设置管理器和适配器
     */
    private void setNewDynamicImagesAdapter(){
        //管理器
        FullyGridLayoutManager manager = new FullyGridLayoutManager(CreateDynamicActivity.this, 4, GridLayoutManager.VERTICAL, false);
        newDynamicImages.setLayoutManager(manager);
        //适配器
        adapter = new GridImageAdapter(CreateDynamicActivity.this, onAddPicClickListener);
        adapter.setList(selectList);
        adapter.setSelectMax(9);
        newDynamicImages.setAdapter(adapter);
        adapter.setOnItemClickListener(new GridImageAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position, View v) {
                if (selectList.size() > 0) {
                    LocalMedia media = selectList.get(position);
                    String pictureType = media.getPictureType();
                    int mediaType = PictureMimeType.pictureToVideo(pictureType);
                    // 预览图片 可自定长按保存路径
                    //PictureSelector.create(MainActivity.this).themeStyle(themeId).externalPicturePreview(position, "/custom_file", selectList);
                    PictureSelector.create(CreateDynamicActivity.this).themeStyle(themeId).openExternalPreview(position, selectList);
                }
            }
        });
    }

    /**
     * 给添加图片按钮设置点击事件监听器
     */
    private GridImageAdapter.onAddPicClickListener onAddPicClickListener = new GridImageAdapter.onAddPicClickListener() {
        @Override
        public void onAddPicClick() {
            PictureSelector.create(CreateDynamicActivity.this)
                    .openGallery(PictureMimeType.ofImage())//启用相册类型（图片）
                    .theme(R.style.picture_default_style)//样式
                    .maxSelectNum(9)//最大选择数量
                    .minSelectNum(1)//最小选择数量
                    .imageSpanCount(4)//相册每行显示数量
                    .selectionMode(PictureConfig.MULTIPLE)//多选
                    .previewImage(true)//预览图片
                    .isCamera(true)//显示启用相机
                    .selectionMedia(selectList)//显示已选列表
                    .forResult(PictureConfig.CHOOSE_REQUEST);
        }
    };

    /**
     * 选择图片完成以后的回调方法
     * @param requestCode
     * @param resultCode
     * @param data
     */
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) switch (requestCode) {
            case PictureConfig.CHOOSE_REQUEST:
                // 图片、视频、音频选择结果回调
                selectList = PictureSelector.obtainMultipleResult(data);

                // 例如 LocalMedia 里面返回三种path
                // 1.media.getPath(); 为原图path
                // 2.media.getCutPath();为裁剪后path，需判断media.isCut();是否为true  注意：音视频除外
                // 3.media.getCompressPath();为压缩后path，需判断media.isCompressed();是否为true  注意：音视频除外
                // 如果裁剪并压缩了，以取压缩路径为准，因为是先裁剪后压缩的

                adapter.setList(selectList);
                adapter.notifyDataSetChanged();
        }
    }

    /**
     * 给newDynamicBack控件设置事件监听器
     */
    private void setNewDynamicBackListener(){
        newDynamicBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }

    /**
     * 给发布动态按钮绑定时间监听器
     */
    private void setPublishNewDynamicListener(){
        publishNewDynamic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //获得动态对象
                dynamic = new Dynamic();
                //设置发布动态的内容
                dynamic.setDynamicContent(getNewDynamicContent());
                //获取响应消息，并为动态添加图片访问路径，上传动态
                final Handler uploadPicListhandler = new Handler(){
                    @Override
                    public void handleMessage(Message msg) {
                        super.handleMessage(msg);
                        Bundle bundle = msg.getData();
                        String picPathStr = bundle.getString("string");
                        Gson gson = new Gson();
                        List<String> picPathList = gson.fromJson(picPathStr, new TypeToken<List<String>>(){}.getType());
                        Set<DynamicPicture> dynamicPicPathSet = new HashSet<>();
                        for(String str: picPathList){
                            DynamicPicture dp = new DynamicPicture();
                            dp.setDynamicPicture(str);
                            dynamicPicPathSet.add(dp);
                            Log.i("李垚：：：：：：：图片的访问路径", str);
                        }
                        //设置图片访问路径
                        dynamic.setDynamicImagePathSet(dynamicPicPathSet);
                        String newDynamic = gson.toJson(dynamic);
                        //上传动态
                        new UploadPictureUtil().requestServer(publishUrl,newDynamic,token,null);
                        Log.i("李垚：：：：：：", "动态对象上传");
                    }
                };
                //上传图片、获得访问路径
                uploadPicAndGetPathList(uploadPicListhandler);
                finish();
            }
        });
    }

    /**
     * 从newDynamicContent中获取动态文字内容
     * @return 返回动态文字内容的字符串
     */
    private String getNewDynamicContent(){
        String str = textContent.getText().toString();
        return str;
    }

    /**
     * 上传选中的图片并返回图片的访问路径
     */
    private void uploadPicAndGetPathList(Handler uploadPicListhandler) {
        UploadPictureUtil util = new UploadPictureUtil();
        //获取图片信息
        Uri uri;
        List<String> photoStringList = new ArrayList<>();
        for (LocalMedia media : selectList) {
            uri = Uri.fromFile(new File(media.getPath()));
            try {
                //获取bitmap
//                    //ContentResolver cr = getContentResolver();
//                    //  bitmap = MediaStore.Images.Media.getBitmap(cr,uri);
                String photoStr = util.getStringFromUri(CreateDynamicActivity.this,uri);
                photoStringList.add(photoStr);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Gson gson = new Gson();
        final String picList = gson.toJson(photoStringList);

        util.requestServer(uploadPictureUrl, picList, token, uploadPicListhandler);
        Toast.makeText(getApplicationContext(),
                "正在上传",
                Toast.LENGTH_SHORT).show();
    }

}