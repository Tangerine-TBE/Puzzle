package com.weilai.jigsawpuzzle.activity.main;

import com.weilai.jigsawpuzzle.base.BaseActivity;
import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.main.DataFragment;

public class DataBaseActivity extends BaseFragmentActivity {

    @Override
    public BaseFragment setRootFragment() {
        return DataFragment.getInstance();
    }
}
