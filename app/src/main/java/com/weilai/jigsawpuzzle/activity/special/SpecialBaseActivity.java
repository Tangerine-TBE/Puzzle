package com.weilai.jigsawpuzzle.activity.special;

import android.content.Intent;

import com.weilai.jigsawpuzzle.base.BaseFragment;
import com.weilai.jigsawpuzzle.base.BaseFragmentActivity;
import com.weilai.jigsawpuzzle.fragment.special.SpecialFragment;

/**
 ** DATE: 2022/10/20
 ** Author:tangerine
 ** Description:
 **/
public class SpecialBaseActivity extends BaseFragmentActivity {
    @Override
    public BaseFragment setRootFragment() {
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        return SpecialFragment.getInstance(type);
    }
}
