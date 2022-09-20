package com.weilai.jigsawpuzzle.activity.puzzle;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.puzzle.PuzzleFragment;

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

    }
}
