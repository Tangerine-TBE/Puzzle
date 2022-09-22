package com.weilai.jigsawpuzzle.fragment.puzzle;

import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;

/**
 ** DATE: 2022/9/20
 ** Author:tangerine
 ** Description:
 **/
public class PuzzleFragment extends BaseFragment implements View.OnClickListener{
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
         view.findViewById(R.id.tv_add).setOnClickListener(this);
    }

    @Override
    protected void initListener(View view) {


    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.tv_add){
            //for test View
            start(PuzzleTestFragment.getInstance());
        }
    }
}
