package com.weilai.jigsawpuzzle.weight.puzzleLP;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.util.DimenUtil;
import com.weilai.jigsawpuzzle.util.L;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * * DATE: 2022/10/8
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleLpEditView extends View {
    private Paint mPaint;
    private Bitmap mBitmap;
    private boolean canScrollUp;
    private boolean canDraw;
    private float mCurrentY;
    private float mMoveY;
    private float mLastY;
    private float mScaleSize;

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
        canScrollUp = typedArray.getBoolean(R.styleable.PuzzleLpEditView_scroll_up, false);
        typedArray.recycle();
    }

    public final Bitmap saveBitmap() {
        if (mBitmap != null) {
//            return Bitmap.createBitmap(mBitmap, 0,0, mBitmap.getWidth(), );
        }
        return null;
    }

    public final void setBitmap(Bitmap bitmap) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Matrix mMatrix = new Matrix();
        int parentWidth = DimenUtil.getScreenWidth();
        int bitmapWidth = bitmap.getWidth();
        BigDecimal parent = new BigDecimal(parentWidth);
        mScaleSize = parent.divide(new BigDecimal(bitmapWidth), 2, RoundingMode.HALF_DOWN).floatValue();
        mMatrix.setScale(mScaleSize, mScaleSize);
        mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mMatrix, true);
        canDraw = true;
        invalidate();
    }

    private float mBitmapSize;

    @Override
    protected void onDraw(Canvas canvas) {
        if (canDraw) {
            if (canScrollUp) {
                //只能向上滑动
                if (mMoveY > 0) {
                    mMoveY = 0;
                    mLastY = 0;
                }
                if (mMoveY < -mBitmap.getHeight()) {
                    mMoveY = -mBitmap.getHeight();
                }
                canvas.drawBitmap(mBitmap, 0, 0 + mMoveY, mPaint);

            } else {
                //只能向下滑动
                int canvasHeight = getHeight();
                int bitmapHeight = mBitmap.getHeight();
                if (mMoveY < 0) {
                    mMoveY = 0;
                    mLastY = 0;
                }
                if (mMoveY > mBitmap.getHeight()) {
                    mMoveY = mBitmap.getHeight();
                }
                canvas.drawBitmap(mBitmap, 0, -bitmapHeight + canvasHeight + mMoveY, mPaint);
                mBitmapSize = bitmapHeight ;
            }
            L.e(mBitmapSize+"");
        }
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mBitmap != null) {
                    if (canScrollUp) {
                        if (mMoveY < -mBitmap.getHeight()) {
                            mMoveY = -mBitmap.getHeight();
                        }
                    } else {
                        if (mMoveY > mBitmap.getHeight()) {
                            mMoveY = mBitmap.getHeight();
                        }
                    }
                    mLastY = mMoveY;
                }

                break;
            case MotionEvent.ACTION_MOVE:
                float moveOffset = mCurrentY - event.getY();
                if (moveOffset > 0) {
                    mMoveY = mLastY - moveOffset;
                } else if (moveOffset < 0) {
                    mMoveY = mLastY - moveOffset;
                }
                invalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                mCurrentY = event.getY();
                break;

            default:
                break;

        }
        return true;
    }
}
