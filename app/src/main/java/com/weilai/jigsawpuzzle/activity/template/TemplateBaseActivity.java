package com.weilai.jigsawpuzzle.activity.template;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.template.TemplateItemFragment;
import com.weilai.jigsawpuzzle.fragment.template.TemplateShowFragment;

public class TemplateBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
//        return new TemplateItemFragment();
        return TemplateItemFragment.getInstance();
    }
}