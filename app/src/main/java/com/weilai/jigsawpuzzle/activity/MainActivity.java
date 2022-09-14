package com.weilai.jigsawpuzzle.activity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.FragmentAdapter;
import com.weilai.jigsawpuzzle.base.BaseActivity;
import com.weilai.jigsawpuzzle.fragment.CrossDressFragment;
import com.weilai.jigsawpuzzle.fragment.EditImageFragment;
import com.weilai.jigsawpuzzle.fragment.MineFragment;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends BaseActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private int[] tableSelectedIcon = {R.mipmap.bb, R.mipmap.bb, R.mipmap.bb};
    private int[] tableUnSelectedIcon = {R.mipmap.bb, R.mipmap.bb, R.mipmap.bb};

    @Override
    protected Object setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainActivity.this,null);
    }

    @Override
    protected void initView(View view) {
        mTabLayout = view.findViewById(R.id.tl_layout);
        initTabLayout();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CrossDressFragment());
        fragments.add(new EditImageFragment());
        fragments.add(new MineFragment());
        mViewPager = view.findViewById(R.id.layout_content);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments));
        mTabLayout.setupWithViewPager(mViewPager);
        initTabLayout();
    }

    @Override
    protected void initListener(View view) {

    }

    /**
     * * DATE: 2022/9/13
     * * Author:tangerine
     * * Description: Here need @String file
     **/
    private void initTabLayout() {
        String[] tableString = {getString(R.string.cross_tab),
                getString(R.string.edit_image_tab),
                getString(R.string.mine_tab)};
        int tabCount = mTabLayout.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tabChild = mTabLayout.getTabAt(i);
            final View view = LayoutInflater.from(this).inflate(R.layout.item_main_tab, mTabLayout, false);
            ImageView imageView = view.findViewById(R.id.image);
            imageView.setImageResource(tableSelectedIcon[i]);
            TextView textView = view.findViewById(R.id.text);
            textView.setText(tableString[i]);
            tabChild.setCustomView(view);
        }

    }

}