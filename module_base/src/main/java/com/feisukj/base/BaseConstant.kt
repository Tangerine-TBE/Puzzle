package com.feisukj.base

import android.app.Application
import android.content.pm.PackageManager
import android.os.Environment
import android.os.Handler
import android.os.Looper
import com.feisukj.base.util.PackageUtils
import com.feisukj.base.util.SPUtil

object BaseConstant {
    val mainHandler:Handler by lazy { Handler(Looper.getMainLooper()) }
    val application: Application by lazy { BaseApplication.application }
    val packageName:String by lazy { application.packageName }
    var isForeground: Boolean = false
    var isFromStart: Boolean = false
    val channel:String by lazy { PackageUtils.getAppMetaData(application, "CHANNEL") }
    val versionName by lazy { application.packageManager.getPackageInfo(application.packageName,
        PackageManager.GET_ACTIVITIES).versionName }
    var adSwitch=!BuildConfig.DEBUG
    fun isAgreement():Boolean{
        return SPUtil.getInstance().getBoolean(AgreementActivity.key)
    }

    val savePath = BaseApplication.application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath + "/CutOut"
//    val cachePath = BaseApplication.application.getExternalFilesDir(Environment.DIRECTORY_PICTURES)!!.absolutePath + "/cache"
    val cachePath = BaseApplication.application.cacheDir.absolutePath+ "/cache"

    //友盟统计事件
    const val apkClick_UM="setting_apk_click"
    const val miniProgressClick_UM="setting_mini_progress_click"
    const val videoClick_UM="setting_see_video_click"

    //广点通开屏
    const val SPLASH_REQUEST_GDT="spreadRequest_gdt"//请求
    const val SPLASH_REQUEST_SUCCESS_GDT="spreadRequestSuccess_gdt"//请求成功
    const val SPLASH_ERROR_GDT="spreadRequestError_gdt"//广告加载失败
    const val SPLASH_SHOW_GDT="spreadShow_gdt"//广告展示成功
    //广点通原生
    const val NATIVE_REQUEST_GDT="nativeRequest_gdt"//请求
    const val NATIVE_REQUEST_SUCCESS_GDT="nativeRequestSuccess_gdt"//广告数据加载成功
    const val NATIVE_ERROR_GDT="nativeRequestError_gdt"//请求失败,无广告填充
    const val NATIVE_SHOW_GDT="nativeShow_gdt"//广告展示成功
    const val NATIVE_FAIL_GDT="nativeonRenderFail_gdt"//渲染广告失败
    //头条开屏
    const val SPLASH_REQUEST_TT="spreadRequest_toutiao"//请求开屏广告
    const val SPLASH_REQUEST_SUCCESS_TT="spreadRequestSuccess_toutiao"//开屏广告请求成功
    const val SPLASH_ERROR_TT="spreadRequestError_toutiao"//请求失败
    const val SPLASH_SHOW_TT="spreadShow_toutiao"//开屏广告展示成功
    const val SPLASH_TIME_OUT_TT="spreadTimeOut_toutiao"//开屏广告加载超时
    //头条原生
    const val NATIVE_REQUEST_TT="nativeRequest_toutiao"//请求原生广告
    const val NATIVE_REQUEST_SUCCESS_TT="nativeRequestSuccess_toutiao"//请求成功
    const val NATIVE_ERROR_TT="nativeRequestError_toutiao"//请求失败
    const val NATIVE_SHOW_TT="nativeShow_toutiao"//展示原生广告

    /*************************************************************************************/

    const val one_click_matting = "10000_one_click_matting"       //首页点击一键漫画脸
    const val portrait_matting = "10001_portrait_matting"     //点击人像抠图
    const val face_effects = "10002_face_effects"     //点击人脸特效
    const val intelligent_matting = "10003_intelligent_matting"       //点击智能抠图
    const val beautify_pic = "10004_beautify_pic"     //点击美化图片
    const val pic_clipping = "10005_pic_clipping"     //点击图片剪裁
    const val add_filter = "10006_add_filter"     //点击添加滤镜
    const val tone_adjustment = "10007_tone_adjustment"       //点击色调调整
    const val add_text = "10008_add_text"     //点击添加文字
    const val enlarge = "10009_enlarge"       //点击放大缩小
    const val sticker = "10010_sticker"       //点击精美贴纸
    const val mosaic = "10011_mosaic"     //点击马赛克涂鸦

    const val cace_effects = "11000_cace_effects"     //人脸特效页面
    const val grow_old = "11001_grow_old"     //变老相机
    const val comic_face = "11002_comic_face"     //一键漫画脸
    const val gender_conversion = "11003_gender_conversion"       //性别转换
    const val tong_yan = "11004_tong_yan"     //童颜相机
    const val magic = "11005_magic"       //魔法相机

    const val login_register = "12000_login_register"     //登录/注册
    const val open_vip = "12001_open_vip"     //开通VIP
    const val feedback = "12002_feedback"     //意见反馈
    const val about = "12003_about"       //关于我们
    const val user_agreement = "12004_user_agreement"     //用户协议
    const val privacy_policy = "12005_privacy_policy"     //隐私政策
    const val privacy_rights = "12006_privacy_rights"     //系统隐私权限

    const val launch_payment = "13000_launch_payment"     //发起支付
    const val pay_success = "13001_pay_success"       //支付成功
    const val pay_faild = "13002_pay_faild"       //支付失败
}