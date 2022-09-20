package com.weilai.jigsawpuzzle.fragment.puzzle;

import android.view.View;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;

/**
 ** DATE: 2022/9/20
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleFragment extends BaseFragment {
    private PuzzleFragment(){
        
    }
    public static PuzzleFragment getInstance(){
        return new PuzzleFragment();
    }
    @Override
    protected Object setLayout() {
        return R.layout.fragment_puzzle;
    }

    @Override
    protected void initView(View view) {

    }

    @Override
    protected void initListener(View view) {

    }
}
