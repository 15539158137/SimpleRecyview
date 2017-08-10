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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * Created by shibo on 2017/6/22.
 */

public class SimpleRecycleview extends RecyclerView {
    private boolean haveNewData;//刷新和加载后是否有数据，有数据就加载，没数据就删除因为刷新和加载而增加的数据
    //设置数据
    List<SimpleBean> mList;
    Handler mHandle;
    //停止动画指令过来时候的当前时间
    private long needStopTime;
    //当前系统时间
    private long currentTime;

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
    //开始刷新的动画
    public void refreshAni() {
        isRefreshNow=true;
        currentTime = System.currentTimeMillis();
    }

    //开始加载的动画
    public void loadMoreAni() {
        isRefreshNow=false;
        currentTime = System.currentTimeMillis();
    }
    //由于遇到动画在某些情况下不能正常运行（提前终止），加入两外一个状态的处理，如果动画结束了，呢么就用handle去删除数据
    boolean isRefreshNow;//现在是否是正在刷新的状态，这个变量用来区分如何删除数据
    public void stopRefreshOrLoad(List mList, boolean haveNewData) {
        this.haveNewData = haveNewData;
        //需要关闭
        needStopTime = System.currentTimeMillis();
        if(haveNewData){
            this.mList = mList;
        }
        //开始去清除因为刷新和加载而新增的数据
        deleteTheAddData();

    }
    /*删除因为刷新和加载而新增的虚拟数据*/
    private void deleteTheAddData(){
        if(isRefreshNow){
            if ((needStopTime - currentTime) / 1000 < 2) {
//小于两秒,等待2秒
                if((needStopTime - currentTime) / 1000 < 0.5){
                    mHandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {;
                            // 0-0.5
                            deleteTheRefreshData();
                        }
                    }, 1500);
                }else if((needStopTime - currentTime) / 1000 < 1&&(needStopTime - currentTime) / 1000 >=0.5){
                    mHandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //0.5-1
                            deleteTheRefreshData();
                        }
                    }, 1000);
                } else if((needStopTime - currentTime) / 1000 >= 1&&(needStopTime - currentTime) / 1000 <1.5){
                    mHandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //1-1.5
                            deleteTheRefreshData();
                        }
                    }, 500);
                }else {
                    //1.5-2
                    deleteTheRefreshData();
                }

            } else {
                //2
                deleteTheRefreshData();
            }
        }else {
            //需要去关闭
            if ((needStopTime - currentTime) / 1000 < 2) {
                if((needStopTime - currentTime) / 1000 < 0.5){
                    mHandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // 0-0.5
                            deleteLoadMore();
                        }
                    }, 1500);
                }else if((needStopTime - currentTime) / 1000 < 1&&(needStopTime - currentTime) / 1000 >=0.5){
                    mHandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //0.5-1
                            deleteLoadMore();
                        }
                    }, 1000);
                } else if((needStopTime - currentTime) / 1000 >= 1&&(needStopTime - currentTime) / 1000 <1.5){
                    mHandle.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            //1-1.5
                            deleteLoadMore();
                        }
                    }, 500);
                }else {
                    //大于1.5
                    deleteLoadMore();
                }

            } else {
                //大于等于2秒
                deleteLoadMore();
            }
        }
    }
    /*删除加载的数据的方法*/
    private void deleteLoadMore(){
        RecyclerView recyclerView = this;
        SimpleRecycleviewAdater simpleRecycleviewAdater1 = (SimpleRecycleviewAdater) this.getAdapter();
        //删除数据
        if (haveNewData) {
            simpleRecycleviewAdater1.mList.clear();
            //设置现在可以加载数据了
            MyUtils.CanLoadDatas=true;
            simpleRecycleviewAdater1.mList.addAll(mList);
            haveNewData = false;
        } else {
            //删除最后一个item
            simpleRecycleviewAdater1.mList.remove(simpleRecycleviewAdater1.mList.size() - 1);
            //设置现在可以加载数据了
            MyUtils.CanLoadDatas=true;
            //还有resturnconunt个需要删除;用来删除空白的
            for (int i = 0; i < needAddCount; i++) {
                simpleRecycleviewAdater1.mList.remove(simpleRecycleviewAdater1.mList.size() - 1);
            }
        }
        recyclerView.getAdapter().notifyDataSetChanged();
        needStopTime = 0;
        currentTime = 0;
        //设置当前可以进行新的刷新和加载了
        isRefreshOrLoading=false;

    }
    /*删除刷新而增加的数据的方法*/
    private void deleteTheRefreshData(){
        RecyclerView recyclerView = this;
        SimpleRecycleviewAdater simpleRecycleviewAdater1 = (SimpleRecycleviewAdater) this.getAdapter();
        //删除数据
        if (haveNewData) {
            haveNewData = false;
            simpleRecycleviewAdater1.mList.clear();
            simpleRecycleviewAdater1.mList.addAll(mList);
            //设置现在可以加载数据了
            MyUtils.CanLoadDatas=true;
        } else {
            simpleRecycleviewAdater1.mList.remove(0);
            //设置现在可以加载数据了
            MyUtils.CanLoadDatas=true;
        }
        recyclerView.getAdapter().notifyDataSetChanged();
        currentTime = 0;
        needStopTime = 0;
        //设置当前可以进行新的刷新和加载了
        isRefreshOrLoading=false;

    }

    //给一个变量，这个变量表示：当前recycleview是否正在刷新或者加载，如果正在进行刷新或者加载，呢么就不去触犯新的刷新和加载，直到刷新和加载的动画结束或者用handle去清除
    boolean isRefreshOrLoading;
    //对比的高度
    @Override
    public boolean onTouchEvent(MotionEvent e) {
        super.onTouchEvent(e);
        if(isRefreshOrLoading){
            return false;
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //获取下item的高度;设置itemheight的数值
                if (this.getChildAt(0) == null) {
                    itemHeight = (int) (0.1 * MyUtils.getHeight(getContext()));
                } else {
                    itemHeight = this.getChildAt(0).getHeight();
                }
                startPointY = e.getY();
                break;
            //  case MotionEvent.ACTION_UP:

            // break;
            case MotionEvent.ACTION_MOVE:
                float nowY = e.getY();
                SimpleRecycleviewAdater simpleRecycleviewAdater = (SimpleRecycleviewAdater) this.getAdapter();
                //先判断到顶了
                //新的比老的大，在上啦，加载更多
                if (isToTop()) {
                    if ((nowY - startPointY) > MyUtils.getHeight(getContext()) * 0.07) {
                        //表示在下拉了
                        //下拉的同时，上啦无效
                        if (isRefreshOrLoading) {
                            //正在加载数据中，不能再做其他操作了
                        } else {
                            //设置现在不能加载数据
                            MyUtils.CanLoadDatas=false;
                            isRefreshOrLoading=true;
                            //吧数据存储下，然后再设置，否则会很卡
                            List tempList=new ArrayList();
                            tempList.addAll(simpleRecycleviewAdater.mList);
                            Collections.reverse(tempList);
                            SimpleBean bean = new SimpleBean();
                            bean.setBeanType(-1);
                            bean.setShowMessage("刷新中...");
                            tempList.add(bean);
                            Collections.reverse(tempList);

                            simpleRecycleviewAdater.mList.clear();
                            simpleRecycleviewAdater.mList.addAll(tempList);
                            this.getAdapter().notifyDataSetChanged();
                            this.scrollToPosition(0);
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
                    if ((nowY - startPointY) < -MyUtils.getHeight(getContext()) * 0.07) {
                        Log.i("是否滚动到底部了", isToBottom() + "==");
                        if (isRefreshOrLoading) {
                            //正在加载数据中，不能再做其他操作了
                        } else {
                            //设置现在不能加载数据
                            MyUtils.CanLoadDatas=false;
                            isRefreshOrLoading = true;
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
