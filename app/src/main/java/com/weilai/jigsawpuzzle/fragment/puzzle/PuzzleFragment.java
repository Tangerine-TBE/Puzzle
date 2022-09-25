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
import com.luck.picture.lib.utils.ToastUtils;
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
        view.findViewById(R.id.tv_21).setOnClickListener(this);
        view.findViewById(R.id.tv_22).setOnClickListener(this);
        view.findViewById(R.id.tv_23).setOnClickListener(this);
        view.findViewById(R.id.tv_24).setOnClickListener(this);
        view.findViewById(R.id.tv_211).setOnClickListener(this);
        view.findViewById(R.id.tv_212).setOnClickListener(this);
        view.findViewById(R.id.tv_213).setOnClickListener(this);
        view.findViewById(R.id.tv_214).setOnClickListener(this);
        view.findViewById(R.id.tv_31).setOnClickListener(this);
        view.findViewById(R.id.tv_32).setOnClickListener(this);
        view.findViewById(R.id.tv_33).setOnClickListener(this);
        view.findViewById(R.id.tv_34).setOnClickListener(this);
        view.findViewById(R.id.tv_311).setOnClickListener(this);
        view.findViewById(R.id.tv_312).setOnClickListener(this);
        view.findViewById(R.id.tv_313).setOnClickListener(this);
        view.findViewById(R.id.tv_314).setOnClickListener(this);
        view.findViewById(R.id.tv_321).setOnClickListener(this);
        view.findViewById(R.id.tv_322).setOnClickListener(this);
        view.findViewById(R.id.tv_323).setOnClickListener(this);
        view.findViewById(R.id.tv_324).setOnClickListener(this);
        view.findViewById(R.id.tv_41).setOnClickListener(this);
        view.findViewById(R.id.tv_42).setOnClickListener(this);
        view.findViewById(R.id.tv_43).setOnClickListener(this);
        view.findViewById(R.id.tv_44).setOnClickListener(this);
        view.findViewById(R.id.tv_411).setOnClickListener(this);
        view.findViewById(R.id.tv_412).setOnClickListener(this);
        view.findViewById(R.id.tv_413).setOnClickListener(this);
        view.findViewById(R.id.tv_414).setOnClickListener(this);
        view.findViewById(R.id.tv_51).setOnClickListener(this);
        view.findViewById(R.id.tv_52).setOnClickListener(this);
        view.findViewById(R.id.tv_53).setOnClickListener(this);
        view.findViewById(R.id.tv_54).setOnClickListener(this);
        view.findViewById(R.id.tv_511).setOnClickListener(this);
        view.findViewById(R.id.tv_512).setOnClickListener(this);
        view.findViewById(R.id.tv_513).setOnClickListener(this);
        view.findViewById(R.id.tv_514).setOnClickListener(this);
        view.findViewById(R.id.tv_521).setOnClickListener(this);
        view.findViewById(R.id.tv_522).setOnClickListener(this);
        view.findViewById(R.id.tv_523).setOnClickListener(this);
        view.findViewById(R.id.tv_524).setOnClickListener(this);
        view.findViewById(R.id.tv_531).setOnClickListener(this);
        view.findViewById(R.id.tv_532).setOnClickListener(this);
        view.findViewById(R.id.tv_533).setOnClickListener(this);
        view.findViewById(R.id.tv_534).setOnClickListener(this);
        view.findViewById(R.id.tv_61).setOnClickListener(this);
        view.findViewById(R.id.tv_62).setOnClickListener(this);
        view.findViewById(R.id.tv_63).setOnClickListener(this);
        view.findViewById(R.id.tv_64).setOnClickListener(this);
        view.findViewById(R.id.tv_611).setOnClickListener(this);
        view.findViewById(R.id.tv_612).setOnClickListener(this);
        view.findViewById(R.id.tv_613).setOnClickListener(this);
        view.findViewById(R.id.tv_614).setOnClickListener(this);
        view.findViewById(R.id.tv_621).setOnClickListener(this);
        view.findViewById(R.id.tv_622).setOnClickListener(this);
        view.findViewById(R.id.tv_623).setOnClickListener(this);
        view.findViewById(R.id.tv_624).setOnClickListener(this);
        view.findViewById(R.id.tv_71).setOnClickListener(this);
        view.findViewById(R.id.tv_72).setOnClickListener(this);
        view.findViewById(R.id.tv_73).setOnClickListener(this);
        view.findViewById(R.id.tv_74).setOnClickListener(this);
        view.findViewById(R.id.tv_711).setOnClickListener(this);
        view.findViewById(R.id.tv_712).setOnClickListener(this);
        view.findViewById(R.id.tv_713).setOnClickListener(this);
        view.findViewById(R.id.tv_714).setOnClickListener(this);
        view.findViewById(R.id.tv_721).setOnClickListener(this);
        view.findViewById(R.id.tv_81).setOnClickListener(this);
        view.findViewById(R.id.tv_82).setOnClickListener(this);
        view.findViewById(R.id.tv_83).setOnClickListener(this);
        view.findViewById(R.id.tv_84).setOnClickListener(this);
        view.findViewById(R.id.tv_811).setOnClickListener(this);
        view.findViewById(R.id.tv_812).setOnClickListener(this);
        view.findViewById(R.id.tv_813).setOnClickListener(this);
        view.findViewById(R.id.tv_814).setOnClickListener(this);
        view.findViewById(R.id.tv_821).setOnClickListener(this);
        view.findViewById(R.id.tv_822).setOnClickListener(this);
        view.findViewById(R.id.tv_91).setOnClickListener(this);
        view.findViewById(R.id.tv_92).setOnClickListener(this);
        view.findViewById(R.id.tv_93).setOnClickListener(this);
        view.findViewById(R.id.tv_94).setOnClickListener(this);
        view.findViewById(R.id.tv_911).setOnClickListener(this);
        view.findViewById(R.id.tv_912).setOnClickListener(this);
        view.findViewById(R.id.tv_913).setOnClickListener(this);
        view.findViewById(R.id.tv_914).setOnClickListener(this);
    }

    @Override
    protected void initListener(View view) {


    }

    @Override
    public void onClick(View v) {
        int minNum =2;
        int maxNum =2;
        switch (v.getId()){
            case R.id.tv_add:
                maxNum = 9;
                break;
            case R.id.tv_21:

                maxNum = 2;

                break;
            default:
                break;
        }
        PictureSelector.create(this)
                .openGallery(SelectMimeType.ofImage())
                .isDisplayCamera(true)
                .setImageEngine(GlideEngine.createGlideEngine())
                .isPreviewImage(true)
                .setMaxSelectNum(maxNum)
                .setMinSelectNum(minNum)
                .setSelectionMode(SelectModeConfig.MULTIPLE)
                .forResult(FILTER_CODE);
    }

    private int type = -1;
    private int theme = -1;

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_CODE) {
            /*Check how many pics did i select and choose  the right puzzle template*/
            if (data != null) {
                ArrayList<LocalMedia> list = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
                if (list != null) {
                    int size = list.size();
                    if (size >= 2) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        for (LocalMedia localMedia : list) {
                            arrayList.add(localMedia.getAvailablePath());
                        }
                        PuzzleEditFragment puzzleEditFragment = PuzzleEditFragment.getInstance(size, type, theme, arrayList);
                        start(puzzleEditFragment);
                    } else {
                        ToastUtils.showToast(getContext(), "必须选择两张或以上的照片!");
                    }
                }
            }
        }
    }
}
