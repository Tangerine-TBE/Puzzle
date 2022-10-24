package com.weilai.jigsawpuzzle.fragment.special

import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.hardware.camera2.CameraCharacteristics
import com.abc.matting.ui.activity.EffectEndActivity
import com.abc.matting.ui.dialog.SelectAgeDialog
import com.abc.matting.ui.dialog.SelectSexDialog
import com.abc.matting.utils.camera.doOnLayout
import com.luck.picture.lib.basic.PictureSelector
import com.luck.picture.lib.config.SelectMimeType.ofImage
import com.luck.picture.lib.config.SelectModeConfig.SINGLE
import com.weilai.jigsawpuzzle.BaseConstant
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.activity.main.SaveBaseActivity
import com.weilai.jigsawpuzzle.application.PuzzleApplication
import com.weilai.jigsawpuzzle.base.BaseActivity
import com.weilai.jigsawpuzzle.util.*
import com.weilai.jigsawpuzzle.util.camera.Camera2Loader
import com.weilai.jigsawpuzzle.util.camera.CameraLoader
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.util.Rotation
import kotlinx.android.synthetic.main.activity_old2.*
import org.json.JSONObject

class OldActivity2 : BaseActivity(), SelectAgeDialog.SelectAgeCallback,
    SelectSexDialog.SelectSexCallback {

    companion object {

        const val typeKey = "typeKey"
        const val TYPE_OLD = "old"
        const val TYPE_YOUNG = "young"
        const val TYPE_COMIC = "comic"
        const val TYPE_SEX = "sex"

        const val ALBUM_CODE = 1110
    }

    private var bitmap: Bitmap? = null
    private var needCamera = true
    private var type = ""
    private lateinit var ageDialog: SelectAgeDialog
    private lateinit var sexDialog: SelectSexDialog
    private val cameraLoader: CameraLoader by lazy {
        Camera2Loader(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_old2

    override fun initView() {
        mImmersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()

        type = intent.getStringExtra(typeKey) ?: TYPE_OLD
        barTitle.text = when (type) {
            TYPE_OLD -> "变老相机"
            TYPE_YOUNG -> "童颜相机"
            TYPE_SEX -> "性别转换"
            TYPE_COMIC -> "一键漫画脸"
            else -> ""
        }
        ageDialog = SelectAgeDialog(this)
        ageDialog.setCallback(this)
        ageDialog.setType(type)

        sexDialog = SelectSexDialog(this)
        sexDialog.setCallback(this)

        cameraLoader.setOnPreviewFrameListener { data, width, height ->
            gpuImageView.updatePreviewFrame(data, width, height)
        }
        gpuImageView.setRotation(getRotation(cameraLoader.getCameraOrientation()))
        gpuImageView.setRenderMode(GPUImageView.RENDERMODE_CONTINUOUSLY)
        initClick()
    }

    private fun initClick() {

        barBack.setOnClickListener {
            finish()
        }

        shutter.setOnClickListener {
            bitmap = gpuImageView.capture()
            cameraLoader.onPause()
            img.setImageBitmap(bitmap)
            invisible(shutter, album, transition)
            visible(img, over, remake)
        }

        remake.setOnClickListener {
            visible(shutter, album, transition)
            invisible(img, over, remake)
            gpuImageView.doOnLayout {
                cameraLoader.onResume(it.width, it.height)
            }
        }

        transition.setOnClickListener {
            cameraLoader.switchCamera()
            gpuImageView.setRotation(getRotation(cameraLoader.getCameraOrientation()))
            if (cameraLoader.getCameraFacing() != CameraCharacteristics.LENS_FACING_BACK)
                gpuImageView.scaleX = -1f
            else
                gpuImageView.scaleX = 1f

        }

        album.setOnClickListener {
            needCamera = false
            PermissionUtils.askStorageAndCameraPermission(this) {
                cameraLoader.onPause()
                PictureSelector.create(this)
                    .openGallery(ofImage())
                    .setImageEngine(GlideEngine.createGlideEngine())
                    .isPreviewImage(true)
//                    .freeStyleCropEnabled(true)
                    .setSelectionMode(SINGLE)
                    .forResult(ALBUM_CODE)
            }
        }

        over.setOnClickListener {
            when (type) {
                TYPE_OLD, TYPE_YOUNG -> ageDialog.show()
                TYPE_COMIC -> comic()
                TYPE_SEX -> sexDialog.show()
            }
        }
    }

    private fun getRotation(orientation: Int): Rotation {
        return when (orientation) {
            90 -> Rotation.ROTATION_90
            180 -> Rotation.ROTATION_180
            270 -> Rotation.ROTATION_270
            else -> Rotation.NORMAL
        }
    }

    override fun onResume() {
        super.onResume()
        if (needCamera) {
            gpuImageView.doOnLayout {
                try {
                    PuzzleApplication.handler.postDelayed({
                        cameraLoader.onResume(it.width, it.height)
                    }, 500)
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
    }

    override fun onPause() {
        cameraLoader.onPause()
        super.onPause()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            ALBUM_CODE -> {
                val path = Utils.getReal(PictureSelector.obtainSelectorList(data))
                if (path == "") {
                    visible(shutter, album, transition)
                    invisible(img, over, remake)
                    needCamera = true
                    gpuImageView.doOnLayout {
                        cameraLoader.onResume(it.width, it.height)
                    }
                    return
                }
                bitmap = BitmapFactory.decodeFile(path)
                invisible(shutter, album, transition)
                visible(img, over, remake)
                img.setImageBitmap(bitmap)
            }
        }
    }

    private fun saveBitmap(url: String, fileName: String, callBack: DownloadUtils.RequestCallback) {
        loadingDialog.show()
        DownloadUtils.downloadFile(
            url,
            fileName,
            callBack
        )
    }

    override fun startToAge(age: Int) {
        loadingDialog.show()
        Thread {
            var url = ""
            try {
                val response = EffectUtils.toOld(age.toLong(), Base64Utils.bitmapToBase64(bitmap))
                val obj = JSONObject(response)
                url = obj.getString("ResultUrl")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            runOnUiThread {
                loadingDialog.dismiss()
                if (url.isEmpty()) {

                    return@runOnUiThread
                }
                val intent = Intent(this, SaveBaseActivity::class.java)
                val fileName = "${BaseConstant.savePath}/${System.currentTimeMillis()}.jpg";
                saveBitmap(url, fileName, object : DownloadUtils.RequestCallback {
                    override fun onSuccess(response: String?) {
                        runOnUiThread {
                            val bitmap = BitmapFactory.decodeFile(fileName)
                            if (bitmap != null) {
                                val path =  FileUtil.saveScreenShot(bitmap, "${System.currentTimeMillis()}")
                                loadingDialog.dismiss()
                                val title = if (type == TYPE_OLD) "变老相机" else "童颜相机"
                                intent.putExtra("type", title)
                                intent.putExtra("data", path)
                                startActivity(intent)
                                finish()
                                ToastUtil.showCenterToast("保存成功")
                            }
                        }
                    }

                    override fun onFailure(msg: String?, e: java.lang.Exception?) {
                        runOnUiThread {
                            loadingDialog.dismiss()
                            ToastUtil.showCenterToast("保存失败")
                        }
                    }

                })

            }
        }.start()
    }

    private fun comic() {
        loadingDialog.show()
        Thread {
            var url = ""
            try {
                val response = EffectUtilss.toComic(Base64Utils.bitmapToBase64(bitmap))
                val obj = JSONObject(response)
                url = obj.getString("ResultUrl")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            runOnUiThread {
                loadingDialog.dismiss()
                if (url.isEmpty()) {
                    return@runOnUiThread
                }
                val fileName = "${BaseConstant.savePath}/${System.currentTimeMillis()}.jpg";
                val intent = Intent(this, SaveBaseActivity::class.java)
                saveBitmap(url, fileName, object : DownloadUtils.RequestCallback {
                    override fun onSuccess(response: String?) {
                        runOnUiThread {
                            val bitmap = BitmapFactory.decodeFile(fileName)
                            if (bitmap != null) {
                               val path =  FileUtil.saveScreenShot(bitmap, "${System.currentTimeMillis()}")
                                loadingDialog.dismiss()
                                intent.putExtra("type", "一键漫画脸")
                                intent.putExtra("data", path)
                                startActivity(intent)
                                finish()
                                ToastUtil.showCenterToast("保存成功")
                            }
                        }
                    }

                    override fun onFailure(msg: String?, e: java.lang.Exception?) {
                        runOnUiThread {
                            loadingDialog.dismiss()
                            ToastUtil.showCenterToast("保存失败")
                        }
                    }

                })
            }
        }.start()
    }

    override fun selectSex(type: Long) {
        loadingDialog.show()
        Thread {
            var url = ""
            try {
                val response = EffectUtilss.toSex(type, Base64Utils.bitmapToBase64(bitmap))
                val obj = JSONObject(response)
                url = obj.getString("ResultUrl")
            } catch (e: Exception) {
                e.printStackTrace()
            }
            runOnUiThread {
                loadingDialog.dismiss()
                if (url.isEmpty()) {
                    ToastUtil.showCenterToast("转换出错")
                    return@runOnUiThread
                }
                val intent = Intent(this, SaveBaseActivity::class.java)
                val fileName = "${BaseConstant.savePath}/${System.currentTimeMillis()}.jpg";
                saveBitmap(url, fileName, object : DownloadUtils.RequestCallback {
                    override fun onSuccess(response: String?) {
                        runOnUiThread {
                            val bitmap = BitmapFactory.decodeFile(fileName)
                            if (bitmap != null) {
                                val path =  FileUtil.saveScreenShot(bitmap, "${System.currentTimeMillis()}")
                                loadingDialog.dismiss()
                                intent.putExtra("type", "性别转换")
                                intent.putExtra("data", path)
                                startActivity(intent)
                                finish()
                                ToastUtil.showCenterToast("保存成功")
                            }
                        }
                    }

                    override fun onFailure(msg: String?, e: java.lang.Exception?) {
                        runOnUiThread {
                            loadingDialog.dismiss()
                            ToastUtil.showCenterToast("保存失败")
                        }
                    }

                })
            }
        }.start()
    }
}