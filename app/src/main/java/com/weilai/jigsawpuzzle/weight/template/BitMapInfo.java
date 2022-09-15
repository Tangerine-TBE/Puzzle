package com.weilai.jigsawpuzzle.weight.template;

import com.weilai.jigsawpuzzle.keep.IBean;

/**
 ** DATE: 2022/9/15
 ** Author:tangerine
 ** Description:
 **/
public class BitMapInfo implements IBean {
    private int x;
    private int y;
    private int width;
    private int height;

    public BitMapInfo(int x, int y, int width, int height) {
        this.x = x;
        this.y = y;
        this.width = width;
        this.height = height;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getWidth() {
        return width;
    }

    public void setWidth(int width) {
        this.width = width;
    }

    public int getHeight() {
        return height;
    }

    public void setHeight(int height) {
        this.height = height;
    }
}
