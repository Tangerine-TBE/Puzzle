package com.weilai.jigsawpuzzle.activity.template;

import android.content.Intent;
import android.text.TextUtils;
import android.view.View;

import com.alibaba.fastjson.JSONObject;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseActivity;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;
import com.weilai.jigsawpuzzle.weight.template.BitMapInfo;
import com.weilai.jigsawpuzzle.weight.template.TemplateView;

/**
 ** DATE: 2022/9/14
 ** Author:tangerine
 ** Description:1.After Show the template
 **/
public class TemplateEditActivity extends BaseActivity implements TemplateView.OutRectClickListener {
    private TemplateView templateEditView;
    private BitMapInfo bitMapInfo;
    @Override
    protected Object setLayout() {
        Intent intent = getIntent();
        String json = intent.getStringExtra("bitmapInfo");
        if (!TextUtils.isEmpty(json)){
             bitMapInfo = JSONObject.parseObject(json,BitMapInfo.class);
        }

        return R.layout.activity_template_edit;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(TemplateEditActivity.this,0,null);
    }

    @Override
    protected void initView(View view) {
        templateEditView = view.findViewById(R.id.icon);
        templateEditView.setTemplateBitmap(bitMapInfo);
    }
    @Override
    protected void initListener(View view) {
        templateEditView.setOutRectClickListener(this);
    }

    @Override
    public void onRectClick(int position,boolean hasPic) {
        if (hasPic){
            //show dialog
        }else{
            //startToCameraActivity To select a pic

        }
    }
}
