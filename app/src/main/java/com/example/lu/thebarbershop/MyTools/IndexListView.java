package com.example.lu.thebarbershop.MyTools;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.ListView;

/**
 * Created by sweet on 2018/5/15.
 */

public class IndexListView extends ListView {


    public IndexListView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
