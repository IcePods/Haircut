package com.example.lu.thebarbershop.MyTools;

import android.content.Context;
import android.widget.ListView;

/**
 * Created by sweet on 2018/5/15.
 */

public class IndexListView extends ListView {
    public IndexListView(Context context) {
        super(context);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec
                ,MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE>>2,MeasureSpec.AT_MOST));
    }
}
