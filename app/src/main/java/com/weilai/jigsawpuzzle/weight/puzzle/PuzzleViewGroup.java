package com.weilai.jigsawpuzzle.weight.puzzle;

import android.content.Context;
import android.util.AttributeSet;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

/**
 * * DATE: 2022/9/22
 * * Author:tangerine
 * * Description: 1.For Two pic
 **/
public class PuzzleViewGroup extends RelativeLayout {
    public PuzzleViewGroup(Context context) {
        this(context, null);
    }

    public PuzzleViewGroup(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PuzzleViewGroup(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    /**
     * * DATE: 2022/9/22
     * * Author:tangerine
     * * Description:Make the ViewGroup attribute value from xml style,if you want lots of pics you might build up more ViewGroup
     * to cover the pic's view
     **/
    private void init() {

    }

    /**
     * * DATE: 2022/9/22
     * * Author:tangerine
     * * Description:May be this function is important. lets see ;l
     **/
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
