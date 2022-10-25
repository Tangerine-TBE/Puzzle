package com.weilai.jigsawpuzzle.fragment.puzzleLPic;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.RectF;
import android.os.Bundle;
import android.util.LruCache;
import android.view.View;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.bean.PicInfo;
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment;
import com.weilai.jigsawpuzzle.util.BitmapUtils;
import com.weilai.jigsawpuzzle.util.DimenUtil;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.ToastUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * * DATE: 2022/10/25
 * * Author:tangerine
 * * Description:图片压缩以及审查
 **/
public class PuzzleAdjustFragment extends BaseFragment {
    private AppCompatSeekBar mAdjustSeekBar;
    private float mScaleSize;

    public static PuzzleAdjustFragment getInstance(String color, int process, ArrayList<PicInfo> picInfos, int type) {
        PuzzleAdjustFragment puzzleAdjustFragment = new PuzzleAdjustFragment();
        Bundle bundle = new Bundle();
        bundle.putString("color", color);
        bundle.putInt("process", process);
        bundle.putParcelableArrayList("data", picInfos);
        bundle.putInt("type", type);
        puzzleAdjustFragment.setArguments(bundle);
        return puzzleAdjustFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_puzzle_adjust;
    }

    private String color;
    private int process;
    private int mBitmapHeight;
    private int mBitmapWidth;
    private int type = -1;
    private TextView textView;
    private TextView processView;
    private ArrayList<PicInfo> picInfos;

    @Override
    protected void initView(View view) {
        color = getArguments().getString("color");
        process = getArguments().getInt("process");
        picInfos = getArguments().getParcelableArrayList("data");
        type = getArguments().getInt("type");
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        tvTitle.setText("保存");
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        TextView tvType = view.findViewById(R.id.tv_type);
        tvType.setText("图片高度");
        textView = view.findViewById(R.id.text);
        mAdjustSeekBar = view.findViewById(R.id.adjust_seekBar);
        mAdjustSeekBar.setMax(100);
        processView = view.findViewById(R.id.frame_text);

    }

