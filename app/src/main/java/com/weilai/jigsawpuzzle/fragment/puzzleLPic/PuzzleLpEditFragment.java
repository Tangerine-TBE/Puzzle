package com.weilai.jigsawpuzzle.fragment.puzzleLPic;

import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.widget.AppCompatTextView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.yalantis.ucrop.view.GestureCropImageView;

import java.io.File;

/**
 ** DATE: 2022/10/10
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleLpEditFragment extends BaseFragment {
    private PuzzleLpEditFragment (){

    }
    public static PuzzleLpEditFragment getInstance(String info){
        Bundle bundle =  new Bundle();
        bundle.putString("data",info);
        PuzzleLpEditFragment puzzleLpEditFragment = new PuzzleLpEditFragment();
        puzzleLpEditFragment.setArguments(bundle);
        return puzzleLpEditFragment;
    }
    @Override
    protected Object setLayout() {
        return R.layout.fragment_puzzle_lp_edit;
    }

    @Override
    protected void initView(View view) {
       String fileName=  getArguments().getString("data");
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("编辑");
    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
    }
}
