package com.abc.matting.ui.activity

import android.content.Intent
import android.graphics.BitmapFactory
import com.bumptech.glide.Glide
import com.weilai.jigsawpuzzle.BaseConstant
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.base.BaseActivity
import com.weilai.jigsawpuzzle.fragment.special.NoSaveDialog
import com.weilai.jigsawpuzzle.fragment.special.OldActivity2
import com.weilai.jigsawpuzzle.util.BitmapUtils
import com.weilai.jigsawpuzzle.util.DownloadUtils
import com.weilai.jigsawpuzzle.util.ToastUtil
import kotlinx.android.synthetic.main.activity_effect_end.*
import java.io.File
import java.lang.Exception

class EffectEndActivity : BaseActivity() {

    companion object{
        const val titleKey = "titleKey"
        const val imgUrlKey = "imgUrlKey"
    }

    private var title_Text = ""
    private var imgUrl = ""
    private var saveCode = 0    //0:未保存 1：保存中 2：已保存

    override fun getLayoutId(): Int = R.layout.activity_effect_end

    override fun initView() {
        mImmersionBar.statusBarColor(R.color.white).statusBarDarkFont(true).init()
        barTitle.text = intent.getStringExtra(titleKey)?:"变老相机"
        imgUrl = intent.getStringExtra(imgUrlKey).toString()
        if (imgUrl.isEmpty()){
            ToastUtil.showCenterToast("出错了")
            finish()
        }
        Glide.with(this)
            .load(imgUrl)
            .into(image)
        barBack.setOnClickListener {
            if (saveCode != 2){
                val dialog = NoSaveDialog(this, {
                    finish()
                },{

                })
                dialog.setTitleText("图片尚未保存,是否退出?").setConfirmText("退出").setCancelText("再想想")
                dialog.show()
            }else{
                finish()
            }
        }
        old.setOnClickListener {
            if (saveCode != 2){
                val dialog = NoSaveDialog(this, {
                    val intent = Intent(this, OldActivity2::class.java)
                    intent.putExtra(OldActivity2.typeKey, OldActivity2.TYPE_OLD)
                    startActivity(intent)
                    finish()
                },{
                    switchAfterSaving {
                        val intent = Intent(this, OldActivity2::class.java)
                        intent.putExtra(OldActivity2.typeKey, OldActivity2.TYPE_OLD)
                        startActivity(intent)
                        finish()
                    }
                })
                dialog.setTitleText("照片还没有保存,是否保存后切换").setConfirmText("不用了").setCancelText("保存后切换")
                dialog.show()
            }else{
                val intent = Intent(this, OldActivity2::class.java)
                intent.putExtra(OldActivity2.typeKey, OldActivity2.TYPE_YOUNG)
                startActivity(intent)
                finish()
            }
        }
        young.setOnClickListener {
            if (saveCode != 2){
                val dialog = NoSaveDialog(this,{
                    val intent = Intent(this, OldActivity2::class.java)
                    intent.putExtra(OldActivity2.typeKey, OldActivity2.TYPE_YOUNG)
                    startActivity(intent)
                    finish()
                },{
                    switchAfterSaving {
                        val intent = Intent(this, OldActivity2::class.java)
                        intent.putExtra(OldActivity2.typeKey, OldActivity2.TYPE_YOUNG)
                        startActivity(intent)
                        finish()
                    }
                })
                dialog.setTitleText("照片还没有保存,是否保存后切换").setConfirmText("不用了").setCancelText("保存后切换")
                dialog.show()
            }else{
                val intent = Intent(this, OldActivity2::class.java)
                intent.putExtra(OldActivity2.typeKey, OldActivity2.TYPE_YOUNG)
                startActivity(intent)
                finish()
            }
        }
        sex.setOnClickListener {
            if (saveCode!=2){
                val dialog = NoSaveDialog(this,{
                    val intent = Intent(this, OldActivity2::class.java)
                    intent.putExtra(OldActivity2.typeKey, OldActivity2.TYPE_SEX)
                    startActivity(intent)
                    finish()
                },{
                    switchAfterSaving {
                        val intent = Intent(this, OldActivity2::class.java)
                        intent.putExtra(OldActivity2.typeKey, OldActivity2.TYPE_SEX)
                        startActivity(intent)
                        finish()
                    }
                })
                dialog.setTitleText("照片还没有保存,是否保存后切换").setConfirmText("不用了").setCancelText("保存后切换")
                dialog.show()
            }else{
                val intent = Intent(this, OldActivity2::class.java)
                intent.putExtra(OldActivity2.typeKey, OldActivity2.TYPE_SEX)
                startActivity(intent)
                finish()
            }
        }
        save.setOnClickListener {
            when(saveCode){
                0 -> doSave()
                1 -> ToastUtil.showCenterToast("保存中...")
                2 -> ToastUtil.showCenterToast("该图片已保存，去作品库中查看吧")
            }
        }
    }

    private fun doSave(){
        saveCode = 1
        val rootFile = File(BaseConstant.savePath)
        if (!rootFile.exists())
            rootFile.mkdirs()
        val filePath = "${BaseConstant.savePath}/${System.currentTimeMillis()}.jpg"
        loadingDialog.show()
        DownloadUtils.downloadFile(imgUrl,filePath,object : DownloadUtils.RequestCallback{
            override fun onSuccess(response: String?) {
                runOnUiThread {
                    BitmapUtils.saveBitmapPhoto(BitmapFactory.decodeFile(filePath))
                    loadingDialog.dismiss()
                    ToastUtil.showCenterToast("保存成功")
                    saveCode = 2
                }
            }

            override fun onFailure(msg: String?, e: Exception?) {
                runOnUiThread {
                    loadingDialog.dismiss()
                    ToastUtil.showCenterToast("保存失败")
                    saveCode = 0
                }
            }

        })
    }

    private fun switchAfterSaving(unit: (()->Unit)){
        saveCode = 1
        val rootFile = File(BaseConstant.savePath)
        if (!rootFile.exists())
            rootFile.mkdirs()
        val filePath = "${BaseConstant.savePath}/${System.currentTimeMillis()}.jpg"
        loadingDialog.show()
        DownloadUtils.downloadFile(imgUrl,filePath,object : DownloadUtils.RequestCallback{
            override fun onSuccess(response: String?) {
                runOnUiThread {
                    BitmapUtils.saveBitmapPhoto(BitmapFactory.decodeFile(filePath))
                    loadingDialog.dismiss()
                    ToastUtil.showCenterToast("保存成功")
                    saveCode = 2
                    unit.invoke()
                }
            }

            override fun onFailure(msg: String?, e: Exception?) {
                runOnUiThread {
                    loadingDialog.dismiss()
                    ToastUtil.showCenterToast("保存失败")
                    saveCode = 0
                }
            }

        })
    }

    override fun onBackPressed() {
        if (saveCode != 2){
            val dialog = NoSaveDialog(this, {
                finish()
            },{

            })
            dialog.setTitleText("图片尚未保存,是否退出?").setConfirmText("退出").setCancelText("再想想")
            dialog.show()
        }else{
            finish()
        }
    }
}