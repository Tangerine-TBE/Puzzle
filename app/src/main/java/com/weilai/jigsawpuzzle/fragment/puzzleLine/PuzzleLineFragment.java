package com.weilai.jigsawpuzzle.fragment.puzzleLine;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.widget.AppCompatTextView;

import com.luck.picture.lib.config.PictureMimeType;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.weight.puzzleLine.PuzzleLineView;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 ** DATE: 2022/10/13
 ** Author:tangerine
 ** Description:拼台词
 **/
public class PuzzleLineFragment extends BaseFragment {
    private PuzzleLineFragment(){

    }
    private PuzzleLineView mPuzzleLineView;
    public static PuzzleLineFragment getInstance(ArrayList<String> path){
        Bundle bundle = new Bundle();
        bundle.putStringArrayList("data", path);
        PuzzleLineFragment puzzleLineFragment = new PuzzleLineFragment();
        puzzleLineFragment.setArguments(bundle);
        return puzzleLineFragment;
    }
    @Override
    protected Object setLayout() {
        return R.layout.fragment_line;
    }

    @Override
    protected void initView(View view) {
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        mPuzzleLineView = view.findViewById(R.id.puzzle_line_view);
        tvTitle.setText("字幕区间");
        initData();
    }
    private void initData(){
        ArrayList<String> arrayList = getArguments().getStringArrayList("data");
        for (String path : arrayList){
            if (path == null || path.isEmpty()){
                return;
            }
            Uri srcUri;
            if (PictureMimeType.isContent(path) || PictureMimeType.isHasHttp(path)) {
                srcUri = Uri.parse(path);
            } else {
                srcUri = Uri.fromFile(new File(path));
            }
            if (srcUri != null) {
                InputStream stream = null;
                try {
                    stream = getBaseActivity().getContentResolver().openInputStream(srcUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                Bitmap bitmap = BitmapFactory.decodeStream(stream);
                mPuzzleLineView.setBitmap(bitmap);
            }



        }
    }

    @Override
    protected void initListener(View view) {
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                _mActivity.finish();
            }
        });
    }
}
