package com.weilai.jigsawpuzzle.dialog.puzzleLP;

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

/**
 ** DATE: 2022/10/8
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleLpEditView extends View{
    private Paint mPaint;
    private boolean canScrollUp;
    private Bitmap bitmap;
    private Matrix matrix;
    public PuzzleLpEditView(Context context) {
        this(context,null);
    }

    public PuzzleLpEditView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PuzzleLpEditView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();

    }
    private void init(){
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        matrix = new Matrix();
    }
    public final void  setBitmap(Bitmap bitmap ){
        this.bitmap = bitmap;
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (bitmap != null){
            canvas.drawBitmap(bitmap,matrix,mPaint);
        }
    }
    float lastEventY;
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            lastEventY = event.getY();
        }
        if (event.getAction() == MotionEvent.ACTION_MOVE){
            if (event.getY() - lastEventY >0){
                //up

            }else if (event.getY() - lastEventY < 0 ){
                //down

            }
        }


        return super.onTouchEvent(event);
    }
}
