package com.feisukj.ad.manager

//import android.util.Log
//import android.view.View
//import com.feisukj.ad.SplashActivity
//import com.qq.e.ads.banner2.UnifiedBannerADListener
//import com.qq.e.ads.banner2.UnifiedBannerView
//import com.qq.e.ads.cfg.DownAPPConfirmPolicy
//import com.qq.e.ads.interstitial2.UnifiedInterstitialAD
//import com.qq.e.ads.interstitial2.UnifiedInterstitialADListener
//import com.qq.e.ads.nativ.ADSize
//import com.qq.e.ads.nativ.NativeExpressAD
//import com.qq.e.ads.nativ.NativeExpressADView
//import com.qq.e.ads.nativ.NativeExpressMediaListener
//import com.qq.e.ads.splash.SplashAD
//import com.qq.e.ads.splash.SplashADListener
//import com.qq.e.comm.constants.AdPatternType
//import com.qq.e.comm.pi.AdData
//import com.qq.e.comm.util.AdError
//import android.graphics.Point
//import android.widget.FrameLayout
//import android.widget.Toast
//import com.feisukj.ad.BuildConfig
//import com.feisukj.base.BaseConstant
//import com.feisukj.base.bean.ad.AD
//import com.feisukj.base.bean.ad.ADConstants
//import com.qq.e.ads.rewardvideo.RewardVideoAD
//import com.qq.e.ads.rewardvideo.RewardVideoADListener
//import com.umeng.analytics.MobclickAgent
//import kotlin.math.roundToInt
//
//
//class GDT_AD : AbsADParent() {
//    companion object{
//        private const val TAG_GDT="广点通广告"
//    }
//    private var splashAD: SplashAD? = null
//    private var unifiedBannerView: UnifiedBannerView? = null
//    private var insertView: UnifiedInterstitialAD? = null
//    private var nativeExpressAD: NativeExpressAD? = null
//    private var nativeExpressADView: NativeExpressADView? = null
//    private var iad:UnifiedInterstitialAD? = null
//
//    private val mediaListener = object : NativeExpressMediaListener {
//        override fun onVideoCached(p0: NativeExpressADView?) {
//
//        }
//
//        override fun onVideoInit(nativeExpressADView: NativeExpressADView) {
//            Log.i(TAG_GDT, "onVideoInit: " + getVideoInfo(nativeExpressADView.boundData.getProperty(AdData.VideoPlayer::class.java)))
//        }
//
//        override fun onVideoLoading(nativeExpressADView: NativeExpressADView) {
//            Log.i(TAG_GDT, "onVideoLoading")
//        }
//
//        override fun onVideoReady(nativeExpressADView: NativeExpressADView, l: Long) {
//            Log.i(TAG_GDT, "onVideoReady")
//        }
//
//        override fun onVideoStart(nativeExpressADView: NativeExpressADView) {
//            Log.i(TAG_GDT, "onVideoStart: " + getVideoInfo(nativeExpressADView.boundData.getProperty(AdData.VideoPlayer::class.java)))
//        }
//
//        override fun onVideoPause(nativeExpressADView: NativeExpressADView) {
//            Log.i(TAG_GDT, "onVideoPause: " + getVideoInfo(nativeExpressADView.boundData.getProperty(AdData.VideoPlayer::class.java)))
//        }
//
//        override fun onVideoComplete(nativeExpressADView: NativeExpressADView) {
//            Log.i(TAG_GDT, "onVideoComplete: " + getVideoInfo(nativeExpressADView.boundData.getProperty(AdData.VideoPlayer::class.java)))
//        }
//
//        override fun onVideoError(nativeExpressADView: NativeExpressADView, adError: AdError) {
//            Log.i(TAG_GDT, "onVideoError")
//        }
//
//        override fun onVideoPageOpen(nativeExpressADView: NativeExpressADView) {
//            Log.i(TAG_GDT, "onVideoPageOpen")
//        }
//
//        override fun onVideoPageClose(nativeExpressADView: NativeExpressADView) {
//            Log.i(TAG_GDT, "onVideoPageClose")
//        }
//    }
//
//    override fun showAdView(type: AD.AdType) {
//        when (type) {
//            AD.AdType.BANNER -> showBannerView()
//            AD.AdType.INSET -> showInsertView()
//            AD.AdType.SPLASH -> showSplashView()
//            AD.AdType.NATIVE ->
//                refreshAd()
//        }
//    }
//
//    private var rewardVideoAD:RewardVideoAD?=null
//    override fun showFullVideoView(jiliCallback: AdController.JILICallback) {
//        rewardVideoAD= RewardVideoAD(activity,ADConstants.kGDTMobSDKJiLiKey,object :
//            RewardVideoADListener{
//            override fun onADExpose() {
//
//            }
//
//            override fun onADClick() {
//
//            }
//
//            override fun onVideoCached() {
//
//            }
//
//            override fun onReward(p0: MutableMap<String, Any>?) {//发放奖励
//
//            }
//
//            override fun onADClose() {
//                jiliCallback.close()
//            }
//
//            override fun onADLoad() {
//                rewardVideoAD?.showAD()
//            }
//
//            override fun onVideoComplete() {
//
//            }
//
//            override fun onError(p0: AdError?) {
//
//                if (p0==null)
//                    return
//                Log.e(TAG_GDT,"广点通 视频 errorCode:${p0.errorCode} errorMsg${p0.errorMsg}")
//            }
//
//            override fun onADShow() {
//                Log.i("onADShow","GDT")
//            }
//
//        })
//        rewardVideoAD?.loadAD()
//    }
//
//
//    override fun destroy(type: AD.AdType) {
//        when (type) {
//            AD.AdType.SPLASH -> {
//            }
//            AD.AdType.INSET ->{
//                insertView?.destroy()
//            }
//            AD.AdType.BANNER -> {
//                unifiedBannerView?.destroy()
//            }
//            AD.AdType.NATIVE -> {
//            }
//        }
//    }
//    /**
//     * 闪屏广告
//     */
//    private fun showSplashView() {
//        MobclickAgent.onEvent(activity, BaseConstant.SPLASH_REQUEST_GDT)
//        splashAD = SplashAD(activity,ADConstants.kGDTMobSDKKaiPingKey, object : SplashADListener {
//            override fun onADDismissed() {
//                Log.i(TAG_GDT, "onADDismissed")
//                activity?.let {
//                    if (it is SplashActivity){
//                        it.checkIn()
//                    }else{
//                        it.finish()
//                    }
//                }
//            }
//
//            override fun onNoAD(adError: AdError) {
//                MobclickAgent.onEvent(activity, BaseConstant.SPLASH_ERROR_GDT)
//                Log.e(TAG_GDT, " 广点通开屏 adError==" + "code:${adError.errorCode} msg:${adError.errorMsg}")
//                if (!isRepetitionRequest){
//                    if (BuildConfig.DEBUG){
//                        activity?.runOnUiThread {
//                            Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
//                        }
//                    }
//                    TT_AD().also { ad ->
//                        ad.isRepetitionRequest=true
//                        ad.activity = activity
//                        ad.container = container
//                        ad.bannerContainer = bannerContainer
//                        ad.page = page
//                        ad.loading = loading
//                        ad.showAdView(AD.AdType.SPLASH)
//                    }
//                }else{
//                    activity?.let {
//                        if (it is SplashActivity){
//                            it.checkIn()
//                        }else{
//                            it.finish()
//                        }
//                    }
//                }
//            }
//
//            override fun onADPresent() {
//                MobclickAgent.onEvent(activity, BaseConstant.SPLASH_REQUEST_SUCCESS_GDT)
//                if (null != activity) {
//                    Log.i(TAG_GDT, "onADPresent")
//                } else {
//
//                }
//            }
//
//            override fun onADLoaded(p0: Long) {
//
//            }
//
//            override fun onADClicked() {
//                if (null != activity) {
//                    Log.i(TAG_GDT, "onADClicked")
//                } else {
//
//                }
//            }
//
//            override fun onADTick(l: Long) {
//                Log.i(TAG_GDT, "SplashADTick: " + l + "ms")
//            }
//
//            override fun onADExposure() {
//                MobclickAgent.onEvent(activity, BaseConstant.SPLASH_SHOW_GDT)
//                Log.i(TAG_GDT, "onADExposure:")
//            }
//        }, 0)
//        splashAD?.fetchAndShowIn(container)
//    }
//
//
//    /**
//     * 原生广告(初始化)NativeExpressAD
//     */
//    private fun refreshAd() {
//        MobclickAgent.onEvent(activity, BaseConstant.NATIVE_REQUEST_GDT)
//        /**
//         * 如果选择支持视频的模版样式，请使用[Constants.NativeExpressSupportVideoPosID]
//         */
//        nativeExpressAD = NativeExpressAD(activity,
//                ADSize(ADSize.AUTO_HEIGHT, ADSize.FULL_WIDTH),
//                ADConstants.kGDTMobSDKNativeKey,
//                object : NativeExpressAD.NativeExpressADListener {
//                    override fun onNoAD(adError: AdError) {
//                        MobclickAgent.onEvent(activity, BaseConstant.NATIVE_ERROR_GDT)
//                        Log.e(TAG_GDT, "广点通原生" +"code:${adError.errorCode} msg:${adError.errorMsg}")
//                        if (!isRepetitionRequest){
//                            if (BuildConfig.DEBUG){
//                                activity?.runOnUiThread {
//                                    Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                            TT_AD().also { ad ->
//                                ad.isRepetitionRequest=true
//                                ad.activity = activity
//                                ad.container = container
//                                ad.bannerContainer = bannerContainer
//                                ad.page = page
//                                ad.loading = loading
//                                ad.showAdView(AD.AdType.NATIVE)
//                            }
//                        }
//                    }
//
//                    override fun onADLoaded(list: List<NativeExpressADView>) {
//                        MobclickAgent.onEvent(activity, BaseConstant.NATIVE_REQUEST_SUCCESS_GDT)
//                        Log.i(TAG_GDT, "initGDT_NativeExpressAD_onADLoaded")
//                        // 释放前一个展示的NativeExpressADView的资源
//                        if (nativeExpressADView != null) {
//                            nativeExpressADView?.destroy()
//                        }
//
//                        if (container?.visibility != View.VISIBLE) {
//                            container?.visibility = View.VISIBLE
//                        }
//
//                        if ((container?.childCount?:0) > 0) {
//                            container?.removeAllViews()
//                        }
//
//                        nativeExpressADView = list[0]
//                        if (nativeExpressADView?.boundData?.adPatternType == AdPatternType.NATIVE_VIDEO) {
//                            nativeExpressADView?.setMediaListener(mediaListener)
//                        }
//                        // 广告可见才会产生曝光，否则将无法产生收益。
//                        container?.visibility = View.VISIBLE
//                        container?.addView(nativeExpressADView)
//                        nativeExpressADView?.render()
//                    }
//
//                    override fun onRenderFail(nativeExpressADView: NativeExpressADView) {
//                        MobclickAgent.onEvent(activity, BaseConstant.NATIVE_FAIL_GDT)
//                        Log.i(TAG_GDT, "initGDT_NativeExpressAD_onRenderFail")
//                        if (!isRepetitionRequest){
//                            if (BuildConfig.DEBUG){
//                                activity?.runOnUiThread {
//                                    Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                            TT_AD().also { ad ->
//                                ad.isRepetitionRequest=true
//                                ad.activity = activity
//                                ad.container = container
//                                ad.bannerContainer = bannerContainer
//                                ad.page = page
//                                ad.loading = loading
//                                ad.showAdView(AD.AdType.NATIVE)
//                            }
//                        }
//                    }
//
//                    override fun onRenderSuccess(nativeExpressADView: NativeExpressADView) {
//                        Log.i(TAG_GDT, "initGDT_NativeExpressAD_onRenderSuccess")
//
//                    }
//
//                    override fun onADExposure(nativeExpressADView: NativeExpressADView) {
//                        MobclickAgent.onEvent(activity, BaseConstant.NATIVE_SHOW_GDT)
//                        Log.i(TAG_GDT, "initGDT_NativeExpressAD_onADExposure")
//
//                    }
//
//                    override fun onADClicked(nativeExpressADView: NativeExpressADView) {
//                        Log.i(TAG_GDT, "initGDT_NativeExpressAD_onADClicked")
//
//                    }
//
//                    override fun onADClosed(nativeExpressADView: NativeExpressADView) {
//                        Log.i(TAG_GDT, "initGDT_NativeExpressAD_onADClosed")
//                        container?.visibility = View.GONE
//                    }
//
//                    override fun onADLeftApplication(nativeExpressADView: NativeExpressADView) {
//                        Log.i(TAG_GDT, "initGDT_NativeExpressAD_onADLeftApplication")
//
//                    }
//                }) // 这里的Context必须为Activity
//        //            nativeExpressAD.setVideoOption(new VideoOption.Builder()
//        //                    .setAutoPlayPolicy(VideoOption.AutoPlayPolicy.WIFI) // 设置什么网络环境下可以自动播放视频
//        //                    .setAutoPlayMuted(true) // 设置自动播放视频时，是否静音
//        //                    .build()); // setVideoOption是可选的，开发者可根据需要选择是否配置
//
//        nativeExpressAD?.loadAD(1)
//        nativeExpressAD?.setDownAPPConfirmPolicy(DownAPPConfirmPolicy.Default)
//
//    }
//
//    /**
//     * 插屏广告
//     */
//    private fun showInsertView(){
//        insertView?.destroy()
//        insertView=UnifiedInterstitialAD(activity,ADConstants.kGDTMobSDKChaPingKey,object : UnifiedInterstitialADListener{
//            override fun onVideoCached() {
//
//            }
//
//            override fun onADExposure() {
//
//            }
//
//            override fun onADOpened() {
//
//            }
//
//            override fun onADClosed() {
//
//            }
//
//            override fun onRenderSuccess() {
//
//            }
//
//            override fun onRenderFail() {
//
//            }
//
//            override fun onADLeftApplication() {
//
//            }
//
//            override fun onADReceive() {
//                insertView?.show()
//            }
//
//            override fun onNoAD(error: AdError?) {
//                Log.e(TAG_GDT,"广点通  插屏广告"+"code:${error?.errorCode} msg:${error?.errorMsg}")
//            }
//
//            override fun onADClicked() {
//
//            }
//
//        })
//        insertView?.loadAD()
//    }
//
//    private fun showBannerView(){
//        unifiedBannerView?.destroy()
//        unifiedBannerView=UnifiedBannerView(activity,ADConstants.kGDTMobSDKBannerKey,object : UnifiedBannerADListener{
//
//            override fun onADExposure() {
//
//            }
//
//            override fun onADClosed() {
//
//            }
//
//            override fun onADLeftApplication() {
//
//            }
//
//            override fun onNoAD(error: AdError?) {
//                Log.e(TAG_GDT,"广点通Banner广告"+"code:${error?.errorCode} msg:${error?.errorMsg}")
//            }
//
//            override fun onADReceive() {
//                bannerContainer?.removeAllViews()
//                if (bannerContainer?.visibility!=View.VISIBLE){
//                    bannerContainer?.visibility=View.VISIBLE
//                }
//                val screenSize = Point()
//                activity?.windowManager!!.defaultDisplay.getSize(screenSize)
//                bannerContainer?.addView(unifiedBannerView,FrameLayout.LayoutParams(screenSize.x, (screenSize.x / 6.4F).roundToInt()))
//            }
//
//            override fun onADClicked() {
//
//            }
//
//        })
//        unifiedBannerView?.loadAD()
//    }
//
//    /**
//     * 获取播放器实例
//     *
//     * 仅当视频回调[NativeExpressMediaListener.onVideoInit]调用后才会有返回值
//     *
//     * @param videoPlayer
//     * @return
//     */
//    private fun getVideoInfo(videoPlayer: AdData.VideoPlayer?): String? {
//        if (videoPlayer != null) {
//            val videoBuilder = StringBuilder()
//            videoBuilder.append("{state:").append(videoPlayer.videoState).append(",")
//                    .append("duration:").append(videoPlayer.duration).append(",")
//                    .append("position:").append(videoPlayer.currentPosition).append("}")
//            return videoBuilder.toString()
//        }
//        return null
//    }
//
////    private var rewardVideoAD:RewardVideoAD?=null
////    fun showRewardVideoAD(rewardVideoCallback: IRewardVideoCallback?=null){
////        rewardVideoAD= RewardVideoAD(activity,ADConstants.kGDTMobSDKAppKey,ADConstants.kGDTMobSDKJiLiKey,object :
////                RewardVideoADListener{
////            override fun onADExpose() {
////
////            }
////
////            override fun onADClick() {
////
////            }
////
////            override fun onVideoCached() {
////                rewardVideoCallback?.onLoadVideoCached()
////            }
////
////            override fun onReward() {//发放奖励
////                rewardVideoCallback?.onRewardVerify(true,1,"")
////            }
////
////            override fun onADClose() {
////
////            }
////
////            override fun onADLoad() {
////                rewardVideoAD?.showAD()
////            }
////
////            override fun onVideoComplete() {
////                rewardVideoCallback?.onVideoComplete()
////            }
////
////            override fun onError(p0: AdError?) {
////                rewardVideoCallback?.onVideoError()
////                if (p0==null)
////                    return
////                Log.e(TAG_GDT,"广点通 视频 errorCode:${p0.errorCode} errorMsg${p0.errorMsg}")
////            }
////
////            override fun onADShow() {
////                Log.i("onADShow","GDT")
////            }
////
////        })
////        rewardVideoAD?.loadAD()
////    }
//
//}