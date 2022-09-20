package com.weilai.jigsawpuzzle.weight.puzzle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;

/**
 ** DATE: 2022/9/20
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleEditView extends ViewGroup {
    public PuzzleEditView(Context context) {
        this(context,null);
    }

    public PuzzleEditView(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PuzzleEditView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    private void init(){

    }
    public final void notifyDataSetChanged(){

    }
    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
