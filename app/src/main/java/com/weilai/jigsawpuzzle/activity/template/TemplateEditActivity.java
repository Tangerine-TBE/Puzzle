package com.weilai.jigsawpuzzle.activity.template;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.View;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseActivity;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;
import com.weilai.jigsawpuzzle.weight.template.TemplateEditView;

/**
 ** DATE: 2022/9/14
 ** Author:tangerine
 ** Description:1.After Show the template
 **/
public class TemplateEditActivity extends BaseActivity {
    private Bitmap mBitmap;
    private TemplateEditView templateEditView;
    @Override
    protected Object setLayout() {
        Intent intent = getIntent();
        byte[] buff =intent.getByteArrayExtra("template");
        mBitmap = BitmapFactory.decodeByteArray(buff, 0, buff.length);
        return R.layout.activity_template_edit;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(TemplateEditActivity.this,0,null);
    }

    @Override
    protected void initView(View view) {
        templateEditView = view.findViewById(R.id.icon);
        templateEditView.computeBitmap(mBitmap);
    }
    @Override
    protected void initListener(View view) {

    }
}
