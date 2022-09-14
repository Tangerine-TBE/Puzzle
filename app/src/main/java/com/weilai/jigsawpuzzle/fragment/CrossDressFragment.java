package com.weilai.jigsawpuzzle.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.ImageBannerAdapter;
import com.youth.banner.Banner;

import java.util.Arrays;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description:首页
 **/
public class CrossDressFragment extends Fragment {
    private Banner<String,ImageBannerAdapter> banner;
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cross_dress,container,false);
        banner = view.findViewById(R.id.banner);
        String[] strings = new String[]{"1","2","3"};
        banner.setAdapter(new ImageBannerAdapter(Arrays.asList(strings.clone())));
        return view;
    }
}
