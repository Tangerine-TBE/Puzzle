package com.feisukj.ad

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.feisukj.ad.manager.AdController
import com.feisukj.ad.manager.TTAdManagerHolder
import com.feisukj.ad.manager.UserDataObtainController
import com.feisukj.base.*
import com.feisukj.base.api.AdService
import com.feisukj.base.bean.ad.ADConstants
import com.feisukj.base.bean.ad.AdsConfig
import com.feisukj.base.retrofitnet.HttpUtils
import com.feisukj.base.util.LogUtils
import com.feisukj.base.util.PackageUtils
import com.feisukj.base.util.SPUtil
import com.google.gson.Gson
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.kwad.sdk.api.KsAdSDK
import com.kwad.sdk.api.KsInitCallback
import com.kwad.sdk.api.SdkConfig
import com.qq.e.comm.managers.GDTAdSdk
import com.umeng.commonsdk.UMConfigure
import io.reactivex.Observer
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_splash.*


/**
 * Author : Gupingping
 * Date : 2018/10/25
 * QQ : 464955343
 */
class SplashActivity : AppCompatActivity() {

    private var builder: AdController? = null
    private var canJump = false
    private var channel: String? = null

    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initTopTheme()
        compositeDisposable = CompositeDisposable()
        BaseApplication.isFromStart = true
        channel = PackageUtils.getAppMetaData(this, "CHANNEL")
        if (!BaseConstant.isAgreement()) {
            var dialog = DisplayLaunchTipDialog()
            supportFragmentManager.beginTransaction().add(dialog, null).commitAllowingStateLoss()
            dialog.setDisplayLaunchTipListener(object : DisplayLaunchTipDialog.DisplayLaunchTipListener {
                override fun yes() {
                    SPUtil.getInstance().putBoolean(AgreementActivity.key, true)  //是否展示用户服务协议和隐私政策弹窗
                            .putLong("time", System.currentTimeMillis())
                    if (!BuildConfig.DEBUG) {
                        UMConfigure.init(BaseApplication.application, "6347dfe288ccdf4b7e4876e9", BaseConstant.channel, UMConfigure.DEVICE_TYPE_PHONE, null)
                    }
                    dialog.dismiss()
                    getAdConfig()
                }

                override fun no() {
                    dialog.dismiss()
                    finish()
                }
            })
        } else {
            getAdConfig()
            if (!BuildConfig.DEBUG) {
                UMConfigure.init(BaseApplication.application, "6347dfe288ccdf4b7e4876e9", BaseConstant.channel, UMConfigure.DEVICE_TYPE_PHONE, null)
            }
        }
    }
    private fun initTopTheme() {
        ImmersionBar.with(this)
                .statusBarColor(android.R.color.transparent)
                .statusBarDarkFont(true)
                .hideBar(BarHide.FLAG_HIDE_BAR)
                .navigationBarColor(com.feisukj.base.R.color.white)
                .init()
    }
    private fun getAdConfig() {
        HttpUtils.setServiceForFeisuConfig(AdService::class.java).getADConfig(channel = channel!!, version = BaseConstant.versionName).subscribeOn(Schedulers.io()).observeOn(AndroidSchedulers.mainThread()).subscribe(object : Observer<AdsConfig> {
                    override fun onComplete() {

                    }

                    override fun onSubscribe(d: Disposable) {
                        compositeDisposable.addAll(d)
                    }

                    override fun onNext(t: AdsConfig) {
                        val gson = Gson()
                        SPUtil.getInstance().putString(ADConstants.START_PAGE, gson.toJson(t.data?.start_page))
                        SPUtil.getInstance().putString(ADConstants.HOME_PAGE, gson.toJson(t.data?.home_page))
                        SPUtil.getInstance().putString(ADConstants.EXIT_PAGE, gson.toJson(t.data?.exit_page))
                        SPUtil.getInstance().putString(ADConstants.TEMPLATELIST_PAGE, gson.toJson(t.data?.templatelist_page))
                        SPUtil.getInstance().putString(ADConstants.SAVESUCCESS_PAGE, gson.toJson(t.data?.savesuccess_page))
                        SPUtil.getInstance().putString(ADConstants.IMGDETAIL_PAGE, gson.toJson(t.data?.imgdetail_page))

                        t.data?.Advertisement?.also {
                            SPUtil.getInstance().putString("kTouTiaoAppKey", it.kTouTiaoAppKey)
                                    .putString("kTouTiaoKaiPing", it.kTouTiaoKaiPing)
                                    .putString("kTouTiaoBannerKey", it.kTouTiaoBannerKey)
                                    .putString("kTouTiaoChaPingKey", it.kTouTiaoChaPingKey)
                                    .putString("kTouTiaoSeniorKey", it.kTouTiaoSeniorKey)
                                    .putString("kTouTiaoJiLiKey", it.kTouTiaoJiLiKey)
                                    .putString("ktouTiaoFullscreenvideoKey", it.kTouTiaoSmallSeniorKey)
                                    .putString("kWaiAppKey", it.kWaiAppKey).
                                    putString("kWaiKaiPing", it.kWaiKaiPing)
                                    .putString("kWaiBannerKey", it.kWaiBannerKey)
                                    .putString("kWaiChaPingKey", it.kWaiChaPingKey)
                                    .putString("kWaiJiLiKey", it.kWaiJiLiKey)
                                    .putString("kWaiSeniorKey", it.kWaiSeniorKey)
                            TTAdManagerHolder.init(BaseApplication.application)
                            KsAdSDK.init(BaseApplication.application, SdkConfig.Builder()
                                    .appId(it.kWaiAppKey)
                                    .showNotification(true)
                                    .customController(
                                            UserDataObtainController.getInstance()
                                                    .setUserAgree(true))
                                    .appName("拼图P图")
                                    .debug(BuildConfig.DEBUG)
                                    .setInitCallback(object : KsInitCallback {
                                        override fun onSuccess() {
                                            LogUtils.e("快手广告：","init success")
                                        }

                                        override fun onFail(code: Int, msg: String) {
                                            LogUtils.e("快手广告：","init fail code:${code}--msg:${msg}")
                                        }
                                    })
                                    .build())
                        }
                        //两个广告一起初始化
                        askPermissions()

                    }

                    override fun onError(e: Throwable) {

                    }
                })
    }

    private fun askPermissions() {
        builder = AdController.Builder(this, ADConstants.START_PAGE).setContainer(splash_container).create()
        builder?.show()
    }

    override fun onPause() {
        super.onPause()
        canJump = false
    }


    override fun onResume() {
        super.onResume()
        if (canJump) {
            next()
        }
        canJump = true
    }

    fun checkIn() {
//        if (BaseConstant.isAgreement()) {
        startActivity(Intent(this, ActivityEntrance.HomeActivity.cls))
        BaseApplication.isFromStart = false
        finish()
//        }else{
//            startActivity(Intent(this, AgreementActivity::class.java))
//            finish()
//        }
    }

    /**
     * 设置一个变量来控制当前开屏页面是否可以跳转，当开屏广告为普链类广告时，点击会打开一个广告落地页，此时开发者还不能打开自己的App主页。当从广告落地页返回以后，
     * 才可以跳转到开发者自己的App主页；当开屏广告是App类广告时只会下载App。
     */
    private operator fun next() {
        if (canJump) {
            checkIn()
        } else {
            canJump = true
        }
    }

    /** 开屏页一定要禁止用户对返回按钮的控制，否则将可能导致用户手动退出了App而广告无法正常曝光和计费  */

    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        builder?.destroy()
        compositeDisposable.dispose()
    }
}
