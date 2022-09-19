package com.weilai.jigsawpuzzle.adapter.main;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

/**
 * @description:
 * @author: luo
 * @date: 2022/9/13
 */
public class FragmentAdapter extends FragmentPagerAdapter {
    private List<Fragment> fragments;
    public FragmentAdapter(@NonNull  FragmentManager fm, int behavior, List<Fragment>fragments) {
        super(fm, behavior);
        this.fragments = fragments;
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}
