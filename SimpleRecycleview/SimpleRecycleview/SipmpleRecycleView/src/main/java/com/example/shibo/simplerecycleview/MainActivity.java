package com.example.shibo.simplerecycleview;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.View;
import android.widget.Button;

import com.example.shibo.simplerecycleview.recycleview.MyUtils;
import com.example.shibo.simplerecycleview.recycleview.SimpleRecycleview;
import com.example.shibo.simplerecycleview.recycleview.SimpleRecycleviewAdater;
import com.example.shibo.simplerecycleview.recycleview.SimpleRecycleviewItemDe;
import com.example.shibo.simplerecycleview.simplebean.SimpleBean;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    List<SimpleBean> list;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Button button = (Button) findViewById(R.id.button);

        list = new ArrayList<>();

        SimpleRecycleview recycleview = (SimpleRecycleview) findViewById(R.id.recycleview);
        //设置item间距的方法
       // recycleview.setItemDe((int) (0.05*MyUtils.getHeight(MainActivity.this)));

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recycleview.setLayoutManager(linearLayoutManager);
        final SonAdapter adapter = new SonAdapter(list, getApplicationContext());
        recycleview.setAdapter(adapter);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (int i = 0; i < 10; i++) {
                    list.add(new SonBean());
                }
                adapter.notifyDataSetChanged();
            }
        });

    }
}
