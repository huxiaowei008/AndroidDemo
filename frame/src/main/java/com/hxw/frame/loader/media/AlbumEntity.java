package com.hxw.frame.loader.media;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by hxw on 2017/5/16.
 */

public class AlbumEntity implements Parcelable {

    public int mCount;
    public boolean mIsSelected;
    public String mBucketId;
    public String mBucketName;
    public List<ImageMedia> mImageList;

    public static AlbumEntity createDefaultAlbum() {
        AlbumEntity result = new AlbumEntity();
        result.mBucketId = "";
        result.mBucketName = "所有相片";
        result.mIsSelected = true;
        return result;
    }

    public AlbumEntity(){
        this.mCount=0;
        this.mIsSelected=false;
        this.mBucketId="";
        this.mBucketName="";
        this.mImageList = new ArrayList<>();
    }

    public boolean hasImages() {
        return mImageList != null && mImageList.size() > 0;
    }

    protected AlbumEntity(Parcel in) {
        this.mBucketId = in.readString();
        this.mCount = in.readInt();
        this.mBucketName = in.readString();
        this.mImageList = new ArrayList<>();
        in.readList(this.mImageList, ImageMedia.class.getClassLoader());
        this.mIsSelected = in.readByte() != 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mBucketId);
        dest.writeInt(this.mCount);
        dest.writeString(this.mBucketName);
        dest.writeList(this.mImageList);
        dest.writeByte(this.mIsSelected ? (byte) 1 : (byte) 0);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<AlbumEntity> CREATOR = new Creator<AlbumEntity>() {
        @Override
        public AlbumEntity createFromParcel(Parcel in) {
            return new AlbumEntity(in);
        }

        @Override
        public AlbumEntity[] newArray(int size) {
            return new AlbumEntity[size];
        }
    };


}
