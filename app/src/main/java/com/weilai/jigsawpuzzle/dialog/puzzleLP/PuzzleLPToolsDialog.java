package com.weilai.jigsawpuzzle.dialog.puzzleLP;

import android.app.Dialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.NonNull;

/**
 ** DATE: 2022/9/27
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleLPToolsDialog {
    private Dialog mDialog;
    private Context mContext;
    public PuzzleLPToolsDialog(@NonNull Context context) {
        mDialog = new Dialog(context);
        this.mContext = context;
    }
    //建立在某一个y上 与x有关的就是居中显示
    /**
     ** DATE: 2022/9/27
     ** Author:tangerine
     ** Description: y 控件最上方的位置  y+height控件最下方的位置
     ** 当
     **/
    public final void show(float y,float height){

    }

}
