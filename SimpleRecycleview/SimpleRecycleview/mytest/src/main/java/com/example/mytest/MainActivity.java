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
import com.example.shibo.simplerecycleview.simplebean.SimpleBean;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {
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
        if(newList==null){
            newList=new ArrayList();
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
        //设置刷新和加载的监听
        //使用时需注意，传入stopRefreshOrLoad（）的list，不能是adapter中的list，必须是一个其他的list
        recycleview.setOnScrollChanListener(new SimpleRecycleview.OnScrollChanListener() {
            @Override
            public void onRefresh() {
                Log.e("刷新的方法", "刷新的方法");
                if(newList.size()>0){
                    newList.remove(newList.size()-1);
                }
                recycleview.stopRefreshOrLoad(newList);

            }

            @Override
            public void loadMore() {
                Log.e("加载的方法", "加载的方法");
                newList.add(new MyBean());
                recycleview.stopRefreshOrLoad(newList);
            }

            @Override
            public void onTimeOut() {
                Log.e("刷新时间超时","刷新超时");
            }
        });

    }
}
