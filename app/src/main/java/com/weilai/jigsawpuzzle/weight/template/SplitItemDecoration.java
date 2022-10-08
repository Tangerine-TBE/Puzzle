package com.weilai.jigsawpuzzle.weight.template;

import android.graphics.Rect;
import android.view.View;

import androidx.recyclerview.widget.RecyclerView;

/**
 * * DATE: 2022/10/8
 * * Author:tangerine
 * * Description:
 **/
public class SplitItemDecoration extends RecyclerView.ItemDecoration {
    private int space;

    public SplitItemDecoration(int space) {
        this.space = space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        outRect.left =space*2;
        outRect.right =space;
        outRect.bottom = space/2;
        outRect.top = space/2;
    }
}
