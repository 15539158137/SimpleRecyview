package com.example.mytest;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
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
    void clickAdd(){
mList.add(new MyBean());
        mRecycleviewAdapter.notifyDataSetChanged();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(MainActivity.this);
        mList=new ArrayList<>();
         mRecycleviewAdapter=new MRecycleviewAdapter(mList, MainActivity.this);
        if(recycleview==null){
            Log.e("Recycleview为空","==========");
        }
        recycleview.addItemDecoration(new SimpleRecycleviewItemDe((int) (MyUtils.getHeight(MainActivity.this)*0.05)));
        LinearLayoutManager linearLayoutManager=new LinearLayoutManager(this);
        recycleview.setLayoutManager(linearLayoutManager);
        recycleview.setAdapter(mRecycleviewAdapter);
    }
}
