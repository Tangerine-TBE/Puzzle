package com.weilai.jigsawpuzzle.weight.puzzleLP;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;
import android.widget.ImageView;

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
    public final void setProcess(int process){
        this.process = process;
        if (parent != null){
            parent.requestLayout();
        }
    }
    public final void setBackground(String color){
        this.color = color;
        if (parent != null){
            parent.requestLayout();
        }
    }

    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        if (this.parent == null){
            this.parent = parent;
        }
        for (int i = 0 ;  i < parent.getChildCount() ; i ++){
            final View child = parent.getChildAt(i);
            if (!TextUtils.isEmpty(color)){
                child.setBackgroundColor(Color.parseColor(color));
            }
            if (i == 0){
                child.setPadding(process,process,process,process);
            }else{
                child.setPadding(process,0,process,process);
            }
        }
    }


}
