package com.feisukj.ad.manager


import android.app.Activity
import android.text.TextUtils
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.feisukj.ad.SplashActivity
import com.feisukj.ad.SplashActivityAD
import com.feisukj.base.BaseConstant
import com.feisukj.base.util.SPUtil


/**
 * Created by Gpp on 2018/1/20.
 */

class AdController(var activity: Activity, var page: String) {
    companion object{
        const val SEE_VIDEO_AD_COUNT_KEY="see_video_count_key"
        const val SEE_VIDEO_AD_TIME_KEY="see_video_time_key"
        fun getNoAdTime(seeCount:Int):Long{
            return when(seeCount){
                0->{
                    0L
                }
                1->{
                    60*60*1000L
                }
                2->{
                    60*60*1000L*3L
                }
                3->{
                    60*60*1000L*24L
                }
                else->{
                    60*60*1000L*24L*(seeCount-2)
                }
            }
        }
        fun getNoAdTimeTip(seeCount: Int):String{
            return when(seeCount){
                0->{
                    "未免广告"
                }
                1->{
                    "免广告1小时"
                }
                2->{
                    "免广告3小时"
                }
                3->{
                    "免广告1天"
                }
                else->{
                    "免广告${seeCount-2}天"
                }
            }
        }

        private val adControllerMap=HashMap<String,Array<View?>>()
        fun hideAd(){
            adControllerMap.entries.forEach { entry ->
                entry.value.forEach {
                    it?.visibility=View.GONE
                }
            }
            adControllerMap.clear()
        }
    }

    private var manager: AdManager? = null
    private var fullVideoManager: AdManager? = null
    var container: FrameLayout? = null
    private var isLoading: Boolean = false
    var tag_ad = ""
    var bannerContainer: FrameLayout? = null

    fun show() {
        val count= SPUtil.getInstance().getInt(SEE_VIDEO_AD_COUNT_KEY,0)
        val timeInterval=System.currentTimeMillis()-SPUtil.getInstance().getLong(SEE_VIDEO_AD_TIME_KEY,0L)
        val noAdTime= getNoAdTime(count)
        if (timeInterval<noAdTime){
            val act=activity
            if (act is SplashActivity){
                BaseConstant.mainHandler.postDelayed({
                    act.checkIn()
                },1000)
            }else if (act is SplashActivityAD){
                BaseConstant.mainHandler.postDelayed({
                    act.finish()
                },500)
            }
            return
        }else{
            SPUtil.getInstance().putInt(SEE_VIDEO_AD_COUNT_KEY,0)
            SPUtil.getInstance().putLong(SEE_VIDEO_AD_TIME_KEY,0L)
        }
        adControllerMap.put(page, arrayOf(container,bannerContainer))
        manager = AdManager(activity, page, container, isLoading, tag_ad, bannerContainer)
        manager!!.show()
    }

//    fun showFullVideo(jiliCallback: JILICallback){
//        val count= SPUtil.getInstance().getInt(SEE_VIDEO_AD_COUNT_KEY,0)
//        val timeInterval=System.currentTimeMillis()-SPUtil.getInstance().getLong(SEE_VIDEO_AD_TIME_KEY,0L)
//        val noAdTime= getNoAdTime(count)
////        if (timeInterval<noAdTime){
////            val act=activity
////            if (act is SplashActivity){
////                BaseConstant.mainHandler.postDelayed({
////                    act.checkIn()
////                },1000)
////            }else if (act is SplashActivityAD){
////                BaseConstant.mainHandler.postDelayed({
////                    act.finish()
////                },500)
////            }
////            return
////        }else{
////            SPUtil.getInstance().putInt(SEE_VIDEO_AD_COUNT_KEY,0)
////            SPUtil.getInstance().putLong(SEE_VIDEO_AD_TIME_KEY,0L)
////        }
//        fullVideoManager = AdManager(activity, page, container, isLoading, tag_ad, bannerContainer)
//        fullVideoManager!!.showFullVideo(jiliCallback)
//    }

    fun destroy() {
        if (null != manager) {
            manager!!.destroy()
            manager = null
            System.gc()
        }
    }

    fun setLoading(isLoading: Boolean) {
        this.isLoading = isLoading
    }


    class Builder(private val activity: Activity,private val page: String) {
        private var container: FrameLayout? = null
        private var isLoading: Boolean = false
        private var bannerContainer: FrameLayout? = null

        fun setContainer(container: FrameLayout): Builder {
            this.container = container
            return this

        }

        fun setLoading(isLoading: Boolean): Builder {
            this.isLoading = isLoading
            return this
        }

        fun setBannerContainer(bannerContainer: FrameLayout): Builder {
            this.bannerContainer = bannerContainer
            return this
        }

        fun create(): AdController {

            val controller = AdController(activity, page)

            controller.setLoading(isLoading)

            if (container != null) {
                controller.container=container
            }

            if (bannerContainer != null) {
                controller.bannerContainer=bannerContainer
            }

            if (TextUtils.isEmpty(page)) {
                throw NullPointerException("page can not null")
            }
            return controller
        }
    }

    interface JILICallback{
        fun close()
    }
}
