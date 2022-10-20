package com.weilai.jigsawpuzzle.fragment.main;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.activity.portrait.PortraitBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzle.PuzzleBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzle9P.Puzzle9PBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzleHLp.PuzzleHLPBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzleLP.PuzzleLPBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzleLine.PuzzleLineBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzleQr.PuzzleQrBaseActivity;
import com.weilai.jigsawpuzzle.activity.puzzleSS.PuzzleSShotBaseActivity;
import com.weilai.jigsawpuzzle.activity.special.SpecialBaseActivity;
import com.weilai.jigsawpuzzle.activity.template.TemplateBaseActivity;
import com.weilai.jigsawpuzzle.adapter.main.ImageBannerAdapter;
import com.weilai.jigsawpuzzle.util.GlideEngine;
import com.weilai.jigsawpuzzle.util.ImageCropEngine;
import com.youth.banner.Banner;
import com.youth.banner.config.IndicatorConfig;
import com.youth.banner.indicator.RectangleIndicator;

import java.util.ArrayList;
import java.util.Arrays;

/**
 * * DATE: 2022/9/13
 * * Author:tangerine
 * * Description:首页
 **/
public class CrossDressFragment extends Fragment implements View.OnClickListener {
    private static final int FILTER_PUZZLE_QR_CODE = 1;
    private static final int FILTER_PUZZLE_LP_CODE = 2;
    private static final int FILTER_PUZZLE_9P_CODE = 3;
    private static final int FILTER_PUZZLE_HLP_CODE = 4;
    private static final int FILTER_PUZZLE_LINE_CODE = 5;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_cross_dress, container, false);

        initUi(view);
        return view;
    }

    private void initUi(View view) {
        Banner<String, ImageBannerAdapter> banner = view.findViewById(R.id.banner);
        String[] strings = new String[]{"1", "2", "3"};//just
        banner.setAdapter(new ImageBannerAdapter(Arrays.asList(strings.clone())))
                .setIndicator(new RectangleIndicator(getContext()))
                .setIndicatorNormalWidth(15)
                .setIndicatorRadius(100)
                .setIndicatorHeight(15)
                .setIndicatorNormalColor(getResources().getColor(R.color.bg_cross_gray))
                .setIndicatorSelectedWidth(50)
                .setIndicatorMargins(new IndicatorConfig.Margins(0, 0, 0, 150));
        view.findViewById(R.id.tv_template).setOnClickListener(this);
        view.findViewById(R.id.tv_pic).setOnClickListener(this);
        view.findViewById(R.id.tv_splic_health).setOnClickListener(this);
        view.findViewById(R.id.tv_splic_hori).setOnClickListener(this);
        view.findViewById(R.id.tv_l_pic).setOnClickListener(this);
        view.findViewById(R.id.tv_lines).setOnClickListener(this);
        view.findViewById(R.id.tv_lattice).setOnClickListener(this);
        view.findViewById(R.id.tv_screen_shot).setOnClickListener(this);
        view.findViewById(R.id.iv_ai).setOnClickListener(this);
        view.findViewById(R.id.iv_comis).setOnClickListener(this);
        view.findViewById(R.id.iv_toning).setOnClickListener(this);


    }

    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.tv_template) {
            startActivity(new Intent(getContext(), TemplateBaseActivity.class));
        } else if (view.getId() == R.id.tv_pic) {
            startActivity(new Intent(getContext(), PuzzleBaseActivity.class));
        } else if (view.getId() == R.id.tv_splic_health) {
            PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .isDisplayCamera(true)
                    .setMaxSelectNum(4)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .setSelectionMode(SelectModeConfig.MULTIPLE)
                    .forResult(FILTER_PUZZLE_QR_CODE);
        } else if (view.getId() == R.id.tv_splic_hori) {
            PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .isDisplayCamera(true)
                    .setMinSelectNum(1)
                    .setMaxSelectNum(10)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .setSelectionMode(SelectModeConfig.MULTIPLE)
                    .forResult(FILTER_PUZZLE_HLP_CODE);
        } else if (view.getId() == R.id.tv_l_pic) {
            PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .isDisplayCamera(true)
                    .setMinSelectNum(1)
                    .setMaxSelectNum(10)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .setSelectionMode(SelectModeConfig.MULTIPLE)
                    .forResult(FILTER_PUZZLE_LP_CODE);
        } else if (view.getId() == R.id.tv_lines) {
            PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .isDisplayCamera(true)
                    .setMinSelectNum(1)
                    .setMaxSelectNum(10)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .setSelectionMode(SelectModeConfig.MULTIPLE)
                    .forResult(FILTER_PUZZLE_LP_CODE);
        } else if (view.getId() == R.id.tv_lattice) {
            PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .isDisplayCamera(true)
                    .setSelectionMode(SelectModeConfig.SINGLE)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .setCropEngine(new ImageCropEngine(1,1))
                    .forResult(FILTER_PUZZLE_9P_CODE);
        } else if (view.getId() == R.id.tv_screen_shot) {

            startActivity(new Intent(getActivity(), PuzzleSShotBaseActivity.class));
        }else if (view .getId() == R.id.iv_ai){
            Intent intent = new Intent();
            intent.putExtra("type","intelligent");
            intent.setClass(getContext(),PortraitBaseActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.iv_comis){
            Intent intent = new Intent();
            intent.putExtra("type","comic");
            intent.setClass(getContext(), SpecialBaseActivity.class);
            startActivity(intent);
        }else if (view.getId() == R.id.iv_toning){

        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (data != null) {
            ArrayList<LocalMedia> list = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
            if (list != null) {
                int size = list.size();
                if (size > 0) {
                    ArrayList<String> arrayList = new ArrayList<>();
                    for (LocalMedia localMedia : list) {
                        arrayList.add(localMedia.getAvailablePath());
                    }
                    Intent intent = new Intent();
                    intent.putStringArrayListExtra("data", arrayList);
                    if (requestCode == FILTER_PUZZLE_QR_CODE) {
                        intent.setClass(getContext(),PuzzleQrBaseActivity.class);
                    }else if (requestCode == FILTER_PUZZLE_LP_CODE){
                        intent.setClass(getContext(), PuzzleLPBaseActivity.class);
                    }else if (requestCode == FILTER_PUZZLE_9P_CODE){
                        intent.setClass(getContext(), Puzzle9PBaseActivity.class);
                    }else if (requestCode == FILTER_PUZZLE_HLP_CODE){
                        intent.setClass(getContext(), PuzzleHLPBaseActivity.class);
                    }else if (requestCode == FILTER_PUZZLE_LINE_CODE){
                        intent.setClass(getContext(), PuzzleLineBaseActivity.class);
                    }
                    startActivity(intent);
                }

            }
        }

    }
}
