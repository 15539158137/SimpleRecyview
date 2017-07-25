package com.example.shibo.simplerecycleview.recycleview;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.animation.Animation;
import android.widget.AbsListView;
import android.widget.Toast;

import com.example.shibo.simplerecycleview.simplebean.SimpleBean;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by shibo on 2017/6/22.
 */

public class SimpleRecycleview extends RecyclerView {
    private boolean haveNewData;//刷新和加载后是否有数据
    private boolean isNeedDelete;//对于刷新状态的结束，使用anima.cancle；由于存在事件有没有两秒，所以需要加这个字段来判断是否真的需要让这个动画end
    //设置数据
    List<SimpleBean> mList;
    Handler mHandle;
    //停止动画指令过来时候的当前时间
    private long needStopTime;

    //当前系统时间
    private long currentTime;
    //这个是动画
    private ValueAnimator valueAnimator;


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
        mHandle = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
            }
        };
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
    }

    //这是对滑动事件的处理
    float startPointY;
    //下拉刷新，上啦加载
    boolean isRefresh;
    boolean isLoadMore;
    //这个变量，表示已经得到刷新的状态了，不需要重复 了
    boolean isWorking;

    //开始刷新的动画
    public void refreshAni() {
        currentTime = System.currentTimeMillis();
        final SimpleRecycleviewAdater simpleRecycleviewAdater1 = (SimpleRecycleviewAdater) this.getAdapter();
        final RecyclerView recyclerView = this;
        valueAnimator = ValueAnimator.ofInt(0, 20000);
        valueAnimator.setDuration(20000);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //删除数据
                if (isNeedDelete) {
                    if (haveNewData) {
                        haveNewData = false;
                        simpleRecycleviewAdater1.mList.clear();
                        simpleRecycleviewAdater1.mList.addAll(mList);
                    } else {
                        simpleRecycleviewAdater1.mList.remove(0);
                    }
                    recyclerView.getAdapter().notifyDataSetChanged();
                    currentTime = 0;
                    needStopTime = 0;
                    isNeedDelete = false;
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                if ((needStopTime - currentTime) / 1000 < 2) {
                    //小于两秒,等待2秒
                    mHandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isNeedDelete = true;
                            valueAnimator.end();

                        }
                    }, 1500);
                    isNeedDelete = false;
                } else {
                    isNeedDelete = true;
                    valueAnimator.end();

                }

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();


    }

    //开始加载的动画
    public void loadMoreAni() {
        currentTime = System.currentTimeMillis();
        final SimpleRecycleviewAdater simpleRecycleviewAdater1 = (SimpleRecycleviewAdater) this.getAdapter();

        final RecyclerView recyclerView = this;
        valueAnimator = ValueAnimator.ofInt(0, 20000);
        valueAnimator.setDuration(20000);
        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                //删除数据
                if (isNeedDelete) {
                    if (haveNewData) {
                        simpleRecycleviewAdater1.mList.clear();
                        simpleRecycleviewAdater1.mList.addAll(mList);
                        haveNewData = false;
                    } else {
                        //删除最后一个item
                        simpleRecycleviewAdater1.mList.remove(simpleRecycleviewAdater1.mList.size() - 1);
                        //还有resturnconunt个需要删除;用来删除空白的
                        for (int i = 0; i < needAddCount; i++) {
                            Log.e("正在删除时候的长度", simpleRecycleviewAdater1.mList.size() + "第" + i + "次");
                            simpleRecycleviewAdater1.mList.remove(simpleRecycleviewAdater1.mList.size() - 1);
                        }
                    }

                    recyclerView.getAdapter().notifyDataSetChanged();


                    needStopTime = 0;
                    currentTime = 0;
                    isNeedDelete = false;
                }

            }

            @Override
            public void onAnimationCancel(Animator animation) {
                //需要去关闭
                if ((needStopTime - currentTime) / 1000 < 2) {
//小于两秒,等待2秒
                    isNeedDelete = false;
                    mHandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            isNeedDelete = true;
                            valueAnimator.end();
                        }
                    }, 1500);

                } else {
                    isNeedDelete = true;
                    valueAnimator.end();
                }
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        valueAnimator.start();

    }

    public void stopRefreshOrLoad(List mList, boolean haveNewData) {
        this.haveNewData = haveNewData;
        //需要关闭
        needStopTime = System.currentTimeMillis();
        if(haveNewData){

            this.mList = mList;
        }
        valueAnimator.cancel();


    }

    //对比的高度
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);
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
            //  case MotionEvent.ACTION_UP:

            // break;
            case MotionEvent.ACTION_MOVE:
                //防止和recycleview自带的拖动出现冲突，加入对是否是用户滚动而到顶部或者是本来就在顶部的判断
                //但是如果数据只有一页，呢么开始的时候就是在顶部和底部，对于用户是希望拖动还是滑动刷新还是不能进行判断
                //所以当是下拉刷新或者上啦加载的recycleview暂不能进行拖拽的处理
                float nowY = e.getY();
                SimpleRecycleviewAdater simpleRecycleviewAdater = (SimpleRecycleviewAdater) this.getAdapter();
                Log.i("是不是到顶部", isToTop() + "==");
                //先判断到顶了
                //新的比老的大，在上啦，加载更多
                if (isToTop()) {
                    if ((nowY - startPointY) > MyUtils.getHeight(getContext()) * 0.1) {
                        //表示在下拉了
                        //下拉的同时，上啦无效
                        if (isWorking) {

                        } else {

                            isWorking = true;
                            isLoadMore = false;
                            isRefresh = true;
                            Collections.reverse(simpleRecycleviewAdater.mList);
                            SimpleBean simpleBean = new SimpleBean();
                            SimpleBean bean = new SimpleBean();
                            bean.setBeanType(-1);
                            bean.setShowMessage("刷新中...");
                            simpleRecycleviewAdater.mList.add(bean);
                            Collections.reverse(simpleRecycleviewAdater.mList);
                            this.getAdapter().notifyDataSetChanged();
                            this.scrollToPosition(0);
                            Log.e("刷新时候的长度", simpleRecycleviewAdater.mList.size() + "===");
                            refreshAni();
//在这就去触发刷新的方法
                            if (onScrollChanListener == null) {
                            } else {
                                onScrollChanListener.onRefresh();
                            }
                        }
                    }
                }

                //新的比老的小，在下啦，刷新
                if (isToBottom() == true) {
                    if ((nowY - startPointY) < -MyUtils.getHeight(getContext()) * 0.1) {
                        Log.i("是否滚动到底部了", isToBottom() + "==");
                        //表示在上拉了
                        if (isWorking) {

                        } else {

                            Log.i("加载的滑动操作", "加载的滑动操作");
                            isWorking = true;
                            isLoadMore = true;
                            isRefresh = false;
                            //需要给list的底部添加数据了
                            //需要给list的底部添加数据了
                            retrunNeedAddCount();
                            Log.e("需要添加的数量前一个", needAddCount + "==");
                            for (int i = 0; i < needAddCount; i++) {
                                SimpleBean simpleBean = new SimpleBean();
                                simpleBean.setBeanType(0);
                                simpleRecycleviewAdater.mList.add(simpleBean);
                            }
                            SimpleBean bean = new SimpleBean();
                            bean.setBeanType(-2);
                            simpleRecycleviewAdater.mList.add(bean);
                            Log.e("加载滑动触发后的长度", simpleRecycleviewAdater.mList.size() + "===");
                            this.getAdapter().notifyDataSetChanged();
                            this.scrollToPosition(simpleRecycleviewAdater.mList.size() - 1);
                            loadMoreAni();
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
