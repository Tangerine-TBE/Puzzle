package com.weilai.jigsawpuzzle.weight;

import android.content.Context;
import android.util.AttributeSet;

import androidx.annotation.Nullable;

import com.youth.banner.indicator.BaseIndicator;

/**
 * @description:
 * @author: luo
 * @date: 2022/9/14
 */
public class MyCircleIndicator extends BaseIndicator {

    public MyCircleIndicator(Context context) {
        this(context,null);
    }

    public MyCircleIndicator(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyCircleIndicator(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
}
