package com.weilai.jigsawpuzzle.util;

import android.net.Uri;
import android.os.Build;
import android.text.TextUtils;

import androidx.core.content.FileProvider;

import com.weilai.jigsawpuzzle.configure.Config;

import java.io.File;

/**
 ** DATE: 2022/10/18
 ** Author:tangerine
 ** Description:
 **/
public class UriUtil {
    public static Uri path2Uri(String path){
        if (!TextUtils.isEmpty(path)){

            if (Build.VERSION.SDK_INT >=Build.VERSION_CODES.N){
                return FileProvider.getUriForFile(Config.getApplicationContext(),Config.getApplicationContext().getApplicationInfo().packageName+".fileprovider",new File(path));

            }else{
                return Uri.fromFile(new File(path));
            }
        }
        return null;
    }
}
