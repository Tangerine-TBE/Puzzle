package com.weilai.jigsawpuzzle.fragment.special

import android.content.Intent
import android.graphics.BitmapFactory
import android.hardware.camera2.CameraCharacteristics
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.abc.matting.utils.camera.doOnLayout
import com.weilai.jigsawpuzzle.BaseConstant
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.Resources
import com.weilai.jigsawpuzzle.activity.main.SaveBaseActivity
import com.weilai.jigsawpuzzle.adapter.special.MagicCameraAdapter
import com.weilai.jigsawpuzzle.base.BaseActivity
import com.weilai.jigsawpuzzle.bean.FilterBean
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment
import com.weilai.jigsawpuzzle.util.BitmapUtils
import com.weilai.jigsawpuzzle.util.PermissionUtils
import com.weilai.jigsawpuzzle.util.camera.Camera2Loader
import com.weilai.jigsawpuzzle.util.camera.CameraLoader
import jp.co.cyberagent.android.gpuimage.GPUImageView
import jp.co.cyberagent.android.gpuimage.util.Rotation
import kotlinx.android.synthetic.main.activity_magic_camera.*

class MagicCameraActivity : BaseActivity(), MagicCameraAdapter.MagicCameraCallback {

    private var list: Array<FilterBean> = arrayOf()
    private var adapter: MagicCameraAdapter = MagicCameraAdapter()
    private val cameraLoader: CameraLoader by lazy {
        Camera2Loader(this)
    }

    override fun getLayoutId(): Int = R.layout.activity_magic_camera

    override fun initView() {
        mImmersionBar.statusBarColor(android.R.color.transparent).statusBarDarkFont(true).init()
        list = Resources.getCameraFilter()
        adapter.setData(list)
        adapter.setCallBack(this)
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter

        cameraLoader.setOnPreviewFrameListener { data, width, height ->
            gpuImageView.updatePreviewFrame(data, width, height)
        }
        gpuImageView.setRotation(getRotation(cameraLoader.getCameraOrientation()))
        gpuImageView.setRenderMode(GPUImageView.RENDERMODE_CONTINUOUSLY)

        initClick()
    }

    private fun getRotation(orientation: Int): Rotation {
        return when (orientation) {
            90 -> Rotation.ROTATION_90
            180 -> Rotation.ROTATION_180
            270 -> Rotation.ROTATION_270
            else -> Rotation.NORMAL
        }
    }

    private fun initClick() {
        barBack.setOnClickListener {
            finish()
        }
        shutter.setOnClickListener {
            PermissionUtils.askStorageAndCameraPermission(this) {
                val fileName = "${System.currentTimeMillis()}.jpg"
                val path = BaseConstant.savePath + "/" + fileName
                loadingDialog.setTitleText("保存中...")
                loadingDialog.show()
                gpuImageView.saveToPictures(BaseConstant.savePath, fileName) {
                    BitmapUtils.saveBitmapPhoto(BitmapFactory.decodeFile(path))
                    loadingDialog.dismiss()
                    val intent = Intent(this, SaveBaseActivity::class.java)
                    intent.putExtra("data", path)
                    intent.putExtra("type","魔法相机")
                    startActivity(intent)
                    finish()
                }
            }
        }
        transition.run {
            if (!cameraLoader.hasMultipleCamera()) {
                visibility = View.GONE
            }
            setOnClickListener {
                cameraLoader.switchCamera()
                gpuImageView.setRotation(getRotation(cameraLoader.getCameraOrientation()))
                if (cameraLoader.getCameraFacing() != CameraCharacteristics.LENS_FACING_BACK)
                    gpuImageView.scaleX = -1f
                else
                    gpuImageView.scaleX = 1f
            }
        }
    }

    override fun onResume() {
        super.onResume()
        gpuImageView.doOnLayout {
            cameraLoader.onResume(it.width, it.height)
        }
    }

    override fun onPause() {
        cameraLoader.onPause()
        super.onPause()
    }

    override fun clickFilterItem(position: Int) {
        gpuImageView.filter = list[position].filterType
        gpuImageView.requestRender()

    }
}