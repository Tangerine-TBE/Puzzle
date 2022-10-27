package com.xiaopo.flying.puzzle;


import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

public class SpecialPuzzleView extends PuzzleView{
    public SpecialPuzzleView(Context context) {
        super(context);
    }

    public SpecialPuzzleView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public SpecialPuzzleView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        return true;
    }
}
