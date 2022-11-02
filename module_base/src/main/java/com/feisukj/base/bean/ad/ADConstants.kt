package com.feisukj.base.bean.ad

import com.feisukj.base.util.SPUtil

object ADConstants {
    /************************页面命名 存储广告显示配置 */
    const val START_PAGE = "start_page"
    const val EXIT_PAGE = "exit_page"
    const val HOME_PAGE = "home_page"
    const val TEMPLATELIST_PAGE = "templatelist_page"
    const val  SAVESUCCESS_PAGE ="savesuccess_page"
    const val IMGDETAIL_PAGE = "imgdetail_page"

    /************************页面命名*************************/

    /************************页面命名 */
    /************************开屏广告 */
    const val AD_APP_LOAD_TIME = "ad_app_load_time" //App启动时间

    const val AD_APP_BACKGROUND_TIME = "ad_app_background_time" //App退到后台时间

    const val AD_SPREAD_PERIOD = "ad_spread_period" //开屏后台设置的时间间隔

    const val AD_SPLASH_STATUS = "ad_splash_status" //开屏开关

    /************************开屏广告*************************/

    /************************开屏广告 */
    /************************插屏广告 */
    const val AD_INSERT_SHOW_PERIOD = "ad_insert_change_period" //插屏广告显示间隔

    const val AD_INSERT_LAST_SHOW = "ad_insert_last_origin" //插屏广告上展示时间

    /************************插屏广告*************************/
    /************************插屏广告 */
    /**
     * 是否开启了页面banner定时器
     */
    const val AD_BANNER_IS_TIMER = "ad_banner_is_timer"

    const val AD_BANNER_LAST_CHANGE = "AD_BANNER_LAST_CHANGE"

    /************************原生广告 */
    const val AD_NATIVE_SHOW_PERIOD = "ad_native_change_period" //原生广告显示间隔

    const val AD_NATIVE_LAST_SHOW = "ad_native_last_origin" //原生广告上ci展示时间

    /************************原生广告 */
    val kTouTiaoAppKey by lazy { SPUtil.getInstance().getString("kTouTiaoAppKey","5346089")?:"5346089" }
    val kTouTiaoKaiPing by lazy { SPUtil.getInstance().getString("kTouTiaoKaiPing","887987502")?:"887987502" }
    val kTouTiaoBannerKey by lazy { SPUtil.getInstance().getString("kTouTiaoBannerKey","946558273")?:"946558273" }
    val kTouTiaoChaPingKey by lazy { SPUtil.getInstance().getString("kTouTiaoChaPingKey","950192228")?:"950192228" }
    val kTouTiaoJiLiKey by lazy { SPUtil.getInstance().getString("kTouTiaoJiLiKey","946558275")?:"946558275" }
    val kTouTiaoSeniorKey by lazy { SPUtil.getInstance().getString("kTouTiaoSeniorKey","950192209")?:"950192209" }
    val kTouTiaoSmallSeniorKey by lazy { SPUtil.getInstance().getString("ktouTiaoFullscreenvideoKey","946558276")?:"946558276" }

    val kWaiAppKey by lazy { SPUtil.getInstance().getString("kWaiAppKey","887200008")?:"887200008" }
    val kWaiKaiPing by lazy { SPUtil.getInstance().getString("kWaiKaiPing","7042622198217197")?:"7042622198217197" }
    val kWaiBannerKey by lazy { SPUtil.getInstance().getString("kWaiBannerKey","3002822148516113")?:"3002822148516113" }
    val kWaiChaPingKey by lazy { SPUtil.getInstance().getString("kWaiChaPingKey","2062728188517126")?:"2062728188517126" }
    val kWaiJiLiKey by lazy { SPUtil.getInstance().getString("kWaiJiLiKey","6072925118019134")?:"6072925118019134" }
    val kWaiSeniorKey by lazy { SPUtil.getInstance().getString("kWaiSeniorKey","9082025128710195")?:"9082025128710195" }
}