package com.weilai.jigsawpuzzle.activity.special

import android.content.Intent
import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.feisukj.base.bean.ad.ADConstants
import com.feisukj.base.bean.ad.AdsConfig
import com.google.gson.Gson
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.kwad.sdk.api.KsAdSDK
import com.kwad.sdk.api.KsInitCallback
import com.kwad.sdk.api.SdkConfig
//import com.qq.e.comm.managers.GDTAdSdk
import com.umeng.analytics.MobclickAgent
import com.umeng.commonsdk.UMConfigure
import com.weilai.jigsawpuzzle.BaseConstant
import com.weilai.jigsawpuzzle.BuildConfig
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.activity.main.MainBaseActivity
import com.weilai.jigsawpuzzle.application.PuzzleApplication
import com.weilai.jigsawpuzzle.configure.Config
import com.weilai.jigsawpuzzle.dialog.special.DisplayLaunchTipDialog
import com.weilai.jigsawpuzzle.util.*
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

    private var canJump = false
    private var channel: String? = null

    lateinit var compositeDisposable: CompositeDisposable

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        initTopTheme()
        compositeDisposable = CompositeDisposable()
        PuzzleApplication.isFromStart = true
        channel = PackageUtils.getAppMetaData(this, "CHANNEL")
        if (!BaseConstant.isAgreement()){
            val dialog = DisplayLaunchTipDialog()
            supportFragmentManager.beginTransaction().add(dialog,null).commitAllowingStateLoss()
            dialog.setDisplayLaunchTipListener(object :
                    DisplayLaunchTipDialog.DisplayLaunchTipListener {
                override fun yes() {
                    SPUtil.getInstance().putBoolean(AgreementActivity.key, true)  //是否展示用户服务协议和隐私政策弹窗
                            .putLong("time", System.currentTimeMillis())
                    //友盟初始化配置
                    UMConfigure.init(Config.getApplicationContext(), "6347dfe288ccdf4b7e4876e9",
                        BaseConstant.channel, UMConfigure.DEVICE_TYPE_PHONE, null)
                    /**
                     *设置组件化的Log开关
                     *参数: boolean 默认为false，如需查看LOG设置为true
                     */
                    UMConfigure.setLogEnabled(BuildConfig.DEBUG)
                    MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
                    dialog.dismiss()
                    checkIn()
//                    askPermissions()
                }
                override fun no() {
                    dialog.dismiss()
                    finish()
                }
            })
        }else{
            //友盟初始化配置
                UMConfigure.init(Config.getApplicationContext(), "6347dfe288ccdf4b7e4876e9",
                    BaseConstant.channel, UMConfigure.DEVICE_TYPE_PHONE, null)
                /**
                 *设置组件化的Log开关
                 *参数: boolean 默认为false，如需查看LOG设置为true
                 */
                UMConfigure.setLogEnabled(BuildConfig.DEBUG)
                MobclickAgent.setPageCollectionMode(MobclickAgent.PageMode.AUTO)
//            GDTAdSdk.init(BaseApplication.application,ADConstants.kGDTMobSDKAppKey)
//            askPermissions()
            PuzzleApplication.handler.postDelayed({  checkIn() },2000)

        }
    }

    private fun initTopTheme() {
        ImmersionBar.with(this)
            .statusBarColor(android.R.color.transparent)
            .statusBarDarkFont(true)
            .hideBar(BarHide.FLAG_HIDE_BAR)
            .navigationBarColor(R.color.white)
            .init()
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
        if (BaseConstant.isAgreement()) {
            startActivity(Intent(this, MainBaseActivity::class.java))
            PuzzleApplication.isFromStart = false
            finish()
        }else{
            startActivity(Intent(this, AgreementActivity::class.java))
            finish()
        }
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
        compositeDisposable.dispose()
    }
}
