package com.feisukj.base.util;

import android.content.Context;

import com.feisukj.base.BaseApplication;

public class DensityUtil {

    /**
     * dp 转成px
     */
    public static float dipTopx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    public static float dipTopx(float dpValue) {
        final float scale = BaseApplication.getApplication().getResources().getDisplayMetrics().density;
        return (dpValue * scale + 0.5f);
    }

    public static int dipTopxAsInt(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    public static int dipTopxAsInt(float dpValue) {
        final float scale = BaseApplication.getApplication().getResources().getDisplayMetrics().density;
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
