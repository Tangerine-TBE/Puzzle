package com.weilai.jigsawpuzzle.fragment.puzzle;


import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;

import com.google.android.material.snackbar.Snackbar;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.weight.puzzle.slant.OneSlantLayout;
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
import com.xiaopo.flying.puzzle.PuzzleLayout;
import com.xiaopo.flying.puzzle.PuzzlePiece;
import com.xiaopo.flying.puzzle.PuzzleView;

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

public class PuzzleEditFragment extends BaseFragment {
    private PuzzleLayout mPuzzleLayout;
    private PuzzleView mPuzzleView;

    private PuzzleEditFragment() {

    }

    public static PuzzleEditFragment getInstance(int size, int type, int theme, ArrayList<String > bitMapPaths) {
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
        int picSize = getArguments().getInt("size");
        int type = getArguments().getInt("type");
        int theme = getArguments().getInt("theme");
        ArrayList<String > bitmaps = getArguments().getStringArrayList("bitmaps");
        PuzzleLayout puzzleLayout = choosePuzzleTemplate(picSize, type, theme);//no 1 no 0
        mPuzzleView = view.findViewById(R.id.puzzle_view);
        mPuzzleView.setPuzzleLayout(puzzleLayout);
        mPuzzleView.setTouchEnable(true);
        mPuzzleView.setNeedDrawLine(false);
        mPuzzleView.setNeedDrawOuterLine(false);
        mPuzzleView.setLineSize(4);
        mPuzzleView.setLineColor(Color.BLACK);
        mPuzzleView.setSelectedLineColor(Color.BLACK);
        mPuzzleView.setHandleBarColor(Color.BLACK);
        mPuzzleView.setAnimateDuration(300);
        mPuzzleView.setOnPieceSelectedListener(new PuzzleView.OnPieceSelectedListener() {
            @Override
            public void onPieceSelected(PuzzlePiece piece, int position) {
            }
        });
        mPuzzleView.setPiecePadding(10);
        loadPhoto(bitmaps);
    }
    public final ArrayList<Bitmap> arrayList = new ArrayList<>();
    private void loadPhoto(List<String> bitmaps){
        Observable.create(new ObservableOnSubscribe<List<Bitmap>>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<List<Bitmap>> emitter) throws Throwable {
                if (bitmaps == null || bitmaps.size() == 0){
                    emitter.onError(new RuntimeException("bitmaps is null"));
                }else{
                    for (String bitMapInfo:bitmaps){
                        if (!TextUtils.isEmpty(bitMapInfo)){
                            Bitmap bitmap = FileUtil.getBitmapFromCache(bitMapInfo);
                            arrayList.add(bitmap);
                        }
                    }
                    emitter.onNext();
                }
            }
        }) .subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Bitmap>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {

            }

            @Override
            public void onNext(@NonNull List<Bitmap> aBoolean) {

            }

            @Override
            public void onError(@NonNull Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        })



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
                    puzzleLayout = new TwoSlantLayout(theme);
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
                    puzzleLayout = new ThreeSlantLayout(theme);
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

    @Override
    protected void initListener(View view) {

    }
}
