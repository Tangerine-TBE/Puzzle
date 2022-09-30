package com.weilai.jigsawpuzzle.net.base;

import io.reactivex.Observable;
import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface INetService {
    @GET("getPicTemplate")
    Observable<ResponseBody> getPicTemplate();
    @GET()
    Observable<ResponseBody> getPhoto(@Url String url);
}