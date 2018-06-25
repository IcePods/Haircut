package com.example.lu.thebarbershop;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Property;
import android.view.View;

import com.example.lu.thebarbershop.Activity.MainActivity;

import net.qiujuer.genius.res.Resource;
import net.qiujuer.genius.ui.compat.UiCompat;

public class LaunchActivity extends AppCompatActivity {

    private ColorDrawable mDrawable;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launch);
        initWidget();
        initData();
    }

    private void initWidget() {
        View root = findViewById(R.id.activity_launch);
        int color = UiCompat.getColor(getResources(),R.color.colorPrimary);
        ColorDrawable drawable = new ColorDrawable(color);
        root.setBackground(drawable);
        mDrawable= drawable;
    }

    private void initData() {
        startAnim(1.0f, new Runnable() {
            @Override
            public void run() {
                skip();
            }
        });
    }

    /**
     * 跳转的函数
     */
    private void skip(){
        MainActivity.show(this);
        finish();
    }

    /**
     * 给背景设置动画
     * @param endProgress 动画结束的进度
     * @param endCallback 动画结束时触发
     */
    private void startAnim(float endProgress,final Runnable endCallback){
        int finalColor = Resource.Color.WHITE;
        //运算当前进度的颜色
        ArgbEvaluator evaluator = new ArgbEvaluator();
        int endColor = (int) evaluator.evaluate(endProgress,mDrawable.getColor(),finalColor);
        ValueAnimator animator = ObjectAnimator.ofObject(this,property,evaluator,endColor);
        animator.setDuration(1500);
        animator.setIntValues(mDrawable.getColor(),endColor);
        animator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                //结束时触发
                endCallback.run();
            }
        });
        animator.start();

    }
    private Property<LaunchActivity,Object> property = new Property<LaunchActivity, Object>(Object.class,"color") {

        @Override
        public Object get(LaunchActivity object) {
            return object.mDrawable.getColor();
        }

        @Override
        public void set(LaunchActivity object, Object value) {
            object.mDrawable.setColor((Integer) value);
        }
    };
}
