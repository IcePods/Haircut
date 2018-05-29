package com.example.lu.thebarbershop.Fragment;

import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import com.example.common.adapters.TextWatcherAdapter;
import com.example.common.views.PortraitView;
import com.example.lu.thebarbershop.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class ChatFragment extends Fragment
        implements AppBarLayout.OnOffsetChangedListener{

    AppBarLayout appBar;
    Toolbar toolBar;
    RecyclerView recyclerView;
    PortraitView portraitView;
    ImageView btn_face;
    ImageView btn_record;
    EditText edit_content;
    ImageView btn_submit;
    public ChatFragment(){
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater,ViewGroup container,
                             Bundle savedInstanceState){
        View root = inflater.inflate(R.layout.fragment_chat, container, false);
        initParams(root);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        initAppBar();
        initToolBar();
        initBtnContent();
        return root;
    }

    /**
     * 初始化
     * 当发送框中有文字的时候，变为发送按钮
     */
    private void initBtnContent() {
        edit_content.addTextChangedListener(new TextWatcherAdapter() {
            @Override
            public void afterTextChanged(Editable s) {
                String content = s.toString().trim();
                boolean needSendMsg = !TextUtils.isEmpty(content);
                btn_submit.setActivated(needSendMsg);
            }
        });
    }

    /**
     * 初始化参数
     * @param root
     */
    private void initParams(View root) {
        appBar = root.findViewById(R.id.appbar);
        toolBar = root.findViewById(R.id.toolbar);
        recyclerView = root.findViewById(R.id.recycler);
        portraitView = root.findViewById(R.id.portraitView);
        btn_face = root.findViewById(R.id.btn_face);
        btn_record = root.findViewById(R.id.btn_record);
        edit_content = root.findViewById(R.id.edit_content);
        btn_submit = root.findViewById(R.id.btn_submit);
    }
    private void initToolBar(){
        Toolbar mToolbar = toolBar;
        mToolbar.setNavigationIcon(R.drawable.ic_back);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });
    }
    private void initAppBar(){
        appBar.addOnOffsetChangedListener(this);
    }
    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        View view = portraitView;
        if(verticalOffset == 0){
            view.setVisibility(View.VISIBLE);
            view.setScaleX(1);
            view.setScaleY(1);
            view.setAlpha(1);
        }else{
            verticalOffset = Math.abs(verticalOffset);
            final int totalScroll = appBarLayout.getTotalScrollRange();
            if(verticalOffset >= totalScroll){
                view.setVisibility(View.INVISIBLE);
                view.setScaleX(0);
                view.setScaleY(0);
                view.setAlpha(0);
            }else{
                float progress = 1-verticalOffset/(float)totalScroll;
                view.setVisibility(View.VISIBLE);
                view.setScaleX(progress);
                view.setScaleY(progress);
                view.setAlpha(progress);
            }
        }
    }
}
