package com.feisukj.ad.manager

import android.annotation.SuppressLint
import android.content.DialogInterface
import android.text.TextUtils
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.bumptech.glide.Glide
import com.feisukj.ad.BuildConfig
import com.feisukj.ad.R
import com.feisukj.ad.SplashActivity
import com.feisukj.base.BaseConstant
import com.feisukj.base.bean.ad.AD
import com.feisukj.base.bean.ad.ADConstants
import com.feisukj.base.util.LogUtils
import com.feisukj.base.util.ToastUtil
import com.kwad.sdk.api.*
import com.kwad.sdk.api.KsInnerAd.KsInnerAdInteractionListener
import com.kwad.sdk.api.KsLoadManager.InterstitialAdListener
import com.kwad.sdk.api.KsLoadManager.SplashScreenAdListener
import com.kwad.sdk.api.KsNativeAd.VideoPlayListener
import com.kwad.sdk.api.KsSplashScreenAd.SplashScreenAdInteractionListener
import com.kwad.sdk.api.model.AdSourceLogoType
import com.kwad.sdk.api.model.InteractionType
import com.kwad.sdk.api.model.KsNativeConvertType
import com.kwad.sdk.api.model.MaterialType

class KS_AD: AbsADParent() {

    private val TAG = "快手广告:"
    private var mKsInterstitialAd: KsInterstitialAd? = null
    private var isAdRequesting = false
    private var mRewardVideoAd: KsRewardVideoAd? = null
    private var isOver = false      //是否观看完整视频

    override fun showAdView(type: AD.AdType) {
        BaseConstant.mainHandler.post {
            when (type) {
                AD.AdType.INSET -> showInsertView() //insert_screen
                AD.AdType.SPLASH -> showSplashView() //spread_screen
                AD.AdType.NATIVE ->
                    if (baseAdAdapter == null){
                        showNativeView() //native_screen
                    }else{
                        getNativeAdLists()
                    }
            }
        }
    }
    private fun showInsertView(){
        val posId = try {
            ADConstants.kWaiChaPingKey.toLong()
        }catch (e: Exception){
            e.printStackTrace()
            0L
        }
        if (posId == 0L)
            return
        if (isAdRequesting) {
            return
        }
        isAdRequesting = true
        mKsInterstitialAd = null
        val scene = KsScene.Builder(posId)
//            .setBackUrl("ksad://returnback")
            .build()
        KsAdSDK.getLoadManager()!!.loadInterstitialAd(scene,
            object : InterstitialAdListener {
                override fun onError(code: Int, msg: String) {
                    if (!isRepetitionRequest) {
                        if (BuildConfig.DEBUG) {
                            activity?.runOnUiThread {
                                Toast.makeText(activity, "重新请求广告", Toast.LENGTH_SHORT).show()
                            }
                        }
                        TT_AD().also { ad ->
                            ad.isRepetitionRequest = true
                            ad.activity = activity
                            ad.container = container
                            ad.bannerContainer = bannerContainer
                            ad.page = page
                            ad.loading = loading
                            ad.showAdView(AD.AdType.INSET)
                        }
                    }
                }

                override fun onRequestResult(adNumber: Int) {
//                    ToastUtil.showToast(mContext, "插屏广告请求填充个数 $adNumber")
                }

                override fun onInterstitialAdLoad( adList: List<KsInterstitialAd>?) {
                    isAdRequesting = false
                    if (adList != null && adList.isNotEmpty()) {
                        mKsInterstitialAd = adList[0]
//                        ToastUtil.showToast(mContext, "插屏广告请求成功")
                        val videoPlayConfig = KsVideoPlayConfig.Builder()
                            .videoSoundEnable(true)
//                            .showLandscape(getRequestedOrientation() == ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE)
                            .build()
                        showInterstitialAd(videoPlayConfig)
                    }
                }
            })
    }

