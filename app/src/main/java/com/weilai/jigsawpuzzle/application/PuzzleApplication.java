package com.weilai.jigsawpuzzle.application;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import androidx.multidex.MultiDexApplication;

import com.feisukj.ad.SplashActivity;
import com.feisukj.ad.SplashActivityAD;
import com.feisukj.base.ActivityEntrance;
import com.feisukj.base.BaseApplication;
import com.weilai.jigsawpuzzle.activity.main.MainBaseActivity;
import com.weilai.jigsawpuzzle.configure.Config;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description:For Application AndroidManifest
 **/
public class PuzzleApplication  extends BaseApplication {
    public static Handler handler;
    public static boolean isForeground;
    public static boolean isFromStart;
    public static Context mContext;
    @Override
    public void onCreate() {
        super.onCreate();
        ActivityEntrance.HomeActivity.setCls(MainBaseActivity.class);
        ActivityEntrance.SplashActivityAD.setCls(SplashActivityAD.class);
        ActivityEntrance.SplashActivity.setCls(SplashActivity.class);
            handler = new Handler(Looper.getMainLooper());
        Config.init(this).withSp(this).withDao(this).withApplication(this).withUMeng(this).Configure();
    }



}
