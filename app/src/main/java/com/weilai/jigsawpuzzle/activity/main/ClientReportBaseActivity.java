package com.weilai.jigsawpuzzle.activity.main;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.main.ClientReportFragment;

public class ClientReportBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
        return ClientReportFragment.getInstance();
    }
}