    private fun showSplashView(){
        val posId = try {
            ADConstants.kWaiKaiPing.toLong()
        }catch (e: Exception){
            e.printStackTrace()
            0L
        }
        if (posId == 0L)
            return
        val scene: KsScene  =  KsScene.Builder(posId).build()
        KsAdSDK.getLoadManager()!!.loadSplashScreenAd(scene, object : SplashScreenAdListener {
            override fun onError(code: Int, msg: String) {
                LogUtils.i(TAG,"开屏广告出错：$code -- $msg")
                if (!isRepetitionRequest){
                    if (BuildConfig.DEBUG){
                        activity?.runOnUiThread {
                            Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
                        }
                    }
                    TT_AD().also { ad ->
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

            override fun onRequestResult(adNumber: Int) {
                LogUtils.e("快手", "onRequestResult")

            }

            override fun onSplashScreenAdLoad( splashScreenAd: KsSplashScreenAd?) {
                val view = splashScreenAd?.getView(activity,
                    object : SplashScreenAdInteractionListener {
                        override fun onAdClicked() {
//                            showTips("开屏广告点击")
                            /**
                             * 开屏广告点击会吊起h5或应用商店，并回调onAdClick(), mGotoMainActivity控制由h5或应用商店返回后是否直接进入主界面
                             * 建议当需要展示开屏小窗时设置为返回后进入主界面，其他情况设为false以继续执行开屏的倒计时
                             */
//                            mGotoMainActivity = mNeedShowMiniWindow
//                            SplashAd.ksSplashScreenAd = null
                        }

                        override fun onAdShowError(code: Int, extra: String) {
                            LogUtils.e(TAG,"开屏广告显示错误 $code extra $extra")
                        }

                        override fun onAdShowEnd() {
//                            showTips("开屏广告显示结束")
                            activity?.let {
                                if (it is SplashActivity){
                                    it.checkIn()
                                }else{
                                    it.finish()
                                }
                            }
                        }

                        override fun onAdShowStart() {
//                            showTips("开屏广告显示开始")
//                            mEmptyView.setVisibility(View.GONE)
                        }

                        override fun onSkippedAd() {
//                            showTips("用户跳过开屏广告")
                            activity?.let {
                                if (it is SplashActivity){
                                    it.checkIn()
                                }else{
                                    it.finish()
                                }
                            }
                        }

                        override fun onDownloadTipsDialogShow() {
//                            showTips("开屏广告显示下载合规弹窗")
                        }

                        override fun onDownloadTipsDialogDismiss() {
//                            showTips("开屏广告关闭下载合规弹窗")
                        }

                        override fun onDownloadTipsDialogCancel() {
//                            showTips("开屏广告取消下载合规弹窗")
                        }
                    })
                container?.removeAllViews()
                container?.addView(view)
            }
        })
    }

    private fun showNativeView(){
        val posId = try {
            ADConstants.kWaiSeniorKey.toLong()
        }catch (e: Exception){
            e.printStackTrace()
            0L
        }
        if (posId == 0L)
            return
        val scene: KsScene = KsScene.Builder(posId).adNum(1).build()
        scene.adNum = 1 // 支持返回多条广告，默认1条，最多5条，参数范围1-5
        KsAdSDK.getLoadManager()!!.loadFeedAd(scene, object : KsLoadManager.FeedAdListener {
            override fun onError(code: Int, msg: String) {
                LogUtils.e("快手广告:","广告数据请求失败$code$msg")
                if (!isRepetitionRequest){
                    if (BuildConfig.DEBUG){
                        activity?.runOnUiThread {
                            Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
                        }
                    }
                    TT_AD().also { ad ->
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

            override fun onFeedAdLoad(adList: MutableList<KsFeedAd>?) {
                if (adList == null || adList.isEmpty()) {
                    LogUtils.e("快手广告:","广告数据为空")
                    if (!isRepetitionRequest){
                        if (BuildConfig.DEBUG){
                            activity?.runOnUiThread {
                                Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
                            }
                        }
                        TT_AD().also { ad ->
                            ad.isRepetitionRequest=true
                            ad.activity = activity
                            ad.container = container
                            ad.bannerContainer = bannerContainer
                            ad.page = page
                            ad.loading = loading
                            ad.showAdView(AD.AdType.NATIVE)
                        }
                    }
                    return
                }
                val view = adList[0].getFeedView(activity)
//                val view = getAdItemView(container,adList[0])
                if (view!=null && view.parent== null)
                    container?.addView(view)
            }
        })

    }
    private fun getNativeAdLists(){
        val posId = try {
            ADConstants.kWaiSeniorKey.toLong()
        }catch (e: Exception){
            e.printStackTrace()
            0L
        }
        if (posId == 0L)
            return
        val scene: KsScene = KsScene.Builder(posId).adNum(3).build()
        scene.adNum = 5 // 支持返回多条广告，默认1条，最多5条，参数范围1-5
        KsAdSDK.getLoadManager()!!.loadFeedAd(scene, object : KsLoadManager.FeedAdListener {
            override fun onError(code: Int, msg: String) {
                LogUtils.e("快手广告:","广告数据请求失败$code$msg")
                if (!isRepetitionRequest){
                    if (BuildConfig.DEBUG){
                        activity?.runOnUiThread {
                            Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
                        }
                    }
                    TT_AD().also { ad ->
                        ad.isRepetitionRequest=true
                        ad.activity = activity
                        ad.container = container
                        ad.bannerContainer = bannerContainer
                        ad.page = page
                        ad.loading = loading
                        ad.baseAdAdapter = baseAdAdapter
                        ad.showAdView(AD.AdType.NATIVE)
                    }
                }
                baseAdAdapter?.addAdList(null)
            }

            override fun onFeedAdLoad(adList: MutableList<KsFeedAd>?) {
                if (adList == null || adList.isEmpty()) {
                    LogUtils.e("快手广告:","广告数据为空")
                    if (!isRepetitionRequest){
                        if (BuildConfig.DEBUG){
                            activity?.runOnUiThread {
                                Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
                            }
                        }
                        TT_AD().also { ad ->
                            ad.isRepetitionRequest=true
                            ad.activity = activity
                            ad.container = container
                            ad.bannerContainer = bannerContainer
                            ad.page = page
                            ad.loading = loading
                            ad.showAdView(AD.AdType.NATIVE)
                        }
                    }
                    return
                }
                /*11*/
               baseAdAdapter?.addAdList(adList)
            }
        })

    }

    override fun showFullVideoView(jiliCallback: AdController.JILICallback) {
        val posId = try {
            ADConstants.kWaiJiLiKey.toLong()
        }catch (e: Exception){
            e.printStackTrace()
            0L
        }
        if (posId == 0L)
            return
        isOver = false
        val builder = KsScene.Builder(posId)
//            .setBackUrl("ksad://returnback")
            .screenOrientation(SdkConfig.SCREEN_ORIENTATION_PORTRAIT)
//        if (rewardCallbackSwitch.isChecked()) {
//            val rewardCallbackExtraData: MutableMap<String, String> = HashMap()
//            rewardCallbackExtraData["thirdUserId"] = "test-uerid-jia"
//            rewardCallbackExtraData["extraData"] = "testExtraData"
//            builder.rewardCallbackExtraData(rewardCallbackExtraData)
//        }
        val scene = builder.build() // 此为测试posId，请联系快手平台申请正式posId

        // 请求的期望屏幕方向传递为1，表示期望为竖屏
        // 请求的期望屏幕方向传递为1，表示期望为竖屏
        KsAdSDK.getLoadManager()!!
            .loadRewardVideoAd(scene, object : KsLoadManager.RewardVideoAdListener {
                override fun onError(code: Int, msg: String) {
                    LogUtils.e(TAG,"激励视频广告请求失败$code$msg")
                    if (!isRepetitionRequest){
                        if (BuildConfig.DEBUG){
                            activity?.runOnUiThread {
                                Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
                            }
                        }
                        TT_AD().also { ad ->
                            ad.isRepetitionRequest=true
                            ad.activity = activity
                            ad.container = container
                            ad.bannerContainer = bannerContainer
                            ad.page = page
                            ad.loading = loading
                            ad.showFullVideoView(jiliCallback)
                        }
                    }

                }

                override fun onRewardVideoResult(p0: MutableList<KsRewardVideoAd>?) {
                    LogUtils.i(TAG,"激励视频广告请求结果返回 $p0")
                }

                override fun onRewardVideoAdLoad(adList: List<KsRewardVideoAd>?) {
                    if (adList != null && adList.size > 0) {
                        mRewardVideoAd = adList[0]

                        // 设置激励视频内部广告的监听
                        mRewardVideoAd?.setInnerAdInteractionListener(
                            object : KsInnerAdInteractionListener {
                                override fun onAdClicked(ksInnerAd: KsInnerAd) {
                                    LogUtils.i(TAG,"激励视频内部广告点击：" + ksInnerAd.type)
                                }

                                override fun onAdShow(ksInnerAd: KsInnerAd) {
                                    LogUtils.i(TAG,"激励视频内部广告曝光：" + ksInnerAd.type)
                                }
                            })
                        LogUtils.i(TAG,"激励视频广告请求成功")
                        showRewardVideoAd(null,jiliCallback)
                    }
                }
            })
    }

    override fun destroy(type: AD.AdType) {

    }

    private fun showInterstitialAd(videoPlayConfig: KsVideoPlayConfig) {
        mKsInterstitialAd?.apply {
            this
                .setAdInteractionListener(object : KsInterstitialAd.AdInteractionListener {
                    override fun onAdClicked() {
//                        ToastUtil.showToast(mContext, "插屏广告点击")
                    }

                    override fun onAdShow() {
//                        ToastUtil.showToast(mContext, "插屏广告曝光")
                    }

                    override fun onAdClosed() {
//                        ToastUtil.showToast(mContext, "用户点击插屏关闭按钮")
                    }

                    override fun onPageDismiss() {
//                        Log.i(
//                            com.kwad.demo.open.interstitial.TestInterstitialAdActivity.TAG,
//                            "插屏广告关闭"
//                        )
//                        ToastUtil.showToast(mContext, "插屏广告关闭")
                    }

                    override fun onVideoPlayError(code: Int, extra: Int) {
//                        ToastUtil.showToast(mContext, "插屏广告播放出错")
                    }

                    override fun onVideoPlayEnd() {
//                        ToastUtil.showToast(mContext, "插屏广告播放完成")
                    }

                    override fun onVideoPlayStart() {
//                        ToastUtil.showToast(mContext, "插屏广告播放开始")
                    }

                    override fun onSkippedAd() {
//                        ToastUtil.showToast(mContext, "插屏广告播放跳过")
                    }
                })
            this.showInterstitialAd(activity, videoPlayConfig)
        }
    }

    private fun showRewardVideoAd(videoPlayConfig: KsVideoPlayConfig?,jiliCallback: AdController.JILICallback) {
        if (mRewardVideoAd != null && mRewardVideoAd!!.isAdEnable) {
            mRewardVideoAd!!
                .setRewardAdInteractionListener(object :
                    KsRewardVideoAd.RewardAdInteractionListener {
                    override fun onAdClicked() {
//                        ToastUtil.showToast(mContext, "激励视频广告点击")
                    }

                    override fun onPageDismiss() {
//                        ToastUtil.showToast(mContext, "激励视频广告关闭")
                        jiliCallback.close()
                    }

                    override fun onVideoPlayError(code: Int, extra: Int) {
//                        ToastUtil.showToast(mContext, "激励视频广告播放出错")
                        if (!isRepetitionRequest){
                            if (BuildConfig.DEBUG){
                                activity?.runOnUiThread {
                                    Toast.makeText(activity,"重新请求广告", Toast.LENGTH_SHORT).show()
                                }
                            }
                            TT_AD().also { ad ->
                                ad.isRepetitionRequest=true
                                ad.activity = activity
                                ad.container = container
                                ad.bannerContainer = bannerContainer
                                ad.page = page
                                ad.loading = loading
                                ad.showFullVideoView(jiliCallback)
                            }
                        }
                    }

                    override fun onVideoPlayEnd() {
//                        ToastUtil.showToast(mContext, "激励视频广告播放完成")
//                        isOver = true
                    }

                    override fun onVideoSkipToEnd(playDuration: Long) {
//                        ToastUtil.showToast(mContext, "激励视频广告跳过播放完成")
                    }

                    override fun onVideoPlayStart() {
//                        ToastUtil.showToast(mContext, "激励视频广告播放开始")
                    }

                    /**
                     * 激励视频广告激励回调，只会回调一次
                     */
                    override fun onRewardVerify() {
//                        ToastUtil.showToast(mContext, "激励视频广告获取激励")
                        isOver = true
                    }

                    /**
                     * 视频激励分阶段回调
                     * @param taskType 当前激励视频所属任务类型
                     * RewardTaskType.LOOK_VIDEO 观看视频类型             属于浅度奖励类型
                     * RewardTaskType.LOOK_LANDING_PAGE 浏览落地页N秒类型  属于深度奖励类型
                     * RewardTaskType.USE_APP 下载使用App N秒类型          属于深度奖励类型
                     * @param currentTaskStatus  当前所完成任务类型，@RewardTaskType中之一
                     */
                    override fun onRewardStepVerify(taskType: Int, currentTaskStatus: Int) {
//                        ToastUtil.showToast(
//                            mContext, "激励视频广告分阶段获取激励，当前任务类型为：" + getTaskStatusStr(taskType) +
//                                    "，当前完成任务类型为：" + getTaskStatusStr(currentTaskStatus)
//                        )
                    }

                    override fun onExtraRewardVerify(p0: Int) {

                    }
                })

            // 设置"再看一个" 广告的监听
            mRewardVideoAd!!.setRewardPlayAgainInteractionListener(object :
                KsRewardVideoAd.RewardAdInteractionListener {
                private val PREFIX = "再看一个"
                override fun onAdClicked() {
//                    ToastUtil.showToast(mContext, PREFIX + "激励视频广告点击")
                }

                override fun onPageDismiss() {
//                    ToastUtil.showToast(mContext, PREFIX + "激励视频广告关闭")
                }

                override fun onVideoPlayError(code: Int, extra: Int) {
//                    ToastUtil.showToast(mContext, PREFIX + "激励视频广告播放出错")
                }

                override fun onVideoPlayEnd() {
//                    ToastUtil.showToast(mContext, PREFIX + "激励视频广告播放完成")
                }

                override fun onVideoSkipToEnd(playDuration: Long) {
//                    ToastUtil.showToast(mContext, PREFIX + "激励视频广告跳过播放完成")
                }

                override fun onVideoPlayStart() {
//                    ToastUtil.showToast(mContext, PREFIX + "激励视频广告播放开始")
                }

                /**
                 * 激励视频广告激励回调，只会回调一次
                 */
                override fun onRewardVerify() {
//                    ToastUtil.showToast(mContext, PREFIX + "激励视频广告获取激励")
                }

                /**
                 * 视频激励分阶段回调
                 * @param taskType 当前激励视频所属任务类型
                 * RewardTaskType.LOOK_VIDEO 观看视频类型             属于浅度奖励类型
                 * RewardTaskType.LOOK_LANDING_PAGE 浏览落地页N秒类型  属于深度奖励类型
                 * RewardTaskType.USE_APP 下载使用App N秒类型          属于深度奖励类型
                 * @param currentTaskStatus  当前所完成任务类型，@RewardTaskType中之一
                 */
                override fun onRewardStepVerify(taskType: Int, currentTaskStatus: Int) {
//                    ToastUtil.showToast(
//                        mContext, "激励视频广告分阶段获取激励，当前任务类型为：" + getTaskStatusStr(taskType) +
//                                "，当前完成任务类型为：" + getTaskStatusStr(currentTaskStatus)
//                    )
                }

                override fun onExtraRewardVerify(p0: Int) {

                }
            })
            mRewardVideoAd!!.showRewardVideoAd(activity, videoPlayConfig)
        } else {
//            ToastUtil.showToast(mContext, "暂无可用激励视频广告，请等待缓存加载或者重新刷新")
        }
    }

    /**
     * 加载自渲染View
     */
    private fun showAd(ksNativeAd: KsNativeAd) {
        container?.removeAllViews()
        val adView: View?
        when (ksNativeAd.materialType) {
            MaterialType.VIDEO ->         // 视频素材，渲染自定义的视频广告
                adView = getVideoItemView(container, ksNativeAd)
            MaterialType.SINGLE_IMG ->         // 单图素材，渲染自定义的单图广告
                adView = getSingleImageItemView(container, ksNativeAd)
            MaterialType.GROUP_IMG ->         // 组图素材，渲染自定义的组图广告
                adView = getGroupImageItemView(container, ksNativeAd)
            MaterialType.UNKNOWN -> adView = getNormalItemView(container)
            else -> adView = getNormalItemView(container)
        }
        if (adView != null && adView.parent == null) {
            container?.addView(adView)
        }
    }

    /**
     * 使用SDK渲染的播放控件
     */
    protected fun getVideoItemView(parent: ViewGroup?, ksNativeAd: KsNativeAd): View? {
        val convertView: View =
            LayoutInflater.from(activity).inflate(R.layout.native_item_video, parent, false)
        val videoViewHolder = AdVideoViewHolder(convertView)

        // 设置广告数据
        bindCommonData(convertView as ViewGroup, videoViewHolder, ksNativeAd)
        ksNativeAd.setVideoPlayListener(object : VideoPlayListener {
            override fun onVideoPlayReady() {

            }

            override fun onVideoPlayStart() {
                LogUtils.i(TAG,"onVideoPlayStart")
            }

            override fun onVideoPlayComplete() {
                LogUtils.i(TAG,"onVideoPlayComplete")
            }

            override fun onVideoPlayError(what: Int, extra: Int) {
                LogUtils.i(TAG,"onVideoPlayError")
            }

            override fun onVideoPlayPause() {

            }

            override fun onVideoPlayResume() {

            }
        })

        // SDK默认渲染的视频view
        val videoPlayConfig = KsAdVideoPlayConfig.Builder()
            .build()
        val videoView = ksNativeAd.getVideoView(activity, videoPlayConfig)
        if (videoView != null && videoView.parent == null) {
            videoViewHolder.mAdVideoContainer.removeAllViews()
            videoViewHolder.mAdVideoContainer.addView(videoView)
        }
        return convertView
    }

    /**
     * 使用媒体自己渲染的播放控件
     */
    protected fun getVideoItemView2(parent: ViewGroup?, ksNativeAd: KsNativeAd): View? {
        val convertView: View =
            LayoutInflater.from(activity).inflate(R.layout.native_item_video, parent, false)
        val viewHolder = AdVideoViewHolder(convertView)

        // 设置广告数据
        bindCommonData(convertView as ViewGroup, viewHolder, ksNativeAd)
        /** 媒体也可以自渲染视频view start  */
        // 获取视频地址
        val videoUrl = ksNativeAd.videoUrl
        // 获取视频时长
        val videoDuration = ksNativeAd.videoDuration
        // 获取视频封面图片
        val ksImage = ksNativeAd.videoCoverImage

        // 特别注意，视频播放需要客户自渲染，所以需要客户在合适时机掉如下方法进行打点，用于统计视频观看时长
        // 1.请在视频播放开始时调用此方法（每次从0秒开始播放，暂停恢复除外）
        ksNativeAd.reportAdVideoPlayStart()
        //    // 2.请在视频播放结束时调用此方法（每次播放到最后1s，暂停恢复除外）
        ksNativeAd.reportAdVideoPlayEnd()
        /** 自渲染视频view end  */
        return convertView
    }

    protected fun getSingleImageItemView(parent: ViewGroup?, ksNativeAd: KsNativeAd): View? {
        val convertView: View =
            LayoutInflater.from(activity).inflate(R.layout.native_item_single_image, parent, false)
        val viewHolder = AdSingleImageViewHolder(convertView)
        bindCommonData(convertView as ViewGroup, viewHolder, ksNativeAd)

        // 获取图片资源
        if (ksNativeAd.imageList != null && !ksNativeAd.imageList!!.isEmpty()) {
            val image = ksNativeAd.imageList!![0]
            if (image != null && image.isValid) {
                activity?.apply {
                    Glide.with(this).load(image.imageUrl).into(viewHolder.mAdImage)
                }
            }
        }
        return convertView
    }

    protected fun getGroupImageItemView(parent: ViewGroup?, ksNativeAd: KsNativeAd): View? {
        val convertView: View =
            LayoutInflater.from(activity).inflate(R.layout.native_item_group_image, parent, false)
        val viewHolder = AdGroupImageViewHolder(convertView)
        bindCommonData(convertView as ViewGroup, viewHolder, ksNativeAd)

        // 获取图片资源
        val ksImageList = ksNativeAd.imageList
        if (ksImageList != null && !ksImageList.isEmpty()) {
            for (i in ksImageList.indices) {
                val image = ksNativeAd.imageList!![i]
                if (image != null && image.isValid) {
                    activity?.apply {
                        when (i) {
                            0 -> {
                                Glide.with(this).load(image.imageUrl).into(viewHolder.mAdImageLeft)
                            }
                            1 -> {
                                Glide.with(this).load(image.imageUrl).into(viewHolder.mAdImageMid)
                            }
                            2 -> {
                                Glide.with(this).load(image.imageUrl).into(viewHolder.mAdImageRight)
                            }
                        }
                    }
                }
            }
        }
        return convertView
    }

    @SuppressLint("DefaultLocale")
    protected fun getNormalItemView(parent: ViewGroup?): View? {
        val convertView: View =
            LayoutInflater.from(activity).inflate(R.layout.native_item_normal, parent, false)
        val normalViewHolder = NormalViewHolder(convertView)
        normalViewHolder.textView.text = "没有广告"
        return convertView
    }

    private fun bindCommonData(
        convertView: ViewGroup, adBaseViewHolder: AdBaseViewHolder,
        ad: KsNativeAd
    ) {
        // 点击转换view的集合，传入的view点击时会触发转换操作：app下载， 打开h5页面
        val clickViewMap: MutableMap<View, Int> = HashMap()
        clickViewMap[adBaseViewHolder.mAdConvertBtn] = KsNativeConvertType.CONVERT
        clickViewMap[adBaseViewHolder.mAdIcon] = KsNativeConvertType.SHOW_DOWNLOAD_TIPS_DIALOG
        clickViewMap[adBaseViewHolder.mAdName] = KsNativeConvertType.SHOW_DOWNLOAD_TIPS_DIALOG
        clickViewMap[adBaseViewHolder.mAdDes] = KsNativeConvertType.SHOW_DOWNLOAD_TIPS_DIALOG
        clickViewMap[adBaseViewHolder.mAdDesc] = KsNativeConvertType.SHOW_DOWNLOAD_TIPS_DIALOG

        // 如果是自定义弹窗，请使用下面的配置
        // 注册View的点击，点击后触发转化
        ad.registerViewForInteraction(activity, convertView, clickViewMap,
            object : KsNativeAd.AdInteractionListener {
                override fun onAdClicked(view: View, ad: KsNativeAd) {
                    if (ad != null) {
//                        Toast.makeText(mContext, "广告" + ad.appName + "被点击", Toast.LENGTH_SHORT)
//                            .show()
                    }
                }

                override fun onAdShow(ad: KsNativeAd) {
                    if (ad != null) {
//                        Toast.makeText(mContext, "广告" + ad.appName + "展示", Toast.LENGTH_SHORT)
//                            .show()
                    }
                }

                /*
           *  @return  返回为true, 则只会给媒体弹出回调，SDK的默认弹窗逻辑不会执行
           * @return  返回为true 返回为 false, 则使用SDK默认的合规弹窗。
           * 弹出弹窗dialog后， 用户确认下载，则媒体需要回调 OnClickListener.onClick(dialog, DialogInterface
           * .BUTTON_POSITIVE)
           * 弹出弹窗dialog后， 用户点击取消，则媒体需要回调 OnClickListener.onClick(dialog, DialogInterface
           * .BUTTON_NEGATIVE)
           * */
                override fun handleDownloadDialog(clickListener: DialogInterface.OnClickListener): Boolean {
                    return false
                }

                override fun onDownloadTipsDialogShow() {
//                    Toast.makeText(mContext, "广告展示下载合规弹窗", Toast.LENGTH_SHORT).show()
                }

                override fun onDownloadTipsDialogDismiss() {
//                    Toast.makeText(mContext, "广告关闭下载合规弹窗", Toast.LENGTH_SHORT).show()
                }
            })

        // 其他数据
        Log.d("AppInfo", "应用名字 = " + ad.appName)
        Log.d("AppInfo", "应用包名 = " + ad.appPackageName)
        Log.d("AppInfo", "应用版本 = " + ad.appVersion)
        Log.d("AppInfo", "开发者 = " + ad.corporationName)
        Log.d("AppInfo", "包大小 = " + ad.appPackageSize)
        Log.d("AppInfo", "隐私条款链接 = " + ad.appPrivacyUrl)
        Log.d("AppInfo", "权限信息 = " + ad.permissionInfo)
        Log.d("AppInfo", "权限信息链接 = " + ad.permissionInfoUrl)
        // 获取app的评分，取值范围0~5.0
        Log.d("AppInfo", "应用评分 = " + ad.appScore)
        // 获取app下载次数文案，例如：800W此下载，自行渲染。
        Log.d("AppInfo", "app下载次数文案 = " + ad.appDownloadCountDes)

        // 广告描述
        adBaseViewHolder.mAdDes.text = ad.adDescription
        val adIconUrl = ad.appIconUrl
        // 广告icon
        if (!TextUtils.isEmpty(adIconUrl)) {
            activity?.apply {
                Glide.with(this).load(adIconUrl).into(adBaseViewHolder.mAdIcon)
            }
            adBaseViewHolder.mAdIcon.visibility = View.VISIBLE
        } else {
            adBaseViewHolder.mAdIcon.visibility = View.GONE
        }
        // 广告转化文案
        adBaseViewHolder.mAdConvertBtn.text = ad.actionDescription
        // 广告名称
        if (ad.interactionType == InteractionType.DOWNLOAD) {
            adBaseViewHolder.mAdName.text = ad.appName
            // 下载类型的可以设置下载监听
            bindDownloadListener(adBaseViewHolder, ad)
        } else {
            adBaseViewHolder.mAdName.text = ad.productName
        }
        // 广告描述
        adBaseViewHolder.mAdDesc.text = ad.adDescription

        // 不喜欢
        adBaseViewHolder.mDislikeBtn.setOnClickListener {
//            ToastUtil.showToast(
//                mContext,
//                "广告" + ad.appName + "不喜欢点击"
//            )
            convertView.removeView(it)
        }
        // 广告来源
        val adSource = ad.adSource
        val grayMode = true // 开发者可根据实际需要调整
        if (TextUtils.isEmpty(adSource)) {
            adBaseViewHolder.mAdSourceDesc.visibility = View.GONE
            adBaseViewHolder.mAdSourceDesc.text = ""
            adBaseViewHolder.mAdLogoIcon.visibility = View.GONE
        } else {
            activity?.apply {
                Glide.with(this)
                    .load(ad.getAdSourceLogoUrl(if (grayMode) AdSourceLogoType.GREY else AdSourceLogoType.NORMAL))
                    .into(adBaseViewHolder.mAdLogoIcon)
            }
            adBaseViewHolder.mAdSourceDesc.setTextColor(if (grayMode) -0x636364 else -0x66000001)
            adBaseViewHolder.mAdSourceDesc.text = adSource
        }
    }

    private fun bindDownloadListener(adBaseViewHolder: AdBaseViewHolder, ad: KsNativeAd) {
        val ksAppDownloadListener: KsAppDownloadListener = object : KsAppDownloadListener {
            override fun onIdle() {
                adBaseViewHolder.mAdConvertBtn.text = ad.actionDescription
            }

            override fun onDownloadStarted() {
                adBaseViewHolder.mAdConvertBtn.text = "开始下载"
            }

            override fun onProgressUpdate(progress: Int) {
                adBaseViewHolder.mAdConvertBtn.text = String.format("%s/100", progress)
            }

            override fun onDownloadFinished() {
                adBaseViewHolder.mAdConvertBtn.text = "立即安装"
            }

            override fun onDownloadFailed() {
                adBaseViewHolder.mAdConvertBtn.text = ad.actionDescription
            }

            override fun onInstalled() {
                adBaseViewHolder.mAdConvertBtn.text = "立即打开"
            }
        }
        // 注册下载监听器
        ad.setDownloadListener(ksAppDownloadListener)
    }

    private class NormalViewHolder internal constructor(convertView: View) {
        var textView: TextView

        init {
            textView = convertView.findViewById(R.id.tv)
        }
    }

    private class AdSingleImageViewHolder internal constructor(convertView: View) :
        AdBaseViewHolder(convertView) {
        var mAdImage: ImageView

        init {
            mAdImage = convertView.findViewById(R.id.ad_image)
        }
    }

    private class AdGroupImageViewHolder internal constructor(convertView: View) :
        AdBaseViewHolder(convertView) {
        var mAdImageLeft: ImageView
        var mAdImageMid: ImageView
        var mAdImageRight: ImageView

        init {
            mAdImageLeft = convertView.findViewById(R.id.ad_image_left)
            mAdImageMid = convertView.findViewById(R.id.ad_image_mid)
            mAdImageRight = convertView.findViewById(R.id.ad_image_right)
        }
    }

    private class AdVideoViewHolder constructor(convertView: View) :
        AdBaseViewHolder(convertView) {
        var mAdVideoContainer: FrameLayout

        init {
            mAdVideoContainer = convertView.findViewById(R.id.video_container)
        }
    }

    open class AdBaseViewHolder internal constructor(convertView: View) {
        var mAdDes: TextView
        var mAdIcon: ImageView
        var mAdName: TextView
        var mAdDesc: TextView
        var mAdConvertBtn: TextView
        var mDislikeBtn: ImageView
        var mAdLogoIcon: ImageView
        var mAdSourceDesc: TextView

        init {
            mAdDes = convertView.findViewById(R.id.ad_desc)
            mAdIcon = convertView.findViewById(R.id.app_icon)
            mAdName = convertView.findViewById(R.id.app_title)
            mAdDesc = convertView.findViewById(R.id.app_desc)
            mAdConvertBtn = convertView.findViewById(R.id.app_download_btn)
            mDislikeBtn = convertView.findViewById(R.id.ad_dislike)
            mAdLogoIcon = convertView.findViewById(R.id.ksad_logo_icon)
            mAdSourceDesc = convertView.findViewById(R.id.ksad_logo_text)
        }
    }

    private fun getAdItemView(parent: ViewGroup?, ksFeedAd: KsFeedAd): View? {
        val convertView1 = LayoutInflater.from(activity).inflate(
                R.layout.feed_list_item_ad_container,
                parent, false
            )
        // 设置监听
        // ksFeedAd.setVideoSoundEnable(false);//视频播放是否，默认静音播放
        ksFeedAd.setAdInteractionListener(object : KsFeedAd.AdInteractionListener {
            override fun onAdClicked() {
                LogUtils.e("onAdClicked   广告点击回调")
            }

            override fun onAdShow() {
                LogUtils.e("onAdShow   广告曝光回调")
            }

            override fun onDislikeClicked() {
                LogUtils.e( "onDislikeClicked   广告不喜欢回调")
//                mFeedList.remove(ksFeedAd)
//                notifyDataSetChanged()
            }

            override fun onDownloadTipsDialogShow() {
                LogUtils.e("onDownloadTipsDialogShow   广告展示下载合规弹窗")
            }

            override fun onDownloadTipsDialogDismiss() {
                LogUtils.e( "onDownloadTipsDialogDismiss   广告关闭下载合规弹窗")
            }
        })
//        val videoView = ksFeedAd.getFeedView(activity)
//        if (videoView != null && videoView.parent == null) {
//            adViewHolder.mAdContainer.removeAllViews()
//            adViewHolder.mAdContainer.addView(videoView)
//        }
        return convertView1
    }
}