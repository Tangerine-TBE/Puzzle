package com.weilai.jigsawpuzzle.web.base;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;

/**
 ** DATE: 2022/10/14
 ** Author:tangerine
 ** Description:
 **/
public abstract class WebBaseFragment extends BaseFragment implements  IWebViewInitializer {
    private WebView mWebView = null;
    private final ReferenceQueue<WebView> WEB_VIEW_QUEUE = new ReferenceQueue<>();
    private String mUrl = null;
    private boolean mIsWebViewAvailable = false;

    public WebBaseFragment() {
    }
    public abstract IWebViewInitializer setInitializer();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }
    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        initWebView();
        super.onLazyInitView(savedInstanceState);
    }

    public final void setUrl(String url) {
        this.mUrl = url;
    }

    public final String getUrl() {
        return mUrl;
    }

    @SuppressLint("JavascriptInterface")
    private void initWebView() {
        if (mWebView != null) {
            mWebView.removeAllViews();
            mWebView.destroy();
        } else {
            final IWebViewInitializer initializer = setInitializer();
            if (initializer != null) {
                final WeakReference<WebView> webViewWeakReference = new WeakReference<>(findWebViewById(), WEB_VIEW_QUEUE);
                mWebView = webViewWeakReference.get();
                mWebView = initializer.initWebView(mWebView);
                mWebView.setWebViewClient(initializer.initWebViewClient());
                WebView.enableSlowWholeDocumentDraw();
                mWebView.setWebChromeClient(initializer.initWebChromeClient());
                mWebView.addJavascriptInterface(WebInterface.create(this), "WeiLai"); //接入名称
                mIsWebViewAvailable = true;
            } else {
                throw new NullPointerException("Initializer is null !");
            }
        }
    }

    protected abstract WebView findWebViewById();

    @Override
    public void onPause() {
        super.onPause();
        if (mWebView != null) {
            mWebView.onResume();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mWebView != null) {
            mWebView.clearCache(true);
            mWebView.clearHistory();
            mWebView.clearFormData();
            mWebView.removeAllViews();
            mWebView.destroy();
            mWebView = null;

        }
    }

    public WebView getWebView() {
        if (mWebView == null) {
            throw new NullPointerException("WebView is Null");
        }
        return mIsWebViewAvailable ? mWebView : null;
    }
}
