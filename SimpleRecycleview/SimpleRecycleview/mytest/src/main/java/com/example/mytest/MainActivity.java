package com.example.mytest;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.Button;

import com.example.shibo.simplerecycleview.SonAdapter;
import com.example.shibo.simplerecycleview.recycleview.MyUtils;
import com.example.shibo.simplerecycleview.recycleview.SimpleRecycleview;
import com.example.shibo.simplerecycleview.recycleview.SimpleRecycleviewAdater;
import com.example.shibo.simplerecycleview.recycleview.SimpleRecycleviewItemDe;
import com.example.shibo.simplerecycleview.simplebean.SimpleBean;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
    //刷新和加载数据时使用的list
    List newList;
    MRecycleviewAdapter mRecycleviewAdapter;
    List<MyBean> mList;
    @BindView(R.id.recycleview)
    SimpleRecycleview recycleview;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        if (newList == null) {
            newList = new ArrayList();
        }
        mList = new ArrayList<>();
        mRecycleviewAdapter = new MRecycleviewAdapter(mList, MainActivity.this);

        //设置item间距的方法
        recycleview.setItemDe((int) (MyUtils.getHeight(MainActivity.this) * 0.01));
        //自定义头部和底部的layout,里面需要传递一个view过去
        //  mRecycleviewAdapter.setHeadView(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null));
        // mRecycleviewAdapter.setFootView(LayoutInflater.from(MainActivity.this).inflate(R.layout.activity_main, null));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setAdapter(mRecycleviewAdapter);
        recycleview.setItemAnimator(new DefaultItemAnimator());
        DefaultItemAnimator animator = new DefaultItemAnimator();

        //设置刷新和加载的监听
        //使用时需注意，传入stopRefreshOrLoad（）的list，不能是adapter中的list，必须是一个其他的list
        recycleview.setOnScrollChanListener(new SimpleRecycleview.OnScrollChanListener() {
            @Override
            public void onRefresh() {
                //对于刷新和加载中传入的newlist可以是同一个list
                if (newList.size() > 0) {
                    newList.remove(newList.size() - 1);
                }
                recycleview.stopRefreshOrLoad(newList, false);

            }

            @Override
            public void loadMore() {
                newList.add(new MyBean());
                recycleview.stopRefreshOrLoad(newList, true);
            }

        });

    }
}
