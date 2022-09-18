package com.weilai.jigsawpuzzle.fragment.template;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSONObject;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseSimpleActivity;
import com.weilai.jigsawpuzzle.fragment.template.TemplateEditFragment;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * * DATE: 2022/9/14
 * * Author:tangerine
 * * Description: @TemplateItemFragment's RecyclerViewItem click join this Activity
 **/
public class TemplateShowFragment extends BaseFragment {
    private AppCompatTextView mTvConfirm;

    private TemplateShowFragment() {

    }

    public static TemplateShowFragment getInstance(String info) {
        TemplateShowFragment templateShowFragment = new TemplateShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("xxx", info);
        templateShowFragment.setArguments(bundle);
        return templateShowFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.activity_template_show;
    }

    @Override
    protected void initView(View view) {
        AppCompatImageView imageView = view.findViewById(R.id.iv_img);
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.drawable.test);
//
        FileUtil.saveBitmapToCache("template", bitmap);
        BitMapInfo bitMapInfo = new BitMapInfo();
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        bitMapInfo.setBitmap("template");
        BitMapInfo.SizeInfo sizeInfo1 = new BitMapInfo.SizeInfo(0.16f, 0.23f, 0, 0.23f, 0.14f);
        BitMapInfo.SizeInfo sizeInfo2 = new BitMapInfo.SizeInfo(0.53f, 0.23f, 0, 0.23f, 0.14f);
        List<BitMapInfo.SizeInfo> sizeInfos = new ArrayList<>();
        sizeInfos.add(sizeInfo1);
        sizeInfos.add(sizeInfo2);
        bitMapInfo.setSizeInfos(sizeInfos);
        imageView.setImageBitmap(bitmap);
        String jsonString = JSONObject.toJSONString(bitMapInfo);
//
        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TemplateEditFragment templateEditFragment = TemplateEditFragment.newInstance(jsonString);
                start(templateEditFragment);
            }
        });
    }

    @Override
    protected void initListener(View view) {

    }
}
