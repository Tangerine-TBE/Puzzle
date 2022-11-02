package com.feisukj.ad.manager

import android.app.Activity
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import com.feisukj.base.bean.ad.AD
import com.feisukj.base.bean.ad.ADConstants
import com.feisukj.base.util.SPUtil

abstract class AbsADParent {
    var isRepetitionRequest=false
    var skipVew: TextView?=null
    var logo: ImageView?=null
    var activity: Activity?=null
    var container: FrameLayout?=null
    var page: String?=null
    var loading: Boolean = false
    var splashHolder: ImageView?=null
    var bannerContainer: FrameLayout?=null

    abstract fun showAdView(type: AD.AdType)

    abstract fun showFullVideoView(jiliCallback: AdController.JILICallback)

    abstract fun destroy(type: AD.AdType)

    protected fun gone(vararg views: View?) {
        if (views.isNotEmpty()) {
            for (view in views) {
                view?.visibility = View.GONE
            }
        }
    }

    protected fun visible(vararg views: View?) {
        if (views.isNotEmpty()) {
            for (view in views) {
                view?.visibility = View.VISIBLE
            }
        }

    }

    companion object {
        const val TAG = "AD_CONTROLLER"
    }


}