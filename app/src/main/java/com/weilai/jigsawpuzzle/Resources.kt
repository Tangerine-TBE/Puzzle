package com.weilai.jigsawpuzzle

import android.graphics.Color
import android.graphics.PointF
import com.weilai.jigsawpuzzle.bean.*
import com.weilai.jigsawpuzzle.configure.Config
import com.weilai.jigsawpuzzle.util.PackageUtils
import jp.co.cyberagent.android.gpuimage.filter.*

object Resources {

    const val tencentcloudapi_SecretId = "AKID5AEILG5lKl4Vox32scDblhS1LSL7qRp1"
    const val tencentcloudapi_SecretKey = "sbn089yb0B8A6pMgmfP3Vpy67ze8QUL3"
    const val pic_up_app_key = "465b2ad2c3e34057bb5636e98728418d"

    val vipTabList: List<VipTxBean>
        get() {
           return arrayListOf(
               VipTxBean("抠图保存","无限制","1次"),
               VipTxBean("抠图保存","无限制","1次"),
               VipTxBean("滤镜特效","无限制","部分可用"),
               VipTxBean("精密贴纸","无限制","部分可用"),
               VipTxBean("人像抠图","无限制","不支持"),
               VipTxBean("智能抠图","无限制","不支持"),
               VipTxBean("童颜特效","无限制","不支持"),
               VipTxBean("变老特效","无限制","不支持"),
               VipTxBean("动漫特效","无限制","不支持"),
               VipTxBean("性别转换","无限制","不支持"),
               VipTxBean("魔法相机","无限制","不支持"),
               VipTxBean("美化图片","无限制","部分可用")
           )
        }

    val idMakeList:List<IdMakeBean>
    get() = arrayListOf(
        IdMakeBean("一寸","295x412px","25x35mm"),
        IdMakeBean("小一寸","260x378px","22x32mm"),
        IdMakeBean("大一寸","390x567px","33x48mm"),
        IdMakeBean("二寸","413x579px","35x49mm")
    )


    fun getFreeSticker():Array<StickerBean>{
        return arrayOf(
            StickerBean(1,R.drawable.ic_free_sticker_1, R.drawable.ic_free_sticker_1, false),
            StickerBean(2,R.drawable.ic_free_sticker_2, R.drawable.ic_free_sticker_2, false),
            StickerBean(3,R.drawable.ic_free_sticker_3, R.drawable.ic_free_sticker_3, false),
            StickerBean(4,R.drawable.ic_free_sticker_4, R.drawable.ic_free_sticker_4, false),
            StickerBean(5,R.drawable.ic_free_sticker_5, R.drawable.ic_free_sticker_5, false),
            StickerBean(6,R.drawable.ic_free_sticker_6, R.drawable.ic_free_sticker_6, false),
            StickerBean(7,R.drawable.ic_free_sticker_7, R.drawable.ic_free_sticker_7, false),
            StickerBean(8,R.drawable.ic_free_sticker_8, R.drawable.ic_free_sticker_8, false),
            StickerBean(9,R.drawable.ic_free_sticker_9, R.drawable.ic_free_sticker_9, false)
        )
    }

