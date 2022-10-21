package com.weilai.jigsawpuzzle.util;

import android.content.res.Resources;
import android.text.TextUtils;

import com.abc.matting.tencentcloudapi.bda.v20200324.BdaClient;
import com.abc.matting.tencentcloudapi.bda.v20200324.models.SegmentPortraitPicRequest;
import com.abc.matting.tencentcloudapi.bda.v20200324.models.SegmentPortraitPicResponse;
import com.abc.matting.tencentcloudapi.common.Credential;
import com.abc.matting.tencentcloudapi.common.exception.TencentCloudSDKException;
import com.abc.matting.tencentcloudapi.common.profile.ClientProfile;
import com.abc.matting.tencentcloudapi.common.profile.HttpProfile;
import com.abc.matting.tencentcloudapi.ft.v20200304.FtClient;
import com.abc.matting.tencentcloudapi.ft.v20200304.models.AgeInfo;
import com.abc.matting.tencentcloudapi.ft.v20200304.models.ChangeAgePicRequest;
import com.abc.matting.tencentcloudapi.ft.v20200304.models.ChangeAgePicResponse;
import com.abc.matting.tencentcloudapi.ft.v20200304.models.FaceCartoonPicResponse;
import com.abc.matting.tencentcloudapi.iai.v20180301.IaiClient;
import com.abc.matting.tencentcloudapi.iai.v20180301.models.DeleteFaceResponse;
import com.abc.matting.tencentcloudapi.iai.v20180301.models.DetectFaceRequest;
import com.abc.matting.tencentcloudapi.iai.v20180301.models.DetectFaceResponse;
import com.weilai.jigsawpuzzle.Constant;

import java.io.File;
import java.util.ArrayList;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.functions.Consumer;
import io.reactivex.functions.Function;
import okhttp3.Call;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;

/**
 * * DATE: 2022/10/20
 * * Author:tangerine
 * * Description:
 **/
public class EffectUtils {


    public static String toOld(Long age, String base64) {
        try {
            Credential credential = new Credential(Constant.tencentcloudapi_SecretId, Constant.tencentcloudapi_SecretKey);
            HttpProfile httpProfile = new HttpProfile();
            httpProfile.setEndpoint("ft.tencentcloudapi.com");
            ClientProfile clientProfile = new ClientProfile();
            clientProfile.setHttpProfile(httpProfile);
            FtClient ftClient = new FtClient(credential, "ap-guangzhou", clientProfile);
            ChangeAgePicRequest changeAgePicRequest = new ChangeAgePicRequest();
            changeAgePicRequest.setImage(base64);
            AgeInfo ageInfo = new AgeInfo();
            ageInfo.setAge(age);
            AgeInfo[] ageInfos = new AgeInfo[1];
            ageInfos[0] = ageInfo;
            changeAgePicRequest.setAgeInfos(ageInfos);
            changeAgePicRequest.setRspImgType("url");
            ChangeAgePicResponse changeAgePicResponse = ftClient.ChangeAgePic(changeAgePicRequest);
            return ChangeAgePicResponse.toJsonString(changeAgePicResponse);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    public static Observable<Object[]> analyzeTheFace(String base64) {
        return Observable.create(new ObservableOnSubscribe<Object[]>() {
            @Override
            public void subscribe(ObservableEmitter<Object[]> emitter) throws Exception {
                Credential credential = new Credential(Constant.tencentcloudapi_SecretId, Constant.tencentcloudapi_SecretKey);
                HttpProfile httpProfile = new HttpProfile();
                httpProfile.setEndpoint("iai.tencentcloudapi.com");
                ClientProfile clientProfile = new ClientProfile();
                clientProfile.setHttpProfile(httpProfile);
                IaiClient iaiClient = new IaiClient(credential, "ap-guangzhou", clientProfile);
                DetectFaceRequest detectFaceRequest = new DetectFaceRequest();
                detectFaceRequest.setImage(base64);
                detectFaceRequest.setFaceModelVersion("3.0");
                detectFaceRequest.setNeedRotateDetection(1L);
                iaiClient.DetectFace(detectFaceRequest);
                Object[] objects = new Object[2];
                objects[0] = true;
                objects[1] = base64;
                emitter.onNext(objects);
            }
        });
    }

    public static Observable<String> portraitCutout(String base64) {
        return Observable.create(new ObservableOnSubscribe<String>() {
            @Override
            public void subscribe(ObservableEmitter<String> emitter) throws Exception {
                Credential credential = new Credential(Constant.tencentcloudapi_SecretId, Constant.tencentcloudapi_SecretKey);
                HttpProfile httpProfile = new HttpProfile();
                httpProfile.setEndpoint("bda.tencentcloudapi.com");
                httpProfile.setConnTimeout(10);
                ClientProfile clientProfile = new ClientProfile();
                clientProfile.setHttpProfile(httpProfile);
                BdaClient bdaClient = new BdaClient(credential, "ap-guangzhou", clientProfile);
                SegmentPortraitPicRequest segmentPortraitPicRequest = new SegmentPortraitPicRequest();
                segmentPortraitPicRequest.setImage(base64);
                SegmentPortraitPicResponse response = bdaClient.SegmentPortraitPic(segmentPortraitPicRequest);
                emitter.onNext(SegmentPortraitPicResponse.toJsonString(response));
            }
        });


    }

    public static String intelligentCutout(String path) {
        try {
            OkHttpClient okHttpClient = new OkHttpClient();
            File file = new File(path);
            RequestBody imageBody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
            RequestBody requestBody = new MultipartBody.Builder().setType(MultipartBody.FORM)
                    .addFormDataPart("file", path, imageBody)
                    .build();
            Request request = new Request.Builder().header("Content-Type", "multipart/form-data")
                    .header("APIKEY", Constant.pic_up_app_key)
                    .url("https://picupapi.tukeli.net/api/v1/matting2?mattingType=6")
                    .post(requestBody)
                    .build();
            Call call = okHttpClient.newCall(request);
            Response response = call.execute();
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String strBody = responseBody.string();
                if (!TextUtils.isEmpty(strBody)) {
                    return strBody;
                }
            }
            return "";
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
}
