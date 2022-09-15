package com.weilai.jigsawpuzzle.bean;

import com.weilai.jigsawpuzzle.keep.IBean;

/**
 ** DATE: 2022/9/14
 ** Author:tangerine
 ** Description:
 **/
public class TemplateEntity implements IBean {
    private String img;
    private String name;

    public TemplateEntity(String img, String name) {
        this.img = img;
        this.name = name;
    }

    public String getImg() {
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
