package com.weilai.jigsawpuzzle.activity.template;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.template.TemplateItemFragment;
import com.weilai.jigsawpuzzle.fragment.template.TemplateShowFragment;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;
import com.weilai.jigsawpuzzle.util.ThemeUtil;

public class TemplateBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
//        return new TemplateItemFragment();
        return TemplateItemFragment.getInstance();
    }

    @Override
    public void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(this, 0, null);
        super.setStatusBar();
    }
}
