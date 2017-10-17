package com.hxw.frame.imageloader;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

/**
 * 占位图错误图暂时是内部写死的,需要拓展出来的时候在自行编码
 * Created by hxw on 2017/3/15.
 */

public interface IImageLoader {

    /**
     * 用于显示网络图或者文件夹中的图片
     *
     * @param img
     * @param uri 文件路径或者网络地址
     */
    void displayString(@NonNull ImageView img, @NonNull String uri);

    /**
     * 用于显示资源中的图片
     *
     * @param img
     * @param res
     */
    void displayRes(@NonNull ImageView img, @DrawableRes int res);

    /**
     * 清除内存缓存
     * @param context
     */
    void clear(Context context);
}
