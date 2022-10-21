package com.abc.matting.ui.dialog

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.weilai.jigsawpuzzle.R
import kotlinx.android.synthetic.main.dialog_select_sex.*

class SelectSexDialog(mContext: Context): Dialog(mContext, R.style.MyDialog) {
    //0：男变女，1：女变男
    private var callback: SelectSexCallback? = null
    private var type = 1L

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_select_sex)
        val la = window?.attributes
        la?.width = WindowManager.LayoutParams.MATCH_PARENT
        la?.height = WindowManager.LayoutParams.WRAP_CONTENT
        man.setOnClickListener {
            type = 1L
            iv_man.setImageResource(R.drawable.ic_select_sex_y)
            iv_woman.setImageResource(R.drawable.ic_select_sex_n)
        }
        woman.setOnClickListener {
            type = 0L
            iv_woman.setImageResource(R.drawable.ic_select_sex_y)
            iv_man.setImageResource(R.drawable.ic_select_sex_n)
        }
        start.setOnClickListener {
            callback?.selectSex(type)
        }
    }

    fun setCallback(callback: SelectSexCallback){
        this.callback = callback
    }

    interface SelectSexCallback{
        fun selectSex(type: Long)
    }
}