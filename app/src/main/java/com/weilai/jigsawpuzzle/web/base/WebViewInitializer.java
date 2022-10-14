package com.weilai.jigsawpuzzle.web.base;

import static android.webkit.WebSettings.MIXED_CONTENT_ALWAYS_ALLOW;

import android.annotation.SuppressLint;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;

import com.weilai.jigsawpuzzle.BuildConfig;

/**
 * @description:
 * @author: luo
 * @date: 2022/10/14
 */
public class WebViewInitializer {
    @SuppressLint("SetJavaScriptEnabled")
    public WebView createWebView(WebView webView) {
        WebView.setWebContentsDebuggingEnabled(BuildConfig.DEBUG);
        webView.setHorizontalScrollBarEnabled(false);
        webView.setVerticalScrollBarEnabled(false);
        webView.setDrawingCacheEnabled(true);
        webView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                return true;
            }
        });
        final WebSettings settings = webView.getSettings();
        final String ua = settings.getUserAgentString();
        settings.setUserAgentString(ua + "DiHealth");
        settings.setBuiltInZoomControls(false);
        settings.setDisplayZoomControls(false);
        settings.setAllowFileAccess(true);
        settings.setAllowFileAccessFromFileURLs(true);
        settings.setAllowUniversalAccessFromFileURLs(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);
        settings.setCacheMode(WebSettings.LOAD_DEFAULT);
        settings.setMixedContentMode(
                MIXED_CONTENT_ALWAYS_ALLOW);
        return webView;
    }
}
