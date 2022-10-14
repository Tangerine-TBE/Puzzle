package com.weilai.jigsawpuzzle.fragment.puzzleLPic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;

import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.event.LpSortEvent;
import com.weilai.jigsawpuzzle.event.LpSplitEvent;
import com.weilai.jigsawpuzzle.util.EvenUtil;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.weight.puzzleLP.PuzzleLpEditView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * * DATE: 2022/10/8
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleLpSplitFragment extends BaseFragment {
    private PuzzleLpEditView mEditBottom;
    private PuzzleLpEditView mEditTop;
    private int type;
    private int mode;

    private PuzzleLpSplitFragment() {
    }

    public static PuzzleLpSplitFragment getInstance(ArrayList<String> bitmapInfo, int type, int mode) {
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("bitmapInfo", bitmapInfo);
        bundle.putInt("type", type);
        bundle.putInt("mode", mode);
        PuzzleLpSplitFragment puzzleLpSplitFragment = new PuzzleLpSplitFragment();
        puzzleLpSplitFragment.setArguments(bundle);
        return puzzleLpSplitFragment;
    }

    @Override
    protected Object setLayout() {
        type = getArguments().getInt("type");
        mode = getArguments().getInt("mode");
        if (mode == 1) {
            return R.layout.fragment_lp_edit;
        } else {
            return R.layout.fragment_h_lp_edit;
        }
    }

    @Override
    protected void initView(View view) {
        assert getArguments() != null;
        mEditBottom = view.findViewById(R.id.edit_bottom);
        mEditTop = view.findViewById(R.id.edit_top);
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        view.findViewById(R.id.tv_save).setVisibility(View.VISIBLE);
        tvTitle.setText("");

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        ArrayList<String> bitmaps = getArguments().getStringArrayList("bitmapInfo");
        type = getArguments().getInt("type");
        if (type == 1) {
            //裁顶部
            Uri srcUri;
            if (PictureMimeType.isContent(bitmaps.get(0)) || PictureMimeType.isHasHttp(bitmaps.get(0))) {
                srcUri = Uri.parse(bitmaps.get(0));
            } else {
                srcUri = Uri.fromFile(new File(bitmaps.get(0)));
            }
            if (srcUri != null) {
                InputStream stream = null;
                try {
                    stream = _mActivity.getContentResolver().openInputStream(srcUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mEditTop.setBitmap(BitmapFactory.decodeStream(stream));
            }
        } else if (type == 2) {
            //裁底部
            Uri srcUri;
            if (bitmaps.size() >= 2) {
                if (PictureMimeType.isContent(bitmaps.get(1)) || PictureMimeType.isHasHttp(bitmaps.get(1))) {
                    srcUri = Uri.parse(bitmaps.get(1));
                } else {
                    srcUri = Uri.fromFile(new File(bitmaps.get(1)));
                }
                if (srcUri != null) {
                    InputStream stream = null;
                    try {
                        stream = _mActivity.getContentResolver().openInputStream(srcUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    mEditTop.setBitmap(BitmapFactory.decodeStream(stream));
                }
            }
            if (PictureMimeType.isContent(bitmaps.get(0)) || PictureMimeType.isHasHttp(bitmaps.get(0))) {
                srcUri = Uri.parse(bitmaps.get(0));
            } else {
                srcUri = Uri.fromFile(new File(bitmaps.get(0)));
            }
            if (srcUri != null) {
                InputStream stream = null;
                try {
                    stream = _mActivity.getContentResolver().openInputStream(srcUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                mEditBottom.setBitmap(BitmapFactory.decodeStream(stream));
            }
        }
        super.onLazyInitView(savedInstanceState);
    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        view.findViewById(R.id.tv_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //save
                showProcessDialog();
                Observable.create(new ObservableOnSubscribe<List<Bitmap>>() {
                    @Override
                    public void subscribe(ObservableEmitter<List<Bitmap>> emitter) throws Exception {
                        Bitmap editBottomBitmap = mEditBottom.saveBitmap();
                        Bitmap editTopBitmap = mEditTop.saveBitmap();
                        ArrayList<Bitmap> bitmaps = new ArrayList<>();
                        if (editTopBitmap != null){
                            bitmaps.add(editTopBitmap);
                        }
                        if (editBottomBitmap != null){
                            bitmaps.add(editBottomBitmap);
                        }
                        emitter.onNext(bitmaps);
                    }
                }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Bitmap>>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                        mDisposable.add(d);
                    }

                    @Override
                    public void onNext(List<Bitmap> o) {
                        ArrayList<String> bitmapInfo = new ArrayList<>();
                        for (Bitmap bitmap : o){
                            bitmapInfo.add(FileUtil.saveBitmapToCache(System.currentTimeMillis() + "Split", bitmap));
                        }
                        hideProcessDialog();
                        EvenUtil.post(new LpSplitEvent(bitmapInfo, type));
                    }
                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                    }
                });


            }
        });
    }
}
