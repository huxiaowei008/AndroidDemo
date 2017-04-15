package com.hxw.frame.di.module;

import com.hxw.frame.widget.imageloader.IImageLoaderStrategy;
import com.hxw.frame.widget.imageloader.glide.GlideLoader;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hxw on 2017/4/14.
 */
@Module
public class ImageModule {
    @Singleton
    @Provides
    public IImageLoaderStrategy provideImageLoaderStrategy(GlideLoader glideImageLoader) {
        return glideImageLoader;
    }
}
