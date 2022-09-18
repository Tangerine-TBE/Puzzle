package com.weilai.jigsawpuzzle.activity.main;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.template.TemplateShowFragment;

public class TemplateBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
//        return new TemplateItemFragment();
        return TemplateShowFragment.getInstance("xxx");
    }
}
