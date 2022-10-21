package com.weilai.jigsawpuzzle.activity.main;

import android.content.Intent;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.main.SaveFragment;
import com.weilai.jigsawpuzzle.fragment.puzzle.PuzzleFragment;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

/**
 ** DATE: 2022/10/21
 ** Author:tangerine
 ** Description:
 **/
public class SaveBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
        Intent intent = getIntent();
       String path =  intent.getStringExtra("data");
        return SaveFragment.getInstance(path);
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        super.setStatusBar();
    }
}
