package com.weilai.jigsawpuzzle.fragment.puzzle;

import android.view.View;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.yalantis.ucrop.view.GestureCropImageView;

/**
 * @description:
 * @author: luo
 * @date: 2022/9/22
 */
public class PuzzleTestFragment extends BaseFragment {
    private GestureCropImageView gestureCropImageView;
    private PuzzleTestFragment(){

    }
    public static PuzzleTestFragment getInstance(){
        return new PuzzleTestFragment();
    }
    @Override
    protected Object setLayout() {
        return R.layout.fragment_puzzle_test;
    }

    @Override
    protected void initView(View view) {
        gestureCropImageView = view.findViewById(R.id.image);
        gestureCropImageView.setRotateEnabled(false);
    }

    @Override
    protected void initListener(View view) {

    }
}
