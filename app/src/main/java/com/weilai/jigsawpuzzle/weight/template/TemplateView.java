package com.weilai.jigsawpuzzle.weight.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.util.FileUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * @description:
 * @author: luo
 * @date: 2022/9/16
 */
public class TemplateView extends View {
    private Paint mPaint;
    private Bitmap mTouchBitmap;
    private Bitmap mTemplateBitmap;
    private int mTemplateHeight;
    private int mTemplateWidth;
    private boolean mDrawView;
    private BitMapInfo mBitmapInfo;
    private final ArrayList<int[]> mAreaTouch = new ArrayList<>();//int 0 Left 1 Top 2 Right 3 Bottom ,4 position,5 hasPic(1 no 2 yes)


    public TemplateView(Context context) {
        this(context, null);
    }

    public TemplateView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemplateView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    public final void setTemplateBitmap(BitMapInfo bitMapInfo) {
        if (bitMapInfo != null) {
            mBitmapInfo = bitMapInfo;
            mTemplateBitmap = FileUtil.getBitmapFromCache(mBitmapInfo.getBitmap());
            if (mTemplateBitmap != null) {
                mTemplateWidth = mTemplateBitmap.getWidth();
                mTemplateHeight = mTemplateBitmap.getHeight();
                mDrawView = true;
                invalidate();
            }
            /*异常！*/

        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (mDrawView) {
            int scaleWidth = getWidth() / 2;
            int scaleHeight = getHeight() / 2;
            int templateX = scaleWidth - mTemplateWidth / 2; //模板圖為居中 x偏移量
            int templateY = scaleHeight - mTemplateHeight / 2;//模板圖為居中 y偏移量
            //计算真实的x坐标与y坐标
            //根据角度，x,y比例绘制底层图
            List<BitMapInfo.SizeInfo> sizeInfos = mBitmapInfo.getSizeInfos();
            for (int i = 0; i < mBitmapInfo.getSizeInfos().size(); i++) {
                //获取旋转角度
                float angle = sizeInfos.get(i).getAngle();
                //中心点位置计算
                float width = sizeInfos.get(i).getCenterX();//目標的中心点x占比
                float height = sizeInfos.get(i).getCenterY();//目標的中心点y 占比
                int centerY = (int) (mTemplateHeight * height); //目標在原圖上的中心点高度
                int centerX = (int) (mTemplateWidth * width);//目標在原圖上的中心点寬度
                //在图片上初始的左上角坐标计算
                float scaleX = sizeInfos.get(i).getX();
                float scaleY = sizeInfos.get(i).getY();
                int x = (int) (mTemplateWidth * scaleX);//在圖片上的x 起始
                int y = (int) (mTemplateHeight * scaleY);//在圖片上的y 起始
                //根据比例以及偏移量计算左上 右下 坐标
                int outAreaLeftX = templateX + x;
                int outAreaLeftY = templateY + y;
                int outAreaRightX = templateX + x + centerX * 2;
                int outAreaRightY = templateY + y + centerY * 2;
                //计算选择前的坐标 以centerX centerY 为中心开始 逆时针旋转 旋转后的坐标为
                //以(x0,y0)为旋转中心点，
                //逆时针旋转30度  -30
                //那么我要复原就是 +30 顺时针旋转
                //已经知旋转前点的位置(x1,y1)和旋转的角度(a)，求旋转后点的新位置(x2,y2)
                double degrees = 30;
                double radians = Math.toRadians(degrees);
                //获取逆时针旋转前的坐标
                int beforeRotateLeftX = (int) ((outAreaLeftX - centerX) * Math.cos(radians) - (outAreaLeftY - centerY) * Math.sin(radians) + centerX);
                int beforeRotateLeftY = (int) ((outAreaLeftY - centerY) * Math.cos(radians) + (outAreaLeftX - centerX) * Math.sin(radians) + centerY);
                int beforeRotateRightX = (int) ((outAreaRightX - centerX) * Math.cos(radians) - (outAreaRightY - centerY) * Math.sin(radians) + centerX);
                int beforeRotateRightY = (int) ((outAreaRightY - centerY) * Math.cos(radians) - (outAreaRightX - centerX) * Math.sin(radians) + centerY);
                if (angle == 0) {
                    @SuppressLint("DrawAllocation")
                    RectF rectF = new RectF(outAreaLeftX, outAreaLeftY, outAreaRightX, outAreaRightY);
                    canvas.drawBitmap(mTouchBitmap, null, rectF, mPaint);
                    //收录一下绘制范围，做监听使用,是否有圖片等
                    int[] touchArea = {outAreaLeftX, outAreaLeftY, outAreaRightX, outAreaRightY, i, 1};
                    mAreaTouch.add(touchArea);
                } else {
                    @SuppressLint("DrawAllocation")
                    RectF rectF = new RectF(beforeRotateLeftX, beforeRotateLeftY, beforeRotateRightX, beforeRotateRightY);
                    Matrix matrix = new Matrix();
                    matrix.setRotate(angle, centerX, centerY);
                    matrix.mapRect(rectF);
                    canvas.drawBitmap(mTouchBitmap, matrix, mPaint);
                    //收录一下绘制范围，做监听使用,是否有圖片等
                    int[] touchArea = {beforeRotateLeftX, beforeRotateLeftY, beforeRotateRightX, beforeRotateRightY, i, 1};
                    mAreaTouch.add(touchArea);
                }

            }
            canvas.drawBitmap(mTemplateBitmap, templateX, templateY, mPaint);
        }
        super.onDraw(canvas);
    }

    private OutRectClickListener listener;

    public final void setOutRectClickListener(OutRectClickListener listener) {
        this.listener = listener;
    }

    public final void setBitMapFromClient(Bitmap bitmap, int position) {
        /*用戶設置圖片進View中*/
        int[] picArea = mAreaTouch.get(position);
        picArea[5] = 2;
    }

    private boolean hasTarget;

    public interface OutRectClickListener {
        void onRectClick(int position, boolean hasPic);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                //抬手的時候也要監聽按下的位置是否也在相應區域
                if (hasTarget) {
                    int upX = (int) event.getX();
                    int upY = (int) event.getY();
                    for (int[] area : mAreaTouch) {
                        if (upX < area[0] || upX > area[2]) {
                            continue;
                        }
                        if (upY < area[1] || upY > area[3]) {
                            continue;
                        }
                        //如果本次up的位置也对,则进行事件监听回调
                        if (listener != null) {
                            listener.onRectClick(area[4], area[5] != 1);
                        }
                        hasTarget = false;
                        break;
                    }
                }
                break;
            case MotionEvent.ACTION_DOWN:
                //下手的位置
                int downX = (int) event.getX();
                int downY = (int) event.getY();
                for (int[] area : mAreaTouch) {
                    if (downX < area[0] || downX > area[2]) {
                        continue;
                    }
                    if (downY < area[1] || downY > area[3]) {
                        continue;
                    }
                    //如果在本次遍历找到则break掉不再循环
                    hasTarget = true;
                    break;
                }
                break;
            default:
                break;
        }
        return true;
    }


    private void init() {
        mPaint = new Paint();
        mTouchBitmap = BitmapFactory.decodeResource(getResources(), R.drawable.bb);
        //加载一个占位图
    }
}
