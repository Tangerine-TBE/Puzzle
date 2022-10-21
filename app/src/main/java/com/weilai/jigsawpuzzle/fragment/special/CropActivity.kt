package com.weilai.jigsawpuzzle.fragment.special

import android.content.Intent
import android.graphics.BitmapFactory
import android.graphics.Color
import com.hjq.permissions.Permission
import com.theartofdev.edmodo.cropper.CropImageView
import com.weilai.jigsawpuzzle.BaseConstant
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.base.BaseActivity
import com.weilai.jigsawpuzzle.util.BitmapUtils
import com.weilai.jigsawpuzzle.util.DeviceUtils
import com.weilai.jigsawpuzzle.util.PermissionUtils
import com.weilai.jigsawpuzzle.util.ToastUtil
import kotlinx.android.synthetic.main.activity_crop.*
import java.io.File

class CropActivity : BaseActivity(), CropImageView.OnCropImageCompleteListener {

    companion object{
        const val IMG_PATH = "img_page"
        const val CACHE_PATH = "cache_path"
        const val CACHE_FILE_NAME = "cache_file_name"
    }

    private lateinit var cachePath: String
    private lateinit var cacheFileName: String

    override fun getLayoutId(): Int = R.layout.activity_crop

    override fun initView() {
        mImmersionBar.statusBarColor(R.color.sel_text_main_color)
            .statusBarDarkFont(false)
            .navigationBarColor(R.color.white).init()
        val filePath = intent.getStringExtra(IMG_PATH)?:""
        if (filePath == ""){
            ToastUtil.showCenterToast("载入图片出错")
            return
        }
        cacheFileName = intent.getStringExtra(CACHE_FILE_NAME)?:""
        cachePath = intent.getStringExtra(CACHE_PATH)?:""
        val bitmap = BitmapFactory.decodeFile(filePath)
        crop.setImageBitmap(bitmap)
        crop.setOnCropImageCompleteListener(this)
        scale_free.setOnClickListener {
            crop.setFixedAspectRatio(false)
            iv_free.setImageResource(R.drawable.ic_crop_scale_free_y)
            iv_self.setImageResource(R.drawable.ic_crop_scale_self_n)
            iv_1_1.setImageResource(R.drawable.ic_crop_scale_1_1_n)
            iv_2_3.setImageResource(R.drawable.ic_crop_scale_2_3_n)
            iv_3_4.setImageResource(R.drawable.ic_crop_scale_3_4_n)
            iv_5_7.setImageResource(R.drawable.ic_crop_scale_5_7_n)
            iv_9_16.setImageResource(R.drawable.ic_crop_scale_9_16_n)
        }
        scale_self.setOnClickListener {
            crop.setAspectRatio(DeviceUtils.getScreenWidth(this), DeviceUtils.getScreenHeight(this))
            crop.setFixedAspectRatio(true)
            iv_free.setImageResource(R.drawable.ic_crop_scale_free_n)
            iv_self.setImageResource(R.drawable.ic_crop_scale_self_y)
            iv_1_1.setImageResource(R.drawable.ic_crop_scale_1_1_n)
            iv_2_3.setImageResource(R.drawable.ic_crop_scale_2_3_n)
            iv_3_4.setImageResource(R.drawable.ic_crop_scale_3_4_n)
            iv_5_7.setImageResource(R.drawable.ic_crop_scale_5_7_n)
            iv_9_16.setImageResource(R.drawable.ic_crop_scale_9_16_n)
        }
        scale_1_1.setOnClickListener {
            crop.setAspectRatio(1, 1)
            crop.setFixedAspectRatio(true)
            iv_free.setImageResource(R.drawable.ic_crop_scale_free_n)
            iv_self.setImageResource(R.drawable.ic_crop_scale_self_n)
            iv_1_1.setImageResource(R.drawable.ic_crop_scale_1_1_y)
            iv_2_3.setImageResource(R.drawable.ic_crop_scale_2_3_n)
            iv_3_4.setImageResource(R.drawable.ic_crop_scale_3_4_n)
            iv_5_7.setImageResource(R.drawable.ic_crop_scale_5_7_n)
            iv_9_16.setImageResource(R.drawable.ic_crop_scale_9_16_n)
        }
        scale_2_3.setOnClickListener {
            crop.setAspectRatio(2, 3)
            crop.setFixedAspectRatio(true)
            iv_free.setImageResource(R.drawable.ic_crop_scale_free_n)
            iv_self.setImageResource(R.drawable.ic_crop_scale_self_n)
            iv_1_1.setImageResource(R.drawable.ic_crop_scale_1_1_n)
            iv_2_3.setImageResource(R.drawable.ic_crop_scale_2_3_y)
            iv_3_4.setImageResource(R.drawable.ic_crop_scale_3_4_n)
            iv_5_7.setImageResource(R.drawable.ic_crop_scale_5_7_n)
            iv_9_16.setImageResource(R.drawable.ic_crop_scale_9_16_n)
        }
        scale_3_4.setOnClickListener {
            crop.setAspectRatio(3, 4)
            crop.setFixedAspectRatio(true)
            iv_free.setImageResource(R.drawable.ic_crop_scale_free_n)
            iv_self.setImageResource(R.drawable.ic_crop_scale_self_n)
            iv_1_1.setImageResource(R.drawable.ic_crop_scale_1_1_n)
            iv_2_3.setImageResource(R.drawable.ic_crop_scale_2_3_n)
            iv_3_4.setImageResource(R.drawable.ic_crop_scale_3_4_y)
            iv_5_7.setImageResource(R.drawable.ic_crop_scale_5_7_n)
            iv_9_16.setImageResource(R.drawable.ic_crop_scale_9_16_n)
        }
        scale_5_7.setOnClickListener {
            crop.setAspectRatio(5, 7)
            crop.setFixedAspectRatio(true)
            iv_free.setImageResource(R.drawable.ic_crop_scale_free_n)
            iv_self.setImageResource(R.drawable.ic_crop_scale_self_n)
            iv_1_1.setImageResource(R.drawable.ic_crop_scale_1_1_n)
            iv_2_3.setImageResource(R.drawable.ic_crop_scale_2_3_n)
            iv_3_4.setImageResource(R.drawable.ic_crop_scale_3_4_n)
            iv_5_7.setImageResource(R.drawable.ic_crop_scale_5_7_y)
            iv_9_16.setImageResource(R.drawable.ic_crop_scale_9_16_n)
        }
        scale_9_16.setOnClickListener {
            crop.setAspectRatio(9, 16)
            crop.setFixedAspectRatio(true)
            iv_free.setImageResource(R.drawable.ic_crop_scale_free_n)
            iv_self.setImageResource(R.drawable.ic_crop_scale_self_n)
            iv_1_1.setImageResource(R.drawable.ic_crop_scale_1_1_n)
            iv_2_3.setImageResource(R.drawable.ic_crop_scale_2_3_n)
            iv_3_4.setImageResource(R.drawable.ic_crop_scale_3_4_n)
            iv_5_7.setImageResource(R.drawable.ic_crop_scale_5_7_n)
            iv_9_16.setImageResource(R.drawable.ic_crop_scale_9_16_y)
        }
        counter_clockwise.setOnClickListener {
            crop.rotatedDegrees = crop.rotatedDegrees - 90
        }
        clockwise_sense.setOnClickListener {
            crop.rotatedDegrees = crop.rotatedDegrees + 90
        }
        rotate_reset.setOnClickListener {
            crop.rotatedDegrees = 0
        }

        ll_crop.setOnClickListener {
            tv_crop.setTextColor(Color.parseColor("#5B92FF"))
            tv_rotate.setTextColor(Color.parseColor("#16181A"))
            visible(view_crop, crop_root_view)
            invisible(rotate_root_view)
        }

        ll_rotate.setOnClickListener {
            tv_rotate.setTextColor(Color.parseColor("#5B92FF"))
            tv_crop.setTextColor(Color.parseColor("#16181A"))
            invisible(view_crop, crop_root_view)
            visible(rotate_root_view)
        }

        barBack.setOnClickListener {
            finish()
        }

        no.setOnClickListener {
            finish()
        }
        yes.setOnClickListener {
            yes.isEnabled = false
            PermissionUtils.askPermission(this, Permission.MANAGE_EXTERNAL_STORAGE){
                crop.getCroppedImageAsync()
            }
        }
    }

    override fun onCropImageComplete(view: CropImageView?, result: CropImageView.CropResult?) {
        if (cachePath != "" && cacheFileName != ""){
            loadingDialog.show()
            val rootFile = File(cachePath)
            if (!rootFile.exists())
                rootFile.mkdirs()
            Thread{
                BitmapUtils.saveImageToGallery(result?.bitmap,cachePath,cacheFileName,false)
                runOnUiThread {
                    val intent = Intent()
                    intent.putExtra(BeautyActivity.BACK_IMG_PATH_KEY,"${cachePath}/${cacheFileName}")
                    setResult(BeautyActivity.BACK,intent)
                    loadingDialog.dismiss()
                    finish()
                }
            }.start()
        }else{
            loadingDialog.show()
            val rootFile = File(BaseConstant.savePath)
            if (!rootFile.exists())
                rootFile.mkdirs()
            Thread{
                val savePath=BitmapUtils.saveImageToGallery(result?.bitmap)
                runOnUiThread {
                    loadingDialog.dismiss()
//                    setResult(HomeActivity.BACK,Intent().apply {
//                        putExtra(IDPhotoActivity.IDPhotoSavePath,savePath)
//                    })
                    finish()
                }
            }.start()
        }
    }
}