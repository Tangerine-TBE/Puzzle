package com.weilai.jigsawpuzzle.fragment.puzzle;


import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
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

import com.luck.picture.lib.basic.PictureSelector;
import com.luck.picture.lib.config.PictureConfig;
import com.luck.picture.lib.config.PictureMimeType;
import com.luck.picture.lib.config.SelectMimeType;
import com.luck.picture.lib.config.SelectModeConfig;
import com.luck.picture.lib.entity.LocalMedia;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.puzzle.PuzzleSizeAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.bean.TabEntity;
import com.weilai.jigsawpuzzle.dialog.ProcessDialog;
import com.weilai.jigsawpuzzle.dialog.template.TemplateConfirmDialog;
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.GlideEngine;
import com.weilai.jigsawpuzzle.util.PuzzleUtil;
import com.weilai.jigsawpuzzle.weight.main.FlyTabLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.slant.ThreeSlantLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.slant.TwoSlantLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.straight.EightStraightLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.straight.FiveStraightLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.straight.FourStraightLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.straight.NineStraightLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.straight.SevenStraightLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.straight.SixStraightLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.straight.ThreeStraightLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.straight.TwoStraightLayout;
import com.weilai.library.listener.CustomTabEntity;
import com.weilai.library.listener.OnTabSelectListener;
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzlePiece;
import com.xiaopo.flying.puzzle.PuzzleView;

import java.io.File;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.schedulers.Schedulers;

public class PuzzleEditFragment extends BaseFragment implements PuzzleSizeAdapter.OnItemClickListener, PuzzleView.OnPieceSelectedListener, View.OnClickListener, OnTabSelectListener, SeekBar.OnSeekBarChangeListener, TemplateConfirmDialog.OnConfirmClickedListener {
    private final String[] title = new String[]{"布局", "边框"};
    private PuzzleView mPuzzleView;
    private RecyclerView recyclerView;
    private AppCompatSeekBar mBorderSeekBar;
    private AppCompatSeekBar mConnerSeekBar;
    private int picSize = 0;
    private LinearLayoutCompat llTips;
    private static final int FILTER_CODE = 1;

    private PuzzleEditFragment() {

    }

    public static PuzzleEditFragment getInstance(int size, int type, int theme, ArrayList<String> bitMapPaths) {
        Bundle bundle = new Bundle();
        bundle.putInt("size", size);
        bundle.putInt("type", type);
        bundle.putInt("theme", theme);
        bundle.putStringArrayList("bitmaps", bitMapPaths);
        PuzzleEditFragment puzzleEditFragment = new
                PuzzleEditFragment();
        puzzleEditFragment.setArguments(bundle);
        return puzzleEditFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_puzzle_edit;
    }

    @Override
    protected void initView(View view) {
        ArrayList<CustomTabEntity> arrayList = new ArrayList<>();
        for (String tile : title) {
            TabEntity tabEntity = new TabEntity(tile);
            arrayList.add(tabEntity);
        }
        FlyTabLayout flyTabLayout = view.findViewById(R.id.tabLayout);
        flyTabLayout.setTabData(arrayList);
        flyTabLayout.setCurrentTab(0);
        flyTabLayout.setOnTabSelectListener(this);

        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("拼图");
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
        llTips = view.findViewById(R.id.tips);
        mBorderSeekBar = view.findViewById(R.id.border_seekbar);
        mConnerSeekBar = view.findViewById(R.id.conner_seekbar);
        picSize = getArguments().getInt("size");
        int type = getArguments().getInt("type");
        int theme = getArguments().getInt("theme");
        PuzzleLayout mPuzzleLayout = choosePuzzleTemplate(picSize, type, theme);//no 1 no 0
        mPuzzleView = view.findViewById(R.id.puzzle_view);
        ArrayList<String> bitmaps = getArguments().getStringArrayList("bitmaps");
        mPuzzleView.setPuzzleLayout(mPuzzleLayout);
        mPuzzleView.setTouchEnable(true);
        mPuzzleView.setNeedDrawLine(false);
        mPuzzleView.setNeedDrawOuterLine(false);
        mPuzzleView.setLineSize(4);
        mPuzzleView.setLineColor(getResources().getColor(R.color.sel_text_main_color));
        mPuzzleView.setSelectedLineColor(getResources().getColor(R.color.sel_text_main_color));
        mPuzzleView.setHandleBarColor(getResources().getColor(R.color.sel_text_main_color));
        mPuzzleView.setAnimateDuration(300);
        mPuzzleView.setOnPieceSelectedListener(this);
        loadPhoto(bitmaps);
        recyclerView = view.findViewById(R.id.rv_change_item);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        recyclerView.setLayoutManager(linearLayoutManager);
    }

