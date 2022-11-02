package com.feisukj.base.util;

import android.content.Context;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;

import java.util.Timer;
import java.util.TimerTask;

public class KeyBoardUtlis {

    /**
     *自动弹出软键盘
     * */
    public static void autoShowKeyBoard(final EditText edit){
        edit.setFocusable(true);
        edit.setFocusableInTouchMode(true);
        edit.requestFocus();
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run()
            {
                InputMethodManager inputManager = (InputMethodManager)edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.showSoftInput(edit, 0);
            }
        }, 500);
    }

    /**
     *取消软键盘
     * */
    public static void autoDismissKeyBoard(final EditText edit){
        edit.clearFocus();
        edit.setFocusable(true);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            public void run()
            {
                InputMethodManager inputManager = (InputMethodManager)edit.getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputManager.hideSoftInputFromWindow(edit.getWindowToken(),0);
            }
        }, 100);
    }

    /**
     *取消软键盘
     * */
    public static void autoDismissKeyBoards(final EditText ... edits){
        for (EditText et :edits) {
            autoDismissKeyBoard(et);
        }
    }
}
