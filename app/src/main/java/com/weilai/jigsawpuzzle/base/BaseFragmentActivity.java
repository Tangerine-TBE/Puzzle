package com.weilai.jigsawpuzzle.base;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.ContentFrameLayout;

import com.weilai.jigsawpuzzle.R;

import me.yokeyword.fragmentation.SupportActivity;

public abstract class BaseFragmentActivity extends SupportActivity {
    public abstract BaseFragment setRootFragment();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initContainer(savedInstanceState);
    }
    @SuppressLint("RestrictedApi")
    private void initContainer(@Nullable Bundle saveInstanceState) {
        final ContentFrameLayout container = new ContentFrameLayout(this);
        container.setId(R.id.fragment_container);
        setContentView(container);
        if (saveInstanceState == null) {
            loadRootFragment(R.id.fragment_container, setRootFragment());
        }
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.gc();
        System.runFinalization();
    }

}
