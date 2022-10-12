package com.xinlan.imageeditlibrary.editimage;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.xiaopo.flying.sticker.Sticker;
import com.xinlan.imageeditlibrary.R;

/**
 * * DATE: 2022/10/12
 * * Author:tangerine
 * * Description:
 **/
public class EditTextDialog extends Dialog {
    private TextView tvCancel;
    private TextView tvConfirm;
    private EditText editText;
    private EditTextCallback editTextCallback;
    private Sticker sticker;

    public EditTextDialog(@NonNull Context context, EditTextCallback editTextCallback) {
        super(context);
        this.editTextCallback = editTextCallback;
    }
    public final EditTextDialog setSticker(Sticker sticker){
        this.sticker = sticker;
        return this;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_edit_text);
        tvCancel = findViewById(R.id.cancel);
        tvConfirm = findViewById(R.id.confirm);
        editText = findViewById(R.id.edit_View);
        WindowManager.LayoutParams layoutParams = getWindow().getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
        layoutParams.gravity = Gravity.CENTER;
        initView();
    }

    private void initView() {
        tvCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
        tvConfirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editTextCallback != null) {
                    editTextCallback.editText(editText.getText().toString(),sticker);
                }
                dismiss();
            }
        });
    }

    public interface EditTextCallback {
        void editText(String str,Sticker sticker);

    }
}
