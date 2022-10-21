package com.weilai.jigsawpuzzle.dialog.special

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.WindowManager
import com.weilai.jigsawpuzzle.R
import kotlinx.android.synthetic.main.dialog_tip.*

class TipDialog(mContext: Context):Dialog(mContext, R.style.MyDialog) {

    private var cancelUnit: (() -> Unit)? = null
    private var confirmUnit: (() -> Unit)? = null
    private var titleText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_tip)
        val la = window?.attributes
        la?.width = WindowManager.LayoutParams.MATCH_PARENT
        la?.height = WindowManager.LayoutParams.WRAP_CONTENT
        la?.gravity = Gravity.CENTER
        title_text.text = titleText
        initEvent()
    }

    private fun initEvent(){
        cancel.setOnClickListener {
            cancelUnit?.invoke()
            dismiss()
        }
        confirm.setOnClickListener {
            confirmUnit?.invoke()
            dismiss()
        }
    }

    fun setTitle(title: String){
        this.titleText = title
    }

    fun setCancel(cancel:(() ->Unit)){
        this.cancelUnit = cancel
    }

    fun setConfirm(confirm: (() -> Unit)){
        this.confirmUnit = confirm
    }
}