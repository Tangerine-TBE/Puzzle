package com.weilai.jigsawpuzzle.net.base;

import androidx.annotation.NonNull;

import com.weilai.jigsawpuzzle.Constant;
import com.weilai.jigsawpuzzle.configure.Config;

import java.io.File;
import java.io.IOException;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * * DATE: 2022/9/30
 * * Author:tangerine
 * * Description:
 **/
public class NetConfig {
    private static volatile NetConfig NET_CONFIG = null;
    private final INetService mINetService;

    public static NetConfig getInstance() {
        if (NET_CONFIG == null) {
            synchronized (NetConfig.class) {
                if (null == NET_CONFIG) {
                    NET_CONFIG = new NetConfig();
                }
            }
        }
        return NET_CONFIG;
    }

    public INetService getINetService() {
        return mINetService;
    }

    private NetConfig() {
        File httpCacheDirectory = new File(Config.getApplicationContext().getExternalCacheDir(), "responses");
        Cache cache = new Cache(httpCacheDirectory, 10 * 1024 * 1024);
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .cache(cache)
                .addInterceptor(chain -> {
                    Response response = chain.proceed(chain.request());
                    CacheControl cacheControl = new CacheControl.Builder()
                            .maxAge(3, TimeUnit.HOURS)
                            .build();
                    return response.newBuilder().header("Cache-Control", cacheControl.toString())
                            .build();
                })
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())//请求的结果转为实体类
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        mINetService = retrofit.create(INetService.class);
    }


}
