package com.weilai.jigsawpuzzle.bean;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class PicInfo implements Parcelable {
    public   String path;
    public int size;
    public boolean shouldLoad;

    public PicInfo(){

    }
    protected PicInfo(Parcel in) {
        path = in.readString();
        size = in.readInt();
        shouldLoad = in.readByte() != 0;
    }

    public static final Creator<PicInfo> CREATOR = new Creator<PicInfo>() {
        @Override
        public PicInfo createFromParcel(Parcel in) {
            return new PicInfo(in);
        }

        @Override
        public PicInfo[] newArray(int size) {
            return new PicInfo[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(path);
        dest.writeInt(size);
        dest.writeByte((byte) (shouldLoad ? 1 : 0));
    }
}
