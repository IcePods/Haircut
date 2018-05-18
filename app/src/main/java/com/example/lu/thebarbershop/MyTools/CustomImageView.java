package com.example.lu.thebarbershop.MyTools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.List;

/**
 * Created by asus on 2018/5/16.
 */

public class CustomImageView extends NineImgView {
    public CustomImageView(Context context) {
        super(context);
    }

    public CustomImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected boolean displayOneImage(ImageView imageView, String url, int parentWidth) {
        return true;
    }

    @Override
    protected void displayImage(ImageView imageView, String url) {
        Glide.with(getContext()).load(url).into(imageView);
    }

    @Override
    protected void onClickImage(int position, String url, List<String> urlList) {
        Toast.makeText(context, "点击了图片", Toast.LENGTH_SHORT).show();
    }

}
