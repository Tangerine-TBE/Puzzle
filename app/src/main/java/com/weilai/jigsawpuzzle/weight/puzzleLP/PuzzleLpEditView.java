package com.weilai.jigsawpuzzle.weight.puzzleLP;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.BitmapRegionDecoder;
import android.graphics.Canvas;
import android.graphics.Rect;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.bean.PicInfo;
import com.weilai.jigsawpuzzle.util.FileUtil;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * * DATE: 2022/10/18
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleLpEditView extends View {
    private final Rect mRect = new Rect();
    private int scrollDirection;
    private BitmapFactory.Options options;
    private boolean canDraw;
    private float mCurrentY;
    private float mMoveY;
    private float mLastY;
    private float mMoveX;
    private float mCurrentX;
    private float mLastX;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int TOP = 2;
    private static final int BOTTOM = 3;
    private int parentWidth;
    private int parentHeight;
    private int templateBitmapWidth;
    private int templateBitmapHeight;
    private BitmapRegionDecoder mDecoder;
    private String mPath;


    public PuzzleLpEditView(Context context) {
        this(context, null);
    }

    public PuzzleLpEditView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PuzzleLpEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(attrs);
    }

    private void init(AttributeSet attrs) {
        TypedArray typedArray = getContext().getTheme().obtainStyledAttributes(attrs, R.styleable.PuzzleLpEditView, 0, 0);
        scrollDirection = typedArray.getInt(R.styleable.PuzzleLpEditView_scroll_direction, -1);
        typedArray.recycle();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        parentWidth = getMeasuredWidth();
        parentHeight = getMeasuredHeight();
    }

    public final void setImage(String path) {
        mPath = path;
        Observable.create(new ObservableOnSubscribe<BitmapRegionDecoder>() {
            @Override
            public void subscribe(ObservableEmitter<BitmapRegionDecoder> emitter) throws Exception {
                Uri srcUri;
                if (PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)) {
                    srcUri = Uri.parse(path);
                } else {
                    srcUri = Uri.fromFile(new File(path));
                }
                if (srcUri != null) {
                    InputStream stream = null;
                    InputStream stream1 = null;
                    try {
                        stream = getContext().getContentResolver().openInputStream(srcUri);
                        //1.获取图片大小
                        options = new BitmapFactory.Options();
                        BitmapFactory.decodeStream(stream, null, options);
                        options.inJustDecodeBounds = true;
                        templateBitmapHeight = options.outHeight;
                        templateBitmapWidth = options.outWidth;
                        stream1 = getContext().getContentResolver().openInputStream(srcUri);
                        mDecoder = BitmapRegionDecoder.newInstance(stream1, false);
                        emitter.onNext(mDecoder);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<BitmapRegionDecoder>() {
            @Override
            public void onSubscribe(Disposable d) {

            }

            @Override
            public void onNext(BitmapRegionDecoder o) {
                canDraw = true;
                invalidate();
            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onComplete() {

            }
        });
    }

    public final Observable<String> saveImage() {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                if (TextUtils.isEmpty(mPath)) {
                    emitter.onNext("");
                    return;
                }
                Uri srcUri;
                if (PictureMimeType.isContent(mPath) || PictureMimeType.isHasHttp(mPath)) {
                    srcUri = Uri.parse(mPath);
                } else {
                    srcUri = Uri.fromFile(new File(mPath));
                }
                    InputStream stream = getContext().getContentResolver().openInputStream(srcUri);
                    mDecoder = BitmapRegionDecoder.newInstance(stream, false);
                    Bitmap bitmap = null;
                    if (BOTTOM == scrollDirection) {
                        if (mDecoder != null) {
                            Rect rect = new Rect(0, 0, templateBitmapWidth, (int) (templateBitmapHeight - mMoveY));
                            bitmap = mDecoder.decodeRegion(rect, null);
                        }
                    } else if (TOP == scrollDirection) {
                        if (mDecoder != null) {
                            Rect rect = new Rect(0, (int) Math.abs(mMoveY), templateBitmapWidth, (int) (templateBitmapHeight + mMoveY));
                            bitmap = mDecoder.decodeRegion(rect, null);
                        }
                    } else if (RIGHT == scrollDirection) {
                        if (mDecoder != null) {
                            Rect rect = new Rect(0, 0, (int) (templateBitmapWidth - mMoveX), templateBitmapHeight);
                            bitmap = mDecoder.decodeRegion(rect, null);
                        }
                    } else if (LEFT == scrollDirection) {
                        if (mDecoder != null) {
                            Rect rect = new Rect((int) Math.abs(mMoveX), 0, (int) (templateBitmapWidth + mMoveX), templateBitmapHeight);
                            bitmap = mDecoder.decodeRegion(rect, null);
                        }
                    }
                    emitter.onNext(FileUtil.saveScreenShot(bitmap, System.currentTimeMillis() + ""));

            }
        });

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (canDraw) {
            if (BOTTOM == scrollDirection) {
                if (mMoveY < 0) {
                    //这里限制了移动，bottom只能向下移动
                    mMoveY = 0;
                    mLastY = 0;
                }

                if (mMoveY >= templateBitmapHeight) {
                    mMoveY = templateBitmapHeight;
                }
                int top = (int) (templateBitmapHeight - parentHeight - mMoveY);
                int bottom = (int) (templateBitmapHeight - mMoveY);
                if (bottom <= 0) {
                    return;
                }
                mRect.set(0, top, templateBitmapWidth, bottom);
            } else if (TOP == scrollDirection) {
                if (mMoveY > 0) {
                    mMoveY = 0;
                    mLastY = 0;
                }
                if (mMoveY < -templateBitmapHeight) {
                    mMoveY = -templateBitmapHeight;
                }
                int top = (int) (0 - mMoveY);
                int bottom = (int) (parentHeight - mMoveY);
                if (bottom >= templateBitmapHeight + parentHeight) {
                    return;
                }
                mRect.set(0, top, parentWidth, bottom);
            } else if (LEFT == scrollDirection) {
                if (mMoveX > 0) {
                    mMoveX = 0;
                    mLastX = 0;
                }
                if (mMoveX < -templateBitmapWidth) {
                    mMoveX = -templateBitmapWidth;
                }
                int left = (int) (0 - mMoveX);
                int right = (int) (parentWidth - mMoveX);
                if (right >= templateBitmapWidth + parentWidth) {
                    return;
                }
                mRect.set(left, 0, right, templateBitmapHeight);
            } else if (RIGHT == scrollDirection) {
                if (mMoveX < 0) {
                    mMoveX = 0;
                    mLastX = 0;
                }
                if (mMoveX > templateBitmapWidth) {
                    mMoveX = templateBitmapWidth;
                }
                int left = (int)(templateBitmapWidth-parentWidth-mMoveX);
                int right = (int) (templateBitmapWidth - mMoveX);
                if (right<=0){
                    return;
                }
                mRect.set(left,0,right,templateBitmapHeight);
            }
            Bitmap bm = mDecoder.decodeRegion(mRect, options);
            //横坐标居中
            if (TOP == scrollDirection || BOTTOM == scrollDirection) {
                canvas.drawBitmap(bm, (parentWidth - templateBitmapWidth) / 2, 0, null);
            } else if (RIGHT == scrollDirection || LEFT == scrollDirection) {
                canvas.drawBitmap(bm, 0, (parentHeight - templateBitmapHeight) / 2, null);
            }
            if (!bm.isRecycled()) {
                bm.recycle();
            }
        }

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_MOVE:
                if (TOP == scrollDirection || BOTTOM == scrollDirection){
                    float offsetY = mCurrentY - event.getY();
                    mMoveY = mLastY - offsetY;
                }else if (LEFT == scrollDirection || RIGHT == scrollDirection){
                    float offsetX = mCurrentX - event.getX();
                    mMoveX = mLastX - offsetX;
                }

                postInvalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                mCurrentY = event.getY();
                mCurrentX = event.getX();
                break;
            case MotionEvent.ACTION_UP:
                if (TOP == scrollDirection) {
                    if (mMoveY < -templateBitmapHeight) {
                        mMoveY = -templateBitmapHeight;
                    }
                    mLastY = mMoveY;
                } else if (BOTTOM == scrollDirection) {
                    if (mMoveY > templateBitmapHeight) {
                        mMoveY = templateBitmapHeight;
                    }
                    mLastY = mMoveY;
                } else if (LEFT == scrollDirection) {
                    if (mMoveX < -templateBitmapWidth) {
                        mMoveX = -templateBitmapWidth;
                    }
                    mLastX = mMoveX;
                } else if (RIGHT == scrollDirection) {
                    if (mMoveX > templateBitmapWidth) {
                        mMoveX = templateBitmapWidth;
                    }
                    mLastX = mMoveX;
                }
                break;
        }
        return true;
    }

}
