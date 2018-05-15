package com.example.lu.thebarbershop.Fragment;

import android.app.Fragment;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.lu.thebarbershop.R;

/**
 * Created by lu on 2018/5/9 0009.
 *"我的" fragment
 */

public class PersonFragment extends Fragment {
    private ImageView imageView;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //加载选项卡对应的选项页面
        View view = inflater.inflate(R.layout.fragment_person,container,false);

        imageView = view.findViewById(R.id.user_person_head_portrait_img);
        imageView.setImageBitmap(getRoundedCornerBitmap(((BitmapDrawable)imageView.getDrawable()).getBitmap(),2));
        return view;
    }

    /**
     * 设置圆角图片 当ratio为2时，为圆形图片
     * @param bitmap 原bitmap图片
     * @param ratio
     * @return
     */
    public static Bitmap getRoundedCornerBitmap(Bitmap bitmap, float ratio) {

        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(),
                bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);

        final Paint paint = new Paint();
        final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight());
        final RectF rectF = new RectF(rect);

        paint.setAntiAlias(true);
        canvas.drawARGB(0, 0, 0, 0);
        canvas.drawRoundRect(rectF, bitmap.getWidth() / ratio,
                bitmap.getHeight() / ratio, paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);
        return output;
    }
}

