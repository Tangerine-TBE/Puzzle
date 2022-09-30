package com.weilai.jigsawpuzzle.fragment.puzzleLPic;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.puzzleLP.ColorItemAdapter;
import com.weilai.jigsawpuzzle.adapter.puzzleLP.LongPicItemAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.bean.TabEntity;
import com.weilai.jigsawpuzzle.event.LpSortEvent;
import com.weilai.jigsawpuzzle.util.AssetsUtil;
import com.weilai.jigsawpuzzle.util.GlideEngine;
import com.weilai.jigsawpuzzle.weight.main.FlyTabLayout;
import com.weilai.jigsawpuzzle.weight.puzzleLP.PaddingItemDecoration;
import com.weilai.library.listener.CustomTabEntity;
import com.weilai.library.listener.OnTabSelectListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;


/**
 * * DATE: 2022/9/27
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleLongPicFragment extends BaseFragment implements OnTabSelectListener, SeekBar.OnSeekBarChangeListener {
    private String titles[] = {"边框", "添加", "排序"};
    private RecyclerView mRvLP;
    private RecyclerView mRvColor;
    private AppCompatSeekBar mFrameSeekBar;
    private LinearLayoutCompat mLayoutColor;
    private LinearLayoutCompat mLayoutFrame;
    private ArrayList<String> bitmaps;
    private static final int FILTER_PUZZLE_LP_CODE = 1;
    private LongPicItemAdapter longPicItemAdapter;

    private PuzzleLongPicFragment() {

    }

    public static PuzzleLongPicFragment getInstance(ArrayList<String> path) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", path);
        PuzzleLongPicFragment puzzleLongPicFragment = new PuzzleLongPicFragment();
        puzzleLongPicFragment.setArguments(bundle);
        return puzzleLongPicFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_puzzle_lp;
    }

    @Override
    protected void initView(View view) {
        mLayoutColor = view.findViewById(R.id.layout_color);
        mLayoutFrame = view.findViewById(R.id.ll_frame);
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
        mRvLP = view.findViewById(R.id.rv_lp);
        mRvColor = view.findViewById(R.id.rv_color);
        LinearLayoutManager picVerManager = new LinearLayoutManager(_mActivity);
        mRvLP.setLayoutManager(picVerManager);
        LinearLayoutManager colorHorManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL, true);
        mRvColor.setLayoutManager(colorHorManager);
        mFrameSeekBar = view.findViewById(R.id.frame_seekbar);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("拼长图");
        ArrayList<CustomTabEntity> entities = new ArrayList<>();
        for (String tile : titles) {
            TabEntity tabEntity = new TabEntity(tile);
            entities.add(tabEntity);
        }
        FlyTabLayout flyTabLayout = view.findViewById(R.id.tabLayout);
        flyTabLayout.setTabData(entities);
        flyTabLayout.setCurrentTab(0);
        flyTabLayout.setOnTabSelectListener(this);
        AppCompatTextView appCompatTextView = view.findViewById(R.id.tv_selected_color);
        //遍历所有的Bitmap
        assert getArguments() != null;
        bitmaps = getArguments().getStringArrayList("data");
        String colorStr = AssetsUtil.getAssertString(_mActivity, "color.json");
        JSONArray jsonArray = JSONArray.parseArray(colorStr);
        ColorItemAdapter colorItemAdapter = new ColorItemAdapter(_mActivity, jsonArray, new ColorItemAdapter.OnColorPickedListener() {
            @Override
            public void onColorPicked(String color) {
                appCompatTextView.setBackgroundColor(Color.parseColor(color));
                paddingItemDecoration.setBackground(color);
            }
        });
        mRvColor.setAdapter(colorItemAdapter);
        longPicItemAdapter = new LongPicItemAdapter(_mActivity, bitmaps);
        mRvLP.setAdapter(longPicItemAdapter);
        paddingItemDecoration = new PaddingItemDecoration();
        mRvLP.addItemDecoration(paddingItemDecoration);
    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
        mFrameSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0:
                mLayoutFrame.setVisibility(View.VISIBLE);
                mLayoutColor.setVisibility(View.VISIBLE);
                break;
            case 1:
                mLayoutColor.setVisibility(View.GONE);
                mLayoutFrame.setVisibility(View.GONE);
                PictureSelector.create(this).openGallery(SelectMimeType.ofImage())
                        .isDisplayCamera(true)
                        .setSelectionMode(SelectModeConfig.SINGLE)
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .forResult(FILTER_PUZZLE_LP_CODE);
                break;
            case 2:
                puzzleLPSortFragment = PuzzleLPSortFragment.getInstance(bitmaps);
                start(puzzleLPSortFragment);
                break;
            default:
                break;
        }
    }

    private PuzzleLPSortFragment puzzleLPSortFragment = null;

    @Override
    public void onTabReselect(int position) {

    }

    private int mCurrentProgress = 0;
    private PaddingItemDecoration paddingItemDecoration;

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser) {
            int offsetProgress = progress - mCurrentProgress;
            //向右滑动 0-5  5-8
            if (offsetProgress > 0) {
                paddingItemDecoration.setProcess(progress);
            } else if (offsetProgress < 0) {
                paddingItemDecoration.setProcess(progress);
            }
            mCurrentProgress = progress;
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSortChange(LpSortEvent event) {
        bitmaps = (ArrayList<String>) event.data;
        longPicItemAdapter.notifyDataSetChanged();
        puzzleLPSortFragment.pop();
        puzzleLPSortFragment = null;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == FILTER_PUZZLE_LP_CODE) {
            if (data != null) {
                ArrayList<LocalMedia> list = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
                if (list != null) {
                    int size = list.size();
                    if (size > 0) {
                        String path = list.get(0).getAvailablePath();
                        bitmaps.add(path);
                        longPicItemAdapter.notifyItemInserted(bitmaps.size());
                    }
                }
            }
        }
    }
}
