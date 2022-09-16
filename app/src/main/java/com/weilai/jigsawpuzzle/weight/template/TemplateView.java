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
            for (int i = 0 ; i < mBitmapInfo.getSizeInfos().size() ; i ++){
                float angle = sizeInfos.get(i).getAngle();
                //坐標是旋轉后的
                if (angle == 0){
                    float scaleX = sizeInfos.get(i).getX();
                    float scaleY = sizeInfos.get(i).getY();
                    int x = (int) (mTemplateWidth * scaleX);//在圖片上的x 起始
                    int y = (int) (mTemplateHeight * scaleY);//在圖片上的y 起始
                    float width = sizeInfos.get(i).getWidth();//目標的寬度 占比
                    float height = sizeInfos.get(i).getHeight();//目標的高度 占比
                    int outRectY = (int) (mTemplateHeight * height); //目標在原圖上的高度
                    int outRectX = (int) (mTemplateWidth * width);//目標在原圖上的寬度

                    @SuppressLint("DrawAllocation")
                    RectF rectF = new RectF(templateX + x, templateY + y, templateX + x + outRectX, templateY + y + outRectY);
                    canvas.rotate(30);
                    canvas.drawBitmap(mTouchBitmap, null, rectF, mPaint);
                    //收录一下绘制范围，做监听使用,是否有圖片等
                    int[] touchArea = {templateX + x, templateY + y, templateX + x + outRectX, templateY + y + outRectY,i,1};
                    mAreaTouch.add(touchArea);
                }else{
                    //先把画布旋转

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
    public final Bitmap setBitMapFromClient(Bitmap bitmap){
        /*用戶設置圖片進View中*/


        return null;
    }

    private boolean hasTarget;

    public interface OutRectClickListener {
        void onRectClick(int position);
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
                            listener.onRectClick(area[4]);
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
