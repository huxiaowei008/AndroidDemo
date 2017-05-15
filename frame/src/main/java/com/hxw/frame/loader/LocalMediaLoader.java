package com.hxw.frame.loader;

import android.content.Context;
import android.database.Cursor;
import android.os.Build;
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
 * 本地多媒体加载回调
 * Created by hxw on 2017/5/13.
 */

public class LocalMediaLoader implements LoaderManager.LoaderCallbacks<Cursor> {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_AUDIO = 2;
    public static final int TYPE_FILE = 3;

    private WeakReference<Context> context;
    private LoaderCallbacks resultCallbacks;
    private int mType = TYPE_IMAGE;


    public LocalMediaLoader(Context context, int type,LoaderCallbacks resultCallbacks) {
        this.context = new WeakReference<>(context);
        this.mType = type;
        this.resultCallbacks=resultCallbacks;
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
        CursorLoader loader = null;
        switch (mType) {
            case TYPE_IMAGE:
                loader = new ImageLoader(context.get());
                break;
            case TYPE_VIDEO:
//                mLoader = new VideoLoader(context.get());
                break;
            case TYPE_AUDIO:
//                mLoader = new AudioLoader(context.get());
                break;
            case TYPE_FILE:
//                mLoader = new FileLoader(context.get());
                break;
            default:
                break;
        }
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
        if (data == null) return;
        switch (mType) {
            case TYPE_IMAGE:
                onImageResult(data);
                break;
            case TYPE_VIDEO:
//                onVideoResult(data);
                break;
            case TYPE_AUDIO:
//                onAudioResult(data);
                break;
            case TYPE_FILE:
//                onFileResult(data);
                break;
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

    @SuppressWarnings("unchecked")
    private void onImageResult(Cursor data) {
        List<ImageMedia> result=new ArrayList<>();

       if (data != null && data.moveToFirst()) {
           do {
               int width = 0;
               int height = 0;
               String id = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media._ID));
               String size = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.SIZE));//大小
               String mimeType = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE));//文件类型
               String picPath = data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Media.DATA));//文件地址
               String thumbnailsPath=data.getString(data.getColumnIndexOrThrow(MediaStore.Images.Thumbnails.DATA));
               if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                   width = data.getInt(data.getColumnIndexOrThrow(MediaStore.Images.Media.WIDTH));
                   height = data.getInt(data.getColumnIndexOrThrow(MediaStore.Images.Media.HEIGHT));
               }

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

       if (resultCallbacks!=null){
           resultCallbacks.onResult(result);
       }
    }

}
