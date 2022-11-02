package com.feisukj.base.bean.ad

import com.feisukj.base.bean.UserBean
import com.feisukj.base.util.GsonUtils
import com.feisukj.base.util.SPUtil


/**
 * Created by Gpp on 2018/1/23.
 */

class StatusBean {
    var status: Boolean = false
        //是否开启
        get() {
            return if (!(SPUtil.getInstance().getString("userbean") == null || SPUtil.getInstance()
                    .getString("userbean").isEmpty())
            ) {
                try {
                    val bean = GsonUtils.parseObject(
                        SPUtil.getInstance().getString("userbean"),
                        UserBean::class.java
                    )
                    if (bean.data.vip > 0) false else field
                } catch (e: Exception) {
                    e.printStackTrace()
                    field
                }
            } else
                field
        }
    var ad_origin: String = ""//广告源
    var times: Long = 0
    var ad_percent: String = ""//广告比例
    var change_times: Long = 0//切换时间
}
