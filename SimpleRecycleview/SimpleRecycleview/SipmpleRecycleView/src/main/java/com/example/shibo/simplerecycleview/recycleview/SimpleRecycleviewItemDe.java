package com.example.shibo.simplerecycleview.recycleview;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Administrator on 2017/6/23.
 */

public class SimpleRecycleviewItemDe extends RecyclerView.ItemDecoration{
    private int space;

    public SimpleRecycleviewItemDe(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left = space;
        outRect.right = space;
        outRect.bottom = space;
        if(parent.getChildPosition(view) != 0){
            outRect.top = space;
        }else {
            outRect.top=space/2;
        }
    }
}
