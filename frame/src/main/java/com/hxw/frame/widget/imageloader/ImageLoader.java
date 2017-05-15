package com.hxw.frame.widget.imageloader;

import android.support.annotation.DrawableRes;
import android.support.annotation.NonNull;
import android.widget.ImageView;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by hxw on 2017/3/15.
 */
@Singleton
public class ImageLoader {
    private IImageLoader mLoader;

    @Inject
    public ImageLoader(IImageLoader loader) {
        this.mLoader = loader;
    }

    public void displayString(@NonNull ImageView img, @NonNull String uri) {
        mLoader.displayString(img, uri);
    }

    public void displayRes(@NonNull ImageView img, @DrawableRes int res) {
        mLoader.displayRes(img, res);
    }

}
