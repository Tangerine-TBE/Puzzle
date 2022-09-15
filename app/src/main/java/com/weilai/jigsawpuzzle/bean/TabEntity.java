package com.weilai.jigsawpuzzle.bean;

import com.weilai.jigsawpuzzle.keep.IBean;
import com.weilai.library.listener.CustomTabEntity;

/**
 ** DATE: 2022/9/13
 ** Author:tangerine
 ** Description:
 **/
public class TabEntity implements CustomTabEntity, IBean {
    public String title;
    public int selectedIcon;
    public int unSelectedIcon;

    public TabEntity(String title) {
        this.title = title;
    }
    public TabEntity(String title, int selectedIcon, int unSelectedIcon) {
        this.title = title;
        this.selectedIcon = selectedIcon;
        this.unSelectedIcon = unSelectedIcon;
    }
    @Override
    public String getTabTitle() {
        return title;
    }

    @Override
    public int getTabSelectedIcon() {
        return selectedIcon;
    }

    @Override
    public int getTabUnselectedIcon() {
        return unSelectedIcon;
    }
}
