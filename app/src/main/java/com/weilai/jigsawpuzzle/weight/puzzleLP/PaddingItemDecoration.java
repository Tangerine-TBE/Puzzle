package com.weilai.jigsawpuzzle.weight.puzzleLP;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.Image;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import androidx.annotation.FontRes;
import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import com.davemorrissey.labs.subscaleview.SubsamplingScaleImageView;
import com.github.chrisbanes.photoview.PhotoView;
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
    public PaddingItemDecoration(RecyclerView recyclerView){
        this.parent = recyclerView;
    }
    public final void setProcess(int process) {
        this.process = process;
        if (parent != null) {
            int size = parent.getItemDecorationCount();
            if (size == 0) {
                parent.addItemDecoration(this);
            } else {
                parent.invalidateItemDecorations();
            }
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

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        if (this.parent == null) {
            this.parent = parent;
        }
        for (int i = 0; i < parent.getChildCount(); i++) {
            final View child = parent.getChildAt(i);
            RelativeLayout parentView = child.findViewById(R.id.item_adjust);
            ImageView imageView = parentView.findViewById(R.id.iv_img);
            if (!TextUtils.isEmpty(color)) {
                parentView.setBackgroundColor(Color.parseColor(color));
            }
            if (i == 0) {
                if (parent.getChildCount() == 1) {
                    imageView.setPadding(process, process, process, process);

                } else {
                    imageView.setPadding(process, process, process, 0);
                }
            } else if (i == parent.getChildCount() - 1) {
                imageView.setPadding(process, process, process, process);
            } else {
                imageView.setPadding(process, process, process, 0);
            }
        }

    }

    public final String getBackgroundColor() {
        if (TextUtils.isEmpty(color)) {
            return "#FFFFFFFF";
        } else {
            return color;
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        super.onDraw(c, parent, state);
    }


}
