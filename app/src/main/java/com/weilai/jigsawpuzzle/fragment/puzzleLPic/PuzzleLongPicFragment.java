package com.weilai.jigsawpuzzle.fragment.puzzleLPic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.puzzleLP.ColorItemAdapter;
import com.weilai.jigsawpuzzle.adapter.puzzleLP.LongPicItemAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.bean.TabEntity;
import com.weilai.jigsawpuzzle.util.AssetsUtil;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.weight.main.FlyTabLayout;
import com.weilai.jigsawpuzzle.weight.puzzleLP.PaddingItemDecoration;
import com.weilai.library.listener.CustomTabEntity;
import com.weilai.library.listener.OnTabSelectListener;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

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
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
        mRvLP = view.findViewById(R.id.rv_lp);
        mRvColor = view.findViewById(R.id.rv_color);
        LinearLayoutManager picVerManager = new LinearLayoutManager(_mActivity);
        mRvLP.setLayoutManager(picVerManager);
        LinearLayoutManager colorHorManager = new LinearLayoutManager(_mActivity, LinearLayoutManager.HORIZONTAL,true);
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
        ArrayList<String> bitmaps = getArguments().getStringArrayList("data");
        String colorStr = AssetsUtil.getAssertString(_mActivity, "color.json");
        JSONArray jsonArray = JSONArray.parseArray(colorStr);
        ColorItemAdapter colorItemAdapter = new ColorItemAdapter(_mActivity, jsonArray, new ColorItemAdapter.OnColorPickedListener() {
            @Override
            public void onColorPicked(String color) {
                appCompatTextView.setBackgroundColor(Color.parseColor(color));
                mRvLP.setBackgroundColor(Color.parseColor(color));
            }
        });
        mRvColor.setAdapter(colorItemAdapter);
        loadPhoto(bitmaps);

    }

    private void loadPhoto(List<String> bitmaps) {
        showProcessDialog();
        Observable.create((ObservableOnSubscribe<List<Bitmap>>) emitter -> {
            if (bitmaps == null || bitmaps.size() == 0) {
                emitter.onError(new RuntimeException("bitmaps is null"));
            } else {
                ArrayList<Bitmap> arrayList = new ArrayList<>();
                for (String bitMapInfo : bitmaps) {
                    if (!TextUtils.isEmpty(bitMapInfo)) {
                        Uri srcUri;
                        if (PictureMimeType.isContent(bitMapInfo) || PictureMimeType.isHasHttp(bitMapInfo)) {
                            srcUri = Uri.parse(bitMapInfo);
                        } else {
                            srcUri = Uri.fromFile(new File(bitMapInfo));
                        }
                        if (srcUri != null) {
                            InputStream stream = _mActivity.getContentResolver().openInputStream(srcUri);
                            Bitmap bitmap = BitmapFactory.decodeStream(stream);
                            if (bitmap != null) {
                                arrayList.add(bitmap);
                            }
                        }
                    }
                }
                emitter.onNext(arrayList);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Bitmap>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull List<Bitmap> bitmaps) {
                if (bitmaps.isEmpty()) {
                    onError(new RuntimeException("bitmaps is 0"));
                } else {
                    LongPicItemAdapter longPicItemAdapter = new LongPicItemAdapter(_mActivity, bitmaps);
                    mRvLP.setAdapter(longPicItemAdapter);
                    paddingItemDecoration = new PaddingItemDecoration();
                    mRvLP.addItemDecoration(paddingItemDecoration);
                    hideProcessDialog();
                }

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });


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

    }

    @Override
    public void onTabReselect(int position) {

    }
    private int mCurrentProgress = 0 ;
    private PaddingItemDecoration paddingItemDecoration ;
    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
           int offsetProgress =progress-mCurrentProgress;
            //向右滑动 0-5  5-8
            if (offsetProgress > 0){
                paddingItemDecoration.setProcess(progress);
            }else if (offsetProgress < 0){
                paddingItemDecoration.setProcess(progress);
            }
            mCurrentProgress = progress;
            mRvLP.requestLayout();
        }
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
