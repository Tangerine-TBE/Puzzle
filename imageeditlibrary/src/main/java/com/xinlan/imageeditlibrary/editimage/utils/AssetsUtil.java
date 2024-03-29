package com.xinlan.imageeditlibrary.editimage.utils;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

/**
 ** DATE: 2022/9/27
 ** Author:tangerine
 ** Description:
 **/
public class AssetsUtil {
    public static String getAssertString(Context context ,String fileName) {
        //将json数据变成字符串
        StringBuilder stringBuilder = new StringBuilder();
        try {
            //获取assets资源管理器
            InputStream is = context.getAssets().open(fileName);
            //通过管理器打开文件并读取
            BufferedReader bf = new BufferedReader(new InputStreamReader(is));
            String line;
            while ((line = bf.readLine()) != null) {
                stringBuilder.append(line.trim());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        return stringBuilder.toString();
    }
    public static Bitmap getAssertFile(Context context,String fileName){
        Bitmap image = null;
        AssetManager am = context.getApplicationContext().getResources().getAssets();
        try {
            InputStream is = am.open(fileName);
            image = BitmapFactory.decodeStream(is);
            is.close();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
        return image;
    }
}
