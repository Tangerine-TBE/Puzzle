package com.weilai.jigsawpuzzle.activity.puzzleLine;

import android.content.Intent;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.puzzleLine.PuzzleLineFragment;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

import java.util.ArrayList;

/**
 * * DATE: 2022/10/13
 * * Author:tangerine
 * * Description:
 **/

public class PuzzleLineBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
        Intent intent = getIntent();
        ArrayList<String> arrayList = intent.getStringArrayListExtra("data");
        return PuzzleLineFragment.getInstance(arrayList);
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        super.setStatusBar();
    }
}
