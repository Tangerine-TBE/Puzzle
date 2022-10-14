package com.weilai.jigsawpuzzle.web.base;

import android.webkit.WebView;

/**
 ** DATE: 2022/10/14
 ** Author:tangerine
 ** Description:
 **/
public class Router {
    private Router() {

    }

    private static class Holder {
        private static final Router INSTANCE = new Router();
    }

    public static Router getInstance() {
        return Holder.INSTANCE;
    }

    public final boolean handleWebUrl(WebBaseFragment activity, String url) {
        activity.setUrl(url);

        return true;
    }

    private void loadWebPage(WebView webView, String url) {
        if (webView != null) {
            webView.loadUrl(url);
        } else {
            throw new NullPointerException("WebView is null");
        }

    }

    private void loadLocalPage(WebView webView, String url) {
        webView.loadDataWithBaseURL("http://localhost",url, "text/html;charset=utf-8", null,null);
    }

    public final void loadPage(WebView webView, String url) {
        if (url.startsWith("file:android_asset")) {
            loadWebPage(webView, url);
        } else if (url.startsWith("http")) {
            loadWebPage(webView, url);
        } else {
            loadLocalPage(webView, url);
        }
    }

    public void loadPage(WebBaseFragment fragment, String url) {
        loadPage(fragment.getWebView(), url);
    }
}
