package com.example.mytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;

import com.example.shibo.simplerecycleview.SonAdapter;
import com.example.shibo.simplerecycleview.recycleview.MyUtils;
import com.example.shibo.simplerecycleview.recycleview.SimpleRecycleview;
import com.example.shibo.simplerecycleview.recycleview.SimpleRecycleviewItemDe;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    MRecycleviewAdapter mRecycleviewAdapter;
    List<MyBean> mList;
    @BindView(R.id.recycleview)
    SimpleRecycleview recycleview;

    @OnClick(R.id.button)
    void clickAdd() {
        mList.add(new MyBean());
        mRecycleviewAdapter.notifyDataSetChanged();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        mList = new ArrayList<>();
        mRecycleviewAdapter = new MRecycleviewAdapter(mList, MainActivity.this);
        if (recycleview == null) {
            Log.e("Recycleview为空", "==========");
        }
        //设置间距的方法
        recycleview.setItemDe((int) (MyUtils.getHeight(MainActivity.this) * 0.01));
        // mRecycleviewAdapter.setHeadView(R.layout.activity_main);
//设置头部和底部的layout
        //  mRecycleviewAdapter.setHeadView(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null));
        // mRecycleviewAdapter.setFootView(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null));
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setAdapter(mRecycleviewAdapter);
        recycleview.setItemAnimator(new DefaultItemAnimator());
        //设置刷新和加载的监听
        recycleview.setOnScrollChanListener(new SimpleRecycleview.OnScrollChanListener() {
            @Override
            public void onRefresh() {
                Log.e("刷新的方法", "刷新的方法");
                recycleview.stopRresh();
                mList.add(new MyBean());

                mRecycleviewAdapter.notifyDataSetChanged();

            }

            @Override
            public void loadMore() {
                Log.e("加载的方法", "加载的方法");
               // recycleview.stopLoadMore();
                mList.add(new MyBean());
                mRecycleviewAdapter.notifyDataSetChanged();
            }
        });

    }
}
