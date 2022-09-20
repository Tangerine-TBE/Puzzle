package com.weilai.jigsawpuzzle.fragment.template;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.fragment.app.FragmentTransaction;

import com.alibaba.fastjson.JSONObject;
import com.bumptech.glide.Glide;
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

    private TemplateShowFragment() {

    }

    public static TemplateShowFragment getInstance(String info) {
        TemplateShowFragment templateShowFragment = new TemplateShowFragment();
        Bundle bundle = new Bundle();
        bundle.putString("bitmapInfo", info);
        templateShowFragment.setArguments(bundle);
        return templateShowFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.activity_template_show;
    }

    @Override
    protected void initView(View view) {
        BitMapInfo bitMapInfo = JSONObject.parseObject(getArguments().getString("bitmapInfo"),BitMapInfo.class);
        AppCompatImageView imageView = view.findViewById(R.id.iv_img);
        if (bitMapInfo.getBitmap().equals("模板1:1")){
            Glide.with(getContext()).load(R.mipmap.template_show_11).into(imageView);
        }else if (bitMapInfo.getBitmap().equals("模板3:2")){
            Glide.with(getContext()).load(R.mipmap.template_shhow_32).into(imageView);
        }else if (bitMapInfo.getBitmap().equals("角度模板1:1")){
            Glide.with(getContext()).load(R.mipmap.template_show_rorate_11).into(imageView);
        }else if (bitMapInfo.getBitmap().equals("角度模板3:2")){
            Glide.with(getContext()).load(R.mipmap.template_show_rorate_32).into(imageView);
        }
        
        
        view.findViewById(R.id.tv_confirm).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                TemplateEditFragment templateEditFragment = TemplateEditFragment.newInstance(getArguments().getString("bitmapInfo"));
                start(templateEditFragment);
            }
        });
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
      AppCompatTextView tvTitle =   view.findViewById(R.id.tv_title);
      tvTitle.setText(getString(R.string.make_template));
    }

    @Override
    protected void initListener(View view) {

    }
}
