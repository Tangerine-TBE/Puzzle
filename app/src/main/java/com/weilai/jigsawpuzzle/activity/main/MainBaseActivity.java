package com.weilai.jigsawpuzzle.activity.main;

import android.Manifest;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.feisukj.ad.manager.AdController;
import com.feisukj.base.bean.ad.ADConstants;
import com.google.android.material.tabs.TabLayout;
import com.kuaishou.weapon.p0.jni.A;
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
    private ArrayList<Integer> tableSelectedIcon;
    private ArrayList<Integer> tableUnSelectedIcon;
    private ArrayList<String> tableString;
    private int mCurrentPosition;
    private String channel = "";


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
        try {
            ApplicationInfo info = getPackageManager().
                    getApplicationInfo(getPackageName(), PackageManager.GET_META_DATA);
            if (info != null && info.metaData != null) {
                String metaData = info.metaData.getString("CHANNEL_VALUE");
                if (!metaData.isEmpty()) {
                    channel = metaData;
                }
            }
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        tableSelectedIcon = new ArrayList<>();
        tableUnSelectedIcon = new ArrayList<>();
        tableString = new ArrayList<>();
        mTabLayout = view.findViewById(R.id.tl_layout);
        initTabLayout();
        List<Fragment> fragments = new ArrayList<>();
        fragments.add(new CrossDressFragment());
        tableUnSelectedIcon.add(R.mipmap.icon_unl_home);
        tableSelectedIcon.add(R.mipmap.icon_sel_home);
        tableString.add(getString(R.string.cross_tab));
        if (!"_xiaomi".equals(channel)) {
            fragments.add(new EditImageFragment());
            tableSelectedIcon.add(R.mipmap.icon_sel_special);
            tableUnSelectedIcon.add(R.mipmap.icon_unl_special);
            tableString.add(getString(R.string.edit_image_tab));
        }
        fragments.add(new MineFragment());
        tableSelectedIcon.add( R.mipmap.icon_sel_mine);
        tableUnSelectedIcon.add(R.mipmap.icon_unl_mine);
        tableString.add(getString(R.string.mine_tab));

        mViewPager = view.findViewById(R.id.layout_content);
        mViewPager.setOffscreenPageLimit(3);
        mViewPager.setAdapter(new FragmentAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT, fragments));
        mTabLayout.setupWithViewPager(mViewPager);
        initTabLayout();

    }


    @Override
    public void onBackPressedSupport() {
        super.onBackPressedSupport();
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
        int tabCount = mTabLayout.getTabCount();
        for (int i = 0; i < tabCount; i++) {
            TabLayout.Tab tabChild = mTabLayout.getTabAt(i);
            final View view = LayoutInflater.from(this).inflate(R.layout.item_main_tab, mTabLayout, false);
            ImageView imageView = view.findViewById(R.id.image);
            TextView textView = view.findViewById(R.id.text);
            textView.setText(tableString.get(i));
            if (i == 0) {
                imageView.setImageResource(tableSelectedIcon.get(i));
                textView.setTextColor(getResources().getColor(R.color.sel_text_main_color));
            } else {

                imageView.setImageResource(tableUnSelectedIcon.get(i));
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
        imageView.setImageResource(tableUnSelectedIcon.get(mCurrentPosition));
    }

    private void resumeTabStatus(int position) {
        TabLayout.Tab tabChild = mTabLayout.getTabAt(position);
        assert tabChild != null;
        View view = tabChild.getCustomView();
        assert view != null;
        ImageView imageView = view.findViewById(R.id.image);
        TextView textView = view.findViewById(R.id.text);
        textView.setTextColor(getResources().getColor(R.color.sel_text_main_color));
        imageView.setImageResource(tableSelectedIcon.get(position));
    }

}