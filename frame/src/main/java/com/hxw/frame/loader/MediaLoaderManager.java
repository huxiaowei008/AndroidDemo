package com.hxw.frame.loader;

import android.content.ContentResolver;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentActivity;

import com.hxw.frame.loader.media.ImageMedia;
import com.hxw.frame.utils.ThreadExecutor;

/**
 * 多媒体加载管理器
 * Created by hxw on 2017/5/16.
 */

public class MediaLoaderManager {
    public static final int TYPE_IMAGE = 0;
    public static final int TYPE_VIDEO = 1;
    public static final int TYPE_AUDIO = 2;
    public static final int TYPE_FILE = 3;

    /**
     * 静态内部类,一种单例模式的写法
     */
    private static class MediaHolder {
        private static final MediaLoaderManager INSTANCE = new MediaLoaderManager();
    }

    private MediaLoaderManager() {
    }

    public static MediaLoaderManager getInstance() {
        return MediaHolder.INSTANCE;
    }

    public void loaderImage(FragmentActivity activity, LoaderCallbacks<ImageMedia> resultCallbacks) {
        loaderImage(activity, null, "", resultCallbacks);
    }

    public void loaderImage(FragmentActivity activity, String bucketId,
                            LoaderCallbacks<ImageMedia> resultCallbacks) {
        loaderImage(activity, null, bucketId, resultCallbacks);
    }

    /**
     * 对于多次调用activity.getSupportLoaderManager().initLoader(int id, Bundle args,
     * LoaderManager.LoaderCallbacks<D> callback),id若是一样的,是不会执行callback中的
     * onCreateLoader去创建新的Loader<Cursor>,直接查询,id不一样才会调用去创建新的Loader<Cursor>;
     * @param activity
     * @param args
     * @param bucketId
     * @param resultCallbacks
     */
    public void loaderImage(FragmentActivity activity, Bundle args, String bucketId,
                            LoaderCallbacks<ImageMedia> resultCallbacks) {
        if (bucketId == null || "".equals(bucketId)) {
            activity.getSupportLoaderManager()
                    .initLoader(TYPE_IMAGE, args, new ImageLoader(activity, bucketId, resultCallbacks));
        } else {
            activity.getSupportLoaderManager()
                    .initLoader(Integer.valueOf(bucketId), args, new ImageLoader(activity, bucketId, resultCallbacks));
        }
    }

    /**
     * @param cr       由getActivity().getApplicationContext().getContentResolver()获取
     * @param callback
     */
    public void loaderAlbum(@NonNull final ContentResolver cr, @NonNull final IAlbumTaskCallback callback) {
        ThreadExecutor.getInstance().runWorker(new Runnable() {
            @Override
            public void run() {
                new AlbumTask().start(cr, callback);
            }
        });
    }
}
