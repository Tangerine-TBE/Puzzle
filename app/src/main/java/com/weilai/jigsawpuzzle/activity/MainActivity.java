package com.weilai.jigsawpuzzle.activity;

import android.graphics.Color;
import android.view.View;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.FragmentAdapter;
import com.weilai.jigsawpuzzle.base.BaseActivity;
import com.weilai.jigsawpuzzle.adapter.ImageBannerAdapter;
import com.weilai.jigsawpuzzle.bean.TabEntity;
import com.weilai.jigsawpuzzle.fragment.CrossDressFragment;
import com.weilai.jigsawpuzzle.fragment.EditImageFragment;
import com.weilai.jigsawpuzzle.fragment.MakeVideoFragment;
import com.weilai.jigsawpuzzle.fragment.MineFragment;
import com.weilai.jigsawpuzzle.fragment.PhotoFragment;
import com.weilai.jigsawpuzzle.util.L;
import com.weilai.jigsawpuzzle.weight.MyTabLayout;
import com.weilai.library.listener.CustomTabEntity;
import com.weilai.library.listener.OnTabSelectListener;
import com.youth.banner.Banner;
import com.youth.banner.indicator.CircleIndicator;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class MainActivity extends BaseActivity {
    private MyTabLayout mMyTabLayout;
    private ViewPager mViewPager;

    @Override
    protected Object setLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initView(View view) {
        mMyTabLayout = view.findViewById(R.id.tab_layout);
        initTabLayout();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CrossDressFragment());
        fragments.add(new EditImageFragment());
        fragments.add(new MakeVideoFragment());
        fragments.add(new PhotoFragment());
        fragments.add(new MineFragment());
        mViewPager = view.findViewById(R.id.layout_content);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments));
    }

    @Override
    protected void initListener(View view) {
        mMyTabLayout.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                mViewPager.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {
                //do not thing
            }
        });
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
                mMyTabLayout.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });

    }

    /**
     * * DATE: 2022/9/13
     * * Author:tangerine
     * * Description: Here need @String file
     **/
    private void initTabLayout() {
        String[] tabLable = {getString(R.string.cross_tab),
                getString(R.string.edit_image_tab),
                getString(R.string.video_tab),
                getString(R.string.photo_tab),
                getString(R.string.mine_tab)};
        ArrayList<CustomTabEntity> title = new ArrayList<>();
        for (String s : tabLable) {
            title.add(new TabEntity(s));
        }
        mMyTabLayout.setTabData(title);
    }

}