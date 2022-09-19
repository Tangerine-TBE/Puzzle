package com.weilai.jigsawpuzzle.weight.template;

import android.graphics.Rect;
import android.util.ArrayMap;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

/**
 * * DATE: 2022/9/19
 * * Author:tangerine
 * * Description:
 **/
public class SpacesItemDecoration extends RecyclerView.ItemDecoration {
    private ArrayMap<String, Integer> spaceValue;//间隔
    private boolean includeEdge;
    private int spanCount;
    public static final String TOP_SPACE = "top_space";
    public static final String BOTTOM_SPACE = "bottom_space";
    public static final String LEFT_SPACE = "left_space";
    public static final String RIGHT_SPACE = "right_space";

    public SpacesItemDecoration(int spanCount, ArrayMap<String, Integer> spaceValue, boolean includeEdge) {
        this.spanCount = spanCount;
        this.spaceValue = spaceValue;
        this.includeEdge = includeEdge;
    }

    @Override
    public void getItemOffsets(@NonNull Rect outRect, @NonNull View view, @NonNull RecyclerView parent, @NonNull RecyclerView.State state) {

        int position = parent.getChildAdapterPosition(view);
        int column = position % spanCount;
        if (includeEdge) {
            outRect.left = spaceValue.get(LEFT_SPACE) - column * spaceValue.get(LEFT_SPACE) / spanCount;
            outRect.right = (column + 1) * spaceValue.get(RIGHT_SPACE) / spanCount;
            if (position <spanCount){
                outRect.top = spaceValue.get(TOP_SPACE);
            }
            outRect.bottom = spaceValue.get(BOTTOM_SPACE);

        }else{
            outRect.left =column *spaceValue.get(LEFT_SPACE ) / spanCount;
            outRect.right = spaceValue.get(RIGHT_SPACE) - (column +1) *spaceValue.get(RIGHT_SPACE) / spanCount;
            if (position   >= spanCount){
                outRect.top =spaceValue.get(TOP_SPACE);
            }
        }
    }
}
