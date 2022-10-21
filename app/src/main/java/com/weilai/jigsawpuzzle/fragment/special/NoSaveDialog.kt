package com.weilai.jigsawpuzzle.fragment.special

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.WindowManager
import com.feisukj.base.bean.ad.ADConstants
import com.weilai.jigsawpuzzle.R
import kotlinx.android.synthetic.main.dialog_no_save.*

class NoSaveDialog(private val mContext: Context, private val confirmUnit:(() ->Unit), private val cancelUnit:(() ->Unit)?): Dialog(mContext, R.style.MyDialog) {

    private var titleText = ""
    private var cancelText = ""
    private var confirmText = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_no_save)
        val la = window?.attributes
        la?.width = WindowManager.LayoutParams.MATCH_PARENT
        la?.height = WindowManager.LayoutParams.WRAP_CONTENT
        title_text.text = titleText
        cancel.text = cancelText
        confirm.text = confirmText
        cancel.setOnClickListener {
            cancelUnit?.invoke()
            dismiss()
        }
        confirm.setOnClickListener {
            confirmUnit.invoke()
            dismiss()
        }
    }

    fun setTitleText(title: String): NoSaveDialog{
        this.titleText = title
        return this
    }

    fun setCancelText(cancel: String): NoSaveDialog{
        this.cancelText = cancel
        return this
    }

    fun setConfirmText(confirm: String): NoSaveDialog{
        this.confirmText = confirm
        return this
    }
}