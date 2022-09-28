package com.weilai.jigsawpuzzle.weight.puzzleLP;

import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.util.L;

/**
 * * DATE: 2022/9/27
 * * Author:tangerine
 * * Description:
 **/
    public class PaddingItemDecoration extends RecyclerView.ItemDecoration {

    private int process;

    public final void setProcess(int process){
        this.process = process;
    }


    @Override
    public void onDraw(@NonNull Canvas c, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {
        super.onDraw(c, parent, state);
        for (int i = 0 ;  i < parent.getChildCount() ; i ++){
            final View child = parent.getChildAt(i);
            if (i == 0){
                child.setPadding(process,process,process,process);
            }else{
                child.setPadding(process,process,process,0);
            }
        }
    }


}
