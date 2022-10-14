package com.weilai.jigsawpuzzle.web.base;

import android.webkit.JsResult;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

/**
 ** DATE: 2022/10/14
 ** Author:tangerine
 ** Description:
 **/
public class ChromeClient extends WebChromeClient {
    @Override
    public boolean onJsAlert(WebView view, String url, String message, JsResult result) {
        return super.onJsAlert(view, url, message, result);
    }
}
