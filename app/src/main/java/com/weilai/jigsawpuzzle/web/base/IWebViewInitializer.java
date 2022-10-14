package com.weilai.jigsawpuzzle.web.base;

import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

/**
 ** DATE: 2022/10/14
 ** Author:tangerine
 ** Description:
 **/
public interface IWebViewInitializer {
    WebView initWebView(WebView webView);
    WebViewClient initWebViewClient();
    WebChromeClient initWebChromeClient();
}
