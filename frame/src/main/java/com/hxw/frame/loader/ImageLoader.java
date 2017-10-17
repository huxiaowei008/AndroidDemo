package com.hxw.frame.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.CursorLoader;
import android.support.v4.content.Loader;

import com.hxw.frame.loader.media.ImageMedia;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

/**
 * 图片加载回调
 * Created by hxw on 2017/5/13.
 */

public class ImageLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    private static final String[] IMAGE_PROJECTION = {//所要查询的字段
            //Base File
            MediaStore.Images.Media._ID,//唯一的id
            MediaStore.Images.Media.TITLE,//内容的标题
            MediaStore.Images.Media.DATA,//磁盘上文件的路径
            MediaStore.Images.Media.SIZE,//文件大小，以字节为单位
            MediaStore.Images.Media.BUCKET_ID,//目录
            MediaStore.Images.Media.BUCKET_DISPLAY_NAME,//目录名称
            MediaStore.Images.Media.DATE_ADDED,//文件被添加的时间
            //Image File
//            MediaStore.Images.Media.ORIENTATION,
            MediaStore.Images.Media.MIME_TYPE,//图片的类型     image/jpeg
            MediaStore.Images.Media.WIDTH,
            MediaStore.Images.Media.HEIGHT,
            MediaStore.Images.Thumbnails.DATA//缩略图地址

    };

    private static final String IMAGE_SORT_ORDER = MediaStore.Images.Media.DATE_MODIFIED + " desc";//根据文件最后修改时间来排序
    private static final int PAGE_LIMIT = 50;//分页查询中一次查50条
    private WeakReference<Context> context;
    private LoaderCallbacks<ImageMedia> resultCallbacks;
    private String bucketId;//目录,在图片中使用到,更具目录查询
//    private int page;//页码,从0开始

    public ImageLoader(Context context, String bucketId, LoaderCallbacks<ImageMedia> resultCallbacks) {
        this.context = new WeakReference<>(context);
        this.bucketId = bucketId;
        this.resultCallbacks = resultCallbacks;
//        this.page = page;
    }


    /**
     * onCreateLoader方法将在创建Loader时候调用，此时需要提供查询的配置,如监听一个URI.
     * 这个方法会在loader初始化的时调用，即调用下面的代码时调用：
     * getLoaderManager().initLoader(id, bundle, loaderCallbacks);
     *
     * @param id
     * @param args
     * @return
     */
    @Override
    public Loader<Cursor> onCreateLoader(int id, Bundle args) {
        String[] selectionArgs;
        String selection;
        if (bucketId==null|| "".equals(bucketId)) {
            selectionArgs = new String[]{//查询条件中问号对应的值
                    "image/jpeg", "image/png", "image/jpg", "image/gif"
            };
            selection = MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=?";//sql语句条件查询
        }else {
            selectionArgs = new String[]{//查询条件中问号对应的值
                    bucketId,"image/jpeg", "image/png", "image/jpg", "image/gif"
            };
            selection = MediaStore.Images.Media.BUCKET_ID + "=? and ("
                    + MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=? or "
                    + MediaStore.Images.Media.MIME_TYPE + "=? )";//sql语句条件查询,会更具目录查询
        }


        CursorLoader loader = new CursorLoader(context.get(),
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                IMAGE_PROJECTION, selection,
                selectionArgs, MediaStore.Images.Media.DATE_MODIFIED + " desc");//最后一个参数是排序
//下面是加了分页的
//MediaStore.Images.Media.DATE_MODIFIED + " desc" + " LIMIT " + page * PAGE_LIMIT + " , " + PAGE_LIMIT
        return loader;
    }


    /**
     * 在Loader完成任务后调用,一般在此读取结果
     *
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {
        if (data == null) {
            return;
        }
        List<ImageMedia> result = new ArrayList<>();

        if (data.moveToFirst()) {
            do {
                int width = 0;
                int height = 0;
                String id = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
                String size = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));//大小
                String mimeType = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));//文件类型
                String picPath = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));//文件地址
                String thumbnailsPath = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
                width = data.getInt(data.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH));
                height = data.getInt(data.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT));

                ImageMedia imageMedia = new ImageMedia(id, picPath);
                imageMedia.setSize(size);
                imageMedia.setMimeType(mimeType);
                imageMedia.setWidth(width);
                imageMedia.setHeight(height);
                imageMedia.setThumbnailPath(thumbnailsPath);

                if (!result.contains(imageMedia)) {
                    result.add(imageMedia);
                }

            } while (!data.isLast() && data.moveToNext());
        }

        if (resultCallbacks != null) {
            resultCallbacks.onResult(result);
        }

    }

    /**
     * onLoaderReset方法是在配置发生变化时调用,一般调用下面的代码后调用:
     * getLoaderManager().restartLoader(id, bundle, loaderCallbacks);
     * restartLoader方法参数同initLoader,重新初始化loader之后,需要用来释放对前面loader查询到的结果引用
     *
     * @param loader
     */
    @Override
    public void onLoaderReset(Loader<Cursor> loader) {

    }

}
