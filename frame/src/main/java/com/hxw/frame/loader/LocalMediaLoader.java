package com.hxw.frame.loader;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.LoaderManager;
import android.support.v4.content.Loader;

/**
 * 本地多媒体加载回调
 * Created by hxw on 2017/5/13.
 */

public class LocalMediaLoader implements LoaderManager.LoaderCallbacks<Cursor> {


    public LocalMediaLoader() {

    }

    public void loadMedia() {

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
        return null;
    }


    /**
     * 在Loader完成任务后调用,一般在此读取结果
     *
     * @param loader
     * @param data
     */
    @Override
    public void onLoadFinished(Loader<Cursor> loader, Cursor data) {

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
