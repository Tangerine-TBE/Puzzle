package com.weilai.jigsawpuzzle.weight.template;


import android.graphics.Bitmap;

import com.luck.picture.lib.entity.LocalMedia;
import com.weilai.jigsawpuzzle.keep.IBean;

public class TemplateViewInfo implements IBean {
    private  int mLeftX;
    private  int mLeftY;
    private  int mRightX;
    private  int mRightY;
    private  int mPosition;
    private  boolean mHasPic;
    private  String mUrl;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;

    public LocalMedia getLocalMedia() {
        return localMedia;
    }

    public void setLocalMedia(LocalMedia localMedia) {
        this.localMedia = localMedia;
    }

    private LocalMedia localMedia;
    public TemplateViewInfo(int leftX,int leftY,int rightX,int rightY,int position,boolean hasPic,String url){
        this.mHasPic = hasPic;
        this.mLeftX = leftX;
        this.mPosition = position;
        this.mRightX = rightX;
        this.mRightY = rightY;
        this.mLeftY = leftY;
        this.mUrl = url;
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

    public int getLeftX() {
        return mLeftX;
    }

    public int getLeftY() {
        return mLeftY;
    }

    public int getRightX() {
        return mRightX;
    }

    public int getRightY() {
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
