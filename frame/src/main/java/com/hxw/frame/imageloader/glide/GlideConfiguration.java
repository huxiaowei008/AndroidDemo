package com.hxw.frame.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.Registry;
import com.bumptech.glide.annotation.GlideModule;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.AppGlideModule;
import com.hxw.frame.base.BaseApplication;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.utils.FileUtils;
import com.hxw.frame.utils.UIUtils;

import java.io.File;
import java.io.InputStream;

/**
 * Created by hxw on 17/2/13.
 */
@GlideModule
public class GlideConfiguration extends AppGlideModule {

    @Override
    public boolean isManifestParsingEnabled() {
        return false;
    }

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        final AppComponent appComponent = UIUtils.getAppComponentFromContext(context);
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                // Careful: the external cache directory doesn't enforce permissions
                //图片磁盘缓存文件最大值为100Mb
                int IMAGE_DISK_CACHE_MAX_SIZE = 100 * 1024 * 1024;
                File cacheDirectory = new File(appComponent.cacheFile(), "Glide");
                return DiskLruCacheWrapper.get(FileUtils.makeDirs(cacheDirectory),
                        IMAGE_DISK_CACHE_MAX_SIZE);
            }
        });
        MemorySizeCalculator calculator = new MemorySizeCalculator.Builder(context)
                .build();
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));

//        builder.setMemoryCache(new LruResourceCache(20 * 1024 * 1024));//内存缓存20M
//        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);//Bitmap格式变化,RGB_565或ARGB_8888
    }

    @Override
    public void registerComponents(Context context, Glide glide, Registry registry) {
        //Glide默认使用HttpURLConnection做网络请求,
        //用了OkHttpUrlLoader.Factory()后会换成OKhttp请求，在这放入我们自己创建的Okhttp
        AppComponent appComponent = UIUtils.getAppComponentFromContext(context);
        registry.replace(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(appComponent.okHttpClient()));
    }
}
