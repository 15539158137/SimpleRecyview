package com.example.shibo.simplerecycleview.simplebean;

/**
 * Created by shibo on 2017/6/22.
 */

public class SimpleBean {
    boolean isHeadOrFoot;

    public boolean isHeadOrFoot() {
        return isHeadOrFoot;
    }

    public void setHeadOrFoot(boolean headOrFoot) {
        isHeadOrFoot = headOrFoot;
    }

    public SimpleBean() {
        this.isHeadOrFoot=false;
    }
}
