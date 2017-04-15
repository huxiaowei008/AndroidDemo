package com.hxw.androiddemo.api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;

/**
 * 缓存模块提供,可以建立不同的缓存接口在这里提供,写法一样
 * Created by hxw on 2017/2/11.
 */
@Module
public class CacheModule {

    /**
     * 提供comCache
     *
     * @param rxCache
     * @return
     */
    @Singleton
    @Provides
    ComCache provideComCache(RxCache rxCache) {
        return rxCache.using(ComCache.class);
    }
}
