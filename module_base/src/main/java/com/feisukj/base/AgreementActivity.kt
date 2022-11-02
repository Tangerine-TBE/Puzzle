package com.feisukj.base

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.SpannableString
import android.text.SpannableStringBuilder
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.text.style.ForegroundColorSpan
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.feisukj.base.util.LogUtils.e
import com.feisukj.base.util.PackageUtils
import com.feisukj.base.util.SPUtil
import com.gyf.immersionbar.ImmersionBar
import kotlinx.android.synthetic.main.activity_agreement_base.*

class AgreementActivity :AppCompatActivity(){
    companion object{
        val key="agreement"
    }
    private var isToAct=false
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_agreement_base)
        ImmersionBar.with(this)
                .statusBarColor("#00000000")
                .statusBarDarkFont(true)
                .init()
        open.setOnClickListener {
            SPUtil.getInstance().putBoolean(key,true)
            if (!isToAct) {
//                    startActivity(Intent(this, ActivityEntrance.HomeActivity.cls))
                finish()
            }
            isToAct=true
        }
        title_text.text = "欢迎使用${PackageUtils.getAppName(this)}"
        body(tip)
    }

    private fun body(view: TextView) {
        val stringBuilder = SpannableStringBuilder()
        val fuwu = SpannableString("《用户服务协议》")
        val yinsi = SpannableString("《隐私政策》")
        fuwu.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                e("点击了服务")
                val intent = Intent(this@AgreementActivity, AgreementContentActivity::class.java)
                intent.putExtra(AgreementContentActivity.FLAG, AgreementContentActivity.FLAG_FUWU)
                startActivity(intent)
            }
        }, 0, fuwu.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        yinsi.setSpan(object : ClickableSpan() {
            override fun onClick(view: View) {
                e("点击了隐私")
                val intent = Intent(this@AgreementActivity, AgreementContentActivity::class.java)
                intent.putExtra(AgreementContentActivity.FLAG, AgreementContentActivity.FLAG_YINSI)
                startActivity(intent)
            }
        }, 0, yinsi.length, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
        fuwu.setSpan(
            ForegroundColorSpan(Color.parseColor("#00D6E6")),
            0,
            fuwu.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        yinsi.setSpan(
            ForegroundColorSpan(Color.parseColor("#00D6E6")),
            0,
            yinsi.length,
            Spanned.SPAN_EXCLUSIVE_EXCLUSIVE
        )
        stringBuilder
            .append("请您在使用前仔细阅读")
            .append(fuwu)
            .append("和")
            .append(yinsi)
            .append(",开始使用代表您以阅读并同意。")
        view.text = stringBuilder
        view.movementMethod = LinkMovementMethod.getInstance() //必须设置才能响应点击事件
    }
}