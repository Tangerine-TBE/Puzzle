package com.weilai.jigsawpuzzle.net.netInfo;

import com.weilai.jigsawpuzzle.keep.IBean;

import java.util.List;

/**
 * * DATE: 2022/9/15
 * * Author:tangerine
 * * Description:
 **/
public class BitMapInfo implements IBean {

    private String bitmap;
    private String name;
    private String template;
    private List<Size> sizeInfos;

    public String getTemplate() {
        return template;
    }

    public void setTemplate(String template) {
        this.template = template;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public static class Size {
        private float x;//左上角x 占比
        private float y;//左上角y 占比
        private int angle;//旋转角度
        private float center_y;//中心点 占比
        private float center_x;//中心点 占比
        private float ratio_width;//宽
        private float ratio_height;//高
        public Size() {

        }

        public Size(float x, float y, int  angle, float height, float width,float ratio_width,float ratio_height) {
            this.x = x;
            this.y = y;
            this.angle = angle;
            this.center_y = height;
            this.center_x = width;
            this.ratio_width = ratio_width;
            this.ratio_height = ratio_height;
        }

        public float getRatioWidth() {
            return ratio_width;
        }

        public void setRatioWidth(int ratio_width) {
            this.ratio_width = ratio_width;
        }

        public float getRatioHeight() {
            return ratio_height;
        }

        public void setRatioHeight(int ratio_height) {
            this.ratio_height = ratio_height;
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

        public float getCenterY() {
            return center_y;
        }

        public void setCenterY(float centerY) {
            this.center_y = centerY;
        }

        public float getCenterX() {
            return center_x;
        }

        public void setCenterX(float centerX) {
            this.center_x = centerX;
        }

    }

    public BitMapInfo() {

    }

    public BitMapInfo(String bitmap,String name,List<Size> list,String template) {
        this.bitmap = bitmap;
        this.sizeInfos = list;
        this.name = name;
        this.template = template;
    }

    public String getBitmap() {
        return bitmap;
    }

    public List<Size> getSize() {
        return sizeInfos;
    }

    public void setSize(List<Size> sizeInfos) {
        this.sizeInfos = sizeInfos;
    }

    public void setBitmap(String bitmap) {
        this.bitmap = bitmap;
    }


}
