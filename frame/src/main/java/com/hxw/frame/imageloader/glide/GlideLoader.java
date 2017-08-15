package com.hxw.frame.imageloader.glide;

import android.content.Context;
import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions;
import com.hxw.frame.R;
import com.hxw.frame.imageloader.IImageLoader;


/**
 * Created by hxw on 2017/3/15.
 */
public class GlideLoader implements IImageLoader {

    /**
     * 用于显示网络图或者文件夹中的图片
     *
     * @param img
     * @param uri 文件路径或者网络地址
     */
    @Override
    public void displayString(@NonNull ImageView img, @NonNull String uri) {
        try {
            // https://github.com/bumptech/glide/issues/1531
            GlideApp.with(img.getContext())
                    .load(uri)
                    .placeholder(R.drawable.ic_placeholder)
                    .error(R.drawable.ic_error)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .centerCrop()
                    .into(img);

        } catch (IllegalArgumentException ignore) {
        }
    }

    /**
     * 用于显示资源中的图片
     *
     * @param img
     * @param res
     */
    @Override
    public void displayRes(@NonNull ImageView img, @DrawableRes int res) {
        GlideApp.with(img.getContext())
                .load(res)
                .placeholder(R.drawable.ic_placeholder)
                .error(R.drawable.ic_error)
                .transition(DrawableTransitionOptions.withCrossFade())
                .centerCrop()
                .into(img);
    }

    /**
     * 清除内存缓存
     *
     * @param context
     */
    @Override
    public void clear(Context context) {
        GlideApp.get(context).clearMemory();
    }
}
