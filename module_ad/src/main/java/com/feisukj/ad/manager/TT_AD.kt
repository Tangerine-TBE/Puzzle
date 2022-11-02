package com.feisukj.ad.manager

import android.os.Bundle
import androidx.annotation.MainThread
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.Toast
import com.bytedance.sdk.openadsdk.*
import com.feisukj.ad.BuildConfig
import com.feisukj.ad.SplashActivity
import com.feisukj.base.BaseApplication
import com.feisukj.base.BaseConstant
import com.feisukj.base.bean.ad.AD
import com.feisukj.base.bean.ad.ADConstants
import com.feisukj.base.bean.ad.TypeBean
import com.feisukj.base.util.*
import com.umeng.analytics.MobclickAgent


/**
 * Author : Gupingping
 * Date : 2019/1/24
 * QQ : 464955343
 */
class TT_AD : AbsADParent() {
    companion object {
        //开屏广告加载超时时间,建议大于1000,这里为了冷启动第一次加载到广告并且展示,示例设置了2000ms
        private const val AD_TIME_OUT = 100000
        private const val MSG_GO_MAIN = 1
        private const val TAG_TT="头条广告"
    }

    private var mTTAdNative: TTAdNative? = null

    private var mHasShowDownloadActive = false
    private var insertTTAd:TTNativeExpressAd?=null
    private var bannerTTAd:TTNativeExpressAd?=null
    private var nativeTTAd:TTNativeExpressAd?=null
    private var nativeSamllTTAd:TTNativeExpressAd?=null
    private var mIsLoaded = false //全屏视频是否加载完毕
    private var mttJiLiVideoAd: TTRewardVideoAd? = null
    var videoTTAd:TTRewardVideoAd?=null

    init {
        TTAdManagerHolder.init(BaseApplication.application)
    }

    override fun showAdView(type: AD.AdType) {
        BaseConstant.mainHandler.post {
            when (type) {
//                AD.AdType.BANNER -> showBannerView()
                AD.AdType.INSET -> showInsertView()
                AD.AdType.SPLASH -> showSplashView()
                AD.AdType.NATIVE -> showNativeView()
            }
        }
    }

