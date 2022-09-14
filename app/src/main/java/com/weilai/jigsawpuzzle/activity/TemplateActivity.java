package com.weilai.jigsawpuzzle.activity;

import android.view.View;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseActivity;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

/**
 * @description:
 * @author: luo
 * @date: 2022/9/14
 */
public class TemplateActivity extends BaseActivity {
    @Override
    protected Object setLayout() {
        return R.layout.activity_template;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(TemplateActivity.this,0,null);
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initListener(View view) {

    }
}
