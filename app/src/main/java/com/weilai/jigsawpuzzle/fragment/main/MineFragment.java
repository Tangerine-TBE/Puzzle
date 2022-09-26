package com.weilai.jigsawpuzzle.fragment.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.weilai.jigsawpuzzle.R;

/**
 ** DATE: 2022/9/26
 ** Author:tangerine
 ** Description:
 **/
public class MineFragment  extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {
        final View view  = inflater.inflate(R.layout.fragment_mine,container,false);
        return view;
    }
}
