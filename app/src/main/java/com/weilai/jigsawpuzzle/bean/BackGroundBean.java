package com.weilai.jigsawpuzzle.bean;

import com.weilai.jigsawpuzzle.keep.IBean;

/**
 ** DATE: 2022/10/19
 ** Author:tangerine
 ** Description:
 **/
public class BackGroundBean implements IBean {
    private  String id;
    private String name;
    private String imgBig;
    private int imgSmall;
    private boolean needVip;

    public BackGroundBean(String id, String name, String imgBig, int imgSmall, boolean needVip) {
        this.id = id;
        this.name = name;
        this.imgBig = imgBig;
        this.imgSmall = imgSmall;
        this.needVip = needVip;
    }
    public BackGroundBean(String id, String name, String imgBig, boolean needVip) {
        this.id = id;
        this.name = name;
        this.imgBig = imgBig;
        this.needVip = needVip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImgBig() {
        return imgBig;
    }

    public void setImgBig(String imgBig) {
        this.imgBig = imgBig;
    }

    public int getImgSmall() {
        return imgSmall;
    }

    public void setImgSmall(int imgSmall) {
        this.imgSmall = imgSmall;
    }

    public boolean isNeedVip() {
        return needVip;
    }

    public void setNeedVip(boolean needVip) {
        this.needVip = needVip;
    }
}
