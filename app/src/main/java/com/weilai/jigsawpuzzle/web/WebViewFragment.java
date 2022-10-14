package com.weilai.jigsawpuzzle.web;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import androidx.annotation.Nullable;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment;
import com.weilai.jigsawpuzzle.util.FileUtil;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.web.base.ChromeClient;
import com.weilai.jigsawpuzzle.web.base.IWebViewInitializer;
import com.weilai.jigsawpuzzle.web.base.Router;
import com.weilai.jigsawpuzzle.web.base.WebBaseFragment;
import com.weilai.jigsawpuzzle.web.base.WebViewClientImpl;
import com.weilai.jigsawpuzzle.web.base.WebViewInitializer;

import java.io.IOException;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.Scheduler;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * * DATE: 2022/10/14
 * * Author:tangerine
 * * Description:
 **/
public class WebViewFragment extends WebBaseFragment {
    private WebView mWebView;

    private WebViewFragment() {

    }

    public static WebViewFragment getInstance(String url) {
        Bundle bundle = new Bundle();
        bundle.putString("URL", url);
        WebViewFragment webViewFragment = new WebViewFragment();
        webViewFragment.setArguments(bundle);
        return webViewFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.web_fragment;
    }

    @Override
    protected void initView(View view) {
        String url = getArguments().getString("URL");
        setUrl(url);
        mWebView = view.findViewById(R.id.web_view);
        view.findViewById(R.id.btn_shot).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                doOnBackGround();
            }
        });
    }

    private void doOnBackGround() {
        mWebView.measure(View.MeasureSpec.makeMeasureSpec(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED),
                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
        mWebView.layout(0, 0, mWebView.getMeasuredWidth(), mWebView.getMeasuredHeight());
        showProcessDialog();
        Observable.create((ObservableOnSubscribe<String>) emitter -> {
            //启用View的DrawingCache功能
            mWebView.buildDrawingCache(true);
            mWebView.setDrawingCacheEnabled(true);
            mWebView.setVerticalScrollBarEnabled(false);
            mWebView.buildDrawingCache();
            Thread.sleep(2000);
            Bitmap bitmap = convertViewToBitmap(mWebView, mWebView.getMeasuredWidth(), mWebView.getMeasuredHeight());
            Canvas canvas = new Canvas(bitmap);
            // 画布的宽高和 WebView 的网页保持一致
            Paint paint = new Paint();
            L.e(mWebView.getMeasuredHeight() + "");
            canvas.drawBitmap(bitmap, 0, mWebView.getMeasuredHeight(), paint);
            mWebView.draw(canvas);
            //保存bitmap 为本地图片或做其他处理
            String filePath = FileUtil.saveScreenShot(bitmap, System.currentTimeMillis() + "");
            mWebView.buildDrawingCache(false);
            emitter.onNext(filePath);
        }).observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io()).subscribe(new Observer<String>() {
            @Override
            public void onSubscribe(Disposable d) {
                mDisposable.add(d);
            }

            @Override
            public void onNext(String o) {
                L.e(o);
                hideProcessDialog();
                startWithPop(SaveFragment.getInstance(o));
            }

            @Override
            public void onError(Throwable e) {
                hideProcessDialog();
            }

            @Override
            public void onComplete() {
            }
        });
    }

    private Bitmap convertViewToBitmap(View view, int bitmapWidth, int bitmapHeight) {
        Bitmap bitmap = null;
        try {
            bitmap = Bitmap.createBitmap(bitmapWidth, bitmapHeight, Bitmap.Config.ARGB_4444);
            view.draw(new Canvas(bitmap));
        } catch (OutOfMemoryError ooe) {
            System.gc();
            return bitmap;
        }
        return bitmap;
    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
        super.onLazyInitView(savedInstanceState);
        Router.getInstance().loadPage(this, getUrl());
    }

    @Override
    protected void initListener(View view) {

    }


    @Override
    public IWebViewInitializer setInitializer() {
        return this;
    }

    @Override
    protected WebView findWebViewById() {
        return mWebView;
    }

    @Override
    public WebView initWebView(WebView webView) {
        return new WebViewInitializer().createWebView(webView);
    }

    @Override
    public WebViewClient initWebViewClient() {
        return new WebViewClientImpl(this);
    }

    @Override
    public WebChromeClient initWebChromeClient() {
        return new ChromeClient();
    }
}