    private void loadAboutPuzzle(int size, List<PuzzleLayout> puzzleLayouts) {
        puzzleLayouts.addAll(PuzzleUtil.getAboutSizeLayouts(size));
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
                mDisposable = d;
            }

            @Override
            public void onNext(@NonNull List<Bitmap> bitmaps) {
                if (bitmaps.isEmpty()) {
                    onError(new RuntimeException("bitmaps is 0"));
                } else {
                    mPuzzleView.addPieces(bitmaps);
                    List<PuzzleLayout> puzzleLayouts = new ArrayList<>();
                    loadAboutPuzzle(picSize, puzzleLayouts);
                    PuzzleSizeAdapter puzzleSizeAdapter = new PuzzleSizeAdapter(getContext(), puzzleLayouts, PuzzleEditFragment.this, bitmaps, mPuzzleView.getPuzzleLayout());
                    recyclerView.setAdapter(puzzleSizeAdapter);
                    hideProcessDialog();
//                    recyclerView.scrollToPosition(puzzleSizeAdapter.getCurrentPosition());
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

    private void loadPhoto(String path) {
        showProcessDialog();
        Observable.create((ObservableOnSubscribe<Bitmap>) emitter -> {
            if (path == null || path.isEmpty()) {
                emitter.onError(new RuntimeException("bitmaps is null"));
            } else {
                Uri srcUri;
                if (PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)) {
                    srcUri = Uri.parse(path);
                } else {
                    srcUri = Uri.fromFile(new File(path));
                }
                if (srcUri != null) {
                    InputStream stream = _mActivity.getContentResolver().openInputStream(srcUri);
                    Bitmap bitmap = BitmapFactory.decodeStream(stream);
                    if (bitmap != null) {
                        emitter.onNext(bitmap);
                    }
                }
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Bitmap>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable1 = d;
            }

            @Override
            public void onNext(@NonNull Bitmap bitmap) {
                mPuzzleView.replace(bitmap, "");
                hideProcessDialog();
            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    private Disposable mDisposable1;

    private PuzzleLayout choosePuzzleTemplate(int pics, int type, int theme) {

        //pic Size to first selection
        PuzzleLayout puzzleLayout = null;
        switch (pics) {
            case 2:
                if (type >= 0 && theme >= 0) {
                    if (type == 0) {
                        puzzleLayout = new TwoSlantLayout(theme);
                    } else {
                        puzzleLayout = new TwoStraightLayout(theme);
                    }
                } else {
                    puzzleLayout = new TwoSlantLayout(0);
                }
                break;
            case 3:
                if (type >= 0 && theme >= 0) {
                    if (type == 0) {
                        puzzleLayout = new ThreeSlantLayout(theme);
                    } else {
                        puzzleLayout = new ThreeStraightLayout(theme);
                    }
                } else {
                    puzzleLayout = new ThreeSlantLayout(0);
                }
                break;
            case 4:
                if (theme >= 0) {
                    puzzleLayout = new FourStraightLayout(theme);
                } else {
                    puzzleLayout = new FourStraightLayout(0);
                }
                break;
            case 5:
                if (theme >= 0) {
                    puzzleLayout = new FiveStraightLayout(theme);
                } else {
                    puzzleLayout = new FiveStraightLayout(0);
                }
                break;
            case 6:
                if (theme >= 0) {
                    puzzleLayout = new SixStraightLayout(theme);
                } else {
                    puzzleLayout = new SixStraightLayout(0);
                }
                break;
            case 7:
                if (theme >= 0) {
                    puzzleLayout = new SevenStraightLayout(theme);
                } else {
                    puzzleLayout = new SevenStraightLayout(0);
                }
                break;
            case 8:
                if (theme >= 0) {
                    puzzleLayout = new EightStraightLayout(theme);
                } else {
                    puzzleLayout = new EightStraightLayout(0);
                }
                break;
            case 9:
                if (theme >= 0) {
                    puzzleLayout = new NineStraightLayout(theme);
                } else {
                    puzzleLayout = new NineStraightLayout(0);
                }
                break;
            default:
                break;
        }
        return puzzleLayout;
    }

    private Bitmap shotScrollView(int width, int height) {
        Bitmap bitmap;
        mPuzzleView.setBackgroundColor(Color.parseColor("#ffffff"));
        bitmap = Bitmap.createBitmap(mPuzzleView.getWidth(), mPuzzleView.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        mPuzzleView.draw(canvas);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        return bitmap;
    }

    private void doOnBackGround() {
        showProcessDialog();
        Bitmap bitmap = shotScrollView( mPuzzleView.getWidth(), mPuzzleView.getHeight());
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            String filePath = FileUtil.saveScreenShot(bitmap, System.currentTimeMillis() + "");
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            emitter.onNext(filePath);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable2 = d;
            }

            @Override
            public void onNext(@NonNull String s) {
                hideProcessDialog();
                SaveFragment saveFragment = SaveFragment.getInstance(s);
                start(saveFragment);
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
        view.findViewById(R.id.ll_replace).setOnClickListener(this);
        view.findViewById(R.id.ll_rorate).setOnClickListener(this);
        view.findViewById(R.id.ll_LeftRightFlip).setOnClickListener(this);
        view.findViewById(R.id.ll_UpDownFlip).setOnClickListener(this);
        view.findViewById(R.id.tv_save).setOnClickListener(this);
        mBorderSeekBar.setOnSeekBarChangeListener(this);
        mConnerSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onItemClick(PuzzleLayout puzzleLayout, List<Bitmap> pics) {
        mPuzzleView.setPuzzleLayout(puzzleLayout);
        mPuzzleView.addPieces(pics);
        if (llTips.getVisibility() == View.VISIBLE) {
            llTips.setVisibility(View.INVISIBLE);
        }
    }


    @Override
    public void onPieceUnSelected() {
        if (llTips.getVisibility() == View.VISIBLE) {
            llTips.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public void onPieceSelected(PuzzlePiece piece, int position) {
        if (llTips.getVisibility() != View.VISIBLE) {
            llTips.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.ll_replace) {
            PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .isDisplayCamera(true)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .setSelectionMode(SelectModeConfig.SINGLE)
                    .forResult(FILTER_CODE);
        } else if (v.getId() == R.id.ll_rorate) {
            mPuzzleView.rotate(90f);
        } else if (v.getId() == R.id.ll_LeftRightFlip) {
            mPuzzleView.flipVertically();

        } else if (v.getId() == R.id.ll_UpDownFlip) {
            mPuzzleView.flipHorizontally();
        } else if (v.getId() == R.id.tv_save) {
            mPuzzleView.setNeedDrawLine(false);
            mPuzzleView.setNeedDrawOuterLine(false);
            doOnBackGround();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == FILTER_CODE) {
            if (data != null) {
                ArrayList<LocalMedia> list = data.getParcelableArrayListExtra(PictureConfig.EXTRA_RESULT_SELECTION);
                if (list != null) {
                    int size = list.size();
                    if (size > 0) {
                        String path = list.get(0).getAvailablePath();
                        loadPhoto(path);
                    }
                }
            }
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0:
                recyclerView.setVisibility(View.VISIBLE);
                mBorderSeekBar.setVisibility(View.GONE);
                mConnerSeekBar.setVisibility(View.GONE);
                break;
            case 1:
                recyclerView.setVisibility(View.GONE);
                mBorderSeekBar.setVisibility(View.VISIBLE);
                mConnerSeekBar.setVisibility(View.VISIBLE);
                break;
            default:
                break;
        }
    }

    @Override
    public void onTabReselect(int position) {

    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (mBorderSeekBar == seekBar) {
            mPuzzleView.setPiecePadding(progress);
        } else if (mConnerSeekBar == seekBar) {
            mPuzzleView.setPieceRadian(progress);
        }

    }

    private Disposable mDisposable;
    private Disposable mDisposable2;

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onDestroy() {
        if (mDisposable != null) {
            if (!mDisposable.isDisposed()) {
                mDisposable.dispose();
            }
        }
        if (mDisposable1 != null) {
            if (!mDisposable1.isDisposed()) {
                mDisposable1.dispose();
            }
        }
        if (mDisposable2 != null) {
            if (!mDisposable2.isDisposed()) {
                mDisposable2.dispose();
            }
        }
        super.onDestroy();
    }

    @Override
    public void onConfirmClicked(String path) {

    }
}
