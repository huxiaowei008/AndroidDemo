package com.hxw.frame.loader;

import android.support.annotation.Nullable;

import com.hxw.frame.loader.media.AlbumEntity;

import java.util.List;

/**
 * 加载相册任务的回调
 * Created by hxw on 2017/5/16.
 */

public interface IAlbumTaskCallback {
    /**
     * get all album in database
     *
     * @param list album list
     */
    void postAlbumList(@Nullable List<AlbumEntity> list);
}
