package com.weilai.jigsawpuzzle.fragment.puzzle;

import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.yalantis.ucrop.view.GestureCropImageView;


public class PuzzleTestFragment extends BaseFragment {
    private GestureCropImageView gestureCropImageView;

    private PuzzleTestFragment() {

    }

    public static PuzzleTestFragment getInstance() {
        return new PuzzleTestFragment();
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_puzzle_test;
    }

    @Override
    protected void initView(View view) {
        GestureCropImageView imageView1 = view.findViewById(R.id.pic_view1);
        imageView1.setTargetAspectRatio(1);
        imageView1.setRotateEnabled(false);
        imageView1.setSelected(true);
        GestureCropImageView imageView2 = view.findViewById(R.id.pic_view2);
        imageView2.setTargetAspectRatio(1);
        imageView2.setRotateEnabled(false);
        imageView2.setSelected(true);
        new Thread(new Runnable() {
            @Override
            public void run() {

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            int x = (int) imageView1.getX();
                            int y = (int) imageView1.getY();
                          int right =   imageView1.getWidth();
                          int bottom = imageView1.getHeight();
                          int x1 = (int) imageView2.getX();
                          int y1 = (int) imageView2.getY();
                          int right1= imageView1.getWidth();
                          int bottom1 = imageView1.getHeight();
                            imageView1.layout(x, y, x + right, y + bottom - 500);
                            imageView2.layout(x1,y1,right1,bottom1+y1+500);
                        }
                    });
                }
        }).start();
    }

    @Override
    protected void initListener(View view) {

    }
}
