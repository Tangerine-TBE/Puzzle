package com.weilai.jigsawpuzzle.fragment.special

import android.content.Intent
import android.view.MotionEvent
import android.view.View
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.hjq.permissions.Permission
import com.weilai.jigsawpuzzle.BaseConstant
import com.weilai.jigsawpuzzle.Resources
import com.weilai.jigsawpuzzle.adapter.special.StickerAdapter
import com.weilai.jigsawpuzzle.base.BaseActivity
import com.weilai.jigsawpuzzle.bean.StickerBean
import com.weilai.jigsawpuzzle.dialog.special.TipDialog
import com.weilai.jigsawpuzzle.util.BitmapUtils
import com.weilai.jigsawpuzzle.util.PermissionUtils
import com.weilai.jigsawpuzzle.util.ToastUtil
import com.xiaopo.flying.sticker.*
import kotlinx.android.synthetic.main.activity_sticker.*
import kotlinx.android.synthetic.main.activity_sticker.no
import kotlinx.android.synthetic.main.activity_sticker.recycler
import kotlinx.android.synthetic.main.activity_sticker.reset
import kotlinx.android.synthetic.main.activity_sticker.show
import kotlinx.android.synthetic.main.activity_sticker.srcImg
import kotlinx.android.synthetic.main.activity_sticker.stickerView
import kotlinx.android.synthetic.main.activity_sticker.yes
import java.io.File

class StickerActivity : BaseActivity(), StickerAdapter.StickerCallback, StickerView.OnStickerOperationListener {

    companion object{
        const val IMG_URI_KEY = "img_uri_key"
        const val CACHE_PATH = "cache_path"
        const val CACHE_FILE_NAME = "cache_file_name"
    }

    private lateinit var cachePath: String
    private lateinit var cacheFileName: String

    private var freeStickerList : Array<StickerBean> = arrayOf()
    private var vipStickerList : Array<StickerBean> = arrayOf()
    private var adapter : StickerAdapter = StickerAdapter()

    override fun getLayoutId(): Int = com.weilai.jigsawpuzzle.R.layout.activity_sticker

    override fun initView() {
        mImmersionBar.statusBarColor(com.weilai.jigsawpuzzle.R.color.sel_text_main_color)
            .navigationBarColor(com.weilai.jigsawpuzzle.R.color.white)
            .statusBarDarkFont(false).init()

        freeStickerList = Resources.getFreeSticker()
        vipStickerList = Resources.getVipSticker()

        adapter.setData(vipStickerList)
        adapter.setCallback(this)
        //recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycler.layoutManager = GridLayoutManager(this,6)
        recycler.adapter = adapter
        val uriString = intent.getStringExtra(IMG_URI_KEY)?:""
        if (uriString == ""){
            ToastUtil.showCenterToast("载入图片出错")
            return
        }
        cacheFileName = intent.getStringExtra(CACHE_FILE_NAME)?:""
        cachePath = intent.getStringExtra(CACHE_PATH)?:""
        Glide.with(this)
            .load(uriString)
            .skipMemoryCache(true)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .into(srcImg)

        val deleteIcon = BitmapStickerIcon(ContextCompat.getDrawable(this,
            com.weilai.jigsawpuzzle.R.drawable.ic_sticker_close),BitmapStickerIcon.LEFT_TOP)
        deleteIcon.iconEvent = DeleteIconEvent()
        val zoomIcon = BitmapStickerIcon(ContextCompat.getDrawable(this,
            com.weilai.jigsawpuzzle.R.drawable.ic_sticker_rotate),BitmapStickerIcon.RIGHT_BOTOM)
        zoomIcon.iconEvent = ZoomIconEvent()
        stickerView.icons = arrayListOf(deleteIcon,zoomIcon)
        stickerView.isLocked = false
        stickerView.isConstrained = true

        barBack.setOnClickListener {
            finish()
        }

        free_sticker.setOnClickListener {
            adapter.setData(freeStickerList)
        }

        vip_sticker.setOnClickListener {
            adapter.setData(vipStickerList)
        }

        reset.setOnClickListener {
            val dialog = TipDialog(this)
            dialog.setTitle("是否清除所有贴纸")
            dialog.setConfirm {
                stickerView.removeAllStickers()
            }
            dialog.show()
        }

        show.setOnTouchListener(object : View.OnTouchListener{
            override fun onTouch(v: View?, event: MotionEvent?): Boolean {
                when(event?.action){
                    MotionEvent.ACTION_DOWN ->{
                        stickerView.setShowSticker(false)
                    }
                    MotionEvent.ACTION_UP ->{
                        stickerView.setShowSticker(true)
                    }
                }
                return true
            }

        })

        no.setOnClickListener {
            finish()
        }
        yes.setOnClickListener {
            yes.isEnabled = false
            PermissionUtils.askPermission(this, Permission.MANAGE_EXTERNAL_STORAGE){
                if (cachePath != "" && cacheFileName != ""){
                    val rootFile = File(cachePath)
                    if (!rootFile.exists())
                        rootFile.mkdirs()
                    loadingDialog.setTitleText("更改中...")
                    loadingDialog.show()
                    Thread{
                        BitmapUtils.saveImageToGallery(stickerView.createBitmap(),cachePath,cacheFileName,false)
                        runOnUiThread{
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
                    val savePath = BaseConstant.savePath
                    val saveFileName = "${System.currentTimeMillis()}.jpg"
                    if (!rootFile.exists())
                        rootFile.mkdirs()
                    Thread{
                        BitmapUtils.saveImageToGallery(stickerView.createBitmap(),cachePath,cacheFileName,true)

                        runOnUiThread {
                            loadingDialog.dismiss()
//                            setResult(HomeActivity.BACK)
                            finish()
                        }
                    }.start()
                }

            }
        }

//        undo.setOnClickListener {
//            val a = addList.lastOrNull() ?: return@setOnClickListener
//            stickerView.remove(a)
//        }
//
//        redo.setOnClickListener {
//            val a = removeList.lastOrNull() ?: return@setOnClickListener
//            removeList.remove(a)
//            stickerView.addSticker(a)
//        }

        stickerView.onStickerOperationListener = this

    }

    override fun clickStickerItem(stickerBean: StickerBean) {
            stickerView.addSticker(DrawableSticker(ContextCompat.getDrawable(this,stickerBean.resId)))
    }

    override fun onStickerAdded(sticker: Sticker) {
//        addList.add(sticker)
    }

    override fun onStickerClicked(sticker: Sticker) {

    }

    override fun onStickerDeleted(sticker: Sticker) {
//        addList.remove(sticker)
//        removeList.add(sticker)
    }

    override fun onStickerDragFinished(sticker: Sticker) {

    }

    override fun onStickerTouchedDown(sticker: Sticker) {

    }

    override fun onStickerZoomFinished(sticker: Sticker) {

    }

    override fun onStickerFlipped(sticker: Sticker) {

    }

    override fun onStickerDoubleTapped(sticker: Sticker) {

    }

}