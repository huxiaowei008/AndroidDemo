package com.hxw.frame.di.module;

import android.app.Application;
import android.content.Context;

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
    Retrofit provideRetrofit(Application application, RetrofitConfiguration configuration,
                             OkHttpClient client, HttpUrl httpUrl, Gson gson) {
        Retrofit.Builder builder = new Retrofit.Builder();
        builder.baseUrl(httpUrl)//域名
                .client(client)//设置okhttp
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用rxjava
                .addConverterFactory(GsonConverterFactory.create(gson));////使用Gson
        configuration.configRetrofit(application, builder);
        return builder.build();
//        .create(new GsonBuilder()
//                .setDateFormat("yyyy-MM-dd HH:mm:ss")
//                .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<>())
//                .create()))

    }

    public interface RetrofitConfiguration {
        RetrofitConfiguration EMPTY = new RetrofitConfiguration() {
            @Override
            public void configRetrofit(Context context, Retrofit.Builder builder) {

            }
        };

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
    OkHttpClient provideClient(Application application, OkHttpConfiguration configuration,
                               Interceptor intercept, List<Interceptor> interceptors,
                               final GlobalHttpHandler handler) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(TIME_OUT, TimeUnit.SECONDS)//读取超时
                .writeTimeout(TIME_OUT, TimeUnit.SECONDS)//写入超时
                .connectTimeout(TIME_OUT, TimeUnit.SECONDS)//连接超时
                .addInterceptor(new Interceptor() {
                    @Override
                    public Response intercept(Chain chain) throws IOException {
                        return chain.proceed(handler.onHttpRequestBefore(chain, chain.request()));
                    }
                })
                .addNetworkInterceptor(intercept);
        //如果外部提供了interceptor的数组则遍历添加
        if (interceptors != null && interceptors.size() > 0) {
            for (Interceptor interceptor : interceptors) {
                builder.addInterceptor(interceptor);
            }
        }
        configuration.configOkHttp(application, builder);
        return builder.build();
    }

    public interface OkHttpConfiguration {
        OkHttpConfiguration EMPTY = new OkHttpConfiguration() {
            @Override
            public void configOkHttp(Context context, OkHttpClient.Builder builder) {

            }
        };

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
    RxCache provideRxCache(Application application, RxCacheConfiguration configuration,
                           @Named("RxCacheDirectory") File cacheDirectory) {
        RxCache.Builder builder = new RxCache.Builder();
        configuration.configRxCache(application, builder);
        return builder.persistence(cacheDirectory, new GsonSpeaker());
    }

    public interface RxCacheConfiguration {
        RxCacheConfiguration EMPTY = new RxCacheConfiguration() {
            @Override
            public void configRxCache(Context context, RxCache.Builder builder) {

            }
        };

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
