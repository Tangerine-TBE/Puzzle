package com.weilai.jigsawpuzzle.application;

import android.app.Application;

import com.weilai.jigsawpuzzle.configure.Config;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description:For Application AndroidManifest
 **/
public class PuzzleApplication  extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        Config.init(this).Configure();
    }



}