    fun getVipSticker():Array<StickerBean>{
        return arrayOf(
            StickerBean(1,R.drawable.ic_vip_sticker_1, R.drawable.ic_vip_sticker_1, true),
            StickerBean(2,R.drawable.ic_vip_sticker_2, R.drawable.ic_vip_sticker_2, true),
            StickerBean(3,R.drawable.ic_vip_sticker_3, R.drawable.ic_vip_sticker_3, true),
            StickerBean(4,R.drawable.ic_vip_sticker_4, R.drawable.ic_vip_sticker_4, true),
//            StickerBean(R.drawable.ic_vip_sticker_5, R.drawable.ic_vip_sticker_5, true),
            StickerBean(5,R.drawable.ic_vip_sticker_6, R.drawable.ic_vip_sticker_6, true),
            StickerBean(6,R.drawable.ic_vip_sticker_7, R.drawable.ic_vip_sticker_7, true)
        )
    }
//
    fun getTextSticker():Array<StickerBean>{
        return arrayOf(
            StickerBean(0,R.mipmap.ic_text_style_small_0,R.drawable.ic_text_style_small_0,false),
//            StickerBean(1,R.drawable.ic_text_style_small_1,R.drawable.ic_text_style_big_1,false),
            StickerBean(2,R.mipmap.ic_text_style_small_2,R.drawable.ic_text_style_big_2,false),
            StickerBean(3,R.mipmap.ic_text_style_small_3,R.drawable.ic_text_style_big_3,false),
            StickerBean(4,R.mipmap.ic_text_style_small_4,R.drawable.ic_text_style_big_4,false),
            StickerBean(5,R.mipmap.ic_text_style_small_5,R.drawable.ic_text_style_big_5,false),
            StickerBean(6,R.mipmap.ic_text_style_small_6,R.drawable.ic_text_style_big_6,false)
        )
    }
//
    fun getCameraFilter():Array<FilterBean>{
        return arrayOf(
            FilterBean("filter/brightness.png", GPUImageBrightnessFilter(0.0f), "原图", false),
            FilterBean("filter/grayscale.png", GPUImageGrayscaleFilter(), "灰度", false),
            FilterBean("filter/colorInvert.png", GPUImageColorInvertFilter(), "颠倒", false),
            FilterBean("filter/zoomBlur.png", GPUImageZoomBlurFilter(), "变焦模糊", false),
            FilterBean("filter/swirl.png", GPUImageSwirlFilter(0.5f, 0.2f, PointF(0.5f, 0.5f)), "扭曲", false),
            FilterBean("filter/sketch.png", GPUImageSketchFilter(), "草图", false),
            FilterBean("filter/toon.png", GPUImageToonFilter(), "Toon", false),
            FilterBean("filter/bulgeDistortion.png", GPUImageBulgeDistortionFilter(), "凸出", false)
        )
    }

    /**
     * 获取VIP价格
     * */
    private const val VIP_13: Double = 78.88
    private const val VIP_1 = 18.88
    private const val VIP_6 = 38.88
    private const val VIP_12 = 48.88

    private const val RATIO = 1.2
    fun getVipPrice():Array<PayBean>{
        return arrayOf(
            PayBean("VIP13",
                "永久卡",
                if (BuildConfig.DEBUG)0.01 else 78.00,
                98.00,
                true,
                PackageUtils.getAppName(Config.getApplicationContext()) + "永久VIP",
                PackageUtils.getAppName(
                    Config.getApplicationContext()) + "永久VIP"),
            PayBean("VIP1",
                "一个月",
                18.00,
                38.00,
                false,
                PackageUtils.getAppName(Config.getApplicationContext()) + "一个月VIP",
                PackageUtils.getAppName(
                    Config.getApplicationContext()) + "一个月VIP"),
            PayBean("VIP6",
                "半年",
                38.00,
                58.00,
                false,
                PackageUtils.getAppName(Config.getApplicationContext()) + "半年VIP",
                PackageUtils.getAppName(
                    Config.getApplicationContext()) + "半年VIP"),
            PayBean("VIP12",
                "一年",
                48.00,
                68.00,
                false,
                PackageUtils.getAppName(Config.getApplicationContext()) + "一年VIP",
                PackageUtils.getAppName(
                    Config.getApplicationContext()) + "一年VIP")
        )
    }

    /**
     * 获取VIP特权
     * */
    fun getVipPrivilege():Array<PrivilegeBean>{
        return arrayOf(
            PrivilegeBean(R.mipmap.ic_vip_privilege1, "人像抠图", "智能去除人像背景"),
            PrivilegeBean(R.mipmap.ic_vip_privilege2, "智能抠图", "一键去除任意图片背景"),
            PrivilegeBean(R.mipmap.ic_vip_privilege3, "滤镜特效", "滤镜美颜效果更佳"),
            PrivilegeBean(R.mipmap.ic_vip_privilege4, "高清背景", "高清背景全部可用"),
            PrivilegeBean(R.mipmap.ic_vip_privilege5, "色调调节", "高清背景全部可用"),
            PrivilegeBean(R.mipmap.ic_vip_privilege6, "美化图片", ""),
            PrivilegeBean(R.mipmap.ic_vip_privilege7, "马赛克", ""),
            PrivilegeBean(R.mipmap.ic_vip_privilege8, "放大缩小", ""),
            PrivilegeBean(R.mipmap.ic_vip_privilege9, "添加文字", ""),
            PrivilegeBean(R.mipmap.ic_vip_privilege10, "图片裁剪", ""),
            PrivilegeBean(R.mipmap.ic_vip_privilege11, "移除广告", "讨厌的广告去无踪"),
            PrivilegeBean(R.mipmap.ic_vip_privilege12, "精美帖纸", "精美帖纸全部可用")
        )
    }

