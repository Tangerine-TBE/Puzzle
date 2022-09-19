package com.weilai.jigsawpuzzle.util;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Environment;

import com.weilai.jigsawpuzzle.configure.Config;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
 ** DATE: 2022/9/19
 ** Author:tangerine
 ** Description:
 **/
public class FileUtil {
    private static final String BASE_PATH =Config.getApplicationContext().getDatabasePath("template").getAbsolutePath() ;
    public static void saveBitmapToCache(String fileName, Bitmap bitmap) {
        File file = new File(BASE_PATH);
        if (!file.exists() || !file.isDirectory()) {
            file.mkdir();
        }
        File bitmapFile = new File(file.getAbsolutePath(), fileName);
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(bitmapFile);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, fileOutputStream);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public static Bitmap getBitmapFromCache(String fileName){
        Bitmap bitmap = null;
        File file = new File(BASE_PATH);
        if (!file.exists() || !file.isDirectory()) {
            return null;
        }
        File bitmapFile = new File(BASE_PATH,fileName);
        if (bitmapFile.exists()){
            try {
                bitmap = BitmapFactory.decodeStream(new FileInputStream(bitmapFile));
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        return bitmap;
    }
}
