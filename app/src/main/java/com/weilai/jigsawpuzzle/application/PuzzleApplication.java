package com.weilai.jigsawpuzzle.application;

import android.app.Application;

import androidx.multidex.MultiDexApplication;

import com.weilai.jigsawpuzzle.configure.Config;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description:For Application AndroidManifest
 **/
public class PuzzleApplication  extends MultiDexApplication {
    @Override
    public void onCreate() {
        super.onCreate();
        Config.init(this).Configure();
    }



}
