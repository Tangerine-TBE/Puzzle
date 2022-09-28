package com.weilai.jigsawpuzzle.base;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.weilai.jigsawpuzzle.dialog.ProcessDialog;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.SupportFragment;

public abstract class BaseFragment extends SupportFragment {

    protected abstract Object setLayout();

    protected abstract void initView(View view);

    protected abstract void initListener(View view);

    private ProcessDialog mProcessDialog;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view;
        if (setLayout() != null) {
            if (setLayout() instanceof View) {
                view = (View) setLayout();
            } else if (setLayout() instanceof Integer) {
                view = inflater.inflate((Integer) setLayout(), null, false);
            } else {
                throw new RuntimeException("the Activity can't access kind of view");
            }
            initView(view);
            initListener(view);
            return view;
        } else {
            throw new RuntimeException("the Fragment can't access kind of view");
        }
    }

    public final void showProcessDialog() {
        mProcessDialog = new ProcessDialog(_mActivity);
        mProcessDialog.show();
    }

    public final void hideProcessDialog() {
        if (mProcessDialog.isShowing()) {
            mProcessDialog.dismiss();
            mProcessDialog.cancel();
            mProcessDialog = null;

        }
    }

    public SupportActivity getBaseActivity() {
        return (SupportActivity) _mActivity;
    }

}
