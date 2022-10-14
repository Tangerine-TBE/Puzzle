package com.weilai.jigsawpuzzle.activity.puzzleSS;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.puzzleSS.PuzzleSShotFragment;

/**
 ** DATE: 2022/10/14
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleSShotBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
        return PuzzleSShotFragment.getInstance();
    }
}
