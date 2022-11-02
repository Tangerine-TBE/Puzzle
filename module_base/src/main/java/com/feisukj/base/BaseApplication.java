package com.feisukj.base;

import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.os.Handler;
import android.os.Looper;
import androidx.multidex.MultiDex;

import com.feisukj.base.baseclass.ForegroundObserver;
import com.feisukj.base.util.PackageUtils;
import com.feisukj.base.util.SPUtil;
import com.umeng.commonsdk.UMConfigure;

/**
 * Author : Gupingping
 * Date : 2019/1/9
 * QQ : 464955343
 */
public class BaseApplication extends Application {
    public static BaseApplication application;
    public static Handler handler;
    public static boolean isForeground;
    public static boolean isFromStart;
    public static Context mContext;
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        application = this;
        mContext = this;
        //MultiDex分包方法 必须最先初始化
        if (Build.VERSION_CODES.LOLLIPOP>Build.VERSION.SDK_INT) {
            MultiDex.install(this);
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        handler = new Handler(Looper.getMainLooper());
        SPUtil.init(this);
        UMConfigure.preInit(application,"60fe3217328eac0d2eb6538c", PackageUtils.getAppMetaData(application, "CHANNEL"));
        ForegroundObserver.init(application);
    }

    public static BaseApplication getApplication() {
        return application;
    }


}

