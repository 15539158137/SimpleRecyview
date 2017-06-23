package com.example.shibo.simplerecycleview.simplebean;

/**
 * Created by shibo on 2017/6/22.
 */

public class SimpleBean {
   //0表示是空白的  -1，-2表示是头和尾，1表示是正常的
    int beanType;

    public int getBeanType() {
        return beanType;
    }

    public void setBeanType(int beanType) {
        this.beanType = beanType;
    }

    public SimpleBean() {
        this.beanType = 1;
    }
}
