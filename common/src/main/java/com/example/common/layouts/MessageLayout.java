package com.example.common.layouts;

import android.content.Context;
import android.graphics.Rect;
import android.os.Build;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.widget.LinearLayout;

/**
 * Created by lenovo on 2018/5/29.
 */

public class MessageLayout extends LinearLayout {
    public MessageLayout(Context context) {
        super(context);
    }

    public MessageLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MessageLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    protected boolean fitSystemWindows(Rect insets) {
        if(Build.VERSION.SDK_INT >=Build.VERSION_CODES.KITKAT){
            insets.left = 0;
            insets.top = 0;
            insets.right = 0;
        }
        return super.fitSystemWindows(insets);
    }
}
