package com.hxw.frame.widget.imageloader;

import android.content.Context;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by hxw on 2017/3/15.
 */
@Singleton
public class ImageLoader {
    private IImageLoaderStrategy mStrategy;

    @Inject
    public ImageLoader(IImageLoaderStrategy strategy) {
        this.mStrategy = strategy;
    }


    public <T extends ImageConfig> void loadImage(Context context, T config) {
        this.mStrategy.loadImage(context, config);
    }


    public <T extends ImageConfig> void clear(Context context, T config) {
        this.mStrategy.clear(context, config);
    }
}
