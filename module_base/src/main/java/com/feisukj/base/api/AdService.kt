package com.feisukj.base.api

import com.feisukj.base.BuildConfig
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
    @GET("/ytkapplicaton/anWlPintu")
    fun getADConfig(@Query("name") name: String="com.weilai.jigsawpuzzle",
                    @Query("channel") channel: String,
                    @Query("version") version: String): Observable<AdsConfig>
}