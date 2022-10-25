package com.weilai.jigsawpuzzle.net.base;

import com.weilai.jigsawpuzzle.bean.CrossBannerEntity;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.QueryMap;
import retrofit2.http.Url;

public interface INetService {
    @GET("getPicTemplate")
    Observable<ResponseBody> getPicTemplate();

    @GET()
    Call<ResponseBody> getPhoto(@Url String url);

    @GET
    Observable<ResponseBody> getPhoto1(@Url String url);

    @GET("anTemplateBanner")
    Observable<List<CrossBannerEntity>> getBanner();

    @POST()
    Observable<ResponseBody> clientReport(@Url String url, @Body MultipartBody multipartBody);

}
