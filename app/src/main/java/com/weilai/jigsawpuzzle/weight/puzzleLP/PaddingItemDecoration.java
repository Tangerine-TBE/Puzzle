package com.weilai.jigsawpuzzle.weight.puzzleLP;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.util.L;

/**
 * * DATE: 2022/9/27
 * * Author:tangerine
 * * Description:
 **/
public class PaddingItemDecoration extends RecyclerView.ItemDecoration {

    private int process;
    private String color;
    private RecyclerView parent;

    public final void setProcess(int process) {
        this.process = process;
        mOffsetWidth = process - mCurrentProcess;
        mCurrentProcess = process;
        if (parent != null) {
            parent.postInvalidate();
        }
    }

    public final int getProcess() {
        return process;
    }

    public final void setBackground(String color) {
        this.color = color;
        if (parent != null) {
            parent.postInvalidate();
        }
    }

    public final String getBackgroundColor() {
        return color;
    }

    private int mCurrentProcess = 0;
    private int mOffsetWidth = 0;

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        if (this.parent == null) {
            this.parent = parent;
        }

        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            if (!TextUtils.isEmpty(color)) {
                child.setBackgroundColor(Color.parseColor(color));
            }
            if (i == 0){
                child.setPadding(process,process,process,process);
            }else{
                child.setPadding(process,0,process,process);
            }
            ImageView ivImg = child.findViewById(R.id.iv_img);
            ImageView ivPlace = child.findViewById(R.id.iv_place);
            ViewGroup.LayoutParams ivImgLayoutParams = ivImg.getLayoutParams();
            ivImgLayoutParams.width = ivImgLayoutParams.width - mOffsetWidth;
            ivImgLayoutParams.height = ivImgLayoutParams.height - mOffsetWidth;
            ViewGroup.LayoutParams ivPlaceLayoutParams = ivPlace.getLayoutParams();
            ivPlaceLayoutParams.width = ivImgLayoutParams.width - mOffsetWidth;
            ivPlaceLayoutParams.height = ivImgLayoutParams.height - mOffsetWidth;
            child.invalidate();
        }
        super.onDraw(c, parent, state);
    }


}
