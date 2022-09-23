package com.weilai.jigsawpuzzle.fragment.main;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatTextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SizeReadyCallback;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;

import java.io.File;

/**
 ** DATE: 2022/9/20
 ** Author:tangerine
 ** Description:
 **/
public class SaveFragment extends BaseFragment {
    private SaveFragment() {

    }

    public static SaveFragment getInstance(String s) {
        SaveFragment saveFragment = new SaveFragment();
        Bundle bundle = new Bundle();
        bundle.putString("filePath", s);
        saveFragment.setArguments(bundle);
        return saveFragment;
    }

    @Override
    protected Object setLayout() {
        return R.layout.fragment_template_save;
    }

    @Override
    protected void initView(View view) {
//        view.findViewById(R.id.rv_show);//是否显示图片信息
//        view.findViewById(R.id.tv_big);//尺寸
//        view.findViewById(R.id.tv_create_date);//创建时间
        AppCompatTextView appCompatTextView = view.findViewById(R.id.tv_title);
        appCompatTextView.setText(R.string.bitmap_info);
        view.findViewById(R.id.layout_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                pop();
            }
        });
        AppCompatTextView tvSave = view.findViewById(R.id.tv_save);
        tvSave.setVisibility(View.VISIBLE);
        tvSave.setText(R.string.share);
        tvSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        view.findViewById(R.id.visible);//是否显示详细信息
        String path = getArguments().getString("filePath");
        ImageView imageView = view.findViewById(R.id.iv_img);
        TextView textView = view.findViewById(R.id.tv_mapInfo);//本地路径

        Glide.with(this).load(new File(path)).into(imageView).getSize(new SizeReadyCallback() {
            @Override
            public void onSizeReady(int width, int height) {
                textView.setText(String.format("图片尺寸:%d*%d", width, height));
            }
        });

    }


    @Override
    protected void initListener(View view) {

    }
}
