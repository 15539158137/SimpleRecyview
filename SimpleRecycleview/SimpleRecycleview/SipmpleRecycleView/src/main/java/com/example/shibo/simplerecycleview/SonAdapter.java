package com.example.shibo.simplerecycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.shibo.simplerecycleview.recycleview.MyUtils;
import com.example.shibo.simplerecycleview.recycleview.SimpleRecycleviewAdater;
import com.example.shibo.simplerecycleview.simplebean.SimpleBean;

import java.util.List;

/**
 * Created by shibo on 2017/6/22.
 */
/*
* Recycleview适配器，继承SimpleRecycleviewAdater
* */
public class SonAdapter extends SimpleRecycleviewAdater{

    public SonAdapter(List mList, Context mContext) {
        super(mList, mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        /*对于个人的viewhold需要写在viewType==1下*/
        if(viewType==1){
            View view= LayoutInflater.from(mContext).inflate(R.layout.item2,null);
            RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height= (int) (MyUtils.getHeight(mContext)*0.1);
            view.setLayoutParams(layoutParams);
            return new MyViewHold(view);
        }else {
        return super.onCreateViewHolder(parent, viewType);}
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if(mList.get(position).getBeanType()==1){
        MyViewHold myViewHold= (MyViewHold) holder;
        myViewHold.textView.setText("第"+position+"位置数据");}
    }

    class MyViewHold extends RecyclerView.ViewHolder{
        TextView textView;
        public MyViewHold(View itemView) {
            super(itemView);
            textView= (TextView) itemView.findViewById(R.id.text);
        }

    }
}
