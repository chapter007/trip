package com.zhangjie.trip.utils;

import android.content.Context;
import android.widget.Toast;

/**
 * Created by zhangjie on 2017/4/27.
 */

public class Utils {

    public static void makeToast(Context context,String text){
        Toast.makeText(context,text,Toast.LENGTH_SHORT).show();
    }
}
