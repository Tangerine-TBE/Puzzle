package com.weilai.jigsawpuzzle.util

import com.feisukj.base.bean.ad.AdsConfig
import io.reactivex.Observable
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * Author : Gupingping
 * Date : 2018/10/8
 * QQ : 464955343
 */
interface AdService {
    @GET("/ytkapplicaton/anTuPianBianJi")
    fun getADConfig(@Query("name") name: String="com.sheshou.bucklediagram",
                    @Query("channel") channel: String,
                    @Query("version") version: String): Observable<AdsConfig>
}