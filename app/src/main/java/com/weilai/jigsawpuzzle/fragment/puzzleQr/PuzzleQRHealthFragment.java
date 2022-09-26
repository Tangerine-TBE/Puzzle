package com.weilai.jigsawpuzzle.fragment.puzzleQr;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.activity.puzzleQr.PuzzleQrBaseActivity;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.dialog.template.TemplateConfirmDialog;
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment;
import com.weilai.jigsawpuzzle.fragment.puzzle.PuzzleEditFragment;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.weight.puzzle.straight.FourStraightLayout;
import com.xiaopo.flying.puzzle.PuzzlePiece;
import com.xiaopo.flying.puzzle.PuzzleView;

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
 * * DATE: 2022/9/26
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleQRHealthFragment extends BaseFragment implements PuzzleView.OnPieceSelectedListener, TemplateConfirmDialog.OnConfirmClickedListener {
    private Disposable mDisposable;
    private PuzzleView mPuzzleView;
    private Disposable mDisposable2;

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
                    int size = bitmaps.size();
                    if (4 - size != 0) {
                        int length = 4 - size;
                        Bitmap bitmap = BitmapFactory.decodeResource(getResources(),R.mipmap.template32);
                        for (int i = 0; i < length; i++) {
                            bitmaps.add(bitmap);
                        }
                    }
                    mPuzzleView.addPieces(bitmaps);
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

        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("健康码");
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

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.layout_back).setOnClickListener(v -> pop());
        view.findViewById(R.id.tv_save).setOnClickListener(v -> {
            mPuzzleView.setNeedDrawLine(false);
            mPuzzleView.setNeedDrawOuterLine(false);
            doOnBackGround();
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
                mDisposable2 = d;
            }

            @Override
            public void onNext(@NonNull String s) {
                new TemplateConfirmDialog(getContext(), PuzzleQRHealthFragment.this, s).show();
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

    }

    @Override
    public void onPieceSelected(PuzzlePiece piece, int position) {

    }

    @Override
    public void onDestroy() {
        if (mDisposable.isDisposed()) {
            mDisposable.dispose();
        }
        if (mDisposable2.isDisposed()){
            mDisposable2.dispose();
        }
        super.onDestroy();
    }

    @Override
    public void onConfirmClicked(String path) {
        SaveFragment saveFragment = SaveFragment.getInstance(path);
        start(saveFragment);
    }
}
