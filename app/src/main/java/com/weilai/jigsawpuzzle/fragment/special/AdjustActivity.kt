package com.weilai.jigsawpuzzle.fragment.special

import android.content.Intent
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.permissions.Permission
import com.weilai.jigsawpuzzle.BaseConstant
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.Resources
import com.weilai.jigsawpuzzle.activity.main.SaveBaseActivity
import com.weilai.jigsawpuzzle.adapter.special.AdjustAdapter
import com.weilai.jigsawpuzzle.base.BaseActivity
import com.weilai.jigsawpuzzle.bean.AdjustBean
import com.weilai.jigsawpuzzle.util.BitmapUtils
import com.weilai.jigsawpuzzle.util.GPUImageFilterTools
import com.weilai.jigsawpuzzle.util.PermissionUtils
import com.weilai.jigsawpuzzle.util.ToastUtil
import com.weilai.jigsawpuzzle.weight.MySeekBar
import jp.co.cyberagent.android.gpuimage.GPUImage
import jp.co.cyberagent.android.gpuimage.filter.*
import kotlinx.android.synthetic.main.activity_adjust.*
import java.io.File

class AdjustActivity : BaseActivity(), AdjustAdapter.AdjustAdapterCallback,
    MySeekBar.MySeekBarCallback {
    companion object{
        const val IMG_PATH = "img_page"
        const val CACHE_PATH = "cache_path"
        const val CACHE_FILE_NAME = "cache_file_name"
    }

    private lateinit var cachePath: String
    private lateinit var cacheFileName: String

    private val adapter = AdjustAdapter()
    private var uri = ""
    private var mList: Array<AdjustBean> = arrayOf()
    private var filterAdjuster: GPUImageFilterTools.FilterAdjuster? = null
    private lateinit var filterType: GPUImageFilterTools.FilterType
    private lateinit var filterGround: GPUImageFilterGroup
    /**亮度*/
    private lateinit var brightnessFilter: GPUImageBrightnessFilter
    private var brightness = 0
    /**对比度*/
    private lateinit var contrastFilter: GPUImageContrastFilter
    private var contrast = 0
    /**色调*/
    private lateinit var hueFilter: GPUImageHueFilter
    private var hue = 0
    /**锐度*/
    private lateinit var sharpenFilter: GPUImageSharpenFilter
    private var sharpen = 0
    /**曝光*/
    private lateinit var exposureFilter: GPUImageExposureFilter
    private var exposure = 0
    /**白平衡*/
    private lateinit var whiteBalanceFilter: GPUImageWhiteBalanceFilter
    private var whiteBalance = 0

    /**饱和度*/
    private lateinit var saturationFilter: GPUImageSaturationFilter
    private var saturation = 0
    /**雾化*/
    private lateinit var gaussianBlurFilter: GPUImageGaussianBlurFilter
    private var gaussianBlur = -50

    override fun getLayoutId(): Int = R.layout.activity_adjust

    override fun initView() {
        initTopTheme()
        mList = Resources.getAdjust()
        uri = intent.getStringExtra(IMG_PATH)?:""
        if (uri == ""){
            ToastUtil.showCenterToast("加载图片失败，请重试")
            finish()
        }
        cacheFileName = intent.getStringExtra(CACHE_FILE_NAME)?:""
        cachePath = intent.getStringExtra(CACHE_PATH)?:""

        initFilter()

        seekBar.setMaxValue(50)
        seekBar.setMinValue(-50)
        seekBar.setCallback(this)
        seekBar.setValue(brightness)

        filterType = GPUImageFilterTools.FilterType.BRIGHTNESS
        filterAdjuster = GPUImageFilterTools.FilterAdjuster(brightnessFilter)

        adapter.setData(mList)
        adapter.setCallback(this)
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycler.adapter = adapter

        gpuImageView.setImage(Uri.fromFile(File(uri)))
        gpuImageView.setScaleType(GPUImage.ScaleType.CENTER_INSIDE)
        gpuImageView.setBackgroundColor(255f,255f,255f)
        gpuImageView.filter = filterGround

        barBack.setOnClickListener {
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
                        BitmapUtils.saveImageToGallery(gpuImageView.gpuImage.bitmapWithFilterApplied,
                            BaseConstant.cachePath,cacheFileName,false)
                        runOnUiThread {
                            loadingDialog.dismiss()
                            val intent = Intent()
                            intent.putExtra(BeautyActivity.BACK_IMG_PATH_KEY,"${cachePath}/${cacheFileName}")
                            setResult(BeautyActivity.BACK,intent)
                            finish()
                        }
                    }.start()
                }else{
                    val fileName = "${System.currentTimeMillis()}.jpg"
                    val path = BaseConstant.savePath+"/"+fileName
                    loadingDialog.setTitleText("保存中...")
                    loadingDialog.show()
                    Thread{
                        BitmapUtils.saveImageToGallery(gpuImageView.gpuImage.bitmapWithFilterApplied,BaseConstant.savePath,fileName,true)
                        runOnUiThread {
                            loadingDialog.dismiss()
                            val intent = Intent(this, SaveBaseActivity::class.java)
                            intent.putExtra("data", path)
                            startActivity(intent)
                            finish()
                        }
                    }.start()
                }
            }
        }

        no.setOnClickListener {
            finish()
        }
    }

    private fun initFilter(){
        brightnessFilter = GPUImageBrightnessFilter(0.0f)
        contrastFilter = GPUImageContrastFilter(1.0f)
        saturationFilter = GPUImageSaturationFilter(1.0f)
        sharpenFilter = GPUImageSharpenFilter(0.0f)
        hueFilter = GPUImageHueFilter(0.0f)
        exposureFilter = GPUImageExposureFilter(0.0f)
        whiteBalanceFilter = GPUImageWhiteBalanceFilter(5000.0f, 0.0f)
//        highlightShadowFilter = GPUImageHighlightShadowFilter(0.0f, 1.0f)
        gaussianBlurFilter = GPUImageGaussianBlurFilter(0.0f)

        filterGround = GPUImageFilterGroup(
            listOf(
                brightnessFilter,
                contrastFilter,
                saturationFilter,
                sharpenFilter,
                hueFilter,
                exposureFilter,
                whiteBalanceFilter
//                gaussianBlurFilter
            )
        )
    }

    override fun clickAdjustItem(type: String) {
        when(type){
            "亮度" ->{
                seekBar.setMaxValue(50)
                seekBar.setMinValue(-50)
                seekBar.setValue(brightness)
                filterType = GPUImageFilterTools.FilterType.BRIGHTNESS
                filterAdjuster = GPUImageFilterTools.FilterAdjuster(brightnessFilter)
            }
            "对比度" ->{
                seekBar.setMaxValue(50)
                seekBar.setMinValue(-50)
                seekBar.setValue(contrast)
                filterType = GPUImageFilterTools.FilterType.CONTRAST
                filterAdjuster = GPUImageFilterTools.FilterAdjuster(contrastFilter)
            }
            "色调" ->{
                seekBar.setMaxValue(100)
                seekBar.setMinValue(0)
                seekBar.setValue(hue)
                filterType = GPUImageFilterTools.FilterType.HUE
                filterAdjuster = GPUImageFilterTools.FilterAdjuster(hueFilter)
            }
            "锐度" ->{
                seekBar.setMaxValue(50)
                seekBar.setMinValue(-50)
                seekBar.setValue(sharpen)
                filterType = GPUImageFilterTools.FilterType.SHARPEN
                filterAdjuster = GPUImageFilterTools.FilterAdjuster(sharpenFilter)
            }
            "曝光" ->{
                seekBar.setMaxValue(50)
                seekBar.setMinValue(-50)
                seekBar.setValue(exposure)
                filterType = GPUImageFilterTools.FilterType.EXPOSURE
                filterAdjuster = GPUImageFilterTools.FilterAdjuster(exposureFilter)
            }
            "白平衡" ->{
                seekBar.setMaxValue(50)
                seekBar.setMinValue(-50)
                seekBar.setValue(whiteBalance)
                filterType = GPUImageFilterTools.FilterType.WHITE_BALANCE
                filterAdjuster = GPUImageFilterTools.FilterAdjuster(whiteBalanceFilter)
            }
            "阴影" ->{ }
            "高光" ->{ }
            "饱和度" ->{
                seekBar.setMaxValue(50)
                seekBar.setMinValue(-50)
                seekBar.setValue(saturation)
                filterType = GPUImageFilterTools.FilterType.SATURATION
                filterAdjuster = GPUImageFilterTools.FilterAdjuster(saturationFilter)
            }
            "雾化" ->{
//                ToastUtil.showCenterToast("雾化")
//                seekBar.setValue(gaussianBlur)
//                filterType = GPUImageFilterTools.FilterType.GAUSSIAN_BLUR
//                filterAdjuster = GPUImageFilterTools.FilterAdjuster(gaussianBlurFilter)
            }
        }
    }

    override fun MySeekBarChange(int: Int) {
        filterAdjuster?.adjust(int)
        gpuImageView.requestRender()
        when(filterType){
            GPUImageFilterTools.FilterType.BRIGHTNESS -> {
                brightness = int
            }
            GPUImageFilterTools.FilterType.CONTRAST -> {
                contrast = int
            }
            GPUImageFilterTools.FilterType.HUE -> {
                hue = int
            }
            GPUImageFilterTools.FilterType.SHARPEN -> {
                sharpen = int
            }
            GPUImageFilterTools.FilterType.EXPOSURE -> {
                exposure = int
            }
            GPUImageFilterTools.FilterType.WHITE_BALANCE -> {
                whiteBalance = int
            }
            GPUImageFilterTools.FilterType.SATURATION -> {
                saturation = int
            }
            GPUImageFilterTools.FilterType.GAUSSIAN_BLUR -> {
                gaussianBlur = int
            }
            else -> {}
        }
    }

}