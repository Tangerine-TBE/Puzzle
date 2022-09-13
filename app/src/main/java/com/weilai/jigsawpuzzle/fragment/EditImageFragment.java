package com.weilai.jigsawpuzzle.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import com.weilai.jigsawpuzzle.R;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description:修圖
 **/
public class EditImageFragment extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull  LayoutInflater inflater, @Nullable  ViewGroup container, @Nullable  Bundle savedInstanceState) {

        final View view = inflater.inflate(R.layout.fragment_edit_image,container,false);

        return view;
    }
}
