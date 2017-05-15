package com.hxw.frame.loader.media;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by hxw on 2017/5/15.
 */

public class ImageMedia extends BaseMedia implements Parcelable {

    private boolean isChecked;
    private String mMimeType;
    private int mHeight;
    private int mWidth;
    private String mThumbnailPath;

    public ImageMedia(String id, String imagePath) {
        super(id, imagePath);
    }

    public void setChecked(boolean checked) {
        this.isChecked = checked;
    }

    public boolean isChecked() {
        return isChecked;
    }

    public void setMimeType(String mimeType) {
        this.mMimeType = mimeType;
    }

    public String getMimeType() {
        return mMimeType;
    }

    public int getHeight() {
        return mHeight;
    }

    public void setHeight(int mHeight) {
        this.mHeight = mHeight;
    }

    public int getWidth() {
        return mWidth;
    }

    public void setWidth(int mWidth) {
        this.mWidth = mWidth;
    }

    public String getThumbnailPath() {
        return mThumbnailPath;
    }

    public void setThumbnailPath(String mThumbnailPath) {
        this.mThumbnailPath = mThumbnailPath;
    }

    protected ImageMedia(Parcel in) {
        super(in);
        this.isChecked = in.readByte() != 0;
        this.mMimeType = in.readString();
        this.mWidth = in.readInt();
        this.mHeight = in.readInt();
        this.mThumbnailPath = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        super.writeToParcel(dest, flags);
        dest.writeByte(this.isChecked ? (byte) 1 : (byte) 0);
        dest.writeString(this.mMimeType);
        dest.writeInt(this.mHeight);
        dest.writeInt(this.mWidth);
        dest.writeString(this.mThumbnailPath);

    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<ImageMedia> CREATOR = new Creator<ImageMedia>() {
        @Override
        public ImageMedia createFromParcel(Parcel in) {
            return new ImageMedia(in);
        }

        @Override
        public ImageMedia[] newArray(int size) {
            return new ImageMedia[size];
        }
    };



}
