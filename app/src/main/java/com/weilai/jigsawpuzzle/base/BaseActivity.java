package com.weilai.jigsawpuzzle.base;

import android.app.Activity;
import android.content.pm.ActivityInfo;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.weilai.jigsawpuzzle.R;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * * DATE: 2022/9/13
 * * Author:tangerine
 * * Description: BaseActivity
 **/
public abstract class BaseActivity extends AppCompatActivity {
    protected abstract Object setLayout();

    protected abstract void initView(View view);

    protected abstract void initListener(View view);

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        final View view;
        if (setLayout() != null) {
            if (setLayout() instanceof View) {
                view = (View) setLayout();
            } else if (setLayout() instanceof Integer) {
                view = LayoutInflater.from(this).inflate((Integer) setLayout(), null, false);
            } else {
                throw new RuntimeException("the Activity can't access kind of view");
            }setTitle();
            initStatusBar();
            super.onCreate(savedInstanceState);
            setContentView(view);
            initView(view);
            initListener(view);
            return;
        }
        throw new NullPointerException("Resource by " + this.getLocalClassName() + " is null");
    }

    private void initStatusBar() {

        Window window = getWindow();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            View decorView = window.getDecorView();
            int option = View.SYSTEM_UI_FLAG_FULLSCREEN
                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
            decorView.setSystemUiVisibility(option);
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.TRANSPARENT);
        } else {
            WindowManager.LayoutParams layoutParams = window.getAttributes();
            int flagTranslucentStatus = WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS;
            layoutParams.flags |= flagTranslucentStatus;
            window.setAttributes(layoutParams);

        }

//        ViewGroup rootView = getWindow().getDecorView().findViewById(android.R.id.content);
//        rootView.setPadding(0,getStatusBarHeight(),);
//    private int getStatusBarHeight(){
//        int result = 0;
//        int resourceId = getResources().getIdentifier("status_bar_height","dimen","android");
//        if (resourceId > 0){
//            result = getResources().getDimensionPixelOffset(resourceId);
//        }
//        return result;
//    }
    }
    public void setTitle() {
        if (MIUISetStatusBarLightMode(this, true) || FlymeSetStatusBarLightMode(this, true)) {
            //设置状态栏为指定颜色
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {//5.0
                getWindow().setStatusBarColor(getResources().getColor(R.color.trans));
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {//4.4
                //调用修改状态栏颜色的方法
                WindowManager.LayoutParams localLayoutParams = getWindow().getAttributes();
                localLayoutParams.flags = (WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS | localLayoutParams.flags);
            }
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //如果是6.0以上将状态栏文字改为黑色
            getWindow().setStatusBarColor(getResources().getColor(R.color.trans));

        }
    }

    //    检查魅族系统
    public static boolean FlymeSetStatusBarLightMode(Activity activity, boolean darkmode) {
        boolean result = false;
        try {
            WindowManager.LayoutParams lp = activity.getWindow().getAttributes();
            Field darkFlag = WindowManager.LayoutParams.class
                    .getDeclaredField("MEIZU_FLAG_DARK_STATUS_BAR_ICON");
            Field meizuFlags = WindowManager.LayoutParams.class
                    .getDeclaredField("meizuFlags");
            darkFlag.setAccessible(true);
            meizuFlags.setAccessible(true);
            int bit = darkFlag.getInt(null);
            int value = meizuFlags.getInt(lp);
            if (darkmode) {
                value |= bit;
            } else {
                value &= ~bit;
            }
            meizuFlags.setInt(lp, value);
            activity.getWindow().setAttributes(lp);
            result = true;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return result;
    }

    //检查小米系统
    public static boolean MIUISetStatusBarLightMode(Activity activity, boolean darkmode) {
        boolean result = false;
        Class<? extends Window> clazz = activity.getWindow().getClass();
        try {
            int darkModeFlag = 0;
            Class<?> layoutParams = Class.forName("android.view.MiuiWindowManager$LayoutParams");
            Field field = layoutParams.getField("EXTRA_FLAG_STATUS_BAR_DARK_MODE");
            darkModeFlag = field.getInt(layoutParams);
            Method extraFlagField = clazz.getMethod("setExtraFlags", int.class, int.class);
            extraFlagField.invoke(activity.getWindow(), darkmode ? darkModeFlag : 0, darkModeFlag);
            result = true;
        } catch (Exception e) {
//            e.printStackTrace();
        }
        return result;
    }
}
