package com.weilai.jigsawpuzzle.activity.main;

import android.Manifest;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.google.android.material.tabs.TabLayout;
import com.weilai.jigsawpuzzle.R;
import com.weilai.jigsawpuzzle.adapter.main.FragmentAdapter;
import com.weilai.jigsawpuzzle.base.BaseSimpleActivity;
import com.weilai.jigsawpuzzle.fragment.main.CrossDressFragment;
import com.weilai.jigsawpuzzle.fragment.main.EditImageFragment;
import com.weilai.jigsawpuzzle.fragment.main.MineFragment;
import com.weilai.jigsawpuzzle.util.StatusBarUtil;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.EasyPermissions;

public class MainBaseActivity extends BaseSimpleActivity {
    private TabLayout mTabLayout;
    private ViewPager mViewPager;
    private final int[] tableSelectedIcon = {R.mipmap.icon_sel_home, R.mipmap.icon_sel_special, R.mipmap.icon_sel_mine};
    private final int[] tableUnSelectedIcon = {R.mipmap.icon_unl_home, R.mipmap.icon_unl_special, R.mipmap.icon_unl_mine};
    private int mCurrentPosition;

    @Override
    protected Object setLayout() {
        return R.layout.activity_main;
    }

    /**
     * * DATE: 2022/9/14
     * * Author:tangerine
     * * Description:@statusBarAlpha: 0 ~ 255  alpha change options  if you need
     **/
    @Override
    protected void setStatusBar() {
        StatusBarUtil.setTranslucentForImageViewInFragment(MainBaseActivity.this, 0, null);
        StatusBarUtil.setLightMode(this);
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
        /*申请读写权限*/
        String[] permissions = {Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE};
        if (!EasyPermissions.hasPermissions(this, permissions)) {
            EasyPermissions.requestPermissions(this, "申请读写权限", 0, permissions);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }

    /**
     * * DATE: 2022/9/14
     * * Author:tangerine
     * * Description:For listen the page changed or not
     **/
    @Override
    protected void initListener(View view) {
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                resumeTabStatus();
                mCurrentPosition = position;
                resumeTabStatus(position);
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
        mTabLayout.setSelectedTabIndicator(0);
        String[] tableString = {getString(R.string.cross_tab),
                getString(R.string.edit_image_tab),
                getString(R.string.mine_tab)};
        int tabCount = mTabLayout.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tabChild = mTabLayout.getTabAt(i);
            final View view = LayoutInflater.from(this).inflate(R.layout.item_main_tab, mTabLayout, false);
            ImageView imageView = view.findViewById(R.id.image);
            TextView textView = view.findViewById(R.id.text);
            textView.setText(tableString[i]);
            if (i == 0) {
                imageView.setImageResource(tableSelectedIcon[0]);
                textView.setTextColor(getResources().getColor(R.color.sel_text_main_color));
            } else {
                imageView.setImageResource(tableUnSelectedIcon[i]);
                textView.setTextColor(getResources().getColor(R.color.unl_text_main_color));

            }
            assert tabChild != null;
            tabChild.setCustomView(view);
        }

    }

    /**
     * * DATE: 2022/9/14
     * * Author:tangerine
     * * Description: For selected tabView
     **/
    private void resumeTabStatus() {
        TabLayout.Tab tabChild = mTabLayout.getTabAt(mCurrentPosition);
        assert tabChild != null;
        View view = tabChild.getCustomView();
        assert view != null;
        ImageView imageView = view.findViewById(R.id.image);
        TextView textView = view.findViewById(R.id.text);
        textView.setTextColor(getResources().getColor(R.color.unl_text_main_color));
        imageView.setImageResource(tableUnSelectedIcon[mCurrentPosition]);
    }

    private void resumeTabStatus(int position) {
        TabLayout.Tab tabChild = mTabLayout.getTabAt(position);
        assert tabChild != null;
        View view = tabChild.getCustomView();
        assert view != null;
        ImageView imageView = view.findViewById(R.id.image);
        TextView textView = view.findViewById(R.id.text);
        textView.setTextColor(getResources().getColor(R.color.sel_text_main_color));
        imageView.setImageResource(tableSelectedIcon[position]);
    }

}