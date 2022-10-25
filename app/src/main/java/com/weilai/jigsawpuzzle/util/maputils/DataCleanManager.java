package com.weilai.jigsawpuzzle.util.maputils;

import android.content.Context;
import android.os.Environment;

import com.weilai.jigsawpuzzle.configure.Config;

import java.io.File;

/**
 ** DATE: 2022/10/25
 ** Author:tangerine
 ** Description:
 **/
public class DataCleanManager {
    private static boolean deleteDir(File dir) {
        if (dir != null && dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }
        return dir.delete();
    }
    public static void clearAllCache( ) {
        deleteDir(Config.getApplicationContext().getCacheDir());
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            deleteDir(Config.getApplicationContext().getExternalCacheDir());
        }
    }

}
