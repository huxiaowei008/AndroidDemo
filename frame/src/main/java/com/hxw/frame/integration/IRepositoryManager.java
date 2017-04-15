package com.hxw.frame.integration;

import android.content.Context;

/**
 * Created by hxw on 2017/4/14.
 */

public interface IRepositoryManager {
    /**
     * 注入RetrofitService,在{@link ConfigModule#registerComponents(Context, IRepositoryManager)}中进行注入
     * @param services
     */
    void injectRetrofitService(Class<?>... services);


    /**
     * 注入CacheService,在{@link ConfigModule#registerComponents(Context, IRepositoryManager)}中进行注入
     * @param services
     */
    void injectCacheService(Class<?>... services);


    /**
     * 根据传入的Class获取对应的Retrofit service
     *
     * @param service
     * @param <T>
     * @return
     */
    <T> T getRetrofitService(Class<T> service);

    /**
     * 根据传入的Class获取对应的RxCache service
     *
     * @param cache
     * @param <T>
     * @return
     */
    <T> T getCacheService(Class<T> cache);
}
