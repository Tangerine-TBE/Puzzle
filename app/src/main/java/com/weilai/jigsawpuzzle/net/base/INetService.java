package com.weilai.jigsawpuzzle.net.base;

import com.weilai.jigsawpuzzle.bean.CrossBannerEntity;

import java.util.List;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface INetService {
    @GET("getPicTemplate")
    Observable<ResponseBody> getPicTemplate();
    @GET()
    Call<ResponseBody> getPhoto(@Url String url);
    @GET
    Observable<ResponseBody> getPhoto1(@Url String url );
    @GET("anTemplateBanner")
    Observable<List<CrossBannerEntity>> getBanner();
}
