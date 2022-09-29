package com.weilai.jigsawpuzzle.weight.template;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Region;
import android.net.Uri;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.luck.picture.lib.entity.LocalMedia;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.net.netInfo.BitMapInfo;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.L;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * * DATE: 2022/9/20
 * * Author:tangerine
 * * Description:
 **/
public class TemplateView extends View {
    private Paint mPaint;
    private Bitmap mTemplate11;
    private Bitmap mTemplateBitmap;
    private int mTemplateHeight;
    private int mTemplateWidth;
    private boolean mDrawView;
    private BitMapInfo mBitmapInfo;
    private Bitmap mTemplate32;
    private boolean shouldCreateBitmap;
    private final ArrayList<TemplateViewInfo> mAreaTouch = new ArrayList<>();

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

    public final int getSettingPosition() {
        return mSettingPosition;
    }

    public final ArrayList<TemplateViewInfo> getTemplateViewInfo() {
        return mAreaTouch;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        int mSize = mAreaTouch.size();
        //不接受回调地狱
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
                int outAreaRightX = templateX + centerX - x + centerX;
                int outAreaRightY = templateY + centerY - y + centerY;
                int centerYInTemplate = centerY + templateY;
                int centerXInTemplate = centerX + templateX;
                //计算选择前的坐标 以centerX centerY 为中心开始 逆时针旋转 旋转后的坐标为
                //以(x0,y0)为旋转中心点，
                //逆时针旋转30度  -30
                //那么我要复原就是 +30 顺时针旋转
                //已经知旋转前点的位置(x1,y1)和旋转的角度(a)，求旋转后点的新位置(x2,y2)
                double radians = Math.toRadians(angle);
                //获取逆时针旋转前的坐标
                if (angle == 0) {
                    @SuppressLint("DrawAllocation")
                    Rect rect = new Rect(outAreaLeftX, outAreaLeftY, outAreaRightX, outAreaRightY);
                    TemplateViewInfo templateViewInfo = null;
                    BitMapInfo.SizeInfo sizeInfo = sizeInfos.get(i);
                    if (mSize > 0) {
                        templateViewInfo = mAreaTouch.get(i);
                        if (!TextUtils.isEmpty(templateViewInfo.getUrl())) {
                            //不是占位图
                            canvas.drawBitmap(extracted(templateViewInfo.getLocalMedia()), null, rect, mPaint);
                        } else {
                            if (!shouldCreateBitmap) {
                                if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1) {
                                    canvas.drawBitmap(mTemplate11, null, rect, mPaint);
                                } else if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1.5) {
                                    canvas.drawBitmap(mTemplate32, null, rect, mPaint);
                                }
                            }
                        }
                        mAreaTouch.remove(i);
                    } else {
                        if (!shouldCreateBitmap) {
                            if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1) {
                                canvas.drawBitmap(mTemplate11, null, rect, mPaint);
                            } else if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1.5) {
                                canvas.drawBitmap(mTemplate32, null, rect, mPaint);
                            }
                            //收录一下绘制范围，做监听使用,是否有圖片等
                            Region region = new Region(rect);
                            templateViewInfo = new TemplateViewInfo(region, i, false, null);
                        }
                    }
                    if (templateViewInfo != null) {
                        mAreaTouch.add(i, templateViewInfo);
                    }
                } else {
                    if (angle > 0) { //顺时针旋转
                        @SuppressLint("DrawAllocation")
                        double resetRadians = Math.toRadians(-angle);
                        //旋转前左上坐标
                        float beforeRotateLeftTopX = (float) ((outAreaLeftX - centerXInTemplate) * Math.cos(resetRadians)
                                - (outAreaLeftY - centerYInTemplate) * Math.sin(resetRadians) + centerXInTemplate);
                        float beforeRotateLeftTopY = (float) ((outAreaLeftY - centerYInTemplate) * Math.cos(resetRadians)
                                + (outAreaLeftX - centerXInTemplate) * Math.sin(resetRadians) + centerYInTemplate);
                        //根据旋转前左上角坐标和中心点求出 旋转前右上 右下 左下坐标
                        //旋转前右上坐标
                        float beforeRotateRightTopX = beforeRotateLeftTopX + (centerXInTemplate - beforeRotateLeftTopX) * 2;
                        //旋转前右下坐标
                        float beforeRotateRightBottomY = beforeRotateLeftTopY + (centerYInTemplate - beforeRotateLeftTopY) * 2;
                        //旋转前左下坐标
                        /*for Test */
//                        Path path1 = new Path();
//                        path1.moveTo(beforeRotateLeftTopX, beforeRotateLeftTopY);
//                        path1.lineTo(beforeRotateRightTopX, beforeRotateRightTopY);
//                        path1.lineTo(beforeRotateRightBottomX, beforeRotateRightBottomY);
//                        path1.lineTo(beforeRotateLeftBottomX, beforeRotateLeftBottomY);
//                        path1.close();
//                        mPaint.setColor(Color.GREEN);
//                        canvas.drawPath(path1, mPaint);
                        //此时四点坐标求出后,再求旋转后的四点坐标
                        //旋转angle后 左上角坐标
                        //右上角坐标
                        double v1 = (beforeRotateRightTopX - centerXInTemplate) * Math.cos(radians);
                        double v2 = (beforeRotateRightTopX - centerXInTemplate) * Math.sin(radians);
                        double v3 = (beforeRotateRightBottomY - centerYInTemplate) * Math.sin(radians);
                        double v4 = (beforeRotateRightBottomY - centerYInTemplate) * Math.cos(radians);
                        float afterRotateRightTopX = (float) (v1
                                - (beforeRotateLeftTopY - centerYInTemplate) * Math.sin(radians) + centerXInTemplate);
                        float afterRotateRightTopY = (float) ((beforeRotateLeftTopY - centerYInTemplate) * Math.cos(radians)
                                + v2 + centerYInTemplate);
                        //右下角坐标
                        float afterRotateRightBottomX = (float) (v1 - v3 + centerXInTemplate);
                        float afterRotateRightBottomY = (float) (v4 + v2 + centerYInTemplate);
                        //坐下角坐标
                        float afterRotateLeftBottomX = (float) ((beforeRotateLeftTopX - centerXInTemplate) * Math.cos(radians) - v3 + centerXInTemplate);
                        float afterRotateLeftBottomY = (float) (v4 + (beforeRotateLeftTopX - centerXInTemplate) * Math.sin(radians) + centerYInTemplate);
                        Path path = new Path();
                        path.moveTo((float) outAreaLeftX, (float) outAreaLeftY);
                        path.lineTo(afterRotateRightTopX, afterRotateRightTopY);
                        path.lineTo(afterRotateRightBottomX, afterRotateRightBottomY);
                        path.lineTo(afterRotateLeftBottomX, afterRotateLeftBottomY);
                        path.close();
                        TemplateViewInfo templateViewInfo = null;
                        BitMapInfo.SizeInfo sizeInfo = sizeInfos.get(i);
                        if (mSize > 0) {
                            templateViewInfo = mAreaTouch.get(i);
                            if (!TextUtils.isEmpty(templateViewInfo.getUrl())) {
                                //不是占位图 有图片
                                Bitmap bitmap = extracted(templateViewInfo.getLocalMedia());
                                matrix.setTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
                                matrix.postRotate(angle);
                                float postScaleX = (beforeRotateRightTopX - beforeRotateLeftTopX) / bitmap.getWidth();
                                float postScaleY = (beforeRotateRightBottomY - beforeRotateLeftTopY) / bitmap.getHeight();
                                matrix.postScale(postScaleX, postScaleY);
                                matrix.postTranslate(centerX + templateX, centerY + templateY);
                                canvas.drawBitmap(bitmap, matrix, mPaint);
                            } else {
                                if (!shouldCreateBitmap) {
                                    if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1) {
                                        matrix.setTranslate(-mTemplate11.getWidth() / 2, -mTemplate11.getHeight() / 2);
                                        matrix.postRotate(angle);
                                        matrix.postScale((float) (beforeRotateRightTopX - beforeRotateLeftTopX) / mTemplate11.getWidth(), (beforeRotateRightBottomY - beforeRotateLeftTopY) / mTemplate11.getHeight());
                                        matrix.postTranslate(centerX + templateX, centerY + templateY);
                                        canvas.drawBitmap(mTemplate11, matrix, mPaint);
                                    } else if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1.5) {
                                        matrix.setTranslate(-mTemplate32.getWidth() / 2, -mTemplate32.getHeight() / 2);
                                        matrix.postRotate(angle);
                                        matrix.postScale((float) (beforeRotateRightTopX - beforeRotateLeftTopX) / mTemplate32.getWidth(), (beforeRotateRightBottomY - beforeRotateLeftTopY) / mTemplate32.getHeight());
                                        matrix.postTranslate(centerX + templateX, centerY + templateY);
                                        canvas.drawBitmap(mTemplate32, matrix, mPaint);
                                    }
                                }

                            }
                            mAreaTouch.remove(i);
                        } else {
                            if (!shouldCreateBitmap) {
                                if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1) {
                                    matrix.setTranslate(-mTemplate11.getWidth() / 2, -mTemplate11.getHeight() / 2);
                                    matrix.postRotate(angle);
                                    matrix.postScale((float) (beforeRotateRightTopX - beforeRotateLeftTopX) / mTemplate11.getWidth(), (beforeRotateRightBottomY - beforeRotateLeftTopY) / mTemplate11.getHeight());
                                    matrix.postTranslate(centerX + templateX, centerY + templateY);
                                    canvas.drawBitmap(mTemplate11, matrix, mPaint);
                                } else if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1.5) {
                                    matrix.setTranslate(-mTemplate32.getWidth() / 2, -mTemplate32.getHeight() / 2);
                                    matrix.postRotate(angle);
                                    matrix.postScale((float) (beforeRotateRightTopX - beforeRotateLeftTopX) / mTemplate32.getWidth(), (beforeRotateRightBottomY - beforeRotateLeftTopY) / mTemplate32.getHeight());
                                    matrix.postTranslate(centerX + templateX, centerY + templateY);
                                    canvas.drawBitmap(mTemplate32, matrix, mPaint);
                                }
                                Region region = new Region();
                                RectF rectF = new RectF();
                                path.computeBounds(rectF, true);
                                region.setPath(path, new Region((int) rectF.left,
                                        (int) rectF.top,
                                        (int) rectF.right,
                                        (int) rectF.bottom));
                                templateViewInfo = new TemplateViewInfo(region, i, false, null);
                            }
                        }
                        if (templateViewInfo != null) {
                            mAreaTouch.add(i, templateViewInfo);
                        }
                    } else if (angle < 0) { //逆时针旋转
                        double resetRadians = Math.toRadians(angle);
                        //旋转前左上坐标
                        float beforeRotateLeftTopX = (float) ((outAreaLeftX - centerXInTemplate) * Math.cos(resetRadians)
                                - (outAreaLeftY - centerYInTemplate) * Math.sin(resetRadians) + centerXInTemplate);
                        float beforeRotateLeftTopY = (float) ((outAreaLeftY - centerYInTemplate) * Math.cos(resetRadians)
                                + (outAreaLeftX - centerXInTemplate) * Math.sin(resetRadians) + centerYInTemplate);
                        //根据旋转前左上角坐标和中心点求出 旋转前右上 右下 左下坐标
                        //旋转前右上坐标
                        float beforeRotateRightTopX = beforeRotateLeftTopX + (centerXInTemplate - beforeRotateLeftTopX) * 2;
                        //旋转前右下坐标
                        float beforeRotateRightBottomY = beforeRotateLeftTopY + (centerYInTemplate - beforeRotateLeftTopY) * 2;
                        //旋转前左下坐标
                        /*for Test */
//                        Path path1 = new Path();
//                        path1.moveTo(beforeRotateLeftTopX, beforeRotateLeftTopY);
//                        path1.lineTo(beforeRotateRightTopX, beforeRotateRightTopY);
//                        path1.lineTo(beforeRotateRightBottomX, beforeRotateRightBottomY);
//                        path1.lineTo(beforeRotateLeftBottomX, beforeRotateLeftBottomY);
//                        path1.close();
//                        mPaint.setColor(Color.GREEN);
//                        canvas.drawPath(path1, mPaint);
                        //此时四点坐标求出后,再求旋转后的四点坐标
                        //旋转angle后 左上角坐标
                        //右上角坐标
                        double v1 = (beforeRotateRightTopX - centerXInTemplate) * Math.cos(radians);
                        double v2 = (beforeRotateRightTopX - centerXInTemplate) * Math.sin(radians);
                        double v3 = (beforeRotateRightBottomY - centerYInTemplate) * Math.sin(radians);
                        double v4 = (beforeRotateRightBottomY - centerYInTemplate) * Math.cos(radians);
                        float afterRotateRightTopX = (float) (v1
                                - (beforeRotateLeftTopY - centerYInTemplate) * Math.sin(radians) + centerXInTemplate);
                        float afterRotateRightTopY = (float) ((beforeRotateLeftTopY - centerYInTemplate) * Math.cos(radians)
                                + v2 + centerYInTemplate);
                        //右下角坐标
                        float afterRotateRightBottomX = (float) (v1 - v3 + centerXInTemplate);
                        float afterRotateRightBottomY = (float) (v4 + v2 + centerYInTemplate);
                        //坐下角坐标
                        float afterRotateLeftBottomX = (float) ((beforeRotateLeftTopX - centerXInTemplate) * Math.cos(radians) - v3 + centerXInTemplate);
                        float afterRotateLeftBottomY = (float) (v4 + (beforeRotateLeftTopX - centerXInTemplate) * Math.sin(radians) + centerYInTemplate);
                        Path path = new Path();
                        path.moveTo((float) outAreaLeftX, (float) outAreaLeftY);
                        path.lineTo(afterRotateRightTopX, afterRotateRightTopY);
                        path.lineTo(afterRotateRightBottomX, afterRotateRightBottomY);
                        path.lineTo(afterRotateLeftBottomX, afterRotateLeftBottomY);
                        path.close();
                        TemplateViewInfo templateViewInfo = null;
                        BitMapInfo.SizeInfo sizeInfo = sizeInfos.get(i);
                        if (mSize > 0) {
                            templateViewInfo = mAreaTouch.get(i);
                            if (!TextUtils.isEmpty(templateViewInfo.getUrl())) {
                                //不是占位图
                                Bitmap bitmap = extracted(templateViewInfo.getLocalMedia());
                                matrix.setTranslate(-bitmap.getWidth() / 2, -bitmap.getHeight() / 2);
                                matrix.postRotate(angle);
                                matrix.postScale((float) (beforeRotateRightTopX - beforeRotateLeftTopX) / bitmap.getWidth(), (beforeRotateRightBottomY - beforeRotateLeftTopY) / bitmap.getHeight());
                                matrix.postTranslate(centerX + templateX, centerY + templateY);
                                canvas.drawBitmap(bitmap, matrix, mPaint);
                            } else {
                                if (!shouldCreateBitmap) {
                                    if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1) {
                                        matrix.setTranslate(-mTemplate11.getWidth() / 2, -mTemplate11.getHeight() / 2);
                                        matrix.postRotate(angle);
                                        matrix.postScale((float) (beforeRotateRightTopX - beforeRotateLeftTopX) / mTemplate11.getWidth(), (beforeRotateRightBottomY - beforeRotateLeftTopY) / mTemplate11.getHeight());
                                        matrix.postTranslate(centerX + templateX, centerY + templateY);
                                        canvas.drawBitmap(mTemplate11, matrix, mPaint);
                                    } else if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1.5) {
                                        matrix.setTranslate(-mTemplate32.getWidth() / 2, -mTemplate32.getHeight() / 2);
                                        matrix.postRotate(angle);
                                        matrix.postScale((float) (beforeRotateRightTopX - beforeRotateLeftTopX) / mTemplate32.getWidth(), (beforeRotateRightBottomY - beforeRotateLeftTopY) / mTemplate11.getHeight());
                                        matrix.postTranslate(centerX + templateX, centerY + templateY);
                                        canvas.drawBitmap(mTemplate32, matrix, mPaint);
                                    }
                                }
                            }
                            mAreaTouch.remove(i);
                        } else {
                            if (!shouldCreateBitmap) {
                                if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1) {
                                    matrix.setTranslate(-mTemplate11.getWidth() / 2, -mTemplate11.getHeight() / 2);
                                    matrix.postRotate(angle);
                                    matrix.postScale((float) (beforeRotateRightTopX - beforeRotateLeftTopX) / mTemplate11.getWidth(), (beforeRotateRightBottomY - beforeRotateLeftTopY) / mTemplate11.getHeight());
                                    matrix.postTranslate(centerX + templateX, centerY + templateY);
                                    canvas.drawBitmap(mTemplate11, matrix, mPaint);
                                } else if (sizeInfo.getAspectRatioWidth() / sizeInfo.getAspectRatioHeight() == 1.5) {
                                    matrix.setTranslate(-mTemplate32.getWidth() / 2, -mTemplate32.getHeight() / 2);
                                    matrix.postRotate(angle);
                                    matrix.postScale((beforeRotateRightTopX - beforeRotateLeftTopX) / mTemplate32.getWidth(), (beforeRotateRightBottomY - beforeRotateLeftTopY) / mTemplate32.getHeight());
                                    matrix.postTranslate(centerX + templateX, centerY + templateY);
                                    canvas.drawBitmap(mTemplate32, matrix, mPaint);
                                }
                                Region region = new Region();
                                RectF rectF = new RectF();
                                path.computeBounds(rectF, true);
                                region.setPath(path, new Region((int) rectF.left,
                                        (int) rectF.top,
                                        (int) rectF.right,
                                        (int) rectF.bottom));
                                templateViewInfo = new TemplateViewInfo(region, i, false, null);
                            }

                        }
                        if (templateViewInfo != null) {
                            mAreaTouch.add(i, templateViewInfo);
                        }
                    }
                }
            }
            canvas.drawBitmap(mTemplateBitmap, templateX, templateY, mPaint);
            if (shouldCreateBitmap && drawBySystem == 1) {
                if (drawFinish != null) {
                    drawBySystem++;
                    drawFinish.drawFinish(templateX, templateY, mTemplateWidth, mTemplateHeight);
                }
            }
        }
        super.onDraw(canvas);
    }

    public interface DrawFinish {
        void drawFinish(int x, int y, int width, int height);
    }

    private boolean hasTarget;
    private Matrix matrix;
    private int drawBySystem = 1;
    private DrawFinish drawFinish;
    private OutRectClickListener listener;


    public final void setDrawFinish(DrawFinish drawFinish) {
        this.drawFinish = drawFinish;
    }

    public final void setOutRectClickListener(OutRectClickListener listener) {
        this.listener = listener;
    }

    public final void setBitMapFromClient(LocalMedia localMedia) {
        /*用戶設置圖片進View中*/
        TemplateViewInfo templateViewInfo = mAreaTouch.get(mSettingPosition);
        templateViewInfo.setHasPic(true);
        templateViewInfo.setLocalMedia(localMedia);
        templateViewInfo.setUrl(localMedia.getAvailablePath());
        mSettingPosition = -1;
        invalidate();
    }

    private Bitmap extracted(LocalMedia localMedia) {
        Uri uri = Uri.fromFile(new File(localMedia.getAvailablePath()));
        try {
            InputStream inputStream = getContext().getContentResolver().openInputStream(uri);
            return BitmapFactory.decodeStream(inputStream);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    public final LocalMedia getInfoFromView() {
        if (mSettingPosition >= 0) {
            TemplateViewInfo templateViewInfo = mAreaTouch.get(mSettingPosition);
            return templateViewInfo.getLocalMedia();
        }
        return null;
    }

    public final void createBitmap() {
        shouldCreateBitmap = true;
        invalidate();
    }

    public final void resetState() {
        drawBySystem = 1;
        shouldCreateBitmap = false;
        invalidate();
    }

    public interface OutRectClickListener {
        void onRectClick(boolean hasPic);
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
                  Region region =   templateViewInfo.getRegion();
                 if (region.contains(upX,upY)){
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
                }
                break;
            default:
                break;
        }
        return true;
    }


    private void init() {
        mPaint = new Paint();
        matrix = new Matrix();
        mTemplate11 = BitmapFactory.decodeResource(getResources(), R.mipmap.template11);
        mTemplate32 = BitmapFactory.decodeResource(getResources(), R.mipmap.template32);

    }
}
