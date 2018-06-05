package com.example.lu.thebarbershop.Fragment;




import android.app.Fragment;
import android.os.Bundle;
import android.support.annotation.Nullable;


import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.lu.thebarbershop.Adapter.NewsAdapter;
import com.example.lu.thebarbershop.Entity.ActiveChat;
import com.example.lu.thebarbershop.R;


import java.util.ArrayList;
import java.util.List;

/**
 * Created by lu on 2018/5/9 0009.
 * 消息fragment
 */

public class NewsFragment extends Fragment {
    RecyclerView recyclerView;
    public NewsFragment() {
        // Required empty public constructor

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        int layId = R.layout.fragment_news;
        View root = inflater.inflate(layId, container, false);
        NewsAdapter adapter = new NewsAdapter(getActivity().getApplicationContext(), R.layout.item_user_news_list,initData());
        recyclerView = root.findViewById(R.id.recyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity().getApplicationContext()));
        recyclerView.setAdapter(adapter);
        return root;
    }


    private List<ActiveChat> initData(){
        List<ActiveChat> list = new ArrayList<>();
        ActiveChat chat1 = new ActiveChat(R.drawable.ic_launcher_background,"Json","hello","15:11");
        list.add(chat1);

        ActiveChat chat2 = new ActiveChat(R.drawable.ic_launcher_background,"Marry","hello","15:11");
        list.add(chat2);

        ActiveChat chat3 = new ActiveChat(R.drawable.ic_launcher_background,"Tom","hello","15:11");
        list.add(chat3);

        ActiveChat chat4 = new ActiveChat(R.drawable.ic_launcher_background,"Alan","hello","15:11");
        list.add(chat4);
        return list;
    }
}
