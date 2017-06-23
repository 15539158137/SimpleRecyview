package com.example.shibo.simplerecycleview.recycleview;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.shibo.simplerecycleview.R;
import com.example.shibo.simplerecycleview.simplebean.SimpleBean;

import java.util.List;

/**
 * Created by shibo on 2017/6/22.
 */

public class SimpleRecycleviewAdater extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    public List<SimpleBean> mList;
    public Context mContext;

    public void setHeadView(int headID){

    }
    public SimpleRecycleviewAdater(List mList, Context mContext) {
        this.mList = mList;
        this.mContext = mContext;
    }

    @Override
    public int getItemViewType(int position) {
        if(mList.get(position).isHeadOrFoot()==true){
            //是头尾
            if(position==0){
                //是头
                return -1;
            }else {

                //是尾巴
                return -2;
            }
        }else {
            //是普通的
            return 1;
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType==-1){
            //返回头
            View view= LayoutInflater.from(mContext).inflate(R.layout.head,null);
            RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height= (int) (MyUtils.getHeight(mContext)*0.1);
            view.setLayoutParams(layoutParams);
            return new SimpleViewHolder(view);
        }else if(viewType==-2){
            //返回尾巴
            View view= LayoutInflater.from(mContext).inflate(R.layout.head,null);
            RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height= (int) (MyUtils.getHeight(mContext)*0.1);
            view.setLayoutParams(layoutParams);
            return new SimpleViewHolder(view);
        }else {
            View view= LayoutInflater.from(mContext).inflate(R.layout.item,null);
            RecyclerView.LayoutParams layoutParams=new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
            layoutParams.height= (int) (MyUtils.getHeight(mContext)*0.1);
            view.setLayoutParams(layoutParams);
            return new SimpleViewHolder(view);
        }

    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return mList.size();
    }
    class SimpleViewHolder extends RecyclerView.ViewHolder{
        public SimpleViewHolder(View itemView) {
            super(itemView);
        }
    }
}
