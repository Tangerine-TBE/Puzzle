package com.weilai.jigsawpuzzle.activity.puzzleQr;

import android.content.Intent;

import com.weilai.jigsawpuzzle.activity.main.MainBaseActivity;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.puzzleQr.PuzzleQRHealthFragment;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

import java.util.ArrayList;


public class PuzzleQrBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
        Intent intent = getIntent();
        ArrayList<String> arrayList = intent.getStringArrayListExtra("data");
        return PuzzleQRHealthFragment.getInstance(arrayList);
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        super.setStatusBar();
    }
}
