package com.weilai.jigsawpuzzle.util;

import android.Manifest;
import android.graphics.BitmapFactory;

/**
 ** DATE: 2022/9/19
 ** Author:tangerine
 ** Description:
 **/
public class BitmapUtils {
    public static int calculateSampleSize(BitmapFactory.Options options ,int reqWidth,int reqHeight){
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;
        if (height > reqHeight || width <reqWidth){
            if (width >height){
                inSampleSize = Math.round((float) height /(float) reqHeight);
            }else{
                inSampleSize = Math.round((float) width /(float) reqWidth);
            }
        }
        return inSampleSize;
    }
}
