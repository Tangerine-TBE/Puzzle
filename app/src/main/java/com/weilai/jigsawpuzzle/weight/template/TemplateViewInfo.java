package com.weilai.jigsawpuzzle.weight.template;


import android.graphics.Bitmap;

import com.luck.picture.lib.entity.LocalMedia;
import com.weilai.jigsawpuzzle.keep.IBean;

public class TemplateViewInfo implements IBean {
    private  float mLeftX;
    private  float mLeftY;
    private  float mRightX;
    private  float mRightY;
    private  int mPosition;
    private  boolean mHasPic;
    private  String mUrl;
    private LocalMedia localMedia;
    public LocalMedia getLocalMedia() {
        return localMedia;
    }
    public void setLocalMedia(LocalMedia localMedia) {
        this.localMedia = localMedia;
    }
    public TemplateViewInfo(float leftX,float leftY,float rightX,float rightY,int position,boolean hasPic,String url){
        this.mHasPic = hasPic;
        this.mLeftX = leftX;
        this.mPosition = position;
        this.mRightX = rightX;
        this.mRightY = rightY;
        this.mLeftY = leftY;
        this.mUrl = url;
    }
    public TemplateViewInfo(){

    }
    public void setLeftX(int mLeftX) {
        this.mLeftX = mLeftX;
    }

    public void setLeftY(int mLeftY) {
        this.mLeftY = mLeftY;
    }

    public void setRightX(int mRightX) {
        this.mRightX = mRightX;
    }

    public void setRightY(int mRightY) {
        this.mRightY = mRightY;
    }

    public void setPosition(int mPosition) {
        this.mPosition = mPosition;
    }

    public void setHasPic(boolean mHasPic) {
        this.mHasPic = mHasPic;
    }

    public void setUrl(String mUrl) {
        this.mUrl = mUrl;
    }

    public float getLeftX() {
        return mLeftX;
    }

    public float getLeftY() {
        return mLeftY;
    }

    public float getRightX() {
        return mRightX;
    }

    public float getRightY() {
        return mRightY;
    }

    public int getPosition() {
        return mPosition;
    }

    public boolean hasPic() {
        return mHasPic;
    }

    public String getUrl() {
        return mUrl;
    }
}
