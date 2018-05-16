package com.example.lu.thebarbershop.MyTools;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.lu.thebarbershop.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sweet on 2018/5/13.
 */

public class ViewPagerTools {
    private Context mContext;
    private ViewPager vp;//layout中viewpager
    private LinearLayout ll_point;//viewpager下方的存放点的部分
    private ArrayList<ImageView> imageViewArrayList;//用于存放图片的集合
    private ViewGroup v;

    private int lastPosition;//上一个点

    public ViewPagerTools(Context mContext, ViewPager vp, LinearLayout ll_point, ArrayList<ImageView> imageViewArrayList) {
        this.mContext = mContext;
        this.vp = vp;
        this.ll_point = ll_point;
        this.imageViewArrayList = imageViewArrayList;
    }

    /*
        * 更改下方点
        * 参数：imageview类型图片的集合
        * */
    public void initDate(List<ImageView> imageViewList){
        View pointView;
       for(int i = 0;i<imageViewList.size();i++){
           //加小白点，指示器（这里的小圆点定义在了drawable下的选择器中了，也可以用小图片代替）
           pointView = new View(mContext);
           pointView.setBackgroundResource(R.drawable.point_selector); //使用选择器设置背景
           LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(10, 10);
           if (i != 0){
               //如果不是第一个点，则设置点的左边距
               layoutParams.leftMargin = 10;
           }
           pointView.setEnabled(false); //默认都是暗色的
           ll_point.addView(pointView, layoutParams);
       }
    }

    /**
     * 初始化适配器
     *
     * */
    public void initAdapter(){
        ll_point.getChildAt(0).setEnabled(true);//设置第一个小圆点亮色
        lastPosition =0;//设置之前的位置为第一个
        vp.setAdapter(new myViewPagerAdapter());
        //设置默认显示中间的某个位置（这样可以左右滑动），这个数只有在整数范围内，可以随便设置
        vp.setCurrentItem(1000);
    }


    //自定义适配器继承自pagerAdapter
    class myViewPagerAdapter extends PagerAdapter{
        //返回要显示的条目内容
        @NonNull
        @Override
        public Object instantiateItem(@NonNull ViewGroup container, int position) {
            //container容器相当于用来存放 imagerview
            //防止下表越界
            int a = imageViewArrayList.size();
            int newPositon  =position % a;//图片list中的图片是，取模防止下表越界
            ImageView imageView = imageViewArrayList.get(newPositon);

            ViewGroup parent =(ViewGroup) imageView.getParent();
            if (parent != null) {
                parent.removeView(imageView);
            }

            //图片添加到container中
            container.addView(imageView);
            //将图片赶回给框架
            return imageView;
        }

        @Override
        public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
            //object:刚才创建的对象，即要销毁的对象
            /*container.removeView((View) object);*/
        }

        //返回显示数据的总条数，为了实现无限循环，把返回的值设置为最大整数
        @Override
        public int getCount() {
            return Integer.MAX_VALUE;
        }
        //指定复用的判断逻辑：固定写法：view== object
        @Override
        public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
            return view == object;
        }
    }
}
