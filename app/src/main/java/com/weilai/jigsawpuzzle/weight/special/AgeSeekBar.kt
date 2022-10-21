package com.weilai.jigsawpuzzle.weight.special

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.util.BitmapUtils
import com.weilai.jigsawpuzzle.util.DensityUtil

class AgeSeekBar @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    //最小值
    private var minValue = 40
    //最大值
    private var maxValue = 80
    //滑块图片
    private var icon : Bitmap? = null
    //左边文字
    private var leftText = "40岁"
    //中间文字
    private var centerText = "60岁"
    //右边文字
    private var rightText = "80岁"
    //滑块文字
    private var text = "60岁"
        set(value) {
            field = value
            invalidate()
        }
    //横线颜色
    private var horizontalLineColor = Color.BLACK
    //竖线颜色
    private var verticalLineColor = Color.BLACK
    //滑块画笔
    private var paint: Paint = Paint()
    //文字画笔
    private var textPaint: Paint = Paint()
    //线画笔
    private var linePaint: Paint = Paint()
    //横线离底部的距离
    private var hor_line_bottom_distance = 0f
    //横线起始坐标
    private var hor_line_Y = 0f
    //竖线与两边的间距
    private var verticalLineMargin = 0f
    //竖线长度
    private var ver_line_lenght = 0f
    //竖线起始Y坐标
    private var ver_line_start_y = 0f
    //竖线结束Y坐标
    private var ver_line_end_y = 0f
    //文字Y坐标
    private var textY = 0f
    //中心与基线的距离
    private var font_distance = 0f
    //滑块文字与背景的上下间距
    private var top_bottom_distance = 0
    //滑块文字与背景的左右间距
    private var left_right_distance = 0
    //值
    private var value = 0
    //位置
    private var iconY: Float = 0f
    private var iconX: Float = 0f
        set(value) {
            field = value
            invalidate()
        }
    private var callback: AgeSeekBarCallback? = null

    init {
        top_bottom_distance = DensityUtil.dipTopxAsInt(3f)
        left_right_distance = DensityUtil.dipTopxAsInt(7f)
        horizontalLineColor = Color.parseColor("#FFF8EF")
        verticalLineColor = Color.parseColor("#FF9D2D")
        paint.apply {
            this.color = Color.WHITE
            this.isAntiAlias = true
            this.textSize = DensityUtil.dipTopx(14f)
            this.textAlign = Paint.Align.CENTER
        }
        textPaint.apply {
            this.color = Color.BLACK
            this.isAntiAlias = true
            this.textSize = DensityUtil.dipTopx(14f)
            this.textAlign = Paint.Align.CENTER
        }
        linePaint.apply {
            this.color = horizontalLineColor
            this.isAntiAlias = true
            this.style = Paint.Style.FILL_AND_STROKE
            this.strokeWidth = DensityUtil.dipTopx(1f)
        }
        val fontMetrics = paint.fontMetrics
        font_distance = (fontMetrics.bottom - fontMetrics.top)/2 - fontMetrics.bottom
        val iconW = left_right_distance*2+getStringWidth(leftText,paint)
        val iconH = top_bottom_distance*2+getStringHeight(leftText,paint)
        icon = BitmapUtils.getBitmap(getContext(), R.drawable.shape_age_slider_bg,iconW,iconH)
    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        hor_line_bottom_distance = icon?.height!!/2f
        hor_line_Y = height - hor_line_bottom_distance
        verticalLineMargin = icon?.width!!/2f
        ver_line_lenght = icon?.height!!/2f
        ver_line_start_y = hor_line_Y - ver_line_lenght/2
        ver_line_end_y = hor_line_Y + ver_line_lenght/2
        iconY = (height-icon?.height!!).toFloat()
        textY = height - icon?.height!!/2f - getStringHeight(leftText,textPaint)
        iconX = width/2 - icon?.width!!/2f
        text = "${(maxValue - minValue)/2+minValue}岁"
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        //画横线
        linePaint.color = horizontalLineColor
        canvas?.drawLine(0f,hor_line_Y,width.toFloat(),hor_line_Y,linePaint)
        linePaint.color = verticalLineColor
        //画左边竖线
        canvas?.drawLine(verticalLineMargin,ver_line_start_y,verticalLineMargin,ver_line_end_y,linePaint)
        //画中间竖线
        canvas?.drawLine(width/2f,ver_line_start_y,width/2f,ver_line_end_y,linePaint)
        //画右边竖线
        canvas?.drawLine(width-verticalLineMargin,ver_line_start_y,width-verticalLineMargin,ver_line_end_y,linePaint)
        //画滑块
        canvas?.drawBitmap(icon!!,iconX,iconY,paint)
        //画滑块文字
        canvas?.drawText(text,iconX+verticalLineMargin,hor_line_Y+font_distance,paint)
        //画左边文字
        canvas?.drawText(leftText,verticalLineMargin,textY,textPaint)
        //画中间文字
        canvas?.drawText(centerText,width/2f,textY,textPaint)
        //画右边文字
        canvas?.drawText(rightText,width-verticalLineMargin,textY,textPaint)
    }

    override fun onTouchEvent(event: MotionEvent?): Boolean {
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

    /**
     * 计算
     * */
    private fun calculate(x: Float){
        iconX = when{
            x <= verticalLineMargin -> verticalLineMargin - icon?.width!!/2f
            x >= width - verticalLineMargin -> width - verticalLineMargin - icon?.width!!/2f
            else -> x - icon?.width!!/2f
        }
        val all = (width - icon?.width!!).toFloat()
        val now = iconX
        val value = ((maxValue - minValue) * (now / all)).toInt()+minValue
        text = "${value}岁"
        callback?.ageSeekBarChange(value)
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

    fun setCallback(callback: AgeSeekBarCallback){
        this.callback = callback
    }

    fun setValue(minValue : Int,maxValue: Int,leftText: String,centerText: String,rightText: String){
        this.minValue = minValue
        this.maxValue = maxValue
        this.leftText = leftText
        this.centerText = centerText
        this.rightText = rightText
        invalidate()
    }

    interface AgeSeekBarCallback{
        fun ageSeekBarChange(int: Int)
    }
}