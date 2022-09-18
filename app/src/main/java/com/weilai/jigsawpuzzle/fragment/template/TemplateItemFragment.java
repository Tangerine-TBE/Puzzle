package com.weilai.jigsawpuzzle.fragment.template;

import android.view.View;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseSimpleActivity;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

/**
 ** DATE: 2022/9/14
 ** Author:tangerine
 ** Description: For Template
 **/
public class TemplateItemFragment extends BaseFragment {
    private RecyclerView mRvTemplate;
    @Override
    protected Object setLayout() {
        return R.layout.activity_template;
    }


    @Override
    protected void initView(View view) {
        mRvTemplate = view.findViewById(R.id.rv_template_data);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(_mActivity,2);
        mRvTemplate.setLayoutManager(gridLayoutManager);
    }

    @Override
    protected void initListener(View view) {
    }
}
