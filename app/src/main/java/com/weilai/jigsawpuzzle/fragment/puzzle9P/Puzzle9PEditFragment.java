package com.weilai.jigsawpuzzle.fragment.puzzle9P;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.puzzle9P.Puzzle9PAdapter;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.weight.template.SpacesItemDecoration;

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
 * * DATE: 2022/9/28
 * * Author:tangerine
 * * Description:
 **/
public class Puzzle9PEditFragment extends BaseFragment {
    private RecyclerView mRv9Pic;

    private Puzzle9PEditFragment() {

    }

    public static Puzzle9PEditFragment getInstance(String path) {
        Puzzle9PEditFragment puzzle9PEditFragment = new Puzzle9PEditFragment();
        Bundle bundle = new Bundle();
        bundle.putString("data", path);
        puzzle9PEditFragment.setArguments(bundle);
        return puzzle9PEditFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_puzzle9p;
    }

    @Override
    protected void initView(View view) {
        assert getArguments() != null;
        mRv9Pic = view.findViewById(R.id.rv_9pc);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(_mActivity, 3);
        mRv9Pic.setLayoutManager(gridLayoutManager);
        String path = getArguments().getString("data");
        showProcessDialog();
        Observable.create((ObservableOnSubscribe<List<Bitmap>>) emitter -> {
            Uri srcUri;
            if (PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)) {
                srcUri = Uri.parse(path);
            } else {
                srcUri = Uri.fromFile(new File(path));
            }
            if (srcUri != null) {
                InputStream stream = _mActivity.getContentResolver().openInputStream(srcUri);
                Bitmap bitmapParent = BitmapFactory.decodeStream(stream);
                int simpleSizeWidth = bitmapParent.getWidth() / 3;
                int simpleSizeHeight = bitmapParent.getHeight() / 3;
                ArrayList<Bitmap> arrayList = new ArrayList<>();
                Bitmap bitmap;
                for (int i = 0; i < 9; i++) {
                    int x = 0;
                    int y = i / 3 * simpleSizeHeight;
                    if (i / 3 == 0) {
                        x = i * simpleSizeWidth;
                    } else if (i / 3 == 1) {
                        x = (i - 3) * simpleSizeWidth;
                    } else if (i / 3 == 2) {
                        x = (i - 6) * simpleSizeWidth;
                    }
                    bitmap = Bitmap.createBitmap(bitmapParent, x, y, simpleSizeWidth, simpleSizeHeight);
                    FileUtil.saveScreenShot(bitmap,System.currentTimeMillis()+"");
                    arrayList.add(bitmap);
                }
                emitter.onNext(arrayList);
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<List<Bitmap>>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(@NonNull List<Bitmap> bitmaps) {
                Puzzle9PAdapter puzzle9PAdapter = new Puzzle9PAdapter(_mActivity, bitmaps);
                ArrayMap<String, Integer> arrayMap = new ArrayMap<>();
                arrayMap.put(SpacesItemDecoration.TOP_SPACE, 10);
                arrayMap.put(SpacesItemDecoration.BOTTOM_SPACE, 10);
                arrayMap.put(SpacesItemDecoration.LEFT_SPACE, 10);
                arrayMap.put(SpacesItemDecoration.RIGHT_SPACE, 10);
                mRv9Pic.addItemDecoration(new SpacesItemDecoration(3, arrayMap, false));
                mRv9Pic.setAdapter(puzzle9PAdapter);
                for (Bitmap bitmap :bitmaps){
                    if (!bitmap.isRecycled()){
                        bitmap.recycle();
                    }
                }
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


    private void doOnBackGround() {
        showProcessDialog();
        Bitmap bitmap = shotScrollView( mRv9Pic.getWidth(), mRv9Pic.getHeight());
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



    private Bitmap shotScrollView(int width, int height) {
        Bitmap bitmap;
        mRv9Pic.setBackgroundColor(Color.parseColor("#ffffff"));
        bitmap = Bitmap.createBitmap(mRv9Pic.getWidth(), mRv9Pic.getHeight(), Bitmap.Config.ARGB_8888);
        final Canvas canvas = new Canvas(bitmap);
        mRv9Pic.draw(canvas);
        bitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height);
        return bitmap;
    }


    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.btn_save).setOnClickListener(v -> doOnBackGround());
    }
}
