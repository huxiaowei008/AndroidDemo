package com.hxw.frame.integration;

import android.content.Context;

import com.hxw.frame.utils.Preconditions;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import dagger.Lazy;
import io.rx_cache2.internal.RxCache;
import retrofit2.Retrofit;

/**
 * 用来管理网络请求层,以及数据缓存层,以后可以添加数据库请求层
 * 需要在{@link ConfigModule}的实现类中先inject需要的服务
 * Created by hxw on 2017/4/14.
 */
@Singleton
public class RepositoryManager implements IRepositoryManager {
    private Lazy<Retrofit> mRetrofit;
    private Lazy<RxCache> mRxCache;
    private final Map<String, Object> mRetrofitServiceCache =  new HashMap<>();
    private final Map<String, Object> mCacheServiceCache =  new HashMap<>();

    @Inject
    public RepositoryManager(Lazy<Retrofit> retrofit, Lazy<RxCache> rxCache) {
        this.mRetrofit = retrofit;
        this.mRxCache = rxCache;
    }

    /**
     * 根据传入的Class获取对应的Retrofit service
     *
     * @param service
     * @return
     */
    @Override
    public <T> T getRetrofitService(Class<T> service) {
        T retrofitService;
        synchronized (mRetrofitServiceCache) {
            retrofitService = (T) mRetrofitServiceCache.get(service.getName());
            if (retrofitService == null) {
                retrofitService = mRetrofit.get().create(service);
                mRetrofitServiceCache.put(service.getName(), retrofitService);
            }
        }
        return retrofitService;
    }

    /**
     * 根据传入的Class获取对应的RxCache service
     *
     * @param cache
     * @return
     */
    @Override
    public <T> T getCacheService(Class<T> cache) {
        T cacheService;
        synchronized (mCacheServiceCache) {
            cacheService = (T) mCacheServiceCache.get(cache.getName());
            if (cacheService == null) {
                cacheService = mRxCache.get().using(cache);
                mCacheServiceCache.put(cache.getName(), cacheService);
            }
        }
        return cacheService;
    }

    /**
     * 清理所有缓存
     */
    @Override
    public void clearAllCache() {
        mRxCache.get().evictAll();
    }
}