    override fun showFullVideoView(jiliCallback: AdController.JILICallback) {
//        var str = SPUtil.getInstance().getString(page)
//        if (str != null){
//            var typeBean: TypeBean?
//            try {
//                typeBean = GsonUtils.parseObject(str, TypeBean::class.java)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                return
//            }
//            if (typeBean?.incentive_video?.status!!){
//
//            }else{
//                Log.e(TAG_TT,"----激励视频开关关闭")
//            }
//        }
        val adSlot =AdSlot.Builder()
            .setCodeId(ADConstants.kTouTiaoJiLiKey)
            .setImageAcceptedSize(640,320)
            .build()
        mTTAdNative = TTAdManagerHolder.get().createAdNative(activity)
        mTTAdNative?.loadRewardVideoAd(adSlot, object : TTAdNative.RewardVideoAdListener{
            override fun onRewardVideoAdLoad(p0: TTRewardVideoAd?) {
                mIsLoaded = false
                mttJiLiVideoAd = p0
                mttJiLiVideoAd?.setRewardAdInteractionListener(object : TTRewardVideoAd.RewardAdInteractionListener{
                    override fun onRewardVerify(
                        p0: Boolean,
                        p1: Int,
                        p2: String?,
                        p3: Int,
                        p4: String?
                    ) {

                    }

                    override fun onRewardArrived(p0: Boolean, p1: Int, p2: Bundle?) {

                    }

                    override fun onSkippedVideo() {

                    }

                    override fun onAdShow() {

                    }

                    override fun onAdVideoBarClick() {

                    }

                    override fun onVideoComplete() {
                        //视频播放完成回调

                    }

                    override fun onAdClose() {
                        jiliCallback.close()
                    }

                    override fun onVideoError() {

                    }
                })
                try{
                    activity?.runOnUiThread {
                        mttJiLiVideoAd?.showRewardVideoAd(activity, TTAdConstant.RitScenes.CUSTOMIZE_SCENES, null)
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }

            override fun onRewardVideoCached() {
                mIsLoaded = true
            }

            override fun onRewardVideoCached(p0: TTRewardVideoAd?) {

            }

            override fun onError(p0: Int, p1: String?) {
                LogUtils.e("${p1}")
            }
        } )
    }

    private fun showSplashView() {
        MobclickAgent.onEvent(activity, BaseConstant.SPLASH_REQUEST_TT)
        mTTAdNative = TTAdManagerHolder.get().createAdNative(activity)
        BaseConstant.mainHandler.sendEmptyMessageDelayed(MSG_GO_MAIN, AD_TIME_OUT.toLong())
        val adSlot = container?.let {
            AdSlot.Builder()
                .setCodeId(ADConstants.kTouTiaoKaiPing)
                .setSupportDeepLink(true)
                .setImageAcceptedSize(        BaseConstant.application.resources.displayMetrics.widthPixels
                        , BaseConstant.application.resources.displayMetrics.heightPixels)
                .build()
        }
        //step4:请求广告，调用开屏广告异步请求接口，对请求回调的广告作渲染处理
        mTTAdNative?.loadSplashAd(adSlot, object : TTAdNative.SplashAdListener {
            @MainThread
            override fun onError(code: Int, message: String) {
                MobclickAgent.onEvent(activity, BaseConstant.SPLASH_ERROR_TT)
                Log.e(TAG_TT, "头条开屏"+"code: ${code} message:${message}")
                if (!isRepetitionRequest){
                    if (BuildConfig.DEBUG){
                        activity?.runOnUiThread {
                            Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
                        }
                    }
                    KS_AD().also { ad ->
                        ad.isRepetitionRequest=true
                        ad.activity = activity
                        ad.container = container
                        ad.bannerContainer = bannerContainer
                        ad.page = page
                        ad.loading = loading
                        ad.showAdView(AD.AdType.SPLASH)
                    }
                }else{
                    activity?.let {
                        if (it is SplashActivity){
                            it.checkIn()
                        }else{
                            it.finish()
                        }
                    }
                }
            }

            @MainThread
            override fun onTimeout() {
                MobclickAgent.onEvent(activity, BaseConstant.SPLASH_TIME_OUT_TT)
                if (!isRepetitionRequest){
                    if (BuildConfig.DEBUG){
                        activity?.runOnUiThread {
                            Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
                        }
                    }
                    KS_AD().also { ad ->
                        ad.isRepetitionRequest=true
                        ad.activity = activity
                        ad.container = container
                        ad.bannerContainer = bannerContainer
                        ad.page = page
                        ad.loading = loading
                        ad.showAdView(AD.AdType.SPLASH)
                    }
                }else{
                    activity?.let {
                        if (it is SplashActivity){
                            it.checkIn()
                        }else{
                            it.finish()
                        }
                    }
                }
            }

            @MainThread
            override fun onSplashAdLoad(ad: TTSplashAd?) {
                MobclickAgent.onEvent(activity, BaseConstant.SPLASH_REQUEST_SUCCESS_TT)
                Log.d(TAG_TT, "开屏广告请求成功")
                //获取SplashView
                val view = ad?.splashView?:return
                val layoutParams = FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT,FrameLayout.LayoutParams.MATCH_PARENT)
                view.layoutParams = layoutParams;
                container?.removeAllViews()
                //把SplashView 添加到ViewGroup中,注意开屏广告view：width >=70%屏幕宽；height >=50%屏幕宽
                container?.addView(view)
                //设置不开启开屏广告倒计时功能以及不显示跳过按钮,如果这么设置，您需要自定义倒计时逻辑
                //ad.setNotAllowSdkCountdown();

                //设置SplashView的交互监听器
                ad.setSplashInteractionListener(object : TTSplashAd.AdInteractionListener {
                    override fun onAdClicked(view: View, type: Int) {
                        Log.d(TAG_TT, "onAdClicked")
                    }

                    override fun onAdShow(view: View, type: Int) {
                        MobclickAgent.onEvent(activity, BaseConstant.SPLASH_SHOW_TT)
                        Log.d(TAG_TT, "onAdShow")
                    }

                    override fun onAdSkip() {
                        Log.d(TAG_TT, "onAdSkip")
                        activity?.let {
                            if (it is SplashActivity){
                                it.checkIn()
                            }else{
                                it.finish()
                            }
                        }

                    }

                    override fun onAdTimeOver() {
                        Log.d(TAG_TT, "onAdTimeOver")
                        activity?.let {
                            if (it is SplashActivity){
                                it.checkIn()
                            }else{
                                it.finish()
                            }
                        }
                    }
                })
            }
        }, AD_TIME_OUT)
    }

    private fun showInsertView() {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(activity)
        val adSlot=AdSlot.Builder()
                .setCodeId(ADConstants.kTouTiaoChaPingKey)
                .setSupportDeepLink(true)
                .setAdCount(1)
                .setExpressViewAcceptedSize(350f,350f) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640,320 )//这个参数设置即可，不影响个性化模板广告的size
                .build()
        mTTAdNative?.loadInteractionExpressAd(adSlot,object :TTAdNative.NativeExpressAdListener{
            override fun onNativeExpressAdLoad(ads: MutableList<TTNativeExpressAd>?) {
                Log.e("头条插屏广告加载成功", TAG_TT)
                if (ads.isNullOrEmpty()){
                    return
                }
                insertTTAd?.destroy()
                insertTTAd = ads[0]
                insertTTAd?.also {
                    bindAdListener(it)
                }
                insertTTAd?.render()//调用render开始渲染广告
            }

            override fun onError(p0: Int, p1: String?) {
                Log.e(TAG_TT, "头条插屏广告请求失败"+"code:${p0} msg:${p1}")
            }

        })
    }

