package com.weilai.jigsawpuzzle.weight

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.util.BitmapUtils
import com.weilai.jigsawpuzzle.util.DeviceUtils
import com.weilai.jigsawpuzzle.util.DeviceUtils.dp2px
import kotlin.math.abs

class MySeekBar @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var callback: MySeekBarCallback? = null
    //最小值
    private var minValue = -100
    //最大值
    private var maxValue = 100
    //滑块图片
    private var icon : Bitmap? = null
    //左边文字
    private var leftText = "减小"
    //右边文字
    private var rightText = "增大"
    //画笔
    private var paint: Paint = Paint()
    //两端文字画笔
    private var textPaint: Paint = Paint()
    private var sliderTextPaint: Paint = Paint()
    //值
    private var value = 0
    //位置
    private var iconX: Float = 0f
    set(value) {
        field = value
        invalidate()
    }
    private var iconY: Float = 0f
    //线条起始位置
    private var startLineX = 0f
    //线条结束位置
    private var endLineX = 0f
    private var lineY = 0f
    //icon与直线之间的距离
    private var distance = 3f

    private var font_distance = 0f

    init {
        icon = BitmapUtils.getBitmap(context, R.drawable.ic_slider,
            DeviceUtils.dp2px(context,30f),DeviceUtils.dp2px(context,30f))
        paint.apply {
            this.color = Color.parseColor("#5B92FF")
            this.isAntiAlias = true
            this.style = Paint.Style.FILL_AND_STROKE
            this.strokeWidth = DeviceUtils.dp2px(context,1.5f).toFloat()
            this.strokeCap = Paint.Cap.ROUND
            this.textSize = 40f
            this.textAlign = Paint.Align.CENTER
        }
        textPaint.apply {
            this.color = Color.parseColor("#5B92FF")
            this.textSize = DeviceUtils.dp2px(context,12f).toFloat()
            this.isAntiAlias = true
        }
        sliderTextPaint.apply {
            this.color = Color.parseColor("#FFFFFF")
            this.textSize = 40f
            this.isAntiAlias = true
            this.textAlign = Paint.Align.CENTER
        }
        val fontMetrics = sliderTextPaint.fontMetrics
        font_distance = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        startLineX = getStringWidth(leftText,textPaint)+icon?.width!!/2f
        endLineX = width - getStringWidth(leftText,textPaint)-icon?.width!!/2f
        lineY = height-getStringHeight(leftText,textPaint)/2f
        iconY = lineY - icon?.height!! - DeviceUtils.dp2px(context,distance)
        iconX = (endLineX - startLineX)/2
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
        val x = event?.x
        val y = event?.y
        when(event?.action){
            MotionEvent.ACTION_DOWN ->{
                calculate(event.x)
            }
            MotionEvent.ACTION_MOVE ->{
                calculate(event.x)
            }
            MotionEvent.ACTION_UP ->{

            }

        }
        return true
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawBitmap(icon!!,iconX-icon?.width!!/2,iconY,paint)
        canvas?.drawText(leftText,0f, height.toFloat()-(getStringHeight(leftText,textPaint))/2, textPaint)
        canvas?.drawText(rightText, (width-getStringWidth(rightText,textPaint)).toFloat(),height.toFloat()-(getStringHeight(leftText,textPaint))/2,textPaint)
        paint.color = Color.parseColor("#5B92FF")
        canvas?.drawLine(startLineX.toFloat(),lineY,iconX,lineY,paint)
        paint.color = Color.parseColor("#F2F2F2")
        canvas?.drawLine(iconX,lineY,endLineX,lineY,paint)

        canvas?.drawText("$value",iconX,iconY+(icon?.height!!/2)+font_distance,sliderTextPaint)
    }

    /**
     * 计算
     * */
    private fun calculate(x: Float){
        iconX = when {
            x<=startLineX -> startLineX
            x>=endLineX -> endLineX
            else -> x
        }
        val a = ((iconX-startLineX)/(endLineX-startLineX)*(maxValue-minValue)).toInt()
        value = (minValue .. maxValue).toList()[a]
        callback?.MySeekBarChange(value)
        invalidate()
    }

    /**
     * 获取文字高度
     * */
    private fun getStringHeight(string: String, paint: Paint): Int {
        val rect = Rect()
        paint.getTextBounds(string, 0, string.length, rect)
        return rect.height()
    }
    /**
     * 获取文字宽度
     * */
    private fun getStringWidth(string: String, paint: Paint): Int {
        val rect = Rect()
        paint.getTextBounds(string, 0, string.length, rect)
        return rect.width()
    }

    fun setCallback(callback: MySeekBarCallback){
        this.callback = callback
    }

    fun setMinValue(min: Int){
        this.minValue = min
    }

    fun setMaxValue(max: Int){
        this.maxValue = max
    }

    fun setValue(int: Int){
        var v = int
        if (v<minValue)
            v = minValue
        else if(v>maxValue)
            v = maxValue
        iconX = if (minValue < 0){
            abs(minValue - v)*(endLineX - startLineX)/(maxValue-minValue)+startLineX
        }else{
            v*(endLineX - startLineX)/(maxValue-minValue)+startLineX
        }
        value = v
        invalidate()
    }

    interface MySeekBarCallback{
        fun MySeekBarChange(int: Int)
    }
}