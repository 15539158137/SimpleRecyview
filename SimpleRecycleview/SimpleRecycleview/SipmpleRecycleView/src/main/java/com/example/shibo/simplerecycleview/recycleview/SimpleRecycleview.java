package com.example.shibo.simplerecycleview.recycleview;

import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.AbsListView;

import com.example.shibo.simplerecycleview.simplebean.SimpleBean;

import java.util.Collection;
import java.util.Collections;

/**
 * Created by shibo on 2017/6/22.
 */

public class SimpleRecycleview extends RecyclerView {
    OnScrollChanListener onScrollChanListener;

    public void setOnScrollChanListener(OnScrollChanListener onScrollChanListener) {
        this.onScrollChanListener = onScrollChanListener;
    }

    public interface OnScrollChanListener {
        void onRefresh();

        void loadMore();
    }

    //item之间的间距，这个属性必须拿到，他关系到空白区域填充item的个数
    int itemDeco;
    int needAddCount;//需要新增空白item的数量，在加载触发后通过returnNeesAddCount方法来计算。
    //单个item的高度，默认是头部的高度，即0.1*height；如果存在数据，这个高度就是item的高度了
    int itemHeight;

    public SimpleRecycleview(Context context) {
        super(context);
    }

    public SimpleRecycleview(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        this.addItemDecoration(new SimpleRecycleviewItemDe((int) (0.01 * MyUtils.getHeight(getContext()))));
    }

    public SimpleRecycleview(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public void setItemDe(int itemDeco) {
        this.itemDeco = itemDeco;
        this.addItemDecoration(new SimpleRecycleviewItemDe((int) (itemDeco)));
    }

    //判断是否滚动到顶部
    public boolean isToTop() {
        SimpleRecycleviewAdater simpleRecycleviewAdater1 = (SimpleRecycleviewAdater) this.getAdapter();
        if (simpleRecycleviewAdater1.mList.size() == 0) {
            return true;
        }
        LinearLayoutManager linearLayoutManager = (LinearLayoutManager) this.getLayoutManager();
        int position = linearLayoutManager.findFirstCompletelyVisibleItemPosition();
        if (position == 0) {
            return true;
        }
        return false;
    }

    //判断是否到底部
    public boolean isToBottom() {

        if (this.computeVerticalScrollExtent() + this.computeVerticalScrollOffset()
                >= this.computeVerticalScrollRange()) {
            return true;
        } else {
            return false;
        }
    }

    //对于recycleview的下拉事件，考虑到如果当前recycleview的数据部分不足以沾满屏幕，会出现下拉的加载框不在底部
    //这个方法直接返回需要填充多少个bean
    //还需要考虑间隔的问题
    private void retrunNeedAddCount() {
        int needReturn;
        //addItemDecoration

        //this.getChildAt(0)
        SimpleRecycleviewAdater simpleRecycleviewAdater1 = (SimpleRecycleviewAdater) this.getAdapter();
        Log.i("recycleview的总高度和单个的高度", this.getHeight() + "==" + MyUtils.getHeight(getContext()));
        int height = this.getHeight();
        if (simpleRecycleviewAdater1.mList.size() == 0) {
            //一个数据都没有，呢么就需要添加height/itemheiget个数据
            needReturn = ((height + itemDeco) / (itemHeight + itemDeco)) - 1;
        } else {
            //有数据了
            needReturn = (int) ((height - itemHeight * simpleRecycleviewAdater1.mList.size() + itemDeco)
                    / (0.1 * MyUtils.getHeight(getContext()) + itemDeco)) - 1;
        }
        needAddCount = needReturn;
        Log.i("最开始计算出来的个数", needAddCount + "==");
    }

    //这是对滑动事件的处理
    float startPointY;
    //下拉刷新，上啦加载
    boolean isRefresh;
    boolean isLoadMore;
    //这个变量，表示已经得到刷新的状态了，不需要重复 了
    boolean isWorking;

    public void stopRresh() {
        final SimpleRecycleviewAdater simpleRecycleviewAdater1 = (SimpleRecycleviewAdater) this.getAdapter();
        if (isRefresh) {
            final RecyclerView recyclerView=this;
            Log.e("走到刷新的方法","走到刷新的方法");
            ValueAnimator valueAnimator=ValueAnimator.ofInt(0,100);
            valueAnimator.setDuration(1000);
            valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
                @Override
                public void onAnimationUpdate(ValueAnimator animation) {
                    if((int)animation.getAnimatedValue()==100){
                        //删除数据
                         simpleRecycleviewAdater1.mList.remove(0);
                      recyclerView.getAdapter().notifyDataSetChanged();
                    }
                }
            });
            valueAnimator.start();
//            //删除最后一个item
           // simpleRecycleviewAdater1.mList.remove(0);
//            this.getAdapter().notifyDataSetChanged();
        }
    }

    public void stopLoadMore() {
        SimpleRecycleviewAdater simpleRecycleviewAdater1 = (SimpleRecycleviewAdater) this.getAdapter();
        if (isLoadMore) {
            Log.i("加载的抬起操作", "加载的抬起操作");
            //删除最后一个item
            simpleRecycleviewAdater1.mList.remove(simpleRecycleviewAdater1.mList.size() - 1);
            Log.i("加载抬起操作后的长度", simpleRecycleviewAdater1.mList.size() + "===");
            //还有resturnconunt个需要删除;用来删除空白的
//由于此时recycleview中已经被充满了空白item，再调用resturnNeedAdd方法 返回的count会是0，这样就出现错误
            Log.i("需要添加的数量", needAddCount + "==");
            for (int i = 0; i < needAddCount; i++) {
                simpleRecycleviewAdater1.mList.remove(simpleRecycleviewAdater1.mList.size() - 1);
            }
            Log.i("加载抬起操作后的长度1", simpleRecycleviewAdater1.mList.size() + "===1");
            this.getAdapter().notifyDataSetChanged();
        }
    }


    //对比的高度
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取下item的高度;设置itemheight的数值
                if (this.getChildAt(0) == null) {
                    Log.i("第一个儿子为空", "=====");
                    itemHeight = (int) (0.1 * MyUtils.getHeight(getContext()));
                } else {
                    itemHeight = this.getChildAt(0).getHeight();
                }


                startPointY = e.getY();
                isLoadMore = false;
                isRefresh = false;
                isWorking = false;
                break;
