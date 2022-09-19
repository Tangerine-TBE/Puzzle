package com.weilai.jigsawpuzzle.dialog.template;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import androidx.appcompat.app.AppCompatDialog;
import androidx.appcompat.widget.AppCompatTextView;

import com.weilai.jigsawpuzzle.R;

/**
 * @description:
 * @author: luo
 * @date: 2022/9/19
 */
public class TemplateConfirmDialog {
    private final Context mContext;
    private AppCompatDialog mAppCompatDialog;
    private OnConfirmClickedListener onConfirmClickedListener;
    public TemplateConfirmDialog(Context context,OnConfirmClickedListener onConfirmClickedListener){
        this.mContext = context;
        this.onConfirmClickedListener = onConfirmClickedListener;
    }
    public final void show(){
        mAppCompatDialog = new AppCompatDialog(mContext, R.style.DialogStyle);
        final View view = LayoutInflater.from(mContext).inflate(R.layout.dialog_template,null,false);
        AppCompatTextView appCompatTextView = view.findViewById(R.id.tv_confirm);
        appCompatTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onConfirmClickedListener != null){
                    onConfirmClickedListener.onConfirmClicked();
                    if (mAppCompatDialog != null){
                        if (mAppCompatDialog.isShowing()){
                            mAppCompatDialog.dismiss();
                            mAppCompatDialog.cancel();
                        }
                    }
                }
            }
        });
        mAppCompatDialog.setContentView(view);
        mAppCompatDialog.show();
    }
    public interface OnConfirmClickedListener{
        void onConfirmClicked();
    }
}
