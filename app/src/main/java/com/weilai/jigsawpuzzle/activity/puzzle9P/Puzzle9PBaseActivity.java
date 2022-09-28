package com.weilai.jigsawpuzzle.activity.puzzle9P;

import android.content.Intent;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.puzzle9P.Puzzle9PEditFragment;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

import java.util.ArrayList;

/**
 * * DATE: 2022/9/28
 * * Author:tangerine
 * * Description:
 **/
public class Puzzle9PBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
        Intent intent = getIntent();
        ArrayList<String> path = intent.getStringArrayListExtra("data");
        return Puzzle9PEditFragment.getInstance(path.get(0));
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
    }
}
