package com.weilai.jigsawpuzzle.dialog.portrait;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.security.identity.EphemeralPublicKeyNotFoundException;
import android.view.View;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;

import me.yokeyword.fragmentation.SupportActivity;

/**
 * * DATE: 2022/10/20
 * * Author:tangerine
 * * Description:
 **/
public class PortraitNoSaveDialog extends Dialog {
    private String titleText;
    private String cancelText;
    private String confirmText;
    private SupportActivity activity;
    public PortraitNoSaveDialog(@NonNull SupportActivity activity) {
        super(activity);
        this. activity = activity;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_tip);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        TextView title = findViewById(R.id.title_text);
        title.setText(titleText);
        TextView cancel = findViewById(R.id.cancel);
        cancel.setText(cancelText);
        TextView confirm = findViewById(R.id.confirm);
        confirm.setText(confirmText);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                activity.finish();
                dismiss();
            }
        });
        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }

    public final PortraitNoSaveDialog setTitleText(String text) {
        this.titleText = text;
        return this;
    }

    public final PortraitNoSaveDialog setCancelText(String text) {
        this.cancelText = text;
        return this;
    }

    public final PortraitNoSaveDialog setConfimText(String text) {
        this.confirmText = text;
        return this;
    }
}
