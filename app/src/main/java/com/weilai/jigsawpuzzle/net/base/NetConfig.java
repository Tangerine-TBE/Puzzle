package com.weilai.jigsawpuzzle.net.base;

import com.weilai.jigsawpuzzle.Constant;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
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
    public INetService getINetService(){
        return mINetService;
    }
    private NetConfig() {
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
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
