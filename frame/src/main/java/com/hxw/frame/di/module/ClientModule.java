package com.hxw.frame.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import com.google.gson.Gson;
import com.hxw.frame.http.GlobalHttpHandler;
import com.hxw.frame.http.RequestInterceptor;
import com.hxw.frame.utils.FileUtils;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.inject.Named;
import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import io.rx_cache2.internal.RxCache;
import io.victoralbertos.jolyglot.GsonSpeaker;
import okhttp3.HttpUrl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hxw on 2017/2/8.
 */
@Module
public class ClientModule {
    //超时时间,默认单位为秒
    private final int TIME_OUT = 10;

    /**
     * 提供retrofit
     *
     * @param client
     * @return
     */
    @Singleton
    @Provides
    Retrofit provideRetrofit(Application application, @Nullable RetrofitConfiguration configuration,
                             OkHttpClient client, HttpUrl httpUrl, Gson gson) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(httpUrl)//域名
                .client(client)//设置okhttp
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用rxjava
                .addConverterFactory(GsonConverterFactory.create(gson));////使用Gson
        if (configuration != null) {
            configuration.configRetrofit(application, builder);
        }
        return builder.build();

    }

    public interface RetrofitConfiguration {

        void configRetrofit(Context context, Retrofit.Builder builder);
    }


    /**
     * 提供OkhttpClient
     *
     * @param intercept
     * @param interceptors
     * @return
     */
    @Singleton
    @Provides
    OkHttpClient provideClient(Application application, @Nullable OkHttpConfiguration configuration,
                               Interceptor intercept, @Nullable List<Interceptor> interceptors,
                               @Nullable final GlobalHttpHandler handler) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)//读取超时
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)//写入超时
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)//连接超时
                .addNetworkInterceptor(intercept);

        if (handler != null) {
            builder.addInterceptor(new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    return chain.proceed(handler.onHttpRequestBefore(chain, chain.request()));
                }
            });
        }
        //如果外部提供了interceptor的数组则遍历添加
        if (interceptors != null && interceptors.size() > 0) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        if (configuration != null) {
            configuration.configOkHttp(application, builder);
        }
        return builder.build();
    }

    public interface OkHttpConfiguration {

        void configOkHttp(Context context, OkHttpClient.Builder builder);
    }

    /**
     * 打印请求信息的拦截器
     *
     * @param intercept
     * @return
     */
    @Singleton
    @Provides
    Interceptor provideIntercept(RequestInterceptor intercept) {
        return intercept;
    }

    /**
     * 提供RXCache客户端
     *
     * @param cacheDirectory RxCache缓存路径
     * @return
     */
    @Singleton
    @Provides
    RxCache provideRxCache(Application application, @Nullable RxCacheConfiguration configuration,
                           @Named("RxCacheDirectory") File cacheDirectory) {
        RxCache.Builder builder = new RxCache.Builder();
        if (configuration != null) {
            configuration.configRxCache(application, builder);
        }
        return builder.persistence(cacheDirectory, new GsonSpeaker());
    }

    public interface RxCacheConfiguration {

        void configRxCache(Context context, RxCache.Builder builder);
    }


    /**
     * 需要单独给RxCache提供缓存路径
     * 提供RxCache缓存地址
     *
     * @param cacheDir
     * @return
     */
    @Singleton
    @Provides
    @Named("RxCacheDirectory")
    File provideRxCacheDirectory(File cacheDir) {
        File cacheDirectory = new File(cacheDir, "RxCache");
        return FileUtils.makeDirs(cacheDirectory);
    }

}
