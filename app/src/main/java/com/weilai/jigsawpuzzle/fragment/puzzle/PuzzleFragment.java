package com.weilai.jigsawpuzzle.fragment.puzzle;

import android.content.Intent;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.util.GlideEngine;

import java.util.ArrayList;

/**
 * * DATE: 2022/9/20
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleFragment extends BaseFragment implements View.OnClickListener {
    private static final int FILTER_CODE = 1;

    private PuzzleFragment() {

    }

    public static PuzzleFragment getInstance() {
        return new PuzzleFragment();
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_puzzle;
    }

    @Override
    protected void initView(View view) {
        view.findViewById(R.id.tv_add).setOnClickListener(this);
    }

    @Override
    protected void initListener(View view) {


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_add) {
            //add 9 Max
            PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .isDisplayCamera(true)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .setMaxSelectNum(9)
                    .setSelectionMode(SelectModeConfig.MULTIPLE)
                    .forResult(FILTER_CODE);

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_CODE) {
            /*Check how many pics did i select and choose  the right puzzle template*/
            if (data != null){
                ArrayList<LocalMedia> list = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
                if (list != null) {
                    int size = list.size();
                    if (size > 0){

                    }
                }
            }
        }
    }
}
