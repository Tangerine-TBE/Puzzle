package com.xinlan.imageeditlibrary.editimage.fragment;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.PopupWindow;
import android.widget.SeekBar;

import com.alibaba.fastjson.JSONArray;
import com.xinlan.imageeditlibrary.R;
import com.xinlan.imageeditlibrary.editimage.EditImageActivity;
import com.xinlan.imageeditlibrary.editimage.ModuleConfig;
import com.xinlan.imageeditlibrary.editimage.adapter.ColorItemAdapter;
import com.xinlan.imageeditlibrary.editimage.task.StickerTask;
import com.xinlan.imageeditlibrary.editimage.utils.AssetsUtil;
import com.xinlan.imageeditlibrary.editimage.view.CustomPaintView;
import com.xinlan.imageeditlibrary.editimage.view.PaintModeView;


/**
 * 用户自由绘制模式 操作面板
 * 可设置画笔粗细 画笔颜色
 * custom draw mode panel
 *
 * @author panyi
 */
public class PaintFragment extends BaseEditFragment implements View.OnClickListener, ColorItemAdapter.OnColorPickedListener, SeekBar.OnSeekBarChangeListener {
    public static final int INDEX = ModuleConfig.INDEX_PAINT;
    public static final String TAG = PaintFragment.class.getName();

    private View mainView;
    private View backToMenu;// 返回主菜单
    private AppCompatTextView mTvPaintColor;
    private RecyclerView mColorListView;//颜色列表View
    private CustomPaintView mPaintView;
    private SaveCustomPaintTask mSavePaintImageTask;
    private AppCompatTextView mTvModeWipe;
    private AppCompatTextView mTvModePaint;
    private AppCompatTextView mTvWidth;
    private ImageView ivSave;

    public static PaintFragment newInstance() {
        return new PaintFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        mainView = inflater.inflate(R.layout.fragment_edit_paint, null);
        return mainView;
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        backToMenu = mainView.findViewById(R.id.back_to_main);
        mTvPaintColor = mainView.findViewById(R.id.tv_selected_color);
        mTvModePaint = mainView.findViewById(R.id.tv_mode_paint);
        mTvModeWipe = mainView.findViewById(R.id.tv_mode_wipe);
        mPaintView = getActivity().findViewById(R.id.custom_paint_view);
        mColorListView = mainView.findViewById(R.id.paint_color_list);
        ivSave = mainView.findViewById(R.id.iv_save);
        AppCompatSeekBar mSeekBar = mainView.findViewById(R.id.stoke_width_seekbar);
        mTvWidth = mainView.findViewById(R.id.tv_width);
        backToMenu.setOnClickListener(this);// 返回主菜单
        initColorListView();
        mTvModePaint.setOnClickListener(this);
        mTvModeWipe.setOnClickListener(this);
        ivSave.setOnClickListener(this);
        mPaintView.setEraser(false);
        mSeekBar.setOnSeekBarChangeListener(this);
        mPaintView.setColor(Color.RED);
        mTvPaintColor.setBackgroundColor(Color.RED);
        mPaintView.setWidth(10);
        mSeekBar.setProgress(10);
    }

    /**
     * 初始化颜色列表
     */
    private void initColorListView() {
        LinearLayoutManager stickerListLayoutManager = new LinearLayoutManager(activity);
        stickerListLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
        mColorListView.setLayoutManager(stickerListLayoutManager);
        String colorStr = AssetsUtil.getAssertString(getContext(), "color.json");
        JSONArray jsonArray = JSONArray.parseArray(colorStr);
        ColorItemAdapter mColorAdapter = new ColorItemAdapter(getContext(), jsonArray, this);
        mColorListView.setAdapter(mColorAdapter);

    }

    @Override
    public void onClick(View v) {
        if (v == backToMenu) {//back button click
            backToMain();
        } else if (v == mTvModePaint){
            mTvModePaint.setTextColor(Color.BLACK);
            mTvModeWipe.setTextColor(Color.parseColor("#C4C4C4"));
            mPaintView.setEraser(false);
        }else if (v == mTvModeWipe){
            mTvModePaint.setTextColor(Color.parseColor("#C4C4C4"));
            mTvModeWipe.setTextColor(Color.BLACK);
            mPaintView.setEraser(true);
        }else if (v == ivSave){
            savePaintImage();
        }
    }

    /**
     * 返回主菜单
     */
    public void backToMain() {
        activity.mode = EditImageActivity.MODE_NONE;
        activity.bottomGallery.setCurrentItem(MainMenuFragment.INDEX);
        activity.mainImage.setVisibility(View.VISIBLE);
        this.mPaintView.setVisibility(View.GONE);
    }

    public void onShow() {
        activity.mode = EditImageActivity.MODE_PAINT;
        activity.mainImage.setImageBitmap(activity.getMainBit());
        this.mPaintView.setVisibility(View.VISIBLE);
    }

    /**
     * 设置画笔颜色
     *
     * @param paintColor
     */
    protected void setPaintColor(String paintColor) {
        mTvPaintColor.setBackgroundColor(Color.parseColor(paintColor));
        this.mPaintView.setColor(Color.parseColor(paintColor));
        mTvModePaint.setTextColor(Color.BLACK);
        mTvModeWipe.setTextColor(Color.parseColor("#C4C4C4"));
        mPaintView.setEraser(false);
    }
    /**
     * 保存涂鸦
     */
    public void savePaintImage() {
        if (mSavePaintImageTask != null && !mSavePaintImageTask.isCancelled()) {
            mSavePaintImageTask.cancel(true);
        }

        mSavePaintImageTask = new SaveCustomPaintTask(activity);
        mSavePaintImageTask.execute(activity.getMainBit());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mSavePaintImageTask != null && !mSavePaintImageTask.isCancelled()) {
            mSavePaintImageTask.cancel(true);
        }
    }

    @Override
    public void onColorPicked(String color) {
        setPaintColor(color);
    }

    @Override
    public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
        if (fromUser){
            mPaintView.setWidth(progress);
        }
        mTvWidth.setText(String.valueOf(progress));
    }

    @Override
    public void onStartTrackingTouch(SeekBar seekBar) {

    }

    @Override
    public void onStopTrackingTouch(SeekBar seekBar) {

    }

    /**
     * 文字合成任务
     * 合成最终图片
     */
    private final class SaveCustomPaintTask extends StickerTask {

        public SaveCustomPaintTask(EditImageActivity activity) {
            super(activity);
        }

        @Override
        public void handleImage(Canvas canvas, Matrix m) {
            float[] f = new float[9];
            m.getValues(f);
            int dx = (int) f[Matrix.MTRANS_X];
            int dy = (int) f[Matrix.MTRANS_Y];
            float scale_x = f[Matrix.MSCALE_X];
            float scale_y = f[Matrix.MSCALE_Y];
            canvas.save();
            canvas.translate(dx, dy);
            canvas.scale(scale_x, scale_y);

            if (mPaintView.getPaintBit() != null) {
                canvas.drawBitmap(mPaintView.getPaintBit(), 0, 0, null);
            }
            canvas.restore();
        }

        @Override
        public void onPostResult(Bitmap result) {
            mPaintView.reset();
            activity.changeMainBitmap(result, true);
            backToMain();
        }
    }//end inner class

}// end class
