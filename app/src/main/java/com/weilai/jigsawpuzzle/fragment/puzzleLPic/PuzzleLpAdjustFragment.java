package com.weilai.jigsawpuzzle.fragment.puzzleLPic;

import android.os.Bundle;
import android.view.View;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;

/**
 ** DATE: 2022/10/10
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleLpAdjustFragment extends BaseFragment {
    private PuzzleLpAdjustFragment (){

    }
    public static PuzzleLpAdjustFragment getInstance(String s){
        PuzzleLpAdjustFragment puzzleLpAdjustFragment = new
                PuzzleLpAdjustFragment();
        Bundle bundle = new Bundle();
        bundle.putString("filePath", s);
        puzzleLpAdjustFragment.setArguments(bundle);
        return puzzleLpAdjustFragment;
    }
    @Override
    protected Object setLayout() {
        return R.layout.fragment_lp_ajust;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initListener(View view) {

    }
}
