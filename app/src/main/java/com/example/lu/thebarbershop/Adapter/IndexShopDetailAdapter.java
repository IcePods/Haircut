package com.example.lu.thebarbershop.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.lu.thebarbershop.Entity.Dynamic;
import com.example.lu.thebarbershop.Entity.UserShopDetail;
import com.example.lu.thebarbershop.R;

import java.util.List;

/**
 * Created by lu on 2018/5/11 0011.
 */

public class IndexShopDetailAdapter extends BaseAdapter {
    //上下文环境
    private Context context;
    //声明数据源
    private List<UserShopDetail> dataSource;
    //声明列表项的布局itemID
    private int item_user_index_shop;
    public IndexShopDetailAdapter(){super();}
    public IndexShopDetailAdapter(Context context, List<UserShopDetail> dataSource, int item_user_index_shop) {
        this.context = context;
        this.dataSource = dataSource;
        this.item_user_index_shop = item_user_index_shop;
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
     * 定义viewHolder
     * */
    class ViewHolder{
        ImageView indexshopPicture;
        TextView indexshopName;
        TextView indexshopIntroduce;
        TextView indexshopAddress;

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
       ViewHolder viewHolder = null;
       if(convertView ==null){
           LayoutInflater minflater = LayoutInflater.from(context);
           convertView = minflater.inflate(R.layout.item_user_index_shop,null);
           viewHolder = new ViewHolder();
           viewHolder.indexshopPicture = convertView.findViewById(R.id.user_shopdetail_picture);
           viewHolder.indexshopName=convertView.findViewById(R.id.user_shopdetail_shopname);
           viewHolder.indexshopIntroduce = convertView.findViewById(R.id.user_shopdetail_introduce);
           viewHolder.indexshopAddress = convertView.findViewById(R.id.user_shopdetail_shopaddress);
           convertView.setTag(viewHolder);
       }else{
           viewHolder = (ViewHolder) convertView.getTag();
       }
        RequestOptions requestOptions = new RequestOptions().centerCrop();
        requestOptions.placeholder(R.mipmap.user_index_perm);
       final UserShopDetail userShopDetail = dataSource.get(position);
        Glide.with(context)
                .load(userShopDetail.getShopPicture())
                .apply(requestOptions)
                .into(viewHolder.indexshopPicture);
        viewHolder.indexshopName.setText(userShopDetail.getShopName());
        viewHolder.indexshopAddress.setText(userShopDetail.getShopAddress());
        viewHolder.indexshopIntroduce.setText(userShopDetail.getShopIntroduce());
        return convertView;
    }
}
