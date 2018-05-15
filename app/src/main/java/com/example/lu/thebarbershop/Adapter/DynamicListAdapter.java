package com.example.lu.thebarbershop.Adapter;

import android.content.Context;
import android.graphics.Point;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.lu.thebarbershop.Entity.Dynamic;
import com.example.lu.thebarbershop.R;

import java.util.List;

/**
 * Created by lu on 2018/5/11 0011.
 */

public class DynamicListAdapter extends BaseAdapter {
    //上下文环境
    private Context context;
    //声明数据源
    private List<Dynamic> dataSource;
    //声明列表项的布局itemID
    private int item_layout_id;
    public DynamicListAdapter(){super();}
    public DynamicListAdapter(Context context, List<Dynamic> dataSource, int item_layout_id) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_layout_id = item_layout_id;
    }
    /**
     * 很重要的一个方法。返回adapter中的包含多少个列表项
     * @return 返回数据源的数量
     */
    @Override
    public int getCount() {
        return dataSource.size();
    }

    /**
     *
     * @param position 当前选择的item的id
     * @return 返回position位置的itemx项的数据
     */
    @Override
    public Object getItem(int position) {
        return dataSource.get(position);
    }
    /**
     *
     * @param position
     * @return  当前选择的第几个item项
     */
    @Override
    public long getItemId(int position) {
        return position;
    }
    /**
     *
     * @param position
     * @param convertView 返回的视图
     * @param parent
     * @return 返回的item的布局视图
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView){
            //1.获取子项目item的布局文件
            //用于加载布局的layoutInflater对象
            LayoutInflater mInflater = LayoutInflater.from(context);
            //2.布局文件复制给convertView 参数
            //使用inflate 方法加载布局文件（参数1：布局文件的ID,参数2：viewGroup）
            convertView = mInflater.inflate(item_layout_id,null);
        }
        //3.获取页面布局文件对象
        //获取东塔用户头型
        ImageView  dynamic_user_head = convertView.findViewById(R.id.dynamic_user_head);
        //互获取动态用户昵称
        TextView dynamic_user_name = convertView.findViewById(R.id.dynamic_user_name);
        //获取动态内容
        TextView dynamic_content = convertView.findViewById(R.id.dynamic_content);
        //获取动态图片
        ImageView dynamic_img = convertView.findViewById(R.id.dynamic_img);

        //4.通过穿过来的数据源给响应的控件对象赋值
        final Dynamic dynamic = dataSource.get(position);
       //设置用户头像
        dynamic_user_head.setImageResource((int)dynamic.getImgName());
        dynamic_user_name.setText(dynamic.getUserName());
        dynamic_content.setText(dynamic.getDynamicContent());
        dynamic_img.setImageResource((int)dynamic.getDynamicImg());
        //5.返回子项目布局文件
        return convertView;
    }
}
