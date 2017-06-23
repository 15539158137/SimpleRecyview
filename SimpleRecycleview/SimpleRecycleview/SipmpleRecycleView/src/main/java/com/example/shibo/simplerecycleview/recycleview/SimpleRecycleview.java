package com.example.shibo.simplerecycleview.recycleview;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;

import com.example.shibo.simplerecycleview.simplebean.SimpleBean;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by shibo on 2017/6/22.
 */

public class SimpleRecycleview extends RecyclerView {
    public SimpleRecycleview(Context context) {
        super(context);
    }

    public SimpleRecycleview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public SimpleRecycleview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    float startPointY;
    //下拉刷新，上啦加载
    boolean isRefresh;
    boolean isLoadMore;
    //这个变量，表示已经得到刷新的状态了，不需要重复 了
    boolean isWorking;

    //对比的高度
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                startPointY = e.getY();
                isLoadMore = false;
                isRefresh = false;
                isWorking = false;
                break;
            case MotionEvent.ACTION_UP:
                SimpleRecycleviewAdater simpleRecycleviewAdater1 = (SimpleRecycleviewAdater) this.getAdapter();

                //如果是加载更多
                if (isLoadMore) {
                    isLoadMore = false;
                    isRefresh = false;
                    isWorking = false;
                    //删除最后一个item
                    simpleRecycleviewAdater1.mList.remove(simpleRecycleviewAdater1.mList.size() - 1);
                    this.getAdapter().notifyDataSetChanged();
                }
//刷新
                if (isRefresh) {
                    Log.e("检测到下啦1","下啦1");
                    isLoadMore = false;
                    isRefresh = false;
                    isWorking = false;
                    //删除最后一个item
                    simpleRecycleviewAdater1.mList.remove(0);
                    this.getAdapter().notifyDataSetChanged();
                }
                isLoadMore = false;
                isRefresh = false;
                isWorking = false;
                break;
            case MotionEvent.ACTION_MOVE:
                float nowY = e.getY();
                SimpleRecycleviewAdater simpleRecycleviewAdater = (SimpleRecycleviewAdater) this.getAdapter();
                //新的比老的大，在上啦，加载更多
                if ((nowY - startPointY) > MyUtils.getHeight(getContext()) * 0.1) {
                    //表示在下拉了
                    if (isWorking) {

                    } else {
                        Log.e("检测到下啦","下啦");
                        isWorking = true;
                        isLoadMore = false;
                        isRefresh = true;
                        Collections.reverse(simpleRecycleviewAdater.mList);
                        SimpleBean bean = new SimpleBean();
                        bean.setHeadOrFoot(true);
                        simpleRecycleviewAdater.mList.add(bean);
                        Collections.reverse(simpleRecycleviewAdater.mList);
                        this.getAdapter().notifyDataSetChanged();

                    }
                }

                //新的比老的小，在下啦，刷新
                if ((nowY - startPointY) < -MyUtils.getHeight(getContext()) * 0.1) {
                    //表示在上拉了
                    if (isWorking) {

                    } else {
                        Log.e("检测到上啦","上啦");
                        isWorking = true;
                        isLoadMore = true;
                        isRefresh = false;
                        //需要给list的底部添加数据了
                        //需要给list的底部添加数据了
                        SimpleBean bean = new SimpleBean();
                        bean.setHeadOrFoot(true);
                        simpleRecycleviewAdater.mList.add(bean);



                        this.getAdapter().notifyDataSetChanged();

                    }
                }
                break;
        }
        return super.onTouchEvent(e);
    }
}
