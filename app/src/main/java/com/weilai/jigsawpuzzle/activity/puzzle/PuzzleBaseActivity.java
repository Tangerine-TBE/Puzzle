package com.weilai.jigsawpuzzle.activity.puzzle;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.puzzle.PuzzleFragment;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

/**
 ** DATE: 2022/9/20
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
        return PuzzleFragment.getInstance();
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        super.setStatusBar();
    }
}
