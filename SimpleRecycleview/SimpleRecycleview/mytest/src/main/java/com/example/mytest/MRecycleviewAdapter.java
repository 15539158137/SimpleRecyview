package com.example.mytest;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

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
        if(viewType==1){
            View view= LayoutInflater.from(mContext).inflate(R.layout.item,null);
            RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height= (int) (MyUtils.getHeight(mContext)*0.1);
            view.setLayoutParams(layoutParams);
            return new MyViewHolder(view);
        }else {
        return super.onCreateViewHolder(parent, viewType);
        }
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
