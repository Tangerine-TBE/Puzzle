package com.weilai.jigsawpuzzle.weight.template;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.view.Display;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.util.L;

import java.security.Security;
import java.util.ArrayList;
import java.util.List;

import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.core.Observable;
import io.reactivex.rxjava3.core.ObservableEmitter;
import io.reactivex.rxjava3.core.ObservableOnSubscribe;
import io.reactivex.rxjava3.core.Observer;
import io.reactivex.rxjava3.core.Scheduler;
import io.reactivex.rxjava3.disposables.Disposable;
import io.reactivex.rxjava3.functions.Consumer;
import io.reactivex.rxjava3.schedulers.Schedulers;

/**
 * * DATE: 2022/9/15
 * * Author:tangerine
 * * Description: For TemplateEditActivity
 **/
public class TemplateEditView extends View {
    /**
     ** DATE: 2022/9/15
     ** Author:tangerine
     ** Description:libOpencvSp.So handle the bitmap
     **/
    static {
        System.loadLibrary("opencvSp");
    }

    private native void imageEdgeCutting(Bitmap bitmap, int type);

    private ArrayList<BitMapInfo> bitMapInfos;

    private Bitmap mBitmap;
    private Paint mPaint;
    private boolean initView;

    public TemplateEditView(Context context) {
        this(context, null);
    }

    public TemplateEditView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemplateEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        bitMapInfos = new ArrayList<>();
        initView = false;
    }

    /**
     * * DATE: 2022/9/15
     * * Author:tangerine
     * * Description:For jni implementation ,do not use it !
     **/
    public void setListBitmapSize(BitMapInfo bitMapInfo) {
        if (bitMapInfos != null) {
            bitMapInfos.add(bitMapInfo);
        }
    }

    /**
     * * DATE: 2022/9/15
     * * Author:tangerine
     * * Description:Test status type is 1; 1 should be Rect 2 should be Cycle 3 should be others
     **/
    public final void computeBitmap(Bitmap bitmap) {
        //show loading
        //run in the thread;
        Observable.create(new ObservableOnSubscribe<Object>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<Object> emitter) throws Throwable {
                imageEdgeCutting(bitmap, 1);
                emitter.onNext("1");
            }
        }).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(o -> {
                    if (bitMapInfos != null) {
                        if (!bitMapInfos.isEmpty()) {
                            setTemplateBitmap(bitmap);
                        }
                    }
                }
        );

        //stop loading
    }

    /**
     * * DATE: 2022/9/15
     * * Author:tangerine
     * * Description:before set up the bitmap should load the bitMapInfo for CutoutArea
     **/
    private void setTemplateBitmap(Bitmap bitmap) {
        mBitmap = bitmap;
        initView = true;
        invalidate();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (initView){
            //根據圖片先計算中心點
            canvas.drawBitmap(mBitmap,0,0,mPaint);
            for (BitMapInfo bitMapInfo :bitMapInfos){
                Rect rect = new Rect(bitMapInfo.getX(),bitMapInfo.getY(),bitMapInfo.getX()+bitMapInfo.getWidth(),bitMapInfo.getY()+bitMapInfo.getHeight());
                canvas.drawRect(rect,mPaint);
            }
        }else{
            canvas.drawColor(getResources().getColor(R.color.card_view_color));
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        return true;
    }
}
