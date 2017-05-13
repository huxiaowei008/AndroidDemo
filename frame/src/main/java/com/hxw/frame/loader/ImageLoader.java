package com.hxw.frame.loader;

import android.content.Context;
import android.net.Uri;
import android.provider.MediaStore;
import android.support.v4.content.CursorLoader;

/**
 * 图片加载,可以理解为数据库查询
 * Created by hxw on 2017/5/13.
 */

public class ImageLoader extends CursorLoader {
    private static final String[] IMAGE_PROJECTION = {//所要查询的字段
            //Base File
            MediaStore.Images.Media._ID,//唯一的id
            MediaStore.Images.Media.TITLE,//内容的标题
            MediaStore.Images.Media.DATA,//磁盘上文件的路径
            MediaStore.Images.Media.SIZE,//文件大小，以字节为单位
            MediaStore.Images.Media.BUCKET_ID,
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,
            MediaStore.Images.Media.DATE_ADDED,//文件被添加的时间
            //Image File
            MediaStore.Images.Media.ORIENTATION
    };
    private static final String IMAGE_SELECTION = MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=? or " + MediaStore.Images.Media.MIME_TYPE + "=?";//sql语句条件查询
    private static final String[] IMAGE_SELECTION_ARGS = {"image/jpeg", "image/png", "image/jpg", "image/gif"};//查询条件中问号对应的值
    private static final String IMAGE_SORT_ORDER = MediaStore.Images.Media.DATE_MODIFIED + " desc";//根据文件最后修改时间来排序

    public ImageLoader(Context context) {
        this(context, MediaStore.Images.Media.EXTERNAL_CONTENT_URI, IMAGE_PROJECTION,
                IMAGE_SELECTION, IMAGE_SELECTION_ARGS, IMAGE_SORT_ORDER);
    }


    public ImageLoader(Context context, Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        super(context, uri, projection, selection, selectionArgs, sortOrder);
    }
}
