package com.hxw.frame.loader;

import com.hxw.frame.loader.media.BaseMedia;

import java.util.List;

/**
 * Created by hxw on 2017/5/15.
 */

public interface LoaderCallbacks<T extends BaseMedia> {
    void onResult(List<T> data);
}
