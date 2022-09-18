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
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * * DATE: 2022/9/13
 * * Author:tangerine
 * * Description: BaseSimpleActivity
 **/
public abstract class BaseSimpleActivity extends AppCompatActivity {
    protected abstract Object setLayout();
    protected abstract void setStatusBar();
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
            }
            super.onCreate(savedInstanceState);
            setContentView(view);
            initView(view);
            initListener(view);
            return;
        }
        throw new NullPointerException("Resource by " + this.getLocalClassName() + " is null");
    }
    /**
     ** DATE: 2022/9/14
     ** Author:tangerine
     ** Description:For change the statusBar
     **/
    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        setStatusBar();
    }
}
