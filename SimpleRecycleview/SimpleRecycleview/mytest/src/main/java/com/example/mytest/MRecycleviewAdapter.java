package com.example.mytest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shibo.simplerecycleview.SonAdapter;
import com.example.shibo.simplerecycleview.recycleview.MyUtils;
import com.example.shibo.simplerecycleview.recycleview.SimpleRecycleviewAdater;

import java.util.List;

/**
 * Created by Administrator on 2017/6/23.
 */

public class MRecycleviewAdapter extends SimpleRecycleviewAdater{
    public MRecycleviewAdapter(List mList, Context mContext) {
        super(mList, mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*重写该方法，在viewType等于1的时候，写入自己的viewholder*/
        if (viewType == 1) {
            //这里返回你自己的viewholder
            View view = LayoutInflater.from(mContext).inflate(R.layout.item, null);
            RecyclerView.LayoutParams layoutParams = new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height = (int) (MyUtils.getHeight(mContext) * 0.1);
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        } else {
            return super.onCreateViewHolder(parent, viewType);
        }
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (mList.get(position).getBeanType() == 1) {
            //其他照抄，下面的数据绑定写自己的
            MyViewHolder myViewHold = (MyViewHolder) holder;
            myViewHold.textView.setText("第" + position + "位置数据");
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView textView;
        public MyViewHolder(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(com.example.shibo.simplerecycleview.R.id.text);
        }
    }
}
