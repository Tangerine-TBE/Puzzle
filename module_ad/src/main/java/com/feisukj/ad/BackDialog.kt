package com.feisukj.ad

import android.app.Activity
import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.ContextThemeWrapper
import android.view.Gravity
import android.view.WindowManager
import com.feisukj.ad.R
import com.feisukj.ad.manager.AdController
import com.feisukj.base.bean.ad.ADConstants
import kotlinx.android.synthetic.main.dialog_back.*

class BackDialog(val context: Activity,themeResId:Int= R.style.MyDialog) :Dialog(context,themeResId){
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog_back)
        val lp=window?.attributes
        lp?.width=WindowManager.LayoutParams.MATCH_PARENT
        lp?.height=WindowManager.LayoutParams.WRAP_CONTENT
        lp?.gravity=Gravity.BOTTOM
        initView()
        requestAd()
    }
    private fun initView(){
        no.setOnClickListener {
            dismiss()
        }
        yes.setOnClickListener {
            context.finish()
            dismiss()
        }
    }
    private fun requestAd(){
        AdController.Builder(context,ADConstants.EXIT_PAGE)
                .setContainer(frameAd)
                .create()
                .show()
    }
}