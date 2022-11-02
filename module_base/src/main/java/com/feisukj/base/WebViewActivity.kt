package com.feisukj.base

import android.widget.LinearLayout
import com.feisukj.base.baseclass.BaseActivity
import com.just.agentweb.AgentWeb
import kotlinx.android.synthetic.main.act_webview.*


class WebViewActivity: BaseActivity() {
    companion object{
        const val URL_KEY="url_key"
    }
    private var agentWeb:AgentWeb?=null

    override fun getLayoutId()= R.layout.act_webview

    override fun initView() {
        mImmersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        val url=intent.getStringExtra(URL_KEY)?:"http://www.baidu.com"
        agentWeb = AgentWeb.with(this)
                .setAgentWebParent(view as LinearLayout, LinearLayout.LayoutParams(-1, -1))
                .useDefaultIndicator()
                .createAgentWeb()
                .ready()
                .go(url)
    }

    override fun onBackPressed() {
        val b=agentWeb?.back()
        if (b!=true){
            super.onBackPressed()
        }
    }

}