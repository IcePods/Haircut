package com.example.lu.thebarbershop.MyTools;

import android.content.Context;

import android.view.ViewGroup;
import android.widget.ImageView;

import java.util.List;

/**
 * Created by asus on 2018/5/15.
 */

public abstract class NineImgView extends ViewGroup {
    private static final float DEFUALT_SPACING = 3f;
    //图片最大显示数量
    private static final int MAX_COUNT = 9;

    protected Context context;

    public NineImgView(Context context) {
        super(context);
    }


    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    /**
     * @param imageView
     * @param url
     * @param parentWidth 父控件宽度
     * @return true 代表按照九宫格默认大小显示，false 代表按照自定义宽高显示
     */
    protected abstract boolean displayOneImage(ImageView imageView, String url, int parentWidth);

    protected abstract void displayImage(ImageView imageView, String url);

    protected abstract void onClickImage(int position, String url, List<String> urlList);
}
