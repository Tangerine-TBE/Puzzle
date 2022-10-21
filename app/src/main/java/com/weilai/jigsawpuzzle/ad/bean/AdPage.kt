package com.feisukj.base.bean.ad


/**
 * Created by ATian on 2018/4/3.
 */

class AdPage {
    var start_page: TypeBean? = null//闪屏页面
    var background_page: TypeBean? = null
    var exit_page: TypeBean? = null

    var Advertisement:Advertisement_?=null

    class Advertisement_{
        var kTouTiaoAppKey= ADConstants.kTouTiaoAppKey
        var kTouTiaoKaiPing=ADConstants.kTouTiaoKaiPing
        var kTouTiaoBannerKey=ADConstants.kTouTiaoBannerKey
        var kTouTiaoChaPingKey=ADConstants.kTouTiaoChaPingKey
        var kTouTiaoJiLiKey=ADConstants.kTouTiaoJiLiKey
        var kTouTiaoSeniorKey=ADConstants.kTouTiaoSeniorKey
        var ktouTiaoFullscreenvideoKey=ADConstants.ktouTiaoFullscreenvideoKey

        var kGDTMobSDKAppKey=ADConstants.kGDTMobSDKAppKey
        var kGDTMobSDKChaPingKey=ADConstants.kGDTMobSDKChaPingKey
        var kGDTMobSDKKaiPingKey=ADConstants.kGDTMobSDKKaiPingKey
        var kGDTMobSDKBannerKey=ADConstants.kGDTMobSDKBannerKey
        var kGDTMobSDKNativeKey=ADConstants.kGDTMobSDKNativeKey
        var kGDTMobSDKJiLiKey=ADConstants.kGDTMobSDKJiLiKey
        var kGDTMobSDKNativeSmallKey=ADConstants.kGDTMobSDKNativeSmallKey

        var kWaiAppKey = ADConstants.kWaiAppKey
        var kWaiChaPingKey = ADConstants.kWaiChaPingKey
        var kWaiKaiPingKey = ADConstants.kWaiKaiPingKey
        var kWaiSeniorKey = ADConstants.kWaiNativeKey
        var kWaiJiLiKey = ADConstants.kWaiJiLiKey
    }
}
