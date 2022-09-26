package com.weilai.jigsawpuzzle.dialog;

import android.app.Dialog;
import android.content.Context;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatDialog;

import com.wang.avi.AVLoadingIndicatorView;
import com.weilai.jigsawpuzzle.util.MResource;

/**
 ** DATE: 2022/9/26
 ** Author:tangerine
 ** Description:
 **/
public class ProcessDialog extends AppCompatDialog {
    private AVLoadingIndicatorView mProgress;
    private final Context context;
    private TextView mTvMessage;

    public ProcessDialog(Context context) {
        super(context,  MResource.getIdByName(context, "style", "loading_dialog"));
        this.context=context;
        init();
    }
    private void init() {
        setContentView(MResource.getIdByName(context, "layout", "dialog_progress"));
        mProgress=(AVLoadingIndicatorView)findViewById(MResource.getIdByName(context, "id", "progressBar"));
        mTvMessage=(TextView)findViewById(MResource.getIdByName(context, "id", "tv_message"));
    }
    public void setType(ProgressType type){
        switch (type){
            case loading:
                mTvMessage.setText(context.getString(MResource.getIdByName(context, "string", "loading")));
                break;
            case submitting:
                mTvMessage.setText(context.getString(MResource.getIdByName(context, "string", "submitting")));
                break;
        }
        mTvMessage.setVisibility(View.VISIBLE);
    }
    public void setMessage(String message){
        if(message!=null){
            mTvMessage.setText(message);
            mTvMessage.setVisibility(View.VISIBLE);
        }else{
            mTvMessage.setVisibility(View.GONE);
        }
    }
   public enum ProgressType{
        loading,submitting
    }
}
