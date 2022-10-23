package com.weilai.jigsawpuzzle.fragment.puzzle;


import static android.view.ViewGroup.LayoutParams.WRAP_CONTENT;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.SeekBar;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatImageView;
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
import com.weilai.jigsawpuzzle.dialog.puzzleLP.PuzzleLpPopUp;
import com.weilai.jigsawpuzzle.dialog.template.TemplateConfirmDialog;
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment;
import com.weilai.jigsawpuzzle.util.DimenUtil;
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

import io.reactivex.Observable;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class PuzzleEditFragment extends BaseFragment implements PuzzleSizeAdapter.OnItemClickListener, PuzzleView.OnPieceSelectedListener, View.OnClickListener, OnTabSelectListener, SeekBar.OnSeekBarChangeListener, TemplateConfirmDialog.OnConfirmClickedListener, PuzzleLpPopUp.OnPopUpDismiss {
    private final String[] title = new String[]{"布局", "边框"};
    private final int[] integers = new int[]{R.mipmap.icon_replace, R.mipmap.icon_rorate, R.mipmap.icon_lr_flip, R.mipmap.icon_tb_flip};
    private PuzzleView mPuzzleView;
    private RecyclerView recyclerView;
    private AppCompatSeekBar mBorderSeekBar;
    private AppCompatSeekBar mConnerSeekBar;
    private int picSize = 0;
    private static final int FILTER_CODE = 1;
    private PuzzleLpPopUp puzzleLpPopUp;
    private PuzzleSizeAdapter puzzleSizeAdapter;

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
        String[] tipsTitle = new String[]{getString(R.string.replace), getString(R.string.ucrop_rotate), getString(R.string.lr_flip), getString(R.string.tb_flip)};
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
                puzzleLpPopUp.dismiss();
                pop();
            }
        });
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
        puzzleLpPopUp = new PuzzleLpPopUp(_mActivity, this, tipsTitle, integers);


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
        mPuzzleView.setLineSize(6);
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
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(List<Bitmap> bitmaps) {
                if (bitmaps.isEmpty()) {
                    onError(new RuntimeException("bitmaps is 0"));
                } else {
                    mPuzzleView.addPieces(bitmaps);
                    List<PuzzleLayout> puzzleLayouts = new ArrayList<>();
                    loadAboutPuzzle(picSize, puzzleLayouts);
                     puzzleSizeAdapter = new PuzzleSizeAdapter(getContext(), puzzleLayouts, PuzzleEditFragment.this, bitmaps, mPuzzleView.getPuzzleLayout());
                    recyclerView.setAdapter(puzzleSizeAdapter);
                    hideProcessDialog();
                }
            }

            @Override
            public void onError(Throwable e) {

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
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(Bitmap bitmap) {
                mPuzzleView.replace(bitmap, "");
                hideProcessDialog();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

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
        Bitmap bitmap = shotScrollView(mPuzzleView.getWidth(), mPuzzleView.getHeight());
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            String filePath = FileUtil.saveScreenShot(bitmap, System.currentTimeMillis() + "");
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            emitter.onNext(filePath);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(String s) {
                hideProcessDialog();
                SaveFragment saveFragment = SaveFragment.getInstance(s);
                start(saveFragment);
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.tv_save).setOnClickListener(this);
        mBorderSeekBar.setOnSeekBarChangeListener(this);
        mConnerSeekBar.setOnSeekBarChangeListener(this);
    }

    @Override
    public void onItemClick(PuzzleLayout puzzleLayout, List<Bitmap> pics) {
        mPuzzleView.setPuzzleLayout(puzzleLayout);
        mPuzzleView.addPieces(pics);
        if (puzzleLpPopUp.isShowing()) {
            puzzleLpPopUp.dismiss();
        }
    }


    @Override
    public void onPieceUnSelected() {
        if (puzzleLpPopUp.isShowing()) {
            puzzleLpPopUp.dismiss();
        }
    }

    @Override
    public void onPieceSelected(PuzzlePiece piece, int position) {
        if (!puzzleLpPopUp.isShowing()) {
            puzzleLpPopUp.show(recyclerView, true);
        }
    }

    @SuppressLint("ResourceType")
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_save) {
            mPuzzleView.setNeedDrawLine(false);
            mPuzzleView.setNeedDrawOuterLine(false);
            puzzleLpPopUp.dismiss();
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
                puzzleLpPopUp.dismiss();
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
        puzzleSizeAdapter.notifyDataSetChanged();
    }


    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }


    @Override
    public void onConfirmClicked(String path) {

    }

    @Override
    public void dismiss() {
    }

    @SuppressLint("ResourceType")
    @Override
    public void clicked(View view, int position) {
        if (view.getId() == 0) {
            PictureSelector.create(this)
                    .openGallery(SelectMimeType.ofImage())
                    .isDisplayCamera(true)
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
                    .setSelectionMode(SelectModeConfig.SINGLE)
                    .forResult(FILTER_CODE);
        } else if (view.getId() == 1) {
            mPuzzleView.rotate(90f);
        } else if (view.getId() == 2) {
            mPuzzleView.flipVertically();

        } else if (view.getId() == 3) {
            mPuzzleView.flipHorizontally();
        }
    }


}
