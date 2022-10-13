package com.weilai.jigsawpuzzle.weight.puzzleLine;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.weilai.jigsawpuzzle.util.DimenUtil;

import java.math.BigDecimal;
import java.math.RoundingMode;

/**
 * * DATE: 2022/10/13
 * * Author:tangerine
 * * Description:
 **/
public class PuzzleLineView extends View {
    public PuzzleLineView(Context context) {
        this(context, null);
    }

    public PuzzleLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PuzzleLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }
    private Paint mPaint;

    private void init() {
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
    }
    private Bitmap mBitmap ;
    public final void setBitmap(Bitmap bitmap) {
        //缩放标准
        int deviceHeight = DimenUtil.getScreenHeight();
        int deviceWidth = DimenUtil.getScreenWidth();
        int bitmapWidth = bitmap.getWidth();
        int bitmapHeight = bitmap.getHeight();
        Matrix matrix = new Matrix();
        if (bitmapHeight > deviceHeight) {
            /*如果屏幕的高放不下图片就以屏幕的高来缩放图片，直到放下为止*/
            BigDecimal deviceHeightBig = new BigDecimal(deviceHeight*3/4 * 9/10);
            float scaleSize =  deviceHeightBig.divide(new BigDecimal(bitmapHeight),2,RoundingMode.HALF_DOWN).floatValue();
            matrix.setScale(scaleSize,scaleSize);
        } else {
            /*如果屏幕的高能放下图片就判断一下图片的宽度,如果放大或缩小至屏幕的3/4*/
            BigDecimal deviceWidthBig = new BigDecimal(deviceWidth * 3 / 4);
            float scaleSize = deviceWidthBig.divide(new BigDecimal(bitmapWidth), 2, RoundingMode.HALF_DOWN).floatValue();
            matrix.setScale(scaleSize,scaleSize);
        }
        mBitmap = Bitmap.createBitmap(bitmap,0,0,bitmapWidth,bitmapHeight,matrix,true);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        //居中显示
        canvas.drawBitmap(mBitmap,(getWidth()-mBitmap.getWidth())/2,(getHeight()-mBitmap.getHeight())/2,mPaint);
    }
}
