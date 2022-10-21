package com.weilai.jigsawpuzzle.fragment.special
import android.content.Intent
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.weilai.jigsawpuzzle.BaseConstant
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.base.BaseActivity
import com.weilai.jigsawpuzzle.util.BitmapUtils
import com.weilai.jigsawpuzzle.util.ToastUtil
import com.weilai.jigsawpuzzle.util.Utils
import kotlinx.android.synthetic.main.activity_beauty.*
import java.io.File

class BeautyActivity : BaseActivity() {

    companion object{
        const val IMG_PATH_KEY = "img_path_key"
        const val BACK_IMG_PATH_KEY = "back_img_path_key"
        const val GO = 1001
        const val BACK = 1002
    }

    private var path = ""
    private lateinit var cacheFileName: String

    override fun getLayoutId(): Int = R.layout.activity_beauty

    override fun initView() {
        initTopTheme()
        path = intent.getStringExtra(IMG_PATH_KEY)?:""
        cacheFileName = "${System.currentTimeMillis()}.jpg"
        if (path == ""){
            ToastUtil.showCenterToast("打开图片失败")
            finish()
            return
        }
        Glide.with(this).load(path).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE).into(image)

        initClick()
    }

    private fun initClick(){

        barBack.setOnClickListener {
            finish()
        }

        crop.setOnClickListener {
            val intent = Intent(this,CropActivity::class.java)
            intent.putExtra(CropActivity.IMG_PATH,path)
            intent.putExtra(CropActivity.CACHE_PATH,BaseConstant.cachePath)
            intent.putExtra(CropActivity.CACHE_FILE_NAME,cacheFileName)
            startActivityForResult(intent, GO)
        }

        filter.setOnClickListener {
            val intent = Intent(this,FilterActivity::class.java)
            intent.putExtra(FilterActivity.IMG_PATH, path)
            intent.putExtra(FilterActivity.CACHE_PATH, BaseConstant.cachePath)
            intent.putExtra(FilterActivity.CACHE_FILE_NAME,cacheFileName)
            startActivityForResult(intent, GO)
        }

        adjust.setOnClickListener {
            val intent = Intent(this,AdjustActivity::class.java)
            intent.putExtra(AdjustActivity.IMG_PATH, path)
            intent.putExtra(AdjustActivity.CACHE_PATH,BaseConstant.cachePath)
            intent.putExtra(AdjustActivity.CACHE_FILE_NAME,cacheFileName)
            startActivityForResult(intent, GO)
        }

        addText.setOnClickListener {
            val intent = Intent(this,AddTextActivity::class.java)
            intent.putExtra(AddTextActivity.IMG_URI_KEY,path)
            intent.putExtra(AddTextActivity.CACHE_FILE_NAME,cacheFileName)
            intent.putExtra(AddTextActivity.CACHE_PATH,BaseConstant.cachePath)
            startActivityForResult(intent, GO)
        }

        stickers.setOnClickListener {
            val intent = Intent(this,StickerActivity::class.java)
            intent.putExtra(StickerActivity.IMG_URI_KEY,path)
            intent.putExtra(StickerActivity.CACHE_FILE_NAME,cacheFileName)
            intent.putExtra(StickerActivity.CACHE_PATH,BaseConstant.cachePath)
            startActivityForResult(intent, GO)
        }

        scale.setOnClickListener {
            val intent = Intent(this,ScaleActivity::class.java)
            intent.putExtra(ScaleActivity.IMG_URI_KEY, Utils.getMediaUriFromPath(this,path).toString())
            intent.putExtra(ScaleActivity.CACHE_FILE_NAME,cacheFileName)
            intent.putExtra(ScaleActivity.CACHE_PATH,BaseConstant.cachePath)
            startActivityForResult(intent, GO)
        }

        doodle.setOnClickListener {
            val intent = Intent(this,DoodleActivity::class.java)
            intent.putExtra(DoodleActivity.IMG_PATH,path)
            intent.putExtra(DoodleActivity.CACHE_FILE_NAME,cacheFileName)
            intent.putExtra(DoodleActivity.CACHE_PATH,BaseConstant.cachePath)
            startActivityForResult(intent, GO)
        }

        save.setOnClickListener {
            Thread{
                val oldFile = File(path)
                val newFile = File("${BaseConstant.savePath}/${cacheFileName}")
                try {
                    oldFile.copyTo(newFile)
                    BitmapUtils.saveBitmapPhoto(BitmapFactory.decodeFile(newFile.path))
                    val file = File(BaseConstant.cachePath)
                    val list = file.listFiles()
                    list.forEach {
                        it.delete()
                    }
                    runOnUiThread {
                        ToastUtil.showCenterToast("保存成功")
                        finish()
                    }
                }catch (e : Exception){
                    e.printStackTrace()
                    runOnUiThread {
                        ToastUtil.showCenterToast("保存失败")
                    }
                }
            }.start()

        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == GO && resultCode == BACK){
            path = data?.getStringExtra(BACK_IMG_PATH_KEY)?:path
//            Glide.with(this).load(path).skipMemoryCache(false).diskCacheStrategy(DiskCacheStrategy.NONE).into(image)
            image.setImageBitmap(BitmapFactory.decodeFile(path))
        }
    }
}