    private void save() {
        showProcessDialog();
        Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                BitmapFactory.Options options = new BitmapFactory.Options();
                final int maxMemory = (int) (Runtime.getRuntime().maxMemory() / 1024);
                LruCache<String, Bitmap> bitmapCache = new LruCache<>(maxMemory);
                Bitmap finallyBitmap;
                if (type == 1) {
                    int iHeight = 0;
                    for (int i = 0; i < picInfos.size(); i++) {
                        BitmapFactory.decodeStream(_mActivity.getContentResolver().openInputStream(BitmapUtils.pathToUri(picInfos.get(i).path)), null, options);
                        Bitmap picBitmap = BitmapFactory.decodeStream(_mActivity.getContentResolver().openInputStream(BitmapUtils.pathToUri(picInfos.get(i).path)));
                        int bitMapWidth = options.outWidth;
                        BigDecimal bitMapWidthBig = new BigDecimal(bitMapWidth);
                        int viewWidth = DimenUtil.getScreenWidth() * 5 / 7;
                        BigDecimal viewWidthBig = new BigDecimal(viewWidth);
                        float value = viewWidthBig.divide(bitMapWidthBig, 2, RoundingMode.HALF_DOWN).floatValue();
                        BigDecimal valueBig = new BigDecimal(value);
                        int bigMapHeight = options.outHeight;
                        int viewHeight = valueBig.multiply(new BigDecimal(bigMapHeight)).intValue();
                        Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        canvas.drawColor(Color.parseColor(color));
                        int finallyLeft;
                        int finallyTop;
                        int finallyRight;
                        int finallyBottom = canvas.getHeight();
                        if (i == 0) {
                            finallyTop = process;
                            finallyLeft = process;
                            finallyRight = canvas.getWidth() - process;
                            if (picInfos.size() == 1) {
                                finallyBottom = canvas.getHeight() - process;
                            }
                        } else if (i == picInfos.size() - 1) {
                            finallyTop = process;
                            finallyLeft = process;
                            finallyBottom = canvas.getHeight() - process;
                            finallyRight = canvas.getWidth() - process;
                        } else {
                            finallyTop = process;
                            finallyLeft = process;
                            finallyRight = canvas.getWidth() - process;
                        }
                        RectF src = new RectF(finallyLeft, finallyTop, finallyRight, finallyBottom);
                        canvas.drawBitmap(picBitmap, null, src, null);
                        if (!picBitmap.isRecycled()) {
                            picBitmap.recycle();
                        }
                        bitmapCache.put(String.valueOf(i), bitmap);
                    }
                    Matrix matrix = new Matrix();
                    finallyBitmap = Bitmap.createBitmap(calculateWidth, calculateHeight, Bitmap.Config.ARGB_8888);
                    Canvas bigCanvas = new Canvas(finallyBitmap);
                    for (int i = 0; i < picInfos.size(); i++) {
                        Bitmap bitmap = bitmapCache.get(String.valueOf(i));
                        if (bitmap != null) {
                            matrix.setScale(mScaleSize, mScaleSize);
                            matrix.postTranslate(0, iHeight * mScaleSize);
                            bigCanvas.drawBitmap(bitmap, matrix, null);
                            iHeight += bitmap.getHeight();
                            bitmap.recycle();
                        }
                    }
                    emitter.onNext(FileUtil.saveScreenShot(finallyBitmap, System.currentTimeMillis() + ""));
                } else if (type == 0) {
                    int width = 0;
                    int iWidth = 0;
                    for (int i = 0; i < picInfos.size(); i++) {
                        Bitmap picBitmap = BitmapFactory.decodeStream(_mActivity.getContentResolver().openInputStream(BitmapUtils.pathToUri(picInfos.get(i).path)));
                        int bitMapHeight = picBitmap.getHeight();
                        BigDecimal bitMapHeightBig = new BigDecimal(bitMapHeight);
                        int viewHeight = DimenUtil.getScreenHeight() / 2;
                        BigDecimal viewHeightBig = new BigDecimal(viewHeight);
                        float value = viewHeightBig.divide(bitMapHeightBig, 2, RoundingMode.HALF_DOWN).floatValue();
                        BigDecimal valueBig = new BigDecimal(value);
                        int bigMapWidth = picBitmap.getWidth();
                        int viewWidth = valueBig.multiply(new BigDecimal(bigMapWidth)).intValue();
                        Bitmap bitmap = Bitmap.createBitmap(viewWidth, viewHeight, Bitmap.Config.ARGB_8888);
                        Canvas canvas = new Canvas(bitmap);
                        canvas.drawColor(Color.parseColor(color));
                        int finallyLeft;
                        int finallyTop;
                        int finallyRight = canvas.getWidth();
                        int finallyBottom;
                        if (i == 0) {
                            finallyTop = process;
                            finallyLeft = process;
                            finallyBottom = canvas.getHeight() - process;
                            if (picInfos.size() == 1) {
                                finallyRight = canvas.getWidth() - process;
                            }
                        } else if (i == picInfos.size() - 1) {
                            finallyTop = process;
                            finallyLeft = process;
                            finallyRight = canvas.getWidth() - process;
                            finallyBottom = canvas.getHeight() - process;
                        } else {
                            finallyTop = process;
                            finallyLeft = process;
                            finallyBottom = canvas.getHeight() - process;
                        }
                        width += viewWidth;
                        RectF src = new RectF(finallyLeft, finallyTop, finallyRight, finallyBottom);
                        canvas.drawBitmap(picBitmap, null, src, null);
                        if (!picBitmap.isRecycled()) {
                            picBitmap.recycle();
                        }
                        bitmapCache.put(String.valueOf(i), bitmap);
                    }
                    Matrix matrix = new Matrix();
                    finallyBitmap = Bitmap.createBitmap(calculateWidth, calculateHeight, Bitmap.Config.ARGB_8888);
                    Canvas bigCanvas = new Canvas(finallyBitmap);
                    for (int i = 0; i < picInfos.size(); i++) {
                        Bitmap bitmap = bitmapCache.get(String.valueOf(i));
                        if (bitmap != null) {
                            matrix.setScale(mScaleSize, mScaleSize);
                            matrix.postTranslate(iWidth * mScaleSize, 0);
                            bigCanvas.drawBitmap(bitmap, matrix, null);
                            iWidth += bitmap.getWidth();
                            bitmap.recycle();
                        }
                    }
                    String filePath = FileUtil.saveScreenShot(finallyBitmap, System.currentTimeMillis() + "");
                    emitter.onNext(filePath);
                }
            }

        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(String filePath) {
                hideProcessDialog();
                SaveFragment saveFragment = SaveFragment.getInstance(filePath, type == 0 ?"横拼图":"长拼图");
                start(saveFragment);
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


    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        Observable.create(new ObservableOnSubscribe<Integer>() {
            @Override
            public void subscribe(ObservableEmitter<Integer> emitter) throws Exception {
                BitmapFactory.Options options = new BitmapFactory.Options();
                options.inJustDecodeBounds = true;
                if (type == 1) {
                    int height = 0;
                    int width = DimenUtil.getScreenWidth() * 5 / 7;
                    for (int i = 0; i < picInfos.size(); i++) {
                        BitmapFactory.decodeStream(_mActivity.getContentResolver().openInputStream(BitmapUtils.pathToUri(picInfos.get(i).path)), null, options);
                        mBitmapWidth = options.outWidth;
                        BigDecimal bitMapWidthBig = new BigDecimal(mBitmapWidth);
                        BigDecimal viewWidthBig = new BigDecimal(width);
                        float value = viewWidthBig.divide(bitMapWidthBig, 2, RoundingMode.HALF_DOWN).floatValue();
                        BigDecimal valueBig = new BigDecimal(value);
                        int bigMapHeight = options.outHeight;
                        int viewHeight = valueBig.multiply(new BigDecimal(bigMapHeight)).intValue();
                        height += viewHeight;
                    }
                    mBitmapHeight = height;
                    emitter.onNext(height);
                } else if (type == 0) {
                    int width = 0;
                    for (int i = 0; i < picInfos.size(); i++) {
                        BitmapFactory.decodeStream(_mActivity.getContentResolver().openInputStream(BitmapUtils.pathToUri(picInfos.get(i).path)), null, options);
                        mBitmapHeight = options.outHeight;
                        BigDecimal bitMapHeightBig = new BigDecimal(mBitmapHeight);
                        int viewHeight = DimenUtil.getScreenHeight() / 2;
                        BigDecimal viewHeightBig = new BigDecimal(viewHeight);
                        float value = viewHeightBig.divide(bitMapHeightBig, 2, RoundingMode.HALF_DOWN).floatValue();
                        BigDecimal valueBig = new BigDecimal(value);
                        int bigMapWidth = options.outWidth;
                        int viewWidth = valueBig.multiply(new BigDecimal(bigMapWidth)).intValue();
                        width += viewWidth;
                    }
                    mBitmapWidth = width;
                    emitter.onNext(width);
                }

            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(new Observer<Integer>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(Integer o) {
                if (type == 1) {
                    mBitmapWidth = DimenUtil.getScreenWidth() * 5 / 7;
                    mBitmapHeight = o;
                    mAdjustSeekBar.setProgress(100);
                } else if (type == 0) {
                    mBitmapHeight = DimenUtil.getScreenHeight() / 2;
                    mBitmapWidth = o;
                    mAdjustSeekBar.setProgress(100);
                }
                //查找最佳输出大小
                //太大的图片无法更新至图库,此处实际为压缩图片操作
                //1.原图大小
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
        view.findViewById(R.id.btn_save).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int size = mBitmapHeight * mBitmapWidth;
                if (size > 60000 * 1000) {
                    ToastUtil.showToast("图片太大了!调整大小或重新编辑");
                    return;
                }
                save();
            }
        });
        mAdjustSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                //10-100;
                if (progress < 10) {
                    progress = 10;
                }

                calculateBitmapSize(progress);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
    }

    private int calculateWidth;
    private int calculateHeight;

    private void calculateBitmapSize(int progress) {
        BigDecimal bigDecimal = new BigDecimal(progress);
        mScaleSize = bigDecimal.divide(new BigDecimal(100), 2, RoundingMode.HALF_DOWN).floatValue();
        calculateWidth = (int) (mBitmapWidth * mScaleSize);
        calculateHeight = (int) (mBitmapHeight * mScaleSize);
        textView.setText(String.format("输出的尺寸为:%d*%d", calculateWidth, calculateHeight));
        if (type == 1) {
            processView.setText(String.valueOf(calculateWidth));
        } else if (type == 0) {
            processView.setText(String.valueOf(calculateHeight));
        }
    }
}