    /**
     * 获取调整列表
     * */
    fun getAdjust():Array<AdjustBean>{
        return arrayOf(
            AdjustBean("亮度",R.drawable.ic_adjust_ld_n,R.drawable.ic_adjust_ld_y),
            AdjustBean("对比度",R.drawable.ic_adjust_dbd_n,R.drawable.ic_adjust_dbd_y),
            AdjustBean("色调",R.drawable.ic_adjust_sd_n,R.drawable.ic_adjust_sd_y),
            AdjustBean("锐度",R.drawable.ic_adjust_rd_n,R.drawable.ic_adjust_rd_y),
            AdjustBean("曝光",R.drawable.ic_adjust_bg_n,R.drawable.ic_adjust_bg_y),
            AdjustBean("白平衡",R.drawable.ic_adjust_bph_n,R.drawable.ic_adjust_bph_y),
//            AdjustBean("阴影",R.drawable.ic_adjust_yy_n,R.drawable.ic_adjust_yy_y),
//            AdjustBean("高光",R.drawable.ic_adjust_gg_n,R.drawable.ic_adjust_gg_y),
            AdjustBean("饱和度",R.drawable.ic_adjust_bhd_n,R.drawable.ic_adjust_bhd_y)
//            AdjustBean("雾化",R.drawable.ic_adjust_wh_n,R.drawable.ic_adjust_wh_y)
        )
    }

    /**
     * 获取背景分组
     * */

    /**自然风光*/

    /**汽车*/

    /**游戏*/

    /**美女*/

    /**明星*/

    /**体育运动*/

    /**动物萌宠*/

    /**影视*/

    /**意境*/

    /**动漫*/

    /**文字*/

    /**情侣*/

    /**简约*/


    fun getTextColorList(): Array<Int>{
        return arrayOf(
            Color.parseColor("#ff0000"),
            Color.parseColor("#ff4000"),
            Color.parseColor("#ff8000"),
            Color.parseColor("#ffbf00"),
            Color.parseColor("#ffff00"),
            Color.parseColor("#bfff00"),
            Color.parseColor("#80ff00"),
            Color.parseColor("#40ff00"),
            Color.parseColor("#00ff00"),
            Color.parseColor("#00ff40"),
            Color.parseColor("#00ff80"),
            Color.parseColor("#00ffbf"),
            Color.parseColor("#00ffff"),
            Color.parseColor("#00bfff"),
            Color.parseColor("#0080ff"),
            Color.parseColor("#0040ff"),
            Color.parseColor("#0000ff"),
            Color.parseColor("#4000ff"),
            Color.parseColor("#8000ff"),
            Color.parseColor("#bf00ff"),
            Color.parseColor("#ff00ff"),
            Color.parseColor("#ff00bf"),
            Color.parseColor("#ff0080"),
            Color.parseColor("#ff0040")
        )
    }

    fun getCommentList(): Array<CommentBean>{
        return arrayOf(
            CommentBean(0,R.mipmap.ic_pay_comment_avatar_1,"用户187****2589","超超超超级好用！ P图小白都可轻松完成，很简单诶。加油。"),
            CommentBean(1,R.mipmap.ic_pay_comment_avatar_2,"用户158****1972","非常有趣好玩的一款抠图p图软件 还有特效相机。"),
            CommentBean(2,R.mipmap.ic_pay_comment_avatar_3,"用户135****8726","我第一次用    感觉超级棒    给闺蜜做图   她特别高兴  嘻嘻嘻。"),
            CommentBean(3,R.mipmap.ic_pay_comment_avatar_4,"用户159****9634","哇，这个软件真的是超级好用，智能扣图很完美。"),
            CommentBean(4,R.mipmap.ic_pay_comment_avatar_5,"用户157****7154","非常好，一抠，抠上瘾了，五星好评，强列推建！")
        )
    }

}