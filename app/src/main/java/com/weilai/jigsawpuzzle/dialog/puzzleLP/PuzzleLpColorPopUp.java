package com.weilai.jigsawpuzzle.dialog.puzzleLP;

import android.content.Context;
import android.graphics.Color;
import android.view.Gravity;
import android.view.View;
import android.widget.SeekBar;

import androidx.appcompat.widget.AppCompatSeekBar;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.alibaba.fastjson.JSONArray;
import com.weilai.jigsawpuzzle.R;
import com.xinlan.imageeditlibrary.editimage.adapter.ColorItemAdapter;
import com.xinlan.imageeditlibrary.editimage.utils.AssetsUtil;

import razerdp.basepopup.BasePopupWindow;

/**
 ** DATE: 2022/10/9
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleLpColorPopUp extends BasePopupWindow  {
    private AppCompatSeekBar mFrameSeekBar;
    private RecyclerView mRvColor;
    private AppCompatTextView mFrameText;
    private int mCurrentProgress = 0;
    private OnColorChangedListener onColorChangedListener;
    public PuzzleLpColorPopUp(Context context,OnColorChangedListener onColorChangedListener ){
        super(context);
        this.onColorChangedListener = onColorChangedListener;
        setContentView(R.layout.layout_edit_color);
        setBackgroundColor(Color.TRANSPARENT);
        setPopupGravityMode(GravityMode.ALIGN_TO_ANCHOR_SIDE);
        setOutSideTouchable(true);
        setOutSideDismiss(true);
        initView();
    }

    public interface  OnColorChangedListener{
        void onProcessed(int position);
        void onColorChanged(String color);
    }
    private void initView(){
        mRvColor = findViewById(R.id.rv_color);
        mFrameText = findViewById(R.id.frame_text);
        mFrameSeekBar = findViewById(R.id.frame_seekbar);
        AppCompatTextView tvHidden = findViewById(R.id.tv_hidden);
        tvHidden.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        AppCompatTextView appCompatTextView = findViewById(R.id.tv_selected_color);

        LinearLayoutManager colorHorManager = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, true);
        mRvColor.setLayoutManager(colorHorManager);
        mFrameSeekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                if (fromUser) {
                    int offsetProgress = progress - mCurrentProgress;
                    //向右滑动 0-5  5-8
                    if (offsetProgress > 0) {
                        onColorChangedListener.onProcessed(progress);
                    } else if (offsetProgress < 0) {
                        onColorChangedListener.onProcessed(progress);
                    }
                    mCurrentProgress = progress;
                    mFrameText.setText(String.valueOf(mCurrentProgress));
                }
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        String colorStr = AssetsUtil.getAssertString(getContext(), "color.json");
        JSONArray jsonArray = JSONArray.parseArray(colorStr);
        ColorItemAdapter colorItemAdapter = new ColorItemAdapter(getContext(), jsonArray, new ColorItemAdapter.OnColorPickedListener() {
            @Override
            public void onColorPicked(String color) {
                appCompatTextView.setBackgroundColor(Color.parseColor(color));
                if (onColorChangedListener != null){
                    onColorChangedListener.onColorChanged(color);
                }
            }
        });
        mRvColor.setAdapter(colorItemAdapter);

    }
    public final void show(View item, boolean isShowTop) {
        setPopupGravity(isShowTop ? Gravity.TOP | Gravity.CENTER : Gravity.BOTTOM | Gravity.CENTER);
        showPopupWindow(item);
    }
}
