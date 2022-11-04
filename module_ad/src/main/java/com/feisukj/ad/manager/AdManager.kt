package com.feisukj.ad.manager

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import android.view.View
import android.widget.FrameLayout
import android.widget.ImageView
import com.feisukj.ad.SplashActivity
import com.feisukj.ad.adapter.BaseAdAdapter
import com.feisukj.base.BaseConstant
import com.feisukj.base.bean.ad.*
import com.feisukj.base.util.GsonUtils
import com.feisukj.base.util.LogUtils
import com.feisukj.base.util.NetworkUtils
import com.feisukj.base.util.SPUtil

import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.HashMap
import java.util.Locale
import java.util.Random
import java.util.TimerTask
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors
import java.util.concurrent.ScheduledExecutorService
import java.util.concurrent.ScheduledThreadPoolExecutor
import java.util.concurrent.TimeUnit


/**
 * Created by Gpp on 2018/1/22.
 */

class AdManager(
    private val activity: Activity,
    private val page: String,
    private var container: FrameLayout?,
    private val isLoading: Boolean,
    private val tag_ad: String,
    private val bannerContainer: FrameLayout?,
    private val baseAdAdapter: BaseAdAdapter?
) : OnInsertADListener {
    //banner切换scheduledExecutorService timerTask
    private var scheduledExecutorService: ScheduledExecutorService? = null
    private var timerTask: TimerTask? = null
    private var pool: ExecutorService? = null
    private var pageBean: TypeBean? = null
    private var adView: AbsADParent? = null
    private var adFullView: AbsADParent? = null
    private var pages: Long = 0
    private var ori : AD.AdOrigin?  = null
    private var isRunning = true
    private var map: HashMap<AD.AdType, List<OriginBean>>? = null

    //获取当前页面需要显示的广告类型，和广告源，添加到map
    private//1.先获取页面的广告类型
    //原生
    //播放页保存广告切换周期
    val showTypeAndOrigin: HashMap<AD.AdType, List<OriginBean>>
        get() {

            val map = HashMap<AD.AdType, List<OriginBean>>()
            var spreadScreenBean: StatusBean? = null
            var insertScreenBean: StatusBean? = null
            var nativeScreenBean: StatusBean? = null

            if (pageBean != null) {
                spreadScreenBean = pageBean?.spread_screen
                insertScreenBean = pageBean?.insert_screen
                nativeScreenBean = pageBean?.native_screen
            }

            if (spreadScreenBean != null) {
                map[AD.AdType.SPLASH] = getOriginBean(spreadScreenBean)
                SPUtil.getInstance().putBoolean(
                    ADConstants.AD_SPLASH_STATUS,
                    spreadScreenBean.status
                )
                SPUtil.getInstance().putLong(ADConstants.AD_SPREAD_PERIOD, spreadScreenBean.times)
            }

//            if (bannerScreenBean != null) {
//                map[AD.AdType.BANNER] = getOriginBean(bannerScreenBean)
//            }

            if (insertScreenBean != null) {
                map[AD.AdType.INSET] = getOriginBean(insertScreenBean)
//                if (page != ADConstants.LISTENING_PAGE) {
                    Log.i(
                        TAG,
                        "页面:" + page + ",插屏广告周期:" + insertScreenBean.times * 1000
                    )
                    SPUtil.getInstance().putLong(
                        page + ADConstants.AD_INSERT_SHOW_PERIOD,
                        insertScreenBean.times * 1000
                    )
//                }
            }
            if (nativeScreenBean != null) {
                map[AD.AdType.NATIVE] = getOriginBean(nativeScreenBean)
            }
            return map
        }

    init {
        pool = Executors.newFixedThreadPool(1)
    }

    fun show() {
        pool!!.execute {
            val string = SPUtil.getInstance().getString(page!!)
            LogUtils.e("pool run page==$page,string==$string")
            if (!TextUtils.isEmpty(string))
                pageBean = GsonUtils.parseObject(string, TypeBean::class.java)
            LogUtils.e("pool run pageBean==" + SPUtil.getInstance().getString(page))
            showByType()
        }
    }
    fun getList(){
        pool!!.execute{
            val string = SPUtil.getInstance().getString(page)
            LogUtils.e("pool run page==$page,string==$string")
            if (!TextUtils.isEmpty(string))
                pageBean = GsonUtils.parseObject(string,TypeBean::class.java)
            LogUtils.e("pool run pageBean==" + SPUtil.getInstance().getString(page))
            showByType()
        }
    }
    private fun showByType() {
        //获取当前页面要展示的广告类型和源
        map = showTypeAndOrigin
        if (map!!.size == 0 && page == ADConstants.START_PAGE) {
            Log.e(TAG, "START_PAGE无配置数据")
            goHome()
            return
        }

        for (type in map!!.keys) {
            val originList = map!![type]
            changeAdByTime(type, originList)
        }
    }

    private fun changeAdByTime(type: AD.AdType, originList: List<OriginBean>?) {
        when (type) {
//            AD.AdType.BANNER -> executeBanner(type, originList)
            AD.AdType.INSET -> executeInsert(type, originList)
            AD.AdType.SPLASH -> {
                if (pageBean!!.spread_screen == null) return
                if (!pageBean!!.spread_screen!!.status || !NetworkUtils.isConnected(activity.applicationContext)) {
                    Log.e(TAG, "SPLASH-----开关关闭,或者无网络不执行")
                    goHome()
                    return
                }
                Log.e(TAG, "SPLASH--")
                showByOrigin(getAdOriginByPercent(originList!!), type)
            }
            AD.AdType.NATIVE -> executeNative(type, originList)
        }
    }

    private fun executeInsert(type: AD.AdType, originList: List<OriginBean>?) {
        if (pageBean!!.insert_screen == null) return

        if (!pageBean!!.insert_screen!!.status || !NetworkUtils.isConnected(activity.applicationContext)) {
            Log.e(TAG, "INSET--$page---开关关闭，或者无网络不执行")
            return
        }

        val period = SPUtil.getInstance().getLong(page!! + ADConstants.AD_INSERT_SHOW_PERIOD)
        val time = System.currentTimeMillis()
        val last = SPUtil.getInstance().getLong(page + ADConstants.AD_INSERT_LAST_SHOW)
        val offset = time - last
        Log.i(
            TAG,
            "页面:" + page + ",当前时间(" + getDate(time) + ")-上次展示时间(" + getDate(last) + "):" + time + "-" + last + "=" + offset
        )
        Log.i(
            TAG,
            "页面:" + page + ",读取插屏广告周期:" + period + "-" + offset + "=" + (period - offset)
        )
        Log.i(TAG, "页面:" + page + ",需要显示吗:" + (time - last >= period))
        BaseConstant.mainHandler.post(Runnable {
            showByOrigin(
                getAdOriginByPercent(originList!!),
                type
            )
        })
//        if (last == 0L) {
//            //第一次进入Application 记录此次显示时间
//            SPUtil.getInstance().putLong(
//                page + ADConstants.AD_INSERT_LAST_SHOW,
//                System.currentTimeMillis()
//            )
//        }
//        if (last > 0 && time - last >= period) {
//            Log.e(TAG, "INSET---时间到显示")
//            Log.e(TAG, "AdManager:tag_ad:$tag_ad")
//            BaseConstant.mainHandler.post(Runnable {
//                showByOrigin(
//                    getAdOriginByPercent(originList!!),
//                    type
//                )
//            })
//        } else {
//            Log.e(TAG, "INSET---时间未到不显示")
//        }
    }

    private fun getDate(time: Long): String {
        return SimpleDateFormat("HH:mm:ss", Locale.CHINA).format(time)
    }

    private fun goHome() {
        if (isLoading) {
            Log.i(TAG, "From BackGround")
            activity.finish()
        } else {
            Log.i(TAG, "From Start")
            BaseConstant.mainHandler.postDelayed(
                { (activity as SplashActivity).checkIn() },
                2000
            )
        }
    }

    private fun executeBanner(type: AD.AdType, originList: List<OriginBean>?) {
        //开关关闭不执行
        if (!pageBean!!.banner_screen!!.status || !NetworkUtils.isConnected(activity.applicationContext)) {
            Log.e(TAG, "BANNER-----开关关闭，或者无网络不执行")
            return
        }
        //        boolean isTimer = SPUtil.getInstance().getBoolean(page + ADConstants.AD_BANNER_IS_TIMER);
        //        if (isTimer) {
        ////            此页面已经开启了定时器
        //            Log.e(TAG, "BANNER-----isTimer:" + page + isTimer);
        //            return;
        //        }
        scheduledExecutorService = ScheduledThreadPoolExecutor(1)
        //        开启定时切换
        timerTask = object : TimerTask() {
            override fun run() {
                BaseConstant.mainHandler.postDelayed(Runnable {
                    //                            cancelTask();取消定时器方法
                    if (isRunning) {
                        Log.e(
                            TAG,
                            "BANNER-----定时切换广告源" + page + ADConstants.AD_BANNER_LAST_CHANGE
                        )
                        Log.e(TAG, "AdManager:tag_ad:$tag_ad")
                        SPUtil.getInstance().putLong(
                            page!! + ADConstants.AD_BANNER_LAST_CHANGE,
                            System.currentTimeMillis()
                        )
                        showByOrigin(getAdOriginByPercent(originList!!), type)
                    } else {
                        timerTask!!.cancel()
                    }
                }, 0)
            }
        }
        try {
            scheduledExecutorService!!.scheduleWithFixedDelay(
                timerTask!!,
                0,
                pageBean!!.banner_screen!!.change_times * 1000,
                TimeUnit.MILLISECONDS
            )
            SPUtil.getInstance().putBoolean(page!! + ADConstants.AD_BANNER_IS_TIMER, true)
        } catch (e: IllegalStateException) {
            e.printStackTrace()
            SPUtil.getInstance().putBoolean(page!! + ADConstants.AD_BANNER_IS_TIMER, false)
        }

        //如果后台配置了关闭广告时间，执行关闭
        closeBannerByTiming()
    }

    private fun executeNative(type: AD.AdType, originList: List<OriginBean>?) {
        if ( !NetworkUtils.isConnected(activity.applicationContext)) {
            Log.e(TAG, "Native--$page---开关关闭，或者无网络不执行")
            return
        }
        val period = SPUtil.getInstance().getLong(page!! + ADConstants.AD_NATIVE_SHOW_PERIOD)
        val time = System.currentTimeMillis()
        val last = SPUtil.getInstance().getLong(page + ADConstants.AD_NATIVE_LAST_SHOW)
        val offset = time - last
        Log.i(
            TAG,
            "页面:" + page + ",当前时间(" + getDate(time) + ")-上次展示时间(" + getDate(last) + "):" + time + "-" + last + "=" + offset
        )
        Log.i(
            TAG,
            "页面:" + page + ",读取广告周期:" + period + "-" + offset + "=" + (period - offset)
        )
        Log.i(TAG, "页面:" + page + ",需要显示吗:" + (time - last >= period))

        if (time - last >= period) {
            Log.e(TAG, "INSET---时间到显示")
            showByOrigin(getAdOriginByPercent(originList!!), type)
        } else {
            Log.e(TAG, "INSET---时间未到不显示")
        }
    }

    /**
     * 关闭广告时间到了，执行关闭
     */
    private fun closeBannerByTiming() {

        if (pageBean!!.banner_screen!!.times != 0L) {

            val closeTime = pageBean!!.banner_screen!!.times

            Log.i(
                TAG,
                closeTime.toString() + " 秒后关闭Banner广告," + pageBean!!.banner_screen!!.change_times + "秒切换广告源" + "---" + page + ""
            )

            BaseConstant.mainHandler.postDelayed(Runnable {
                isRunning = false

                cancelTask()
            }, closeTime * 1000)
        }
    }

    /**
     * Random 选取广告源
     * 目前只做了2种广告源随机
     */
    private fun getAdOriginByPercent(originList: List<OriginBean>): AD.AdOrigin {

        if (originList.size == 1) {

            return originList[0].origin

        }

        val first = originList[0]

        val sec = originList[1]

        val random = Random()

        val next = random.nextInt(101)


        return if (first.precent < sec.precent) {

            if (next < first.precent) first.origin else sec.origin

        } else {

            if (next <= sec.precent) sec.origin else first.origin
        }
        //        return sec.getOrigin();
    }

    //根据广告广告源显示相应的广告类型
    private fun showByOrigin(origin: AD.AdOrigin, type: AD.AdType) {
        when (origin) {
            AD.AdOrigin.kwai -> {
                adView = KS_AD()
                Log.e(TAG, "AdManager showByOrigin   快手- gdt - $page ---$type")
            }
//            AD.AdOrigin.bd -> {
//                Log.i(TAG, "AdManager showByOrigin   百度-- $page ---$type")
//                adView = GDT_AD()
//            }
            AD.AdOrigin.toutiao -> {
                Log.e(TAG, "AdManager showByOrigin   头条-- $page ---$type")
                adView = TT_AD()
            }
        }
        adView?.activity = activity
        adView?.container = container
        adView?.bannerContainer = bannerContainer
        adView?.baseAdAdapter = baseAdAdapter
        adView?.page = page
        adView?.loading = isLoading
        //有网进入阅读页，后关闭网络，banner会显示无网络图片
        if (!NetworkUtils.isConnected(activity.applicationContext)) {
            if (null != container) {
                try {
                    container!!.visibility = View.GONE
                    Log.i(TAG, "关闭网络，不显示banner容器")
                }catch (e : Exception){
                    e.printStackTrace()
                }
            }
            return
        }

//        if (!isRunning && AD.AdType.BANNER == type) {
//
//            Log.i(TAG, "已经到时间关闭了，不能展示广告~~")
//
//            return
//        }

        adView!!.showAdView(type)
    }

    //获取广告源对应的percent
    private fun getOriginBean(statusBean: StatusBean): List<OriginBean> {
        val list = ArrayList<OriginBean>()

        var origin = statusBean.ad_origin

        var percent = statusBean.ad_percent.replace(" ", "")

        val replace = origin.replace("_", "")

        val times = origin.length - replace.length

        for (i in 0..times) {

            val originBean = OriginBean()

            val lastIndex = origin.lastIndexOf("_")

            val sub = origin.substring(lastIndex + 1, origin.length)

            originBean.origin = AD.AdOrigin.valueOf(sub)

            if (lastIndex > 0) {
                origin = origin.substring(0, lastIndex)
            }
            val lastIndexPercent = percent.lastIndexOf("_")

            val subPercent = percent.substring(lastIndexPercent + 1, percent.length)

            originBean.precent = Integer.valueOf(subPercent)
            if (lastIndexPercent > 0) {
                percent = percent.substring(0, lastIndexPercent)
            }

            list.add(originBean)

        }
        return list
    }

    fun destroy() {

        cancelTask()

        if (adView != null) {
            adView!!.destroy(AD.AdType.INSET)
        }

        if (pool != null) {
            pool!!.shutdown()
            pool = null
        }

    }

    private fun cancelTask() {

        isRunning = false

        if (scheduledExecutorService != null) {
            if (!scheduledExecutorService!!.isShutdown || !scheduledExecutorService!!.isTerminated) {
                scheduledExecutorService!!.shutdownNow()
                scheduledExecutorService = null
            }
        }
        SPUtil.getInstance().putBoolean(page!! + ADConstants.AD_BANNER_IS_TIMER, false)//记录定时器已消失
        Log.e(TAG, "BANNER-----记录定时器已消失:" + page + ADConstants.AD_BANNER_IS_TIMER)
//        if (adView != null) {
//
//            adView!!.destroy(AD.AdType.BANNER)
//        }

        if (container != null && container!!.isShown) {

            container!!.removeAllViews()

            container!!.visibility = View.GONE

            container = null

            Log.i(TAG, "$page 关闭Banner广告")

        }
    }

    override fun clickNextPage(showNative: Boolean) {

        ++pages

        Log.i(TAG, "click Next Page ==$pages")

        if (null != pageBean && null != pageBean!!.insert_screen) {

            if (pageBean!!.insert_screen!!.times == pages && pageBean!!.insert_screen!!.status) {

                Log.i(TAG, "Pages ==" + pageBean!!.insert_screen!!.times)

                showByOrigin(getAdOriginByPercent(map!![AD.AdType.INSET]!!), AD.AdType.INSET)

            }
        }
    }

    companion object {
        val TAG = "controller"
    }

}