    private fun showBannerView() {
        mTTAdNative = TTAdManagerHolder.get().createAdNative(activity)
        val displayMetrics=activity?.resources?.displayMetrics?:return
        val width=displayMetrics.widthPixels/displayMetrics.scaledDensity
        val adSlot = AdSlot.Builder()
                .setCodeId(ADConstants.kTouTiaoBannerKey) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(width,70f) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640,320 )//这个参数设置即可，不影响个性化模板广告的size
                .build()
        mTTAdNative?.loadBannerExpressAd(adSlot,object :TTAdNative.NativeExpressAdListener{
            override fun onNativeExpressAdLoad(ads: MutableList<TTNativeExpressAd>?) {
                Log.e("头条Banner广告加载成功", TAG_TT)
                bannerTTAd?.destroy()
                bannerTTAd=ads?.get(0)
                bannerTTAd?.also {
                    bindAdListener(it)
                    it.setDislikeCallback(activity,object :TTAdDislike.DislikeInteractionCallback{
                        override fun onSelected(p0: Int, p1: String?, p2: Boolean) {
                            bannerContainer?.removeAllViews()
                        }

                        override fun onCancel() {

                        }

                        override fun onShow() {

                        }

                    })
                }
                bannerTTAd?.render()
            }

            override fun onError(p0: Int, p1: String?) {
                Log.e(TAG_TT,"头条Banner广告请求失败"+"code:${p0} msg:${p1}")
            }

        })
    }

    private fun showNativeView() {
        MobclickAgent.onEvent(activity, BaseConstant.NATIVE_REQUEST_TT)
        mTTAdNative = TTAdManagerHolder.get().createAdNative(activity)
        val adSlot = AdSlot.Builder()
                .setCodeId(ADConstants.kTouTiaoSeniorKey) //广告位id
                .setSupportDeepLink(true)
                .setAdCount(1) //请求广告数量为1到3条
                .setExpressViewAcceptedSize(300f,SizeUtils.fitFeedHeight(DeviceUtils.getScreenHeight(activity))) //期望个性化模板广告view的size,单位dp
                .setImageAcceptedSize(640,320) //这个参数设置即可，不影响个性化模板广告的size
                .build()

        //step5:请求广告，对请求回调的广告作渲染处理  5.6.13.15.20.21.22.27.29
        mTTAdNative?.loadNativeExpressAd(adSlot, object :TTAdNative.NativeExpressAdListener{
            override fun onNativeExpressAdLoad(p0: MutableList<TTNativeExpressAd>?) {
                MobclickAgent.onEvent(activity, BaseConstant.NATIVE_REQUEST_SUCCESS_TT)
                Log.e(TAG_TT,"头条原生广告加载成功")
                nativeTTAd?.destroy()
                nativeTTAd=p0?.get(0)
                nativeTTAd?.also {
                    bindAdListener(it)
                    it.setDislikeCallback(activity,object :TTAdDislike.DislikeInteractionCallback{
                        override fun onSelected(p0: Int, p1: String?, p2: Boolean) {
                            container?.removeAllViews()
                        }

                        override fun onCancel() {

                        }

                        override fun onShow() {

                        }

                    })
                }
                nativeTTAd?.render()
            }

            override fun onError(p0: Int, p1: String?) {
                MobclickAgent.onEvent(activity, BaseConstant.NATIVE_ERROR_TT)
                Log.e(TAG_TT,"头条原生广告请求失败"+"code:${p0} msg:${p1}")
                if (!isRepetitionRequest){
                    if (BuildConfig.DEBUG){
                        activity?.runOnUiThread {
                            Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
                        }
                    }
                    KS_AD().also { ad ->
                        ad.isRepetitionRequest=true
                        ad.activity = activity
                        ad.container = container
                        ad.bannerContainer = bannerContainer
                        ad.page = page
                        ad.loading = loading
                        ad.showAdView(AD.AdType.NATIVE)
                    }
                }
            }

        })

    }

    private var isLoadVideoAd=false
    fun showRewardVideoAd(rewardVideoCallback: IRewardVideoCallback?=null){
        if (isLoadVideoAd){
            return
        }
        isLoadVideoAd=true
        TTAdManagerHolder.get().requestPermissionIfNecessary(activity)
        mTTAdNative = TTAdManagerHolder.get().createAdNative(activity)
        val adSlot = AdSlot.Builder()
                .setCodeId(ADConstants.kTouTiaoJiLiKey)
                .setSupportDeepLink(true)
                .setAdCount(1)
                .setImageAcceptedSize(BaseConstant.application.resources.displayMetrics.widthPixels, BaseConstant.application.resources.displayMetrics.heightPixels)
                //必传参数，表来标识应用侧唯一用户；若非服务器回调模式或不需sdk透传
                //可设置为空字符串
                .setUserID("")
                .setOrientation(TTAdConstant.VERTICAL)  //设置期望视频播放的方向，为TTAdConstant.HORIZONTAL或TTAdConstant.VERTICAL
//                .setMediaExtra("media_extra") //用户透传的信息，可不传
                .build()
        mTTAdNative?.loadRewardVideoAd(adSlot,object :TTAdNative.RewardVideoAdListener{
            override fun onRewardVideoAdLoad(p0: TTRewardVideoAd?) {
                isLoadVideoAd=false
                videoTTAd=p0
                videoTTAd?.setShowDownLoadBar(false)
                videoTTAd?.setRewardAdInteractionListener(object :TTRewardVideoAd.RewardAdInteractionListener{
                    override fun onRewardVerify(
                        p0: Boolean,
                        p1: Int,
                        p2: String?,
                        p3: Int,
                        p4: String?
                    ) {
                        rewardVideoCallback?.onRewardVerify(p0,p1,p2)
                    }

                    override fun onRewardArrived(p0: Boolean, p1: Int, p2: Bundle?) {

                    }

                    override fun onSkippedVideo() {
//                        rewardVideoCallback?.onSkippedVideo()
                    }

                    override fun onAdShow() {
                        rewardVideoCallback?.onAdShow()
                    }

                    override fun onAdVideoBarClick() {
                        rewardVideoCallback?.onAdVideoBarClick()
                    }

                    override fun onVideoComplete() {
                        rewardVideoCallback?.onVideoComplete()
                    }

                    override fun onAdClose() {
                        rewardVideoCallback?.onAdClose()
                    }

                    override fun onVideoError() {
                        rewardVideoCallback?.onVideoError()
                    }

                })
            }

            override fun onRewardVideoCached() {
                rewardVideoCallback?.onLoadVideoCached()
                isLoadVideoAd=false
                videoTTAd?.showRewardVideoAd(activity)
            }

            override fun onRewardVideoCached(p0: TTRewardVideoAd?) {

            }

            override fun onError(p0: Int, p1: String?) {
                isLoadVideoAd=false
                Log.e(TAG_TT,"头条视频广告失败"+"code:${p0} msg:${p1}")
                rewardVideoCallback?.onVideoError()
            }

        })
    }

    //绑定广告行为
    private fun bindAdListener(ad: TTNativeExpressAd) {
        ad.setExpressInteractionListener(object : TTNativeExpressAd.AdInteractionListener {

            override fun onAdDismiss() {//广告关闭
            }

            override fun onAdClicked(view: View, type: Int) {//广告被点击
            }

            override fun onAdShow(view: View, type: Int) {//广告展示
                if (ad==nativeTTAd){
                    MobclickAgent.onEvent(activity, BaseConstant.NATIVE_SHOW_TT)
                }
            }

            override fun onRenderFail(view: View, msg: String, code: Int) {
//                TToast.show(mContext, "$msg code:$code")
            }

            override fun onRenderSuccess(view: View, width: Float, height: Float) {//渲染成功//宽高 单位 dp
                if (ad==insertTTAd){
                    ad.showInteractionExpressAd(activity)
                }else if (ad==nativeTTAd){
                    container?.removeAllViews()
                    container?.addView(view)
                }else if (ad==bannerTTAd){
                    bannerContainer?.removeAllViews()
                    bannerContainer?.addView(view)
                }

            }
        })

        if (ad.interactionType != TTAdConstant.INTERACTION_TYPE_DOWNLOAD) {
            return
        }
        //可选，下载监听设置
        ad.setDownloadListener(object : TTAppDownloadListener {
            override fun onIdle() {//点击开始下载
            }

            override fun onDownloadActive(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {
                if (!mHasShowDownloadActive) {//下载中，点击暂停
                    mHasShowDownloadActive = true
                }
            }

            override fun onDownloadPaused(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {//下载暂停，点击继续
            }

            override fun onDownloadFailed(totalBytes: Long, currBytes: Long, fileName: String, appName: String) {//下载失败，点击重新下载
            }

            override fun onInstalled(fileName: String, appName: String) {//安装完成，点击图片打开
            }

            override fun onDownloadFinished(totalBytes: Long, fileName: String, appName: String) {//点击安装
            }
        })
    }


    override fun destroy(type: AD.AdType) {
        when (type) {

            AD.AdType.SPLASH -> {
            }
            AD.AdType.INSET -> {
                insertTTAd?.destroy()
            }
            AD.AdType.NATIVE -> {
            }
        }
    }
}
