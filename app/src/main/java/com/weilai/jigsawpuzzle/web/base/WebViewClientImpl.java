package com.weilai.jigsawpuzzle.web.base;

import android.webkit.WebResourceRequest;
import android.webkit.WebResourceResponse;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.nostra13.universalimageloader.utils.L;

import java.io.InputStream;
import java.net.URLConnection;

/**
 ** DATE: 2022/10/14
 ** Author:tangerine
 ** Description:
 **/
public class WebViewClientImpl extends WebViewClient {
    private   final WebBaseFragment webBaseFragment;

    public WebViewClientImpl(WebBaseFragment webBaseFragment) {
        this.webBaseFragment = webBaseFragment;
    }

    @Override
    public boolean shouldOverrideUrlLoading(WebView view, String url) {
        Router.getInstance().loadPage(view,url);
        return true;
    }

//    @Nullable
//    @Override
//    public WebResourceResponse shouldInterceptRequest(WebView view, WebResourceRequest request) {
//        InputStream input;
//
//        String url = request.getUrl().toString();
//
//        String key = "http://dibox/";
//        if (url.contains(key)) {
//            String filePath = url.replace(key, "");
//            try {
//                input=   webBaseFragment.getBaseActivity().getAssets().open(filePath);
//                return new WebResourceResponse(URLConnection.getFileNameMap().getContentTypeFor(filePath), "UTF-8", input);
//            } catch (Exception e) {
//                e.printStackTrace();
//
//            }
//
//        }
//
//
//        return super.shouldInterceptRequest(view, request);
//    }
}
