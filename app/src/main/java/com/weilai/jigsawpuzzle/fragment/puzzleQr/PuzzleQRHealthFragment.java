package com.weilai.jigsawpuzzle.fragment.puzzleQr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.appcompat.widget.LinearLayoutCompat;

import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.bean.TabEntity;
import com.weilai.jigsawpuzzle.dialog.template.TemplateConfirmDialog;
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.util.ToastUtil;
import com.weilai.jigsawpuzzle.weight.main.FlyTabLayout;
import com.weilai.jigsawpuzzle.weight.puzzle.straight.FourStraightLayout;
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


/**
 * * DATE: 2022/9/26
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleQRHealthFragment extends BaseFragment implements PuzzleView.OnPieceSelectedListener, TemplateConfirmDialog.OnConfirmClickedListener, OnTabSelectListener, SeekBar.OnSeekBarChangeListener {
    private PuzzleView mPuzzleView;
    private LinearLayoutCompat llFrame;
    private LinearLayoutCompat llRorate;
    private LinearLayoutCompat llMirror;
    private AppCompatTextView mTvRoRate;
    private AppCompatTextView mTvConner;
    private AppCompatTextView mTvFrame;
    private final String[] titles = {"边框", "旋转", "翻转"};

    private PuzzleQRHealthFragment() {

    }

    public static PuzzleQRHealthFragment getInstance(ArrayList<String> path) {
        PuzzleQRHealthFragment puzzleQRHealthFragment = new PuzzleQRHealthFragment();
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", path);
        puzzleQRHealthFragment.setArguments(bundle);
        return puzzleQRHealthFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_puzzle_qr;
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
            public void onNext(@NonNull List<Bitmap> bitmaps) {
                if (bitmaps.isEmpty()) {
                    onError(new RuntimeException("bitmaps is 0"));
                } else {
                    int size = bitmaps.size();
                    if (4 - size != 0) {
                        int length = 4 - size;
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.template43);
                        for (int i = 0; i < length; i++) {
                            bitmaps.add(bitmap);
                        }
                    }
                    mPuzzleView.addPieces(bitmaps);
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
    protected void initView(View view) {
        assert getArguments() != null;
        ArrayList<String> data = getArguments().getStringArrayList("data");
        ArrayList<CustomTabEntity> arrayList = new ArrayList<>();
        for (String tile : titles) {
            TabEntity tabEntity = new TabEntity(tile);
            arrayList.add(tabEntity);
        }
        FlyTabLayout flyTabLayout = view.findViewById(R.id.tabLayout);
        flyTabLayout.setTabData(arrayList);
        flyTabLayout.setCurrentTab(0);
        flyTabLayout.setOnTabSelectListener(this);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("健康码");
        llRorate = view.findViewById(R.id.ll_rorate);
        llFrame = view.findViewById(R.id.ll_frame);
        llMirror = view.findViewById(R.id.ll_mirror);
        mTvRoRate = view.findViewById(R.id.rorate_text);
        mTvConner = view.findViewById(R.id.conner_text);
        mTvFrame = view.findViewById(R.id.frame_text);
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
        mPuzzleView = view.findViewById(R.id.puzzle_view);
        mPuzzleView.setTouchEnable(true);
        mPuzzleView.setNeedDrawLine(false);
        mPuzzleView.setNeedDrawOuterLine(false);
        mPuzzleView.setLineSize(4);
        mPuzzleView.setLineColor(getResources().getColor(R.color.sel_text_main_color));
        mPuzzleView.setSelectedLineColor(getResources().getColor(R.color.sel_text_main_color));
        mPuzzleView.setHandleBarColor(getResources().getColor(R.color.sel_text_main_color));
        mPuzzleView.setAnimateDuration(300);
        mPuzzleView.setOnPieceSelectedListener(this);
        mPuzzleView.setPuzzleLayout(new FourStraightLayout(2));
        loadPhoto(data);

    }

    private AppCompatSeekBar rorateSeekBar;

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.layout_back).setOnClickListener(v -> _mActivity.finish());
        view.findViewById(R.id.tv_save).setOnClickListener(v -> {
            mPuzzleView.setNeedDrawLine(false);
            mPuzzleView.setNeedDrawOuterLine(false);
            doOnBackGround();
        });
        AppCompatSeekBar frameSeekBar = view.findViewById(R.id.frame_seekbar);
        AppCompatSeekBar connerSeekBar = view.findViewById(R.id.conner_seekbar);
        rorateSeekBar = view.findViewById(R.id.rorate_seekbar);

        frameSeekBar.setOnSeekBarChangeListener(this);
        connerSeekBar.setOnSeekBarChangeListener(this);
        rorateSeekBar.setOnSeekBarChangeListener(this);
        view.findViewById(R.id.hz_mirror).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPuzzleView.flipHorizontally();
            }
        });
        view.findViewById(R.id.vt_mirror).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPuzzleView.flipVertically();
            }
        });
        view.findViewById(R.id.reset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<Bitmap> bitmaps = new ArrayList<>();
                List<PuzzlePiece> list = mPuzzleView.getPuzzlePieces();
                for (PuzzlePiece piece : list) {
                    BitmapDrawable drawable = (BitmapDrawable) piece.getDrawable();
                    bitmaps.add(drawable.getBitmap());
                }
                PuzzleLayout puzzleLayout = mPuzzleView.getPuzzleLayout();
                mPuzzleView.reset();
                mPuzzleView.setPuzzleLayout(puzzleLayout);
                rorateSeekBar.setProgress(90);
                currentAngle = 90;
                rightAngle = 0;
                mTvRoRate.setText(rightAngle + "");
                mPuzzleView.addPieces(bitmaps);
            }
        });
    }

    private Bitmap shotScrollView(int x, int y, int width, int height) {
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
        Bitmap bitmap = shotScrollView((int) mPuzzleView.getX(), (int) mPuzzleView.getY(), mPuzzleView.getWidth(), mPuzzleView.getHeight());
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            String filePath = FileUtil.saveScreenShot(bitmap, System.currentTimeMillis() + "");
            if (!bitmap.isRecycled()) {
                bitmap.recycle();
            }
            emitter.onNext(filePath);
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull String s) {
                hideProcessDialog();
                SaveFragment saveFragment = SaveFragment.getInstance(s, "拼健康码");
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
    public void onPieceUnSelected() {
        rorateSeekBar.setProgress(90);
        currentAngle = 90;
        rightAngle = 0;
        mTvRoRate.setText(rightAngle + "");
    }

    private int currentAngle = 90;
    private int currentPosition = -1;

    @Override
    public void onPieceSelected(PuzzlePiece piece, int position) {
        if (position != currentPosition) {
            currentPosition = position;
            int rotateDegree = (int) piece.getRotateDegree();
            int processOffset = rotateDegree / 3;
            if (processOffset == 0) {
                rorateSeekBar.setProgress(90);
                currentAngle = 90;
                rightAngle = 0;
            } else {
                rorateSeekBar.setProgress(90 + processOffset);
                currentAngle = 90 + processOffset;
                rightAngle = rotateDegree;
            }
            currentPosition = position;
            mTvRoRate.setText(rightAngle + "");
        }

    }


    @Override
    public void onConfirmClicked(String path) {

    }

    @Override
    public void onTabSelect(int position) {
        switch (position) {
            case 0:
                llFrame.setVisibility(View.VISIBLE);
                llRorate.setVisibility(View.GONE);
                llMirror.setVisibility(View.GONE);
                break;
            case 1:
                llFrame.setVisibility(View.GONE);
                llRorate.setVisibility(View.VISIBLE);
                llMirror.setVisibility(View.GONE);
                break;
            case 2:
                llFrame.setVisibility(View.GONE);
                llRorate.setVisibility(View.GONE);
                llMirror.setVisibility(View.VISIBLE);
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
        if (!fromUser) {
            return;
        }
        if (mPuzzleView.getHandlingPiece() == null) {
            seekBar.setProgress(90);
            return;
        }
        int id = seekBar.getId();
        switch (id) {
            case (R.id.conner_seekbar):
                mPuzzleView.setPieceRadian(progress);
                mTvConner.setText("" + progress);
                break;
            case (R.id.rorate_seekbar):
                int angle = (progress - currentAngle) * 3;
                mPuzzleView.rotate(angle);
                currentAngle = progress;
                rightAngle = rightAngle + angle;
                mTvRoRate.setText(rightAngle + "");
                break;
            case (R.id.frame_seekbar):
                mPuzzleView.setPiecePadding(progress);
                mTvFrame.setText("" + progress);

                break;
        }
    }

    private int rightAngle = 0;

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }
}
