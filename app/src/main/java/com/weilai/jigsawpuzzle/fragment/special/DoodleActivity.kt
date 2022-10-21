package com.weilai.jigsawpuzzle.fragment.special

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.view.MotionEvent
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import cn.hzw.doodle.*
import cn.hzw.doodle.core.*
import com.hjq.permissions.Permission
import com.weilai.jigsawpuzzle.R
import com.weilai.jigsawpuzzle.Resources
import com.weilai.jigsawpuzzle.base.BaseActivity
import com.weilai.jigsawpuzzle.dialog.PortraitTextColorAdapter
import com.weilai.jigsawpuzzle.util.BitmapUtils
import com.weilai.jigsawpuzzle.util.PermissionUtils
import com.weilai.jigsawpuzzle.util.ToastUtil
import com.weilai.jigsawpuzzle.weight.MySeekBar
import kotlinx.android.synthetic.main.activity_doodle.*
import java.util.*

class DoodleActivity : BaseActivity(), MySeekBar.MySeekBarCallback,
    PortraitTextColorAdapter.PortraitTextColorCallback {

    companion object{
        const val IMG_PATH = "img_page"
        const val CACHE_PATH = "cache_path"
        const val CACHE_FILE_NAME = "cache_file_name"
    }

    private lateinit var cachePath: String
    private lateinit var cacheFileName: String

    private var mDoodle: IDoodle? = null
    private var mDoodleView: DoodleView? = null
    private var mDoodleParams: DoodleParams? = null
    private var mTouchGestureListener: DoodleOnTouchGestureListener? = null
    private var penColor = 0
    private var colorList: Array<Int> = arrayOf()
    private var adapter: PortraitTextColorAdapter =
        PortraitTextColorAdapter()

    override fun getLayoutId(): Int = R.layout.activity_doodle

    override fun initView() {
        initTopTheme()
        penColor = Color.argb(255, 255, 0, 0)
        mDoodleParams = DoodleParams()
        mDoodleParams?.mOptimizeDrawing = true
        mDoodleParams?.mPaintUnitSize = DoodleView.DEFAULT_SIZE.toFloat()
        mDoodleParams?.mPaintColor = Color.RED
        mDoodleParams?.mSupportScaleItem = true

        colorList = Resources.getTextColorList()
        adapter.setData(colorList)
        adapter.setCallback(this)
        recycler.layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recycler.adapter = adapter

        val path = intent.getStringExtra(IMG_PATH)?:""
        if (path == ""){
            ToastUtil.showCenterToast("加载图片失败")
            return
        }

        cacheFileName = intent.getStringExtra(CACHE_FILE_NAME)?:""
        cachePath = intent.getStringExtra(CACHE_PATH)?:""

        val bgBitmap = BitmapFactory.decodeFile(path)
        if(bgBitmap == null){
            ToastUtil.showCenterToast("加载图片失败")
            finish()
            return
        }
        mDoodle = DoodleViewWrapper(this, bgBitmap, mDoodleParams?.mOptimizeDrawing!!, object :
            IDoodleListener {
            /*
             *此时view已经测量完成，涂鸦前的准备工作已经完成，在这里可以设置大小、颜色、画笔、形状等。
             * */
            override fun onReady(doodle: IDoodle?) {
                mDoodle?.size = 12f
                mDoodle?.pen = DoodlePen.MOSAIC
                mDoodle!!.shape = DoodleShape.HAND_WRITE
                mDoodle!!.color = DoodlePath.getMosaicColor(mDoodle, DoodlePath.MOSAIC_LEVEL_3)
                if (mTouchGestureListener!!.selectedItem != null) {
                    mTouchGestureListener!!.selectedItem.color = mDoodle!!.color.copy()
                }
                mDoodle!!.doodleMinScale = 0.5f
                mTouchGestureListener!!.isSupportScaleItem = mDoodleParams!!.mSupportScaleItem
            }

            /*
             * 保存涂鸦图像时调用
             * */
            override fun onSaved(doodle: IDoodle?, doodleBitmap: Bitmap?, callback: Runnable?) {
                PermissionUtils.askPermission(
                    this@DoodleActivity,
                    Permission.MANAGE_EXTERNAL_STORAGE
                ) {
                    loadingDialog.show()
                    if (doodleBitmap != null) {
                        if (cachePath != "" && cacheFileName != ""){
                            Thread{
                                BitmapUtils.saveImageToGallery(doodleBitmap,cachePath,cacheFileName,false)
                                runOnUiThread {
                                    loadingDialog.dismiss()
                                    val intent = Intent()
                                    intent.putExtra(BeautyActivity.BACK_IMG_PATH_KEY,"${cachePath}/${cacheFileName}")
                                    setResult(BeautyActivity.BACK,intent)
                                    finish()
                                }
                            }.start()
                        }else{
                            Thread {
                                BitmapUtils.saveImageToGallery(doodleBitmap)
                                runOnUiThread {
                                    loadingDialog.dismiss()
//                                    setResult(HomeActivity.BACK)
                                    finish()
                                }
                            }.start()
                        }


                    } else{
                        ToastUtil.showToast("保存失败")
                        yes.isEnabled = true
                    }

                }
            }

        }).also {mDoodleView = it}

        mTouchGestureListener = object : DoodleOnTouchGestureListener(
            mDoodleView,
            object : ISelectionListener {
                // save states before being selected
                var mLastPen: IDoodlePen? = null
                var mLastColor: IDoodleColor? = null
                var mSize: Float? = null
                var mIDoodleItemListener = IDoodleItemListener { property ->
                    if (mTouchGestureListener!!.selectedItem == null) {
                        return@IDoodleItemListener
                    }
//                    if (property == IDoodleItemListener.PROPERTY_SCALE) {
//                        mItemScaleTextView.setText((mTouchGestureListener!!.selectedItem.scale * 100 + 0.5f) as Int.toString() + "%")
//                    }
                }

                override fun onSelectedItem(
                    doodle: IDoodle,
                    selectableItem: IDoodleSelectableItem,
                    selected: Boolean
                ) {
                    if (selected) {
                        if (mLastPen == null) {
                            mLastPen = mDoodle?.pen
                        }
                        if (mLastColor == null) {
                            mLastColor = mDoodle?.color
                        }
                        if (mSize == null) {
                            mSize = mDoodle?.size
                        }
                        mDoodleView!!.isEditMode = true
                        mDoodle?.pen = selectableItem.pen
                        mDoodle?.color = selectableItem.color
                        mDoodle?.size = selectableItem.size
                        selectableItem.addItemListener(mIDoodleItemListener)
                    } else {
                        selectableItem.removeItemListener(mIDoodleItemListener)
                        if (mTouchGestureListener!!.selectedItem == null) { // nothing is selected. 当前没有选中任何一个item
                            if (mLastPen != null) {
                                mDoodle?.pen = mLastPen!!
                                mLastPen = null
                            }
                            if (mLastColor != null) {
                                mDoodle?.color = mLastColor!!
                                mLastColor = null
                            }
                            if (mSize != null) {
                                mDoodle?.size = mSize!!
                                mSize = null
                            }
                        }
                    }
                }

                override fun onCreateSelectableItem(doodle: IDoodle, x: Float, y: Float) {
//                    if (mDoodle?.pen === DoodlePen.TEXT) {
//                    createDoodleText(null, x, y)
//                    } else if (mDoodle?.pen === DoodlePen.BITMAP) {
//                    createDoodleBitmap(null, x, y)
//                    }
                }
            }) {
            override fun setSupportScaleItem(supportScaleItem: Boolean) {
                super.setSupportScaleItem(supportScaleItem)
//                if (supportScaleItem) {
//
//                } else {
//
//                }
            }
        }

        doodle_container.addView(
            mDoodleView,
            ViewGroup.LayoutParams.MATCH_PARENT,
            ViewGroup.LayoutParams.MATCH_PARENT
        )
        val detector: IDoodleTouchDetector = DoodleTouchDetector(
            applicationContext,
            mTouchGestureListener
        )
        mDoodleView!!.defaultTouchDetector = detector

        mDoodle!!.color = DoodlePath.getMosaicColor(mDoodle, DoodlePath.MOSAIC_LEVEL_2)
        if (mTouchGestureListener!!.selectedItem != null) {
            mTouchGestureListener!!.selectedItem.color = mDoodle!!.color.copy()
        }

        seekBar.setMinValue(0)
        seekBar.setMaxValue(100)
        seekBar.setValue(12)
        seekBar.setCallback(this)

        initClick()
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun initClick(){

        barBack.setOnClickListener {
            finish()
        }

        clean.setOnClickListener {
            mDoodle?.clear()
        }

        un_do.setOnClickListener {
            mDoodle?.undo()
            if (mDoodle?.allItem?.size==0){
                it.tag = 0
                un_do.setImageResource(R.drawable.ic_un_do_n)
            }else
                re_do.setImageResource(R.drawable.ic_re_do_y)
        }

        re_do.setOnClickListener {
            mDoodle?.redo(1)
            if (mDoodle?.redoItemCount!! == 0)
                re_do.setImageResource(R.drawable.ic_re_do_n)
            if (mDoodle?.allItem?.size!! > 0) {
                it.tag = 1
                un_do.setImageResource(R.drawable.ic_un_do_y)
            }
        }

        mosaic.setOnClickListener {
            mDoodle?.pen = DoodlePen.MOSAIC
            iv_mosaic.setImageResource(R.drawable.ic_mosaic_y)
            iv_doodle.setImageResource(R.drawable.ic_doodle_n)

            mDoodle!!.color = DoodlePath.getMosaicColor(mDoodle, DoodlePath.MOSAIC_LEVEL_3)
            if (mTouchGestureListener!!.selectedItem != null) {
                mTouchGestureListener!!.selectedItem.color = mDoodle!!.color.copy()
            }
        }

        doodle.setOnClickListener {
            mDoodle?.pen = DoodlePen.BRUSH
            visible(recycler)
            iv_mosaic.setImageResource(R.drawable.ic_mosaic_n)
            iv_doodle.setImageResource(R.drawable.ic_doodle_y)

            mDoodle?.color = DoodleColor(penColor)
        }

        no.setOnClickListener {
            finish()
        }

        yes.setOnClickListener {
            yes.isEnabled = false
            mDoodle?.save()
        }

        mDoodleView?.setOnTouchListener{view,e ->
            when(e.action){
                MotionEvent.ACTION_DOWN ->{
                    gone(recycler)
                }
                MotionEvent.ACTION_UP ->{
                    view.performClick()
                }
            }
            false
        }
    }

    /**
     * 包裹DoodleView，监听相应的设置接口，以改变UI状态
     */
    inner class DoodleViewWrapper(
        context: Context?,
        bitmap: Bitmap?,
        optimizeDrawing: Boolean,
        listener: IDoodleListener?
    ) : DoodleView(context, bitmap, optimizeDrawing, listener) {
        private val mBtnPenIds: MutableMap<IDoodlePen, Int> = HashMap()
        override fun setPen(pen: IDoodlePen) {
//            val oldPen = getPen()
//            super.setPen(pen)
//            if (pen === DoodlePen.BITMAP || pen === DoodlePen.TEXT) {
//
//            } else if (pen === DoodlePen.MOSAIC) {
//
//            } else {
//                if (pen === DoodlePen.COPY || pen === DoodlePen.ERASER) {
//
//                } else {
//
//                }
//            }
//            if (pen === DoodlePen.BRUSH) {
//
//            } else if (pen === DoodlePen.MOSAIC) {
//
//            } else if (pen === DoodlePen.COPY) {
//            } else if (pen === DoodlePen.ERASER) {
//            } else if (pen === DoodlePen.TEXT) {
//
//            } else if (pen === DoodlePen.BITMAP) {
//
//            }
        }
        override fun setShape(shape: IDoodleShape) {
            super.setShape(shape)

        }
        override fun setSize(paintSize: Float) {
            super.setSize(paintSize)

        }

        override fun setColor(color: IDoodleColor) {
            super.setColor(color)

        }

        override fun enableZoomer(enable: Boolean) {
            super.enableZoomer(enable)

        }

        override fun undo(): Boolean {
            return super.undo()
        }

        override fun clear() {
            super.clear()
            un_do.setImageResource(R.drawable.ic_un_do_n)
            re_do.setImageResource(R.drawable.ic_re_do_n)
            un_do.tag = 0
        }

        override fun addItem(item: IDoodleItem) {
            super.addItem(item)
            if (un_do.tag != 1){
                un_do.tag = 1
                un_do.setImageResource(R.drawable.ic_un_do_y)
            }
            if (mDoodle?.redoItemCount == 0)
                re_do.setImageResource(R.drawable.ic_re_do_n)
        }
        override fun setEditMode(editMode: Boolean) {
            super.setEditMode(editMode)

        }

    }

    override fun MySeekBarChange(int: Int) {
        mDoodle?.size = int.toFloat()
    }

    override fun selectTextColor(position: Int) {
        if (mDoodle?.pen == DoodlePen.BRUSH){
            mDoodle?.color = DoodleColor(colorList[position])
        }
    }
}