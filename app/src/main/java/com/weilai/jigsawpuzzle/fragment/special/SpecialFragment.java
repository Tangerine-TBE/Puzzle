package com.weilai.jigsawpuzzle.fragment.special;

import android.os.Bundle;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.DiffUtil;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.util.camera.Camera2Loader;
import com.weilai.jigsawpuzzle.util.camera.CameraLoader;

/**
 ** DATE: 2022/10/20
 ** Author:tangerine
 ** Description:
 **/
public class SpecialFragment extends BaseFragment {
    private String TYPE_OLD ="old";
    private String TYPE_YOUNG ="young";
    private String TYPE_COMIC = "comic";
    private String TYPE_GENDER = "gender";
    private SpecialFragment (){

    }
    public static SpecialFragment getInstance(String type){
        SpecialFragment specialFragment = new SpecialFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type",type);
        specialFragment.setArguments(bundle);
        return specialFragment;
    }
    @Override
    protected Object setLayout() {
        return R.layout.activity_old2;
    }

    @Override
    protected void initView(View view) {
        String type = getArguments().getString("type");
        AppCompatTextView tvTitle = view.findViewById(R.id.tv_title);
        if (TYPE_OLD.equals(type)){
            tvTitle.setText("变老相机");
        }else if (TYPE_COMIC.equals(type)){
            tvTitle.setText("一键漫画脸");
        }else if (TYPE_GENDER.equals(type)){
            tvTitle.setText("性别转换");
        }else if (TYPE_YOUNG.equals(type)){
            tvTitle.setText("童颜相机");
        }

    }

    @Override
    public void onLazyInitView(@Nullable Bundle savedInstanceState) {
       new  Camera2Loader(_mActivity);
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
