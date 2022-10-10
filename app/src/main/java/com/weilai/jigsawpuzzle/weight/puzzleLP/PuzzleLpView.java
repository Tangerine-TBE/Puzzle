package com.weilai.jigsawpuzzle.weight.puzzleLP;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

/**
 ** DATE: 2022/10/10
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleLpView extends View {
    public PuzzleLpView(Context context) {
        this(context,null);
    }

    public PuzzleLpView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public PuzzleLpView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
