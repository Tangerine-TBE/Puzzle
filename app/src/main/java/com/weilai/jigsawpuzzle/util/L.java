package com.weilai.jigsawpuzzle.util;

import android.os.Build;
import android.util.Log;

import com.weilai.jigsawpuzzle.BuildConfig;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description: Debug log
 **/
public class L {
    private  static final String TAG = "WeiLai";

    public static void e (String msg){
        if (BuildConfig.DEBUG){
            Log.e(TAG, msg);
        }
    }
}
