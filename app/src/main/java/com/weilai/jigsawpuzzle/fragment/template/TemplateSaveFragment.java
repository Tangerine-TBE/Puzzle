package com.weilai.jigsawpuzzle.fragment.template;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.ScrollView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.ScreenShotUtil;
import com.weilai.jigsawpuzzle.weight.template.TemplateView;
import com.weilai.jigsawpuzzle.weight.template.TemplateViewInfo;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.util.LinkedList;
import java.util.List;

/**
 ** DATE: 2022/9/20
 ** Author:tangerine
 ** Description:
 **/
public class TemplateSaveFragment extends BaseFragment {
    private TemplateSaveFragment() {

    }

    public static TemplateSaveFragment getInstance(String s) {
        TemplateSaveFragment templateSaveFragment = new TemplateSaveFragment();
        Bundle bundle = new Bundle();
        bundle.putString("filePath", s);
        templateSaveFragment.setArguments(bundle);
        return templateSaveFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_template_save;
    }

    @Override
    protected void initView(View view) {
//        view.findViewById(R.id.rv_show);//是否显示图片信息
//        view.findViewById(R.id.tv_big);//尺寸
//        view.findViewById(R.id.tv_create_date);//创建时间
        AppCompatTextView appCompatTextView = view.findViewById(R.id.tv_title);
        appCompatTextView.setText(R.string.bitmap_info);
        view.findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        AppCompatTextView tvSave = view.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setText(R.string.share);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.visible);//是否显示详细信息
        String path = getArguments().getString("filePath");
        ImageView imageView = view.findViewById(R.id.iv_img);
        TextView textView = view.findViewById(R.id.tv_mapInfo);//本地路径

        Glide.with(this).load(new File(path)).into(imageView).getSize(new SizeReadyCallback() {
            @Override
            public void onSizeReady(int width, int height) {
                textView.setText(String.format("图片尺寸:%d*%d", width, height));

            }
        });

    }


    @Override
    protected void initListener(View view) {

    }
}
