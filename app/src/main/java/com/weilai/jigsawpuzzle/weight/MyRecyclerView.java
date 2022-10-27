package com.weilai.jigsawpuzzle.weight;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

/**
 ** DATE: 2022/10/27
 ** Author:tangerine
 ** Description:
 **/
public class MyRecyclerView extends RecyclerView {
    public MyRecyclerView(@NonNull Context context) {
        super(context);
    }
    public final void setCanDraw(boolean canDraw){
        this.canDraw = canDraw;
    }
    private boolean canDraw;

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public MyRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void onDraw(Canvas c) {
        if (canDraw){
            return;
        }
        super.onDraw(c);
    }
}
