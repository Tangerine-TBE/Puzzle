package com.weilai.jigsawpuzzle.dialog.template;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatTextView;

import com.weilai.jigsawpuzzle.R;

public class TemplateEditDialog implements View.OnClickListener {
    private final Context mContext;
    private final TemplateDialogItemListener templateDialogItemListener;
    private AppCompatTextView mCrop;
    private AppCompatTextView mReplace;
    private AppCompatDialog mAppCompatDialog;

    public TemplateEditDialog(Context context, TemplateDialogItemListener templateDialogItemListener) {
        this.templateDialogItemListener = templateDialogItemListener;
        this.mContext = context;
    }

    public final void show() {
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_template_edit, null);
         mAppCompatDialog = new AppCompatDialog(mContext, R.style.DialogStyle);
        mCrop = view.findViewById(R.id.dialog_crop);
        mReplace = view.findViewById(R.id.dialog_replace);
        AppCompatTextView mCancel = view.findViewById(R.id.dialog_cancel);
        mCrop.setOnClickListener(this);
        mReplace.setOnClickListener(this);
        mCancel.setOnClickListener(this);
        mAppCompatDialog.setContentView(view);
        final Window dialogWindow = mAppCompatDialog.getWindow();
        if (dialogWindow != null) {
            WindowManager.LayoutParams layoutParams = dialogWindow.getAttributes();
            layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.gravity = Gravity.BOTTOM;
        }
        mAppCompatDialog.show();
    }

    @Override
    public void onClick(View v) {
        if (v == mCrop) {
            templateDialogItemListener.clickCrop();
        }  else if (v == mReplace) {
            templateDialogItemListener.clickReplace();
        }
        cancel();
    }
    public final void cancel(){
        if (mAppCompatDialog != null){
            if (mAppCompatDialog.isShowing()){
                mAppCompatDialog.dismiss();
                mAppCompatDialog.cancel();
            }
        }
    }

    public interface TemplateDialogItemListener {
        void clickReplace();
        void clickCrop();

    }
}
