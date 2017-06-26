package com.example.shibo.simplerecycleview;

import com.example.shibo.simplerecycleview.simplebean.SimpleBean;

/**
 * Created by shibo on 2017/6/22.
 */
/*
* 使用，需继承自SimpleBean
* */
public class SonBean extends SimpleBean{
    String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
