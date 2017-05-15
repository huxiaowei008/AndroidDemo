package com.hxw.frame.di.module;

import android.app.Application;
import android.text.TextUtils;

import com.hxw.frame.http.GlobalHttpHandler;
import com.hxw.frame.http.OnResponseErrorListener;
import com.hxw.frame.utils.FileUtils;
import com.hxw.frame.utils.Preconditions;
import com.hxw.frame.widget.imageloader.IImageLoader;
import com.hxw.frame.widget.imageloader.glide.GlideLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;

/**
 * Created by hxw on 2017/2/8.
 */
@Module
public class GlobalConfigModule {
    private HttpUrl mApiUrl;
    private GlobalHttpHandler mHandler;
    private List<Interceptor> mInterceptors;
    private OnResponseErrorListener mErrorListener;
    private File mCacheFile;
    private IImageLoader mLoaderStrategy;
    private AppModule.GsonConfiguration mGsonConfiguration;
    private ClientModule.RetrofitConfiguration mRetrofitConfiguration;
    private ClientModule.OkHttpConfiguration mOkHttpConfiguration;
    private ClientModule.RxCacheConfiguration mRxCacheConfiguration;

    /**
     * 设置网络全局信息
     *
     * @param builder
     */
    private GlobalConfigModule(Builder builder) {
        this.mApiUrl = builder.apiUrl;
        this.mHandler = builder.handler;
        this.mInterceptors = builder.interceptors;
        this.mErrorListener = builder.onResponseErrorListener;
        this.mCacheFile = builder.cacheFile;
        this.mLoaderStrategy = builder.loaderStrategy;
        this.mRetrofitConfiguration = builder.retrofitConfiguration;
        this.mOkHttpConfiguration = builder.okHttpConfiguration;
        this.mRxCacheConfiguration = builder.rxCacheConfiguration;
        this.mGsonConfiguration = builder.gsonConfiguration;

    }

    public static Builder builder() {
        return new Builder();
    }

    @Singleton
    @Provides
    List<Interceptor> provideInterceptors() {
        return mInterceptors;
    }


    @Singleton
    @Provides
    HttpUrl provideBaseUrl() {
        return mApiUrl;
    }


    /**
     * 打印请求信息
     *
     * @return
     */
    @Singleton
    @Provides
    GlobalHttpHandler provideGlobeHttpHandler() {
        return mHandler == null ? GlobalHttpHandler.EMPTY : mHandler;
    }


    /**
     * 提供缓存地址
     *
     * @param application
     * @return
     */
    @Singleton
    @Provides
    File provideCacheFile(Application application) {
        return mCacheFile == null ? FileUtils.getCacheFile(application) : mCacheFile;
    }

    /**
     * 图片加载框架默认使用glide
     *
     * @return
     */
    @Singleton
    @Provides
    IImageLoader provideImageLoaderStrategy() {
        return mLoaderStrategy == null ? new GlideLoader() : mLoaderStrategy;
    }

    @Singleton
    @Provides
    AppModule.GsonConfiguration provideGsonConfiguration() {
        return mGsonConfiguration == null ? AppModule.GsonConfiguration.EMPTY : mGsonConfiguration;
    }

    @Singleton
    @Provides
    ClientModule.RetrofitConfiguration provideRetrofitConfiguration() {
        return mRetrofitConfiguration == null ? ClientModule.RetrofitConfiguration.EMPTY : mRetrofitConfiguration;
    }

    @Singleton
    @Provides
    ClientModule.OkHttpConfiguration provideOkhttpConfiguration() {
        return mOkHttpConfiguration == null ? ClientModule.OkHttpConfiguration.EMPTY : mOkHttpConfiguration;
    }

    @Singleton
    @Provides
    ClientModule.RxCacheConfiguration provideRxCacheConfiguration() {
        return mRxCacheConfiguration == null ? ClientModule.RxCacheConfiguration.EMPTY : mRxCacheConfiguration;
    }


    /**
     * 提供处理Rxjava错误的管理器的回调
     * 暂时没什么用
     *
     * @return
     */
    @Singleton
    @Provides
    OnResponseErrorListener provideResponseErrorListener() {
        return mErrorListener == null ? OnResponseErrorListener.EMPTY : mErrorListener;
    }

    public static final class Builder {
        private HttpUrl apiUrl = HttpUrl.parse("https://api.github.com/");
        private GlobalHttpHandler handler;
        private List<Interceptor> interceptors = new ArrayList<>();
        private OnResponseErrorListener onResponseErrorListener;
        private File cacheFile;
        private IImageLoader loaderStrategy;
        private AppModule.GsonConfiguration gsonConfiguration;
        private ClientModule.RetrofitConfiguration retrofitConfiguration;
        private ClientModule.OkHttpConfiguration okHttpConfiguration;
        private ClientModule.RxCacheConfiguration rxCacheConfiguration;

        private Builder() {
        }

        public Builder baseUrl(String baseUrl) {//基础url
            if (TextUtils.isEmpty(baseUrl)) {
                throw new IllegalArgumentException("baseUrl can not be empty");
            }
            this.apiUrl = HttpUrl.parse(baseUrl);
            return this;
        }

        //用来处理http响应结果
        public Builder globeHttpHandler(GlobalHttpHandler handler) {
            this.handler = handler;
            return this;
        }

        //动态添加任意个interceptor
        public Builder addInterceptor(Interceptor interceptor) {
            this.interceptors.add(interceptor);
            return this;
        }

        //处理所有Rxjava的onError逻辑
        public Builder responseErrorListener(OnResponseErrorListener listener) {
            this.onResponseErrorListener = listener;
            return this;
        }


        public Builder cacheFile(File cacheFile) {
            this.cacheFile = cacheFile;
            return this;
        }

        //用来请求网络图片
        public Builder imageLoaderStrategy(IImageLoader loaderStrategy) {
            this.loaderStrategy = loaderStrategy;
            return this;
        }

        //retrofit补充配置
        public Builder retrofitConfiguration(ClientModule.RetrofitConfiguration retrofitConfiguration) {
            this.retrofitConfiguration = retrofitConfiguration;
            return this;
        }

        //okHttp补充配置
        public Builder okhttpConfiguration(ClientModule.OkHttpConfiguration okHttpConfiguration) {
            this.okHttpConfiguration = okHttpConfiguration;
            return this;
        }

        //rxCache补充配置
        public Builder rxCacheConfiguration(ClientModule.RxCacheConfiguration rxCacheConfiguration) {
            this.rxCacheConfiguration = rxCacheConfiguration;
            return this;
        }

        //Gson补充配置
        public Builder gsonConfiguration(AppModule.GsonConfiguration gsonConfiguration) {
            this.gsonConfiguration = gsonConfiguration;
            return this;
        }

        public GlobalConfigModule build() {
            Preconditions.checkNotNull(apiUrl, "baseUrl is required");
            return new GlobalConfigModule(this);
        }


    }
}
