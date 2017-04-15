package com.hxw.frame.widget.imageloader.glide;

import android.content.Context;

import com.bumptech.glide.Glide;
import com.bumptech.glide.GlideBuilder;
import com.bumptech.glide.load.engine.bitmap_recycle.LruBitmapPool;
import com.bumptech.glide.load.engine.cache.DiskCache;
import com.bumptech.glide.load.engine.cache.DiskLruCacheWrapper;
import com.bumptech.glide.load.engine.cache.LruResourceCache;
import com.bumptech.glide.load.engine.cache.MemorySizeCalculator;
import com.bumptech.glide.load.model.GlideUrl;
import com.bumptech.glide.module.GlideModule;
import com.hxw.frame.base.BaseApplication;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.http.OkHttpUrlLoader;
import com.hxw.frame.utils.FileUtils;

import java.io.File;
import java.io.InputStream;

import static com.hxw.frame.base.Config.IMAGE_DISK_CACHE_MAX_SIZE;

/**
 * Created by hxw on 17/2/13.
 */
public class GlideConfiguration implements GlideModule {

    @Override
    public void applyOptions(Context context, GlideBuilder builder) {
        final AppComponent appComponent = ((BaseApplication) context
                .getApplicationContext()).getAppComponent();
        builder.setDiskCache(new DiskCache.Factory() {
            @Override
            public DiskCache build() {
                // Careful: the external cache directory doesn't enforce permissions

                File cacheDirectory = new File(appComponent.cacheFile(), "Glide");
                return DiskLruCacheWrapper.get(FileUtils.makeDirs(cacheDirectory),
                        IMAGE_DISK_CACHE_MAX_SIZE);
            }
        });

        MemorySizeCalculator calculator = new MemorySizeCalculator(context);
        int defaultMemoryCacheSize = calculator.getMemoryCacheSize();
        int defaultBitmapPoolSize = calculator.getBitmapPoolSize();

        int customMemoryCacheSize = (int) (1.2 * defaultMemoryCacheSize);
        int customBitmapPoolSize = (int) (1.2 * defaultBitmapPoolSize);

        builder.setMemoryCache(new LruResourceCache(customMemoryCacheSize));
        builder.setBitmapPool(new LruBitmapPool(customBitmapPoolSize));
//        builder.setDecodeFormat(DecodeFormat.PREFER_ARGB_8888);//Bitmap格式变化,RGB_565或ARGB_8888
    }

    @Override
    public void registerComponents(Context context, Glide glide) {
        //Glide默认使用HttpURLConnection做网络请求,
        //用了OkHttpUrlLoader.Factory()后会换成OKhttp请求，在这放入我们自己创建的Okhttp
        AppComponent appComponent = ((BaseApplication) context.getApplicationContext()).getAppComponent();
        glide.register(GlideUrl.class, InputStream.class, new OkHttpUrlLoader.Factory(appComponent.okHttpClient()));
    }
}
