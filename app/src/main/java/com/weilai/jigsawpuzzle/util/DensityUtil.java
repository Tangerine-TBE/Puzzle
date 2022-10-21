package com.weilai.jigsawpuzzle.util;

import android.content.Context;

import com.weilai.jigsawpuzzle.configure.Config;

public class DensityUtil {

    public static int sp2px(Context context, float spValue) {
        final float fontScale = context.getResources().getDisplayMetrics().scaledDensity;
        return (int) (spValue * fontScale + 0.5f);
    }

    /**
     * dp 转成px
     */
    public static float dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    public static float dipTopx(float dpValue) {
        final float scale = Config.getApplicationContext().getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    public static int dipTopxAsInt(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int dipTopxAsInt(float dpValue) {
        final float scale = Config.getApplicationContext().getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * px转成dp
     */
    public static float pxTodip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (pxValue / scale + 0.5f);
    }
}
