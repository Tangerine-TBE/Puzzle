package com.feisukj.base.util;

import android.annotation.SuppressLint;

import java.text.NumberFormat;

public class SizeUtils {
    @SuppressLint("DefaultLocale")
    public static String getDataSize(long size){
        if (size<1024){
            return size+"byte";
        }else if (size<1024*1024){
            return String.format("%.2fKb",size/1024f);
        }else if (size<1024*1024*1024){
            return String.format("%.2fMb",size/1024f/1024f);
        }else {
            return String.format("%.2fG",size/1024f/1024f/1024f+"G");
        }
    }

    public static String numFormat(Double d,int minInt,int maxInt,int minFra,int maxFra){
        NumberFormat nf = NumberFormat.getNumberInstance();
        nf.setMaximumIntegerDigits(maxInt); //最大整数位
        nf.setMinimumIntegerDigits(minInt); //最小整数位
        nf.setMinimumFractionDigits(minFra); //最小小数位
        nf.setMaximumFractionDigits(maxFra); //最大小数位
        String str = nf.format(d);
        return str;
    }

    public static float fitFeedHeight(int height) {
        float feedHeight=0;
        if (height>=2000) {
            feedHeight = height / 8f;
            return feedHeight;
        }
        if (height < 2000) {
            feedHeight=height/4.5f;
            return feedHeight;
        }
        return    height / 8f;
    }
}
