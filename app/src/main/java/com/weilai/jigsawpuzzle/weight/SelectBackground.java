package com.weilai.jigsawpuzzle.weight;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.LinearLayout;

import com.weilai.jigsawpuzzle.R;


/**
 * Created by shihaibin on 2017/2/8.
 * 布局背景点击效果（纯色值）
 */

public class SelectBackground extends LinearLayout {


    /**
     * 默认背景色
     */
    public int mBackGroundNormColor;
    /**
     * 点击背景色
     */
    public int mBackGroundSelectColor;
    /**
     * 边线颜色
     */
    public int mLineColor;
    /**
     * 边线颜色
     */
    public float mLineWidth;

    /**
     * 背景圆角
     */
    public int mRadius;

    /**
     * 画笔
     */
    private Paint mPaint = new Paint();
    /**
     * 控件宽度
     */
    private int mWidth;
    /**
     * 控件高度
     */
    private int mHeight;
    /**
     * 点击状态
     */
    private boolean isPress;

    public SelectBackground(Context context) {
        super(context);
    }

    public SelectBackground(Context context, AttributeSet attrs) {
        super(context, attrs);
        TypedArray mTypedArray = context.obtainStyledAttributes(attrs, R.styleable.SelectBackground);
        mBackGroundNormColor = mTypedArray.getColor(R.styleable.SelectBackground_background_norm_color, 0xffffffff);
        mBackGroundSelectColor = mTypedArray.getColor(R.styleable.SelectBackground_background_selected_color, 0xffffffff);
        mRadius = (int) mTypedArray.getDimension(R.styleable.SelectBackground_background_radius, 0f);
        mLineColor = (int) mTypedArray.getColor(R.styleable.SelectBackground_background_line_color, 0xffffffff);
        mLineWidth = (int) mTypedArray.getDimension(R.styleable.SelectBackground_background_line_width, 0f);
    }

    public SelectBackground(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                isPress = true;
                invalidate();
                break;
            case MotionEvent.ACTION_UP:
                isPress = false;
                invalidate();
                break;
        }
        return true;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        init();
        drawLine(canvas);
        drawBackground(canvas);
        super.onDraw(canvas);
    }



    private void init() {
        mWidth = getWidth();
        mHeight = getHeight();
        mPaint.setColor(isPress || isSelected() ? mBackGroundSelectColor : mBackGroundNormColor);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setAntiAlias(true);
    }

    /**
     * 绘制背景线-覆盖式
     * @param canvas
     */
    private void  drawLine(Canvas canvas){

        if(mLineWidth==0){
            return;
        }

        mPaint.setColor(mLineColor);
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeWidth(mLineWidth);
        canvas.drawRoundRect(getRect(), mRadius, mRadius, mPaint);

    }

    private RectF getRect() {
        return new RectF(0+mLineWidth/2, 0+mLineWidth/2, mWidth-mLineWidth/2, mHeight-mLineWidth/2);
    }

    /**
     * 绘制背景
     *
     * @param canvas
     */

    private void drawBackground(Canvas canvas) {
        // drawLine(canvas, mRadius);
        drawRoundRect(canvas, mRadius);
    }

    /**
     * 绘制圆角全背景
     *
     * @param canvas
     * @param Radius
     */
    private void drawRoundRect(Canvas canvas, int Radius) {
        setBackgroundPaint();
        canvas.drawRoundRect(getBackgroundRect(), Radius, Radius, mPaint);
    }

    /**
     * 设置背景画笔
     */
    private void setBackgroundPaint() {
        mPaint.setColor(isPress || isSelected() ? mBackGroundSelectColor : mBackGroundNormColor);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
    }

    /**
     * 设置线框画笔
     */
    private void setLinePaint() {
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setColor(mLineColor);
        mPaint.setStrokeWidth(mLineWidth);
        mPaint.setStrokeCap(Paint.Cap.BUTT);
        mPaint.setStrokeJoin(Paint.Join.ROUND);
    }

    private RectF getBackgroundRect() {
        return new RectF(mLineWidth, mLineWidth, mWidth - mLineWidth, mHeight - mLineWidth);
    }

}
