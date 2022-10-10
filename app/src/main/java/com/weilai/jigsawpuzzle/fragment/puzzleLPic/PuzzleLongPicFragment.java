package com.weilai.jigsawpuzzle.fragment.puzzleLPic;

import android.content.Intent;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.luck.picture.lib.utils.ToastUtils;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.puzzleLP.LongPicItemAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.bean.TabEntity;
import com.weilai.jigsawpuzzle.dialog.puzzleLP.PuzzleLpColorPopUp;
import com.weilai.jigsawpuzzle.dialog.puzzleLP.PuzzleLpPopUp;
import com.weilai.jigsawpuzzle.event.LpSortEvent;
import com.weilai.jigsawpuzzle.event.LpSplitEvent;
import com.weilai.jigsawpuzzle.util.GlideEngine;
import com.weilai.jigsawpuzzle.weight.main.FlyTabLayout;
import com.weilai.jigsawpuzzle.weight.puzzleLP.PaddingItemDecoration;
import com.weilai.library.listener.CustomTabEntity;
import com.weilai.library.listener.OnTabSelectListener;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;


/**
 * * DATE: 2022/9/27
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleLongPicFragment extends BaseFragment implements OnTabSelectListener, PuzzleLpPopUp.OnPopUpDismiss, PuzzleLpColorPopUp.OnColorChangedListener {
    private final String[] titles = {"边框", "添加", "排序"};
    private final int[] titlesIcon = new int[]{R.mipmap.icon_lp_frame, R.mipmap.icon_add, R.mipmap.icon_sort};
    private RecyclerView mRvLP;

    private ArrayList<String> bitmaps;
    private static final int FILTER_PUZZLE_LP_CODE = 1;
    private static final int FILTER_PUZZLE_LP_SINGLE = 2;
    private LongPicItemAdapter longPicItemAdapter;
    private PuzzleLpPopUp mPuzzleLpPopUp;
    private float mActionBarHeight;
    private FlyTabLayout mFlyTabLayout;
    private PuzzleLpColorPopUp mPuzzleLpColor;
    private final int[] integers = new int[]{R.mipmap.icon_split_top, R.mipmap.icon_split_bottom, R.mipmap.icon_edit, R.mipmap.icon_replace, R.mipmap.icon_delete};

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

    private PaddingItemDecoration paddingItemDecoration;

    @Override
    protected void initView(View view) {
        String[] tipsTitle = new String[]{getString(R.string.split_top), getString(R.string.split_bottom), getString(R.string.edit), getString(R.string.replace), getString(R.string.delete)};
        mPuzzleLpPopUp = new PuzzleLpPopUp(_mActivity, this, tipsTitle, integers);
        mPuzzleLpColor = new PuzzleLpColorPopUp(_mActivity, this);
        mPuzzleLpPopUp.setOutSideDismiss(true);
        mPuzzleLpPopUp.setOutSideTouchable(true);
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
        mRvLP = view.findViewById(R.id.rv_lp);
        LinearLayoutManager picVerManager = new LinearLayoutManager(_mActivity);
        mRvLP.setLayoutManager(picVerManager);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("拼长图");
        ArrayList<CustomTabEntity> entities = new ArrayList<>();
        for (int i = 0; i < titles.length; i++) {
            TabEntity tabEntity = new TabEntity(titles[i], titlesIcon[i], titlesIcon[i]);
            entities.add(tabEntity);
        }
        mFlyTabLayout = view.findViewById(R.id.tabLayout);
        mFlyTabLayout.setTabData(entities);
        mFlyTabLayout.setCurrentTab(0);
        mFlyTabLayout.setOnTabSelectListener(this);
        //遍历所有的Bitmap
        assert getArguments() != null;
        bitmaps = getArguments().getStringArrayList("data");
        longPicItemAdapter = new LongPicItemAdapter(_mActivity, bitmaps);
        mRvLP.setAdapter(longPicItemAdapter);
        paddingItemDecoration = new PaddingItemDecoration();
        mRvLP.addItemDecoration(paddingItemDecoration);
        TypedArray actionbarSizeTypedArray = _mActivity.obtainStyledAttributes(new int[]{
                android.R.attr.actionBarSize
        });
        mActionBarHeight = actionbarSizeTypedArray.getDimension(0, 0);
        actionbarSizeTypedArray.recycle();
    }

    @Override
    protected void initListener(View view) {
        mRvLP.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                mPuzzleLpPopUp.setVisibility();
                if (longPicItemAdapter != null) {
                    longPicItemAdapter.resetItem();
                }
                super.onScrolled(recyclerView, dx, dy);
            }
        });
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
        longPicItemAdapter.setOnItemClickedListener(new LongPicItemAdapter.OnItemClickedListener() {
            @Override
            public void onItemClicked(View itemView, int position) {
                int rvLp = mRvLP.getHeight();
                if (mPuzzleLpPopUp.isShowing()) {
                    mPuzzleLpPopUp.dismiss();
                }
                if (itemView.getTop() >= mActionBarHeight) {
                    mPuzzleLpPopUp.show(itemView, true, position);
                } else if (itemView.getBottom() <= rvLp - mActionBarHeight) {
                    mPuzzleLpPopUp.show(itemView, false, position);
                } else {
                    mPuzzleLpPopUp.show(mRvLP, false, position);
                }
            }
        });
    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0:
                //show
                mPuzzleLpColor.show(mFlyTabLayout, false);
                break;
            case 1:
                if (bitmaps.size() < 10) {
                    PictureSelector.create(this).openGallery(SelectMimeType.ofImage())
                            .isDisplayCamera(true)
                            .setMaxSelectNum(10 - bitmaps.size())
                            .setSelectionMode(SelectModeConfig.MULTIPLE)
                            .setImageEngine(GlideEngine.createGlideEngine())
                            .forResult(FILTER_PUZZLE_LP_CODE);
                } else {
                    Toast.makeText(_mActivity, "不能在添加照片了", Toast.LENGTH_SHORT).show();
                }

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
    private PuzzleLpSplitFragment puzzleLpSplitFragment = null;

    @Override
    public void onTabReselect(int position) {
        onTabSelect(position);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSortChange(LpSortEvent event) {
        bitmaps = (ArrayList<String>) event.data;
        longPicItemAdapter.notifyDataSetChanged();
        puzzleLPSortFragment.pop();
        puzzleLPSortFragment = null;
    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDataSplitChange(LpSplitEvent event) {
        ArrayList<String> data = (ArrayList<String>) event.data;
        int type = event.type;
        if (type == 1){
            bitmaps.remove(selectedPosition);
            bitmaps.add(selectedPosition,data.get(0));
        }else if (type == 2){
            bitmaps.remove(selectedPosition);
            bitmaps.add(selectedPosition,data.get(0));
            if (selectedPosition +1 < bitmaps.size()) {
                bitmaps.remove(selectedPosition + 1);
                bitmaps.add(selectedPosition+1,data.get(1));
            }
        }
        longPicItemAdapter.notifyDataSetChanged();
        puzzleLpSplitFragment.pop();
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
                        for (LocalMedia localMedia : list) {
                            String path = localMedia.getAvailablePath();
                            bitmaps.add(path);
                            longPicItemAdapter.notifyItemInserted(bitmaps.size());
                        }
                    }
                }
            }
        } else if (requestCode == FILTER_PUZZLE_LP_SINGLE) {
            if (data != null) {
                ArrayList<LocalMedia> list = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
                if (list != null) {
                    int size = list.size();
                    if (size > 0) {
                        String path = list.get(0).getAvailablePath();
                        bitmaps.remove(selectedPosition);
                        bitmaps.add(selectedPosition, path);
                        longPicItemAdapter.notifyItemChanged(selectedPosition);
                    }
                }
            }
        }
    }

    @Override
    public void dismiss() {
        longPicItemAdapter.resetItem();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        if (hidden) {
            mPuzzleLpPopUp.dismiss();
        }
        super.onHiddenChanged(hidden);
    }

    private int selectedPosition = -1;

    @Override
    public void clicked(View view, int position) {
        selectedPosition = position;
        longPicItemAdapter.resetItem();
        mPuzzleLpPopUp.dismiss();
        ArrayList<String> bitmapsInfo = new ArrayList<>();
        switch (view.getId()) {
            case 0:
                //裁顶
                if (selectedPosition == 0) {
                    bitmapsInfo.add(bitmaps.get(selectedPosition));
                    puzzleLpSplitFragment = PuzzleLpSplitFragment.getInstance(bitmapsInfo, 1);
                    start(puzzleLpSplitFragment);
                }
                break;
            case 1:
                bitmapsInfo.add(bitmaps.get(selectedPosition));
                if (selectedPosition + 1 < bitmaps.size()) {
                    bitmapsInfo.add(bitmaps.get(selectedPosition + 1));
                }
                puzzleLpSplitFragment = PuzzleLpSplitFragment.getInstance(bitmapsInfo, 2);
                start(puzzleLpSplitFragment);
                //裁底
                break;
            case 2:
                //编辑
                break;
            case 3:
                //替换

                PictureSelector.create(this).openGallery(SelectMimeType.ofImage())
                        .isDisplayCamera(true)
                        .setSelectionMode(SelectModeConfig.SINGLE)
                        .setImageEngine(GlideEngine.createGlideEngine())
                        .forResult(FILTER_PUZZLE_LP_SINGLE);

                break;
            case 4:
                bitmaps.remove(position);
                longPicItemAdapter.notifyDataSetChanged();
                //删除
                break;
        }

    }


    @Override
    public void onProcessed(int position) {
        paddingItemDecoration.setProcess(position);
    }

    @Override
    public void onColorChanged(String color) {
        paddingItemDecoration.setBackground(color);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mPuzzleLpPopUp.onDestroy();
        mPuzzleLpColor.onDestroy();
    }
}
