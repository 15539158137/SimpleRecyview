package com.example.shibo.simplerecycleview.recycleview;

import android.content.Context;
import android.view.WindowManager;

/**
 * Created by shibo on 2017/6/22.
 */

public class MyUtils {
    public static int screenHeight;
    public static boolean CanLoadDatas=true;
    public static  int getHeight(Context context){
    WindowManager wm = (WindowManager)
            context.getSystemService(Context.WINDOW_SERVICE);
   int widthScreen = wm.getDefaultDisplay().getHeight();
    return  widthScreen;
    }
}
