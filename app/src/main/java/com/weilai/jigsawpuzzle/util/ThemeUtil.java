package com.weilai.jigsawpuzzle.util;

import android.content.res.Configuration;

import com.weilai.jigsawpuzzle.configure.Config;

/**
 ** DATE: 2022/10/8
 ** Author:tangerine
 ** Description:
 **/
public class ThemeUtil {

    public static boolean getDarkModeStatus(){
        int mode = Config.getApplicationContext().getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        return mode == Configuration.UI_MODE_NIGHT_YES;
    }
}
