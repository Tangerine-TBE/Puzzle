package com.weilai.jigsawpuzzle.fragment.special

import android.content.Intent
import android.net.Uri
import com.hjq.permissions.Permission
import com.steelkiwi.cropiwa.config.ConfigChangeListener
import com.steelkiwi.cropiwa.config.CropIwaSaveConfig
import com.weilai.jigsawpuzzle.BaseConstant
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.application.PuzzleApplication
import com.weilai.jigsawpuzzle.base.BaseActivity
import com.weilai.jigsawpuzzle.util.PermissionUtils
import com.weilai.jigsawpuzzle.util.ToastUtil
import com.weilai.jigsawpuzzle.weight.MySeekBar
import kotlinx.android.synthetic.main.activity_scale.*
import java.io.File

class ScaleActivity : BaseActivity(), ConfigChangeListener, MySeekBar.MySeekBarCallback {

    companion object{
        const val IMG_URI_KEY = "img_uri_key"
        const val CACHE_PATH = "cache_path"
        const val CACHE_FILE_NAME = "cache_file_name"
    }

    private lateinit var cachePath: String
    private lateinit var cacheFileName: String
    private lateinit var savePath: String
    private lateinit var saveFileName: String

    private var saveConfig: CropIwaSaveConfig.Builder? = null
    override fun getLayoutId(): Int = R.layout.activity_scale

    override fun initView() {
        mImmersionBar.statusBarColor(R.color.sel_text_main_color)
            .navigationBarColor(R.color.white)
            .statusBarDarkFont(false).init()
        val path = intent.getStringExtra(IMG_URI_KEY)?:""
        if (path == ""||path == "null"){
            ToastUtil.showCenterToast("载入图片失败")
            finish()
            return
        }
        cacheFileName = intent.getStringExtra(CACHE_FILE_NAME)?:""
        cachePath = intent.getStringExtra(CACHE_PATH)?:""
        cropView.setImageUri(Uri.fromFile(File(path)))
        cropView.configureImage().addConfigChangeListener(this)
        mySeekBar.setCallback(this)
        mySeekBar.setMaxValue(100)
        mySeekBar.setMinValue(0)

        saveConfig = if (cacheFileName!=""&&cachePath!="")
            CropIwaSaveConfig.Builder(Uri.fromFile(File(
                "${cachePath}/${cacheFileName}"))).setToGallery(false)
        else{
            savePath = BaseConstant.savePath
            saveFileName = "${System.currentTimeMillis()}.jpg"
            CropIwaSaveConfig.Builder(Uri.fromFile(File(
                "$savePath/$saveFileName"))).setToGallery(true)
        }

        saveConfig?.setQuality(100)
        saveConfig

        barBack.setOnClickListener {
            finish()
        }

        no.setOnClickListener {
            finish()
        }
        yes.setOnClickListener {
            yes.isEnabled = false
            PermissionUtils.askPermission(this,Permission.MANAGE_EXTERNAL_STORAGE){
                if (cachePath != "" && cacheFileName != ""){
                    val rootFile = File(cachePath)
                    if (!rootFile.exists())
                        rootFile.mkdirs()
                    loadingDialog.setTitleText("更改中...")
                    loadingDialog.show()
                    Thread{
                        cropView.crop(saveConfig?.build())
                        PuzzleApplication.handler.postDelayed({
                            val intent = Intent()
                            intent.putExtra(BeautyActivity.BACK_IMG_PATH_KEY,"${cachePath}/${cacheFileName}")
                            setResult(BeautyActivity.BACK,intent)
                            loadingDialog.dismiss()
                            finish()
                        },2000)
                    }.start()
                }else{
                    loadingDialog.setTitleText("保存中...")
                    loadingDialog.show()
                    val file = File(BaseConstant.savePath)
                    if (!file.exists())
                        file.mkdirs()
                    Thread{
                        cropView.crop(saveConfig?.build())
                        PuzzleApplication.handler.postDelayed({
                            loadingDialog.dismiss()
//                            setResult(HomeActivity.BACK)
                            finish()
                        },2000)
                    }.start()
                }
            }
        }
    }

    override fun onConfigChanged() {
        mySeekBar.setValue((cropView.configureImage().scale * 100).toInt())
    }

    override fun MySeekBarChange(int: Int) {
        cropView.configureImage().setScale(int / 100f).apply()
    }

}