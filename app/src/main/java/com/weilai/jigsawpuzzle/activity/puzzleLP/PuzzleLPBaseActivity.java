package com.weilai.jigsawpuzzle.activity.puzzleLP;

import android.content.Intent;

import com.weilai.jigsawpuzzle.activity.main.MainBaseActivity;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.puzzleLPic.PuzzleLongPicFragment;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

import java.util.ArrayList;


public class PuzzleLPBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
        Intent intent = getIntent();
        ArrayList<String> arrayList = intent.getStringArrayListExtra("data");
        return PuzzleLongPicFragment.getInstance(arrayList);
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        StatusBarUtil.setLightMode(this);
    }
}
