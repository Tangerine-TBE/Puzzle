package com.weilai.jigsawpuzzle.util.dao;

import android.content.Context;

import com.weilai.jigsawpuzzle.DaoMaster;

import org.greenrobot.greendao.database.DatabaseOpenHelper;

public class PuzzleOpenHelper extends DaoMaster.OpenHelper {
    public PuzzleOpenHelper(Context context, String name) {
        super(context, name);
    }
}
