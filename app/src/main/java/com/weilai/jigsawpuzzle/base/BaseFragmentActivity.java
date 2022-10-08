package com.weilai.jigsawpuzzle.base;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.ContentFrameLayout;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.util.EvenUtil;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;
import com.weilai.jigsawpuzzle.util.ThemeUtil;

import me.yokeyword.fragmentation.SupportActivity;
import me.yokeyword.fragmentation.anim.DefaultHorizontalAnimator;
import me.yokeyword.fragmentation.anim.FragmentAnimator;

public abstract class BaseFragmentActivity extends SupportActivity {
    public abstract BaseFragment setRootFragment();
    public void setStatusBar(){
        if (ThemeUtil.getDarkModeStatus()) {
            StatusBarUtil.setDarkMode(this);
        } else {
            StatusBarUtil.setLightMode(this);
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContainer(savedInstanceState);
    }
    @SuppressLint("RestrictedApi")
    private void initContainer(@Nullable Bundle saveInstanceState) {
        final ContentFrameLayout container = new ContentFrameLayout(this);
        container.setId(R.id.fragment_container);
        container.setFitsSystemWindows(true);
        setContentView(container);
        setStatusBar();
        if (saveInstanceState == null) {
            loadRootFragment(R.id.fragment_container, setRootFragment(),true,true);
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        System.runFinalization();
    }
    @Override
    public FragmentAnimator onCreateFragmentAnimator() {
        return new DefaultHorizontalAnimator();
    }

}
