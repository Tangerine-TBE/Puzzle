package com.weilai.jigsawpuzzle.weight.puzzleHLP;

import android.graphics.Canvas;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

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
    public synchronized void setProcess(int process) {
        this.process = process;
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
        if (TextUtils.isEmpty(color)){
            return "#FFFFFFFF";
        }else{
            return color;
        }
    }

    private int mCurrentProcess = 0;
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
            if (parent.getChildCount() > 1){
                if (i == 0){
                    child.setPadding(process,process,0,process);
                }else{
                    child.setPadding(0,process,process,process);
                }
            }else{
                child.setPadding(process,process,process,process);
            }
            child.invalidate();
        }
        super.onDraw(c, parent, state);
    }
}
