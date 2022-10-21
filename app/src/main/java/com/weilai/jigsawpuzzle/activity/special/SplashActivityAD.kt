package com.weilai.jigsawpuzzle.activity.special

import android.os.Bundle
import android.view.KeyEvent
import androidx.appcompat.app.AppCompatActivity
import com.feisukj.base.bean.ad.ADConstants
import com.gyf.immersionbar.BarHide
import com.gyf.immersionbar.ImmersionBar
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.application.PuzzleApplication
import kotlinx.android.synthetic.main.activity_splash.*

/**
 * Author : Gupingping
 * Date : 2018/10/25
 * QQ : 464955343
 */
class SplashActivityAD : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTopTheme()
        //this.window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //this.requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_splash_ad)

        PuzzleApplication.isFromStart=true


    }


    override fun onKeyDown(keyCode: Int, event: KeyEvent): Boolean {
        return if (keyCode == KeyEvent.KEYCODE_BACK || keyCode == KeyEvent.KEYCODE_HOME) {
            true
        } else super.onKeyDown(keyCode, event)
    }

    override fun onDestroy() {
        super.onDestroy()
        PuzzleApplication.isFromStart=false
    }

    private fun initTopTheme() {
        ImmersionBar.with(this)
            .statusBarColor(android.R.color.transparent)
            .statusBarDarkFont(true)
            .navigationBarColor(R.color.white)
            .hideBar(BarHide.FLAG_HIDE_BAR)
            .init()
    }
}
