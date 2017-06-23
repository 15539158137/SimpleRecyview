package com.example.shibo.simplerecycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shibo.simplerecycleview.recycleview.MyUtils;
import com.example.shibo.simplerecycleview.recycleview.SimpleRecycleviewAdater;
import com.example.shibo.simplerecycleview.simplebean.SimpleBean;

import java.util.List;

/**
 * Created by shibo on 2017/6/22.
 */

public class SonAdapter extends SimpleRecycleviewAdater{

    public SonAdapter(List mList, Context mContext) {
        super(mList, mContext);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==1){
            View view= LayoutInflater.from(mContext).inflate(R.layout.item2,null);
            RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height= (int) (MyUtils.getHeight(mContext)*0.1);
            view.setLayoutParams(layoutParams);
            return new myViewHold(view);
        }else {
        return super.onCreateViewHolder(parent, viewType);}
    }
    class myViewHold extends RecyclerView.ViewHolder{
        public myViewHold(View itemView) {
            super(itemView);
        }
    }
}
