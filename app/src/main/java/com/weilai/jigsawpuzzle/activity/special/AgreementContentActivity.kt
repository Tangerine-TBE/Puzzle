package com.weilai.jigsawpuzzle.activity.special

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.text.style.UnderlineSpan
import android.view.View
import android.webkit.*
import androidx.appcompat.app.AppCompatActivity
import com.gyf.immersionbar.ImmersionBar
import com.weilai.jigsawpuzzle.BaseConstant
import com.weilai.jigsawpuzzle.R
import kotlinx.android.synthetic.main.activity_agreement_content_base.*

class AgreementContentActivity:AppCompatActivity() {
    companion object{
        const val FLAG="agreement_flag"
        const val FLAG_YINSI="yingsi"
        const val FLAG_FUWU="fuwu"
        const val email = "1508306421@qq.com"
        const val com = "武汉市射手科技有限公司"
    }
    private val umUrl = "https://www.umeng.com/page/policy?spm=a211eg.10560647.0.0.547034dcafEUZJ"
    private val gdtUrl = "https://imgcache.qq.com/gdt/cdn/adn/uniondoc/ylh_sdk_privacy_statement.html"
    private val ttUrl = "https://partner.oceanengine.com/privacy"
    private val weChatUrl = "https://open.weixin.qq.com/cgi-bin/frame?t=news/protocol_developer_tmpl"
    private val qqUrl = "https://wiki.open.qq.com/wiki/%E8%85%BE%E8%AE%AF%E5%BC%80%E6%94%BE%E5%B9%B3%E5%8F%B0%E5%BC%80%E5%8F%91%E8%80%85%E5%8D%8F%E8%AE%AE"

    private var count = 10

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement_content_base)
        ImmersionBar.with(this)
            .statusBarColor(android.R.color.transparent)
            .statusBarDarkFont(true)
            .navigationBarColor(R.color.white)
            .init()
        val flag=intent.getStringExtra(FLAG)
        if (flag==FLAG_FUWU){
            barTitle.text="用户协议"
            fuwu()
        }else{
            barTitle.text="隐私政策"
            yinSi()
        }
        barBack.setOnClickListener {
            finish()
        }
//        barTitle.setOnClickListener {
//            count--
//            if (count == 0){
//                val cm: ClipboardManager = this.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
//                cm.setPrimaryClip(ClipData.newPlainText("text", getFuwu().toString())) //text也可以是"null"
//
//                if (cm.hasPrimaryClip()) {
//                    cm.primaryClip?.getItemAt(0)?.text
//                }
//                ToastUtil.showCenterToast("复制成功")
//            }
//        }
    }
    private fun fuwu(){
        agreementContent.text=getString(R.string.userAgreement,packageManager.getApplicationLabel(applicationInfo),com, email)
    }
    private fun yinSi(){
//        agreementContent.text=getString(R.string.privacyStatement,packageManager.getApplicationLabel(applicationInfo),com, email,getFuwu())
//        agreementContent.text = getFuwu()
//        agreementContent.movementMethod = LinkMovementMethod.getInstance()

        webView.visibility = View.VISIBLE
        initWebView()

    }

    private fun initWebView() {
        webView.webChromeClient = WebChromeClient()
        val webSettings = webView.settings
        webSettings.javaScriptEnabled = true
//        webSettings.useWideViewPort = true
//        webSettings.loadWithOverviewMode = true

        if (Build.VERSION.SDK_INT>= Build.VERSION_CODES.LOLLIPOP){
            webSettings.mixedContentMode = WebSettings.MIXED_CONTENT_ALWAYS_ALLOW
        }
        webSettings.setSupportZoom(true)
        webSettings.builtInZoomControls = true
        webSettings.displayZoomControls = false

        webSettings.domStorageEnabled = true//不加这句有些h5登陆窗口出不来 H5页面使用DOM storage API导致的页面加载问题
        webSettings.cacheMode = WebSettings.LOAD_NO_CACHE
        webSettings.allowFileAccess = true
        webSettings.javaScriptCanOpenWindowsAutomatically = true
        webSettings.loadsImagesAutomatically = true
        webSettings.defaultTextEncodingName = "utf-8"
        webView.webViewClient = object : WebViewClient(){
            override fun shouldOverrideUrlLoading(
                view: WebView?,
                request: WebResourceRequest?
            ): Boolean {
                return false
            }
//            override fun onPageFinished(view: WebView?, url: String?) {
//                super.onPageFinished(view, url)
//                webView.loadUrl("javascript:androidSetAppName('${packageManager.getApplicationLabel(applicationInfo)}')")
//                webView.loadUrl("javascript:androidSetCompanyName('${com}')")
//                webView.loadUrl("javascript:androidSetEmail('${email}')")
//            }
        }
        //加载网络资源
        webView.loadUrl("https://catapi.aisou.club/android/privacy_policy.html?app_name=${packageManager.getApplicationLabel(applicationInfo)}&pack_name=${BaseConstant.packageName}")
    }

    override fun onResume() {
        super.onResume()
        webView.onResume()
    }

    override fun onPause() {
        super.onPause()
        webView.onPause()
    }

    override fun onBackPressed() {
        if (webView.canGoBack()){
            webView.goBack()
        }else{
            finish()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        webView.destroy()
    }
}