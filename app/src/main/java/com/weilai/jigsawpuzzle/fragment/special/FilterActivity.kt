package com.weilai.jigsawpuzzle.fragment.special

import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.recyclerview.widget.LinearLayoutManager
import com.hjq.permissions.Permission
import com.luck.picture.lib.config.PictureMimeType
import com.weilai.jigsawpuzzle.BaseConstant
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.Resources
import com.weilai.jigsawpuzzle.activity.main.SaveBaseActivity
import com.weilai.jigsawpuzzle.adapter.special.FilterAdapter
import com.weilai.jigsawpuzzle.base.BaseActivity
import com.weilai.jigsawpuzzle.bean.FilterBean
import com.weilai.jigsawpuzzle.util.BitmapUtils
import com.weilai.jigsawpuzzle.util.PermissionUtils
import com.weilai.jigsawpuzzle.util.ToastUtil
import jp.co.cyberagent.android.gpuimage.GPUImage
import kotlinx.android.synthetic.main.activity_filter.*
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream

class FilterActivity : BaseActivity(), FilterAdapter.FilterCallback {
    companion object {
        const val IMG_PATH = "img_page"
        const val CACHE_PATH = "cache_path"
        const val CACHE_FILE_NAME = "cache_file_name"
    }

    private lateinit var cachePath: String
    private lateinit var cacheFileName: String

    private var uri = ""
    private var mList: Array<FilterBean> = arrayOf()
    private var adapter: FilterAdapter = FilterAdapter()

    override fun getLayoutId(): Int = R.layout.activity_filter

    override fun initView() {
        mImmersionBar.statusBarColor(R.color.sel_text_main_color)
            .statusBarDarkFont(false)
            .navigationBarColor(R.color.white).init()

        cacheFileName = intent.getStringExtra(CACHE_FILE_NAME) ?: ""
        cachePath = intent.getStringExtra(CACHE_PATH) ?: ""

        mList = Resources.getCameraFilter()
        adapter.setData(mList)
        adapter.setCallBack(this)
        adapter.setNeedVip(cachePath == "" && cacheFileName == "")
        recycler.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        recycler.adapter = adapter

        uri = intent.getStringExtra(IMG_PATH) ?: ""
        if (uri == "") {
            ToastUtil.showCenterToast("加载图片失败，请重试")
            finish()
        }
        val srcUri: Uri? = if (PictureMimeType.isContent(uri) || PictureMimeType.isHasHttp(uri)) {
            Uri.parse(uri)
        } else {
            Uri.fromFile(File(uri))
        }
        gpuImageView.setImage(srcUri)
        gpuImageView.setBackgroundColor(255f, 255f, 255f)
        yes.setOnClickListener {
            yes.isEnabled = false
            PermissionUtils.askPermission(this, Permission.MANAGE_EXTERNAL_STORAGE) {
                if (cachePath != "" && cacheFileName != "") {
                    val rootFile = File(cachePath)
                    if (!rootFile.exists())
                        rootFile.mkdirs()
                    loadingDialog.setTitleText("更改中...")
                    loadingDialog.show()
                    Thread {
                        BitmapUtils.saveImageToGallery(
                            gpuImageView.gpuImage.bitmapWithFilterApplied,
                            BaseConstant.cachePath, cacheFileName, false
                        )
                        runOnUiThread {
                            loadingDialog.dismiss()
                            val intent = Intent()
                            intent.putExtra(
                                BeautyActivity.BACK_IMG_PATH_KEY,
                                "${cachePath}/${cacheFileName}"
                            )
                            setResult(BeautyActivity.BACK, intent)
                            finish()
                        }
                    }.start()
                } else {
                    val fileName = "${System.currentTimeMillis()}.jpg"
                    val path = BaseConstant.savePath + "/" + fileName
                    loadingDialog.setTitleText("保存中...")
                    loadingDialog.show()
                    Thread {
                        BitmapUtils.saveImageToGallery(
                            gpuImageView.gpuImage.bitmapWithFilterApplied,
                            BaseConstant.savePath, fileName, true
                        )
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

        barBack.setOnClickListener {
            finish()
        }

        no.setOnClickListener {
            finish()
        }
    }

    override fun clickFilterItem(position: Int) {
        gpuImageView.filter = mList[position].filterType
        gpuImageView.requestRender()
    }

}