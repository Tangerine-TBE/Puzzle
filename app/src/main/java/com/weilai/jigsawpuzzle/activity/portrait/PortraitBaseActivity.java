package com.weilai.jigsawpuzzle.activity.portrait;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.portrait.PortraitFragment;

/**
 ** DATE: 2022/10/19
 ** Author:tangerine
 ** Description:
 **/
public class PortraitBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
        String data = getIntent().getStringExtra("data");
        return PortraitFragment.getInstance(data);
    }
}
