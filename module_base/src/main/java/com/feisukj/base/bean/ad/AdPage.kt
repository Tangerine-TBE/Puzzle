package com.feisukj.base.bean.ad


/**
 * Created by ATian on 2018/4/3.
 */

class AdPage {
    var start_page: TypeBean? = null//启动广告 --》spread_screen
    var home_page :TypeBean? =null//主页广告 -->native_screen insert_screen
    var templatelist_page: TypeBean? = null //模板列表 --》native_screen
    var savesuccess_page: TypeBean? = null //保存成功广告 --》native_screen
    var imgdetail_page  :TypeBean? = null //保存信息广告 insert_screen --》
    var exit_page :TypeBean? = null // 热启动广告 native_screen
    var Advertisement:Advertisement_?=null

    class Advertisement_{
        var kTouTiaoAppKey= ADConstants.kTouTiaoAppKey
        var kTouTiaoKaiPing=ADConstants.kTouTiaoKaiPing
        var kTouTiaoBannerKey=ADConstants.kTouTiaoBannerKey
        var kTouTiaoChaPingKey=ADConstants.kTouTiaoChaPingKey
        var kTouTiaoJiLiKey=ADConstants.kTouTiaoJiLiKey
        var kTouTiaoSeniorKey=ADConstants.kTouTiaoSeniorKey
        var kTouTiaoSmallSeniorKey=ADConstants.kTouTiaoSmallSeniorKey

        var kWaiAppKey=ADConstants.kWaiAppKey
        var kWaiKaiPing=ADConstants.kWaiKaiPing
        var kWaiBannerKey=ADConstants.kWaiBannerKey
        var kWaiChaPingKey=ADConstants.kWaiChaPingKey
        var kWaiJiLiKey=ADConstants.kWaiJiLiKey
        var kWaiSeniorKey=ADConstants.kWaiSeniorKey
    }
}
