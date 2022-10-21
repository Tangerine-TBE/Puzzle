package com.abc.matting.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.weilai.jigsawpuzzle.fragment.special.OldActivity2
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.weight.special.AgeSeekBar
import kotlinx.android.synthetic.main.dialog_select_age.*

class SelectAgeDialog(mContext: Context) : Dialog(mContext, R.style.MyDialog),
    AgeSeekBar.AgeSeekBarCallback {

    private var age = 60
    private var callback: SelectAgeCallback? = null
    private var minValue = 40
    private var maxValue = 80
    private var leftText = "40岁"
    private var centerText = "60岁"
    private var rightText = "80岁"
    private var btnText = "开始变老"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_select_age)
        val la = window?.attributes
        la?.width = WindowManager.LayoutParams.MATCH_PARENT
        la?.height = WindowManager.LayoutParams.WRAP_CONTENT
        la?.gravity = Gravity.BOTTOM
        ageSeekBar.setValue(minValue,maxValue,leftText,centerText,rightText)
        start.text = btnText
        initEvent()
    }

    private fun initEvent(){
        ageSeekBar.setCallback(this)
        start.setOnClickListener {
            callback?.startToAge(age)
            dismiss()
        }
    }

    fun setCallback(callback: SelectAgeCallback){
        this.callback = callback
    }

    fun setType(type: String){
        when(type){
            OldActivity2.TYPE_OLD -> setValue(40,80,"40岁","60岁","80岁","开始变老",60)
            OldActivity2.TYPE_YOUNG -> setValue(10,30,"10岁","20岁","30岁","开始童颜",20)
            else -> setValue(40,80,"40岁","60岁","80岁","开始变老",60)
        }
    }

    private fun setValue(minValue : Int,maxValue: Int,leftText: String,centerText: String,rightText: String,btnText: String,age: Int){
        this.minValue = minValue
        this.maxValue = maxValue
        this.leftText = leftText
        this.centerText = centerText
        this.rightText = rightText
        this.btnText = btnText
        this.age = age
    }

    interface SelectAgeCallback{
        fun startToAge(age: Int)
    }

    override fun ageSeekBarChange(int: Int) {
        age = int
    }
}