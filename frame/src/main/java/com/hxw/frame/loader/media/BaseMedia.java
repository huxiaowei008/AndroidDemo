package com.hxw.frame.loader.media;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hxw on 2017/5/15.
 */

public abstract class BaseMedia implements Parcelable {

    protected String mPath;
    protected String mId;
    protected String mSize;

    public BaseMedia() {
    }

    public BaseMedia(String id, String path) {
        mId = id;
        mPath = path;
    }

    public String getId() {
        return mId;
    }

    public long getSize() {
        try {
            long result = Long.valueOf(mSize);
            return result > 0 ? result : 0;
        }catch (NumberFormatException size) {
            return 0;
        }
    }

    public void setId(String id) {
        mId = id;
    }

    public void setSize(String size) {
        mSize = size;
    }

    public String getPath(){
        return mPath;
    }

    public void setPath(String path) {
        mPath = path;
    }


    protected BaseMedia(Parcel in) {
        this.mPath = in.readString();
        this.mId = in.readString();
        this.mSize = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mPath);
        dest.writeString(this.mId);
        dest.writeString(this.mSize);
    }

    @Override
    public int describeContents() {
        return 0;
    }

}
