package com.weilai.jigsawpuzzle.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.activity.template.TemplateActivity;
import com.weilai.jigsawpuzzle.adapter.ImageBannerAdapter;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;

import java.util.Arrays;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description:首页
 **/
public class CrossDressFragment extends Fragment implements View.OnClickListener {
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cross_dress,container,false);

        initUi(view);
        return view;
    }
    private void initUi(View view){
        Banner<String, ImageBannerAdapter> banner = view.findViewById(R.id.banner);
        String[] strings = new String[]{"1","2","3"};//just
        banner.setAdapter(new ImageBannerAdapter(Arrays.asList(strings.clone())))
                .setIndicator(new RectangleIndicator(getContext()))
                .setIndicatorNormalWidth(15)
                .setIndicatorRadius(100)
                .setIndicatorHeight(15)
                .setIndicatorNormalColor(getResources().getColor(R.color.bg_cross_gray))
                .setIndicatorSelectedWidth(50)
                .setIndicatorMargins(new IndicatorConfig.Margins(0,0,0,150));
         view.findViewById(R.id.tv_template).setOnClickListener(this);
         view.findViewById(R.id.tv_pic).setOnClickListener(this);
         view.findViewById(R.id.tv_splic_health).setOnClickListener(this);
         view.findViewById(R.id.tv_splic_hori).setOnClickListener(this);
         view.findViewById(R.id.tv_l_pic).setOnClickListener(this);
         view.findViewById(R.id.tv_lines).setOnClickListener(this);
         view.findViewById(R.id.tv_lattice).setOnClickListener(this);
         view.findViewById(R.id.tv_screen_shot).setOnClickListener(this);



    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_template){
            startActivity(new Intent(getContext(), TemplateActivity.class));
        }else if (view.getId() == R.id.tv_pic){

        }else if (view.getId() == R.id.tv_splic_health){

        }else if (view.getId() == R.id.tv_splic_hori){

        }else if (view.getId() == R.id.tv_l_pic){

        }else if (view.getId() == R.id.tv_lines){

        }else if (view.getId() == R.id.tv_lattice){

        }else if (view.getId() == R.id.tv_screen_shot){

        }
    }
}
