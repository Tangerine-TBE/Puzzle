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
    private int scrollDirection;
    private boolean canDraw;
    private float mCurrentY;
    private float mMoveY;
    private float mLastY;
    private float mCurrentX;
    private float mMoveX;
    private float mLastX;
    private float mScaleSize;
    private int templateBitmapWidth;
    private static final int LEFT = 0;
    private static final int RIGHT = 1;
    private static final int TOP = 2;
    private static final int BOTTOM = 3;
    private int templateBitmapHeight;


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

    public final Bitmap saveBitmap() {

        if (mBitmap != null) {
            Matrix matrix = new Matrix();
            if (BOTTOM == scrollDirection || TOP == scrollDirection) {
                int parentWidth = DimenUtil.getScreenWidth();
                BigDecimal templateBitmap = new BigDecimal(templateBitmapWidth);
                mScaleSize = templateBitmap.divide(new BigDecimal(parentWidth), 2, RoundingMode.HALF_DOWN).floatValue();
                matrix.setScale(mScaleSize, mScaleSize);//还原大小
            }else if (RIGHT == scrollDirection || LEFT == scrollDirection){
                int parentHeight = DimenUtil.getScreenHeight() /2;
                BigDecimal templateBitmap = new BigDecimal(templateBitmapHeight);
                mScaleSize = templateBitmap.divide(new BigDecimal(parentHeight),2,RoundingMode.HALF_DOWN).floatValue();
                matrix.setScale(mScaleSize,mScaleSize);
            }
            if (BOTTOM == scrollDirection) {
                return Bitmap.createBitmap(mBitmap, 0, 0, mBitmap.getWidth(), (int) (mBitmap.getHeight() - mMoveY), matrix, true);
            } else if (TOP == scrollDirection) {
                return Bitmap.createBitmap(mBitmap, 0, (int) Math.abs(mMoveY), mBitmap.getWidth(), (int) (mBitmap.getHeight() + mMoveY), matrix, true);
            } else if (RIGHT == scrollDirection) {
                return Bitmap.createBitmap(mBitmap, 0, 0, (int) (mBitmap.getWidth() - mMoveX), mBitmap.getHeight(), matrix, true);
            } else if (LEFT == scrollDirection) {
                return Bitmap.createBitmap(mBitmap, (int) Math.abs(mMoveX), 0, (int) (mBitmap.getWidth() + mMoveX), mBitmap.getHeight(), matrix, true);
            }
        }

        return null;
    }

    public final void setBitmap(Bitmap bitmap) {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        Matrix mMatrix = new Matrix();
        if (scrollDirection == TOP || scrollDirection == BOTTOM) {
            int parentWidth = DimenUtil.getScreenWidth();
            templateBitmapWidth = bitmap.getWidth();
            BigDecimal parent = new BigDecimal(parentWidth);
            mScaleSize = parent.divide(new BigDecimal(templateBitmapWidth), 2, RoundingMode.HALF_DOWN).floatValue();
            mMatrix.setScale(mScaleSize, mScaleSize);
            mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mMatrix, true);
            canDraw = true;
            invalidate();
        } else if (scrollDirection == LEFT || scrollDirection == RIGHT) {
            int parentHeight = DimenUtil.getScreenHeight() / 2;
            templateBitmapHeight = bitmap.getHeight();
            BigDecimal parent = new BigDecimal(parentHeight);
            mScaleSize = parent.divide(new BigDecimal(templateBitmapHeight), 2, RoundingMode.HALF_DOWN).floatValue();
            mMatrix.setScale(mScaleSize, mScaleSize);
            mBitmap = Bitmap.createBitmap(bitmap, 0, 0, bitmap.getWidth(), bitmap.getHeight(), mMatrix, true);
            canDraw = true;
            invalidate();
        }
    }


    @Override
    protected void onDraw(Canvas canvas) {
        if (canDraw) {
            if (TOP == scrollDirection) {
                //只能向上滑动
                if (mMoveY > 0) {
                    mMoveY = 0;
                    mLastY = 0;
                }
                if (mMoveY < -mBitmap.getHeight()) {
                    mMoveY = -mBitmap.getHeight();
                }
                canvas.drawBitmap(mBitmap, 0, 0 + mMoveY, mPaint);
            } else if (BOTTOM == scrollDirection) {
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
            } else if (LEFT == scrollDirection) {
                //只能向左滑动
                if (mMoveX > 0) {
                    mMoveX = 0;
                    mLastX = 0;
                }
                if (mMoveX < -mBitmap.getWidth()) {
                    mMoveX = -mBitmap.getWidth();
                }
                float top = (getHeight() - mBitmap.getHeight()) / 2;//只为居中显示
                canvas.drawBitmap(mBitmap, 0 + mMoveX, top, mPaint);
            } else if (RIGHT == scrollDirection) {
                //只能向右滑动
                float top = (getHeight() - mBitmap.getHeight()) / 2;
                int canvasWidth = getWidth();
                int bitmapWidth = mBitmap.getWidth();
                if (mMoveX < 0) {
                    mMoveX = 0;
                    mLastX = 0;
                }
                if (mMoveX > mBitmap.getWidth()) {
                    mMoveX = mBitmap.getWidth();
                }
                canvas.drawBitmap(mBitmap, -bitmapWidth + canvasWidth + mMoveX, top, mPaint);
            }

        }
        super.onDraw(canvas);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mBitmap != null) {
                    if (TOP == scrollDirection) {
                        if (mMoveY < -mBitmap.getHeight()) {
                            mMoveY = -mBitmap.getHeight();
                        }
                        mLastY = mMoveY;
                    } else if (BOTTOM == scrollDirection) {
                        if (mMoveY > mBitmap.getHeight()) {
                            mMoveY = mBitmap.getHeight();
                        }
                        mLastY = mMoveY;
                    } else if (LEFT == scrollDirection) {
                        if (mMoveX < -mBitmap.getWidth()) {
                            mMoveX = -mBitmap.getWidth();
                        }
                        mLastX = mMoveX;
                    } else if (RIGHT == scrollDirection) {
                        if (mMoveX > mBitmap.getWidth()) {
                            mMoveX = mBitmap.getWidth();
                        }
                        mLastX = mMoveX;
                    }
                }

                break;
            case MotionEvent.ACTION_MOVE:
                if (TOP == scrollDirection || BOTTOM == scrollDirection) {
                    float moveOffset = mCurrentY - event.getY();
                    if (moveOffset > 0) {
                        mMoveY = mLastY - moveOffset;
                    } else if (moveOffset < 0) {
                        mMoveY = mLastY - moveOffset;
                    }
                } else if (LEFT == scrollDirection || RIGHT == scrollDirection) {
                    float moveOffset = mCurrentX - event.getX();
                    if (moveOffset > 0) {
                        mMoveX = mLastX - moveOffset;
                    } else if (moveOffset < 0) {
                        mMoveX = mLastX - moveOffset;
                    }
                }

                invalidate();
                break;
            case MotionEvent.ACTION_DOWN:
                mCurrentY = event.getY();
                mCurrentX = event.getX();
                break;

            default:
                break;

        }
        return true;
    }
}