//           case MotionEvent.ACTION_UP:
////               isLoadMore = false;
////               isRefresh = false;
////               isWorking = false;
////
//                SimpleRecycleviewAdater simpleRecycleviewAdater1 = (SimpleRecycleviewAdater) this.getAdapter();
//                //如果是加载更多
//                if (isLoadMore) {
//                    Log.i("加载的抬起操作", "加载的抬起操作");
//                    isLoadMore = false;
//                    isRefresh = false;
//                    isWorking = false;
//                    //删除最后一个item
//                    simpleRecycleviewAdater1.mList.remove(simpleRecycleviewAdater1.mList.size() - 1);
//                    Log.e("加载抬起操作后的长度", simpleRecycleviewAdater1.mList.size() + "===");
//                    //还有resturnconunt个需要删除;用来删除空白的
////由于此时recycleview中已经被充满了空白item，再调用resturnNeedAdd方法 返回的count会是0，这样就出现错误
//                    Log.i("需要添加的数量", needAddCount + "==");
//                    for (int i = 0; i < needAddCount; i++) {
//                        simpleRecycleviewAdater1.mList.remove(simpleRecycleviewAdater1.mList.size() - 1);
//                    }
//                    Log.i("加载抬起操作后的长度1", simpleRecycleviewAdater1.mList.size() + "===1");
//                    this.getAdapter().notifyDataSetChanged();
//                }
////刷新
//                if (isRefresh) {
//                    Log.i("刷新的抬起操作", "刷新的抬起操作");
//                    isLoadMore = false;
//                    isRefresh = false;
//                    isWorking = false;
//                    //删除最后一个item
//                    simpleRecycleviewAdater1.mList.remove(0);
//                    this.getAdapter().notifyDataSetChanged();
//                }
//                isLoadMore = false;
//                isRefresh = false;
//                isWorking = false;
//               break;
            case MotionEvent.ACTION_MOVE:
                float nowY = e.getY();
                SimpleRecycleviewAdater simpleRecycleviewAdater = (SimpleRecycleviewAdater) this.getAdapter();
                Log.i("是不是到顶部", isToTop() + "==");
                //新的比老的大，在上啦，加载更多
                if ((nowY - startPointY) > MyUtils.getHeight(getContext()) * 0.1) {
                    //表示在下拉了
                    //下拉的同时，上啦无效
                    if (isWorking) {

                    } else {
                        if (isToTop()) {
                            Log.i("刷新的滑动操作", "刷新的滑动操作");
                            isWorking = true;
                            isLoadMore = false;
                            isRefresh = true;
                            Collections.reverse(simpleRecycleviewAdater.mList);
                            SimpleBean simpleBean = new SimpleBean();
                            SimpleBean bean = new SimpleBean();
                            bean.setBeanType(-1);
                            simpleRecycleviewAdater.mList.add(bean);
                            Collections.reverse(simpleRecycleviewAdater.mList);
                           this.getAdapter().notifyDataSetChanged();
//在这就去触发刷新的方法
                            if (onScrollChanListener == null) {
                            } else {
                               onScrollChanListener.onRefresh();
                            }
                        }
                    }
                }

                //新的比老的小，在下啦，刷新
                if ((nowY - startPointY) < -MyUtils.getHeight(getContext()) * 0.1) {
                    Log.i("是否滚动到底部了", isToBottom() + "==");
                    //表示在上拉了
                    if (isWorking) {

                    } else {
                        if (isToBottom() == true) {

                            Log.i("加载的滑动操作", "加载的滑动操作");
                            isWorking = true;
                            isLoadMore = true;
                            isRefresh = false;
                            //需要给list的底部添加数据了
                            //需要给list的底部添加数据了

                            retrunNeedAddCount();
                            Log.i("需要添加的数量前一个", needAddCount + "==");
                            for (int i = 0; i < needAddCount; i++) {
                                SimpleBean simpleBean = new SimpleBean();
                                simpleBean.setBeanType(0);
                                simpleRecycleviewAdater.mList.add(simpleBean);
                            }
                            SimpleBean bean = new SimpleBean();
                            bean.setBeanType(-2);
                            simpleRecycleviewAdater.mList.add(bean);

                            Log.i("加载滑动触发后的长度", simpleRecycleviewAdater.mList.size() + "===");
                            this.getAdapter().notifyDataSetChanged();
                            this.scrollToPosition(simpleRecycleviewAdater.mList.size() - 1);
                            //去触发加载的方法了
                            if (onScrollChanListener == null) {
                            } else {
                                onScrollChanListener.loadMore();
                            }
                        }
                    }
                }
                break;
        }
        return super.onTouchEvent(e);
    }
}
