package com.example.lu.thebarbershop.Adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.common.views.PortraitView;
import com.example.lu.thebarbershop.Entity.ActiveChat;
import com.example.lu.thebarbershop.R;

import java.util.List;

/**
 * Created by lenovo on 2018/5/25.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder>{
    private Context mContext;
    private List<ActiveChat> lists;
    private int layId;

    public NewsAdapter(Context mContext,int layId, List<ActiveChat> lists) {
        this.mContext = mContext;
        this.layId = layId;
        this.lists = lists;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(mContext).inflate(layId,parent,false);
        ViewHolder holder = new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.mPortrait.setImageResource((int)lists.get(position).getmPortrait());
        holder.mName.setText(lists.get(position).getmName());
        holder.mContent.setText(lists.get(position).getmContent());
        holder.mTime.setText(lists.get(position).getmTime());
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        protected PortraitView mPortrait;
        protected TextView mName;
        protected TextView mContent;
        protected TextView mTime;
        public ViewHolder(View itemView) {
            super(itemView);
            mPortrait = itemView.findViewById(R.id.user_portrait);
            mName = itemView.findViewById(R.id.user_txt_name);
            mContent = itemView.findViewById(R.id.user_txt_content);
            mTime = itemView.findViewById(R.id.user_txt_time);


        }
    }
}
