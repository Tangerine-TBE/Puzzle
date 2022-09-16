package com.weilai.jigsawpuzzle.weight.template;

import android.graphics.Bitmap;

import com.weilai.jigsawpuzzle.keep.IBean;

import java.util.List;

/**
 * * DATE: 2022/9/15
 * * Author:tangerine
 * * Description:
 **/
public class BitMapInfo implements IBean {

    private String bitmap;
    private List<SizeInfo> sizeInfos;
    public static class SizeInfo {
        private float x;
        private float y;
        private int angle;
        private float height;
        private float width;

        public SizeInfo() {

        }

        public SizeInfo(float x, float y, int  angle, float height, float width) {
            this.x = x;
            this.y = y;
            this.angle = angle;
            this.height = height;
            this.width = width;
        }

        public float getX() {
            return x;
        }

        public void setX(float x) {
            this.x = x;
        }

        public float getY() {
            return y;
        }

        public void setY(float y) {
            this.y = y;
        }

        public float getAngle() {
            return angle;
        }

        public void setAngle(int  angle) {
            this.angle = angle;
        }

        public float getHeight() {
            return height;
        }

        public void setHeight(float height) {
            this.height = height;
        }

        public float getWidth() {
            return width;
        }

        public void setWidth(float width) {
            this.width = width;
        }

    }

    public BitMapInfo() {

    }

    public BitMapInfo(String bitmap,List<SizeInfo> list) {
        this.bitmap = bitmap;
        this.sizeInfos = list;
    }

    public String getBitmap() {
        return bitmap;
    }

    public List<SizeInfo> getSizeInfos() {
        return sizeInfos;
    }

    public void setSizeInfos(List<SizeInfo> sizeInfos) {
        this.sizeInfos = sizeInfos;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }


}
