package com.hxw.frame.integration;

/**
 *
 * @author hxw
 * @date 2017/4/14
 */

public interface IRepositoryManager {

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

    /**
     * 清理所有缓存
     */
    void clearAllCache();
}
