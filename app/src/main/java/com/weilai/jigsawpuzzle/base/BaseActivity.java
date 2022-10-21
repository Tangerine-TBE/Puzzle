package com.weilai.jigsawpuzzle.base;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.LayoutRes;
import androidx.appcompat.app.AppCompatActivity;

import com.gyf.immersionbar.ImmersionBar;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.dialog.LoadingDialog;
import com.weilai.jigsawpuzzle.weight.ActionBar;

/**
 * Author : Gupingping
 * Date : 2019/1/17
 * QQ : 464955343
 * 普通activity
 * 需要actionBar时，返回true，默认false
 */
public abstract class BaseActivity extends AppCompatActivity {
    public Activity mContext;

    protected ImmersionBar mImmersionBar;
    //    protected DaoSession daoSession;
    protected LoadingDialog loadingDialog;//正在加载
    protected ActionBar actionBar;

    @Override
    public void setContentView(@LayoutRes int layoutResID) {
        super.setContentView(layoutResID);
        mContext = this;
    }

    @Override
    public void setContentView(View view) {
        super.setContentView(view);
        mContext = this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        loadingDialog = new LoadingDialog(this);
        loadingDialog.setCancelable(cancelDialog());
        if (isActionBar()) {
            setContentView(R.layout.activity_base);
            ((ViewGroup) findViewById(R.id.fl_content)).addView(getLayoutInflater().inflate(getLayoutId(), null));
            actionBar = findViewById(R.id.actionbar);
            actionBar.setVisibility(View.VISIBLE);
        } else {
            if (Build.VERSION.SDK_INT<Build.VERSION_CODES.M) {
                setContentView(R.layout.activity_base);
                ((ViewGroup) findViewById(R.id.fl_content)).addView(getLayoutInflater().inflate(getLayoutId(), null));
            }else {
                setContentView(getLayoutId());
            }
        }
        //沉浸式状态栏
        initImmersionBar(getStatusBarColor());
        initView();
        initData();
    }

    protected int getStatusBarColor() {
        return R.color.sel_text_main_color;
    }

    /**
     * 沉浸栏颜色
     */
    protected void initImmersionBar(int color) {
        mImmersionBar = ImmersionBar.with(this);
        if (color != 0) {
            mImmersionBar.statusBarColor(color);
        }
        mImmersionBar.init();
    }

    protected abstract int getLayoutId();


    public void showLoading() {
        if (loadingDialog != null) {
            loadingDialog.show();
        }
    }

    public void dismissLoading() {
        if (loadingDialog != null) {
            if (loadingDialog.isShowing())
                loadingDialog.dismiss();
        }
    }

    /**
     * 初始化View
     */
    protected abstract void initView();

    /**
     * 初始化数据
     */
    protected void initData() {
    }

    /**
     * 是否需要ActionBar
     */
    protected boolean isActionBar() {
        return false;
    }

    protected boolean cancelDialog() {
        return false;
    }

    public void visible(View... views) {
        for (View view : views) {
            view.setVisibility(View.VISIBLE);
        }
    }

    public void gone(View... views) {
        for(View view : views){
            view.setVisibility(View.GONE);
        }
    }

    public void invisible(View... views) {
        for (View view : views) {
            view.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

//    @Subscribe(threadMode = ThreadMode.MAIN)
//    public void receive(Event event) {
//
//    }

    protected void setTitleText(String title) {
        if (actionBar != null) {
            actionBar.setCenterText(title);
        }
    }
    protected void setTitleText(int title) {
        if (actionBar != null) {
            actionBar.setCenterText(title);
        }
    }

    protected void setShowBackImg(boolean shown) {
        if (actionBar != null) {
            actionBar.showBackImg(shown);
        }
    }

    protected void setTransparentActionBar() {
        if (actionBar != null) {
            actionBar.setBackgroundColor(getResources().getColor(R.color.transparent));
        }
    }

    protected void initTopTheme(boolean isBlack){
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .statusBarDarkFont(isBlack)
                .navigationBarColor(R.color.white)
                .init();
    }

    protected void initTopTheme(){
        ImmersionBar.with(this)
                .statusBarColor(R.color.white)
                .statusBarDarkFont(true)
                .navigationBarColor(R.color.white)
                .init();
    }
}