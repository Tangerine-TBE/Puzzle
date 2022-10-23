package com.weilai.jigsawpuzzle.weight.puzzleHLP;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Rect;
import android.media.Image;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.github.chrisbanes.photoview.PhotoView;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.util.L;

/**
 ** DATE: 2022/10/13
 ** Author:tangerine
 ** Description:
 **/
public class PaddingHirItemDecoration  extends RecyclerView.ItemDecoration{
    private int process;
    private String color;
    private RecyclerView parent;
    public final void setProcess(int process) {
        this.process = process;
        if (parent != null) {
            parent.invalidateItemDecorations();
        }
    }

    public final int getProcess() {
        return process;
    }

    public final void setBackground(String color) {
        this.color = color;
        if (parent != null) {
            parent.invalidateItemDecorations();
        }
    }

    public final String getBackgroundColor() {
        if (TextUtils.isEmpty(color)){
            return "#FFFFFFFF";
        }else{
            return color;
        }
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (this.parent == null) {
            this.parent = parent;
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            RelativeLayout parentView = child.findViewById(R.id.item_adjust);
            ImageView ivImg = parentView.findViewById(R.id.iv_img);
            if (!TextUtils.isEmpty(color)) {
                parentView.setBackgroundColor(Color.parseColor(color));
            }
            if (i ==  0){
                if (parent.getChildCount() == 1) {
                    ivImg.setPadding(process, process, process, process);
                } else {
                    ivImg.setPadding(process, process, 0, process);
                }
            }else if (i == parent.getChildCount()-1){
                ivImg.setPadding(process,process,process,process);
            }else{
                ivImg.setPadding(process,process,0,process);
            }
        }
    }
}
