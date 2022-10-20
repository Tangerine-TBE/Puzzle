package com.weilai.jigsawpuzzle.bean;

import com.weilai.jigsawpuzzle.keep.IBean;

/**
 ** DATE: 2022/10/20
 ** Author:tangerine
 ** Description:
 **/
public class BackGroundBean implements IBean {
    private String id;
    private String  imgSmall;
    private String imgSrc;
    private boolean needVip;

    public BackGroundBean(String id, String imgSmall, String imgSrc, boolean needVip) {
        this.id = id;
        this.imgSmall = imgSmall;
        this.imgSrc = imgSrc;
        this.needVip = needVip;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getImgSmall() {
        return imgSmall;
    }

    public void setImgSmall(String imgSmall) {
        this.imgSmall = imgSmall;
    }

    public String getImgSrc() {
        return imgSrc;
    }

    public void setImgSrc(String imgSrc) {
        this.imgSrc = imgSrc;
    }

    public boolean isNeedVip() {
        return needVip;
    }

    public void setNeedVip(boolean needVip) {
        this.needVip = needVip;
    }
}
