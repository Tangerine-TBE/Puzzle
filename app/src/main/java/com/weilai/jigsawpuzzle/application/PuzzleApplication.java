package com.weilai.jigsawpuzzle.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.multidex.MultiDexApplication;

import com.weilai.jigsawpuzzle.configure.Config;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description:For Application AndroidManifest
 **/
public class PuzzleApplication  extends MultiDexApplication {
    public static Handler handler;
    public static boolean isForeground;
    public static boolean isFromStart;
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        Config.init(this).withSp(this).withApplication(this).Configure();
    }



}
