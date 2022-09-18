package com.weilai.jigsawpuzzle.weight.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.luck.picture.lib.entity.LocalMedia;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.L;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import io.reactivex.rxjava3.internal.util.LinkedArrayList;

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
    private Bitmap mTestBitmap;
    private final LinkedList<TemplateViewInfo> mAreaTouch = new LinkedList<>();


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
        int mSize = mAreaTouch.size();
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

                if (angle == 0) {
                    @SuppressLint("DrawAllocation")
                    RectF rectF = new RectF(outAreaLeftX, outAreaLeftY, outAreaRightX, outAreaRightY);
                    TemplateViewInfo templateViewInfo;
                    if (mSize > 0) {
                        templateViewInfo = mAreaTouch.get(i);
                        if (!TextUtils.isEmpty(templateViewInfo.getUrl())) {
                            //不是占位图
                            canvas.drawBitmap(templateViewInfo.getBitmap(), null, rectF, mPaint);
                        } else {
                            canvas.drawBitmap(mTouchBitmap, null, rectF, mPaint);
                        }
                        mAreaTouch.remove(i);
                    } else {
                        canvas.drawBitmap(mTouchBitmap, null, rectF, mPaint);
                        //收录一下绘制范围，做监听使用,是否有圖片等
                        templateViewInfo = new TemplateViewInfo(outAreaLeftX, outAreaLeftY, outAreaRightX, outAreaRightY, i, false, null);
                    }
                    mAreaTouch.add(i, templateViewInfo);

                } else {
                    double v0 = (outAreaLeftX - centerX) * Math.cos(radians);
                    double v1 = (outAreaLeftY - centerY) * Math.cos(radians);
                    double v2 = (outAreaRightX - centerX) * Math.cos(radians);
                    double v3 = (outAreaRightY - centerY) * Math.cos(radians);
                    double v4 = (outAreaLeftY - centerY) * Math.sin(radians);
                    double v5 = (outAreaLeftX - centerX) * Math.sin(radians);
                    double v6 = (outAreaRightY - centerY) * Math.sin(radians);
                    double v7 = (outAreaRightX - centerX) * Math.sin(radians);
                    if (angle > 0) { //顺时针旋转
                        @SuppressLint("DrawAllocation")
                        int beforeRotateLeftX = (int) (v0 + v4 + centerX);
                        int beforeRotateLeftY = (int) (v1 - v5 + centerY);
                        int beforeRotateRightX = (int) (v2 + v6 + centerX);
                        int beforeRotateRightY = (int) (v3 - v7 + centerY);
                        RectF rectF = new RectF(beforeRotateLeftX, beforeRotateLeftY, beforeRotateRightX, beforeRotateRightY);
                        Matrix matrix = new Matrix();
                        matrix.setRotate(angle, centerX, centerY);
                        matrix.mapRect(rectF);
                        TemplateViewInfo templateViewInfo;
                        if (mSize > 0) {

                            templateViewInfo = mAreaTouch.get(i);
                            if (!TextUtils.isEmpty(templateViewInfo.getUrl())) {
                                //不是占位图
                                canvas.drawBitmap(mTestBitmap, null, rectF, mPaint);
                            } else {
                                canvas.drawBitmap(mTouchBitmap, null, rectF, mPaint);
                            }
                            mAreaTouch.remove(i);
                        } else {
                            canvas.drawBitmap(mTouchBitmap, null, rectF, mPaint);
                            //收录一下绘制范围，做监听使用,是否有圖片等
                            templateViewInfo = new TemplateViewInfo(beforeRotateLeftX, beforeRotateLeftY, beforeRotateRightX, beforeRotateRightY, i, false, null);
                        }
                        mAreaTouch.add(i, templateViewInfo);
                        canvas.drawBitmap(mTouchBitmap, matrix, mPaint);
                    } else if (angle < 0) { //逆时针旋转
                        int beforeRotateLeftX = (int) (v0 - v4 + centerX);
                        int beforeRotateLeftY = (int) (v1 + v5 + centerY);
                        int beforeRotateRightX = (int) (v2 - v6 + centerX);
                        int beforeRotateRightY = (int) (v3 + v7 + centerY);
                        RectF rectF = new RectF(beforeRotateLeftX, beforeRotateLeftY, beforeRotateRightX, beforeRotateRightY);
                        Matrix matrix = new Matrix();
                        matrix.setRotate(angle, centerX, centerY);
                        matrix.mapRect(rectF);
                        TemplateViewInfo templateViewInfo;
                        if (mSize > 0) {
                            templateViewInfo = mAreaTouch.get(i);
                            if (!TextUtils.isEmpty(templateViewInfo.getUrl())) {
                                //不是占位图
                                canvas.drawBitmap(mTestBitmap, null, rectF, mPaint);
                            } else {
                                canvas.drawBitmap(mTouchBitmap, null, rectF, mPaint);
                            }
                            mAreaTouch.remove(i);
                        } else {
                            canvas.drawBitmap(mTouchBitmap, null, rectF, mPaint);
                            //收录一下绘制范围，做监听使用,是否有圖片等
                            templateViewInfo = new TemplateViewInfo(beforeRotateLeftX, beforeRotateLeftY, beforeRotateRightX, beforeRotateRightY, i, false, null);
                        }
                        mAreaTouch.add(i, templateViewInfo);
                    }
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

    public final void setBitMapFromClient(LocalMedia localMedia) {
        /*用戶設置圖片進View中*/
        TemplateViewInfo templateViewInfo = mAreaTouch.get(mSettingPosition);
        templateViewInfo.setHasPic(true);
        templateViewInfo.setLocalMedia(localMedia);
        templateViewInfo.setUrl(localMedia.getAvailablePath());
        Uri uri = Uri.fromFile(new File(localMedia.getAvailablePath()));
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
            templateViewInfo.setBitmap(bitmap);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }


        mSettingPosition = -1;
        invalidate();
    }

    public final LocalMedia getInfoFromView() {
        if (mSettingPosition >= 0) {
            TemplateViewInfo templateViewInfo = mAreaTouch.get(mSettingPosition);
            return templateViewInfo.getLocalMedia();
        }
        return null;
    }

    private boolean hasTarget;

    public interface OutRectClickListener {
        boolean onRectClick(boolean hasPic);
    }

    private int mSettingPosition;
    private long mLastTime;

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_DOWN:
                int upX = (int) event.getX();
                int upY = (int) event.getY();
                long currentTime = 0;
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    currentTime = System.currentTimeMillis();
                }
                for (TemplateViewInfo templateViewInfo : mAreaTouch) {
                    if (upX < templateViewInfo.getLeftX() || upX > templateViewInfo.getRightX()) {
                        continue;
                    }
                    if (upY < templateViewInfo.getLeftY() || upY > templateViewInfo.getRightY()) {
                        continue;
                    }
                    if (event.getAction() == MotionEvent.ACTION_UP && hasTarget) {
                        hasTarget = false;
                        if (listener != null) {
                            listener.onRectClick(templateViewInfo.hasPic());
                            mSettingPosition = templateViewInfo.getPosition();
                            break;
                        }
                    } else if (event.getAction() == MotionEvent.ACTION_DOWN) {
                        if (currentTime - mLastTime < 500) {
                            break;
                        }
                        mLastTime = currentTime;
                        hasTarget = true;
                    }
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
        mTestBitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_home_tab1);
    }
}
