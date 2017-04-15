package com.hxw.frame.di.module;

import com.google.gson.GsonBuilder;
import com.hxw.frame.http.GlobeHttpHandler;
import com.hxw.frame.http.RequestInterceptor;
import com.hxw.frame.utils.FileUtils;
import com.hxw.frame.utils.NullStringToEmptyAdapterFactory;

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
    Retrofit provideRetrofit(OkHttpClient client, HttpUrl httpUrl) {
        return new Retrofit.Builder()
                .baseUrl(httpUrl)//域名
                .client(client)//设置okhttp
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())//使用rxjava
                .addConverterFactory(GsonConverterFactory.create(new GsonBuilder()
                        .setDateFormat("yyyy-MM-dd HH:mm:ss")
                        .registerTypeAdapterFactory(new NullStringToEmptyAdapterFactory<>())
                        .create()))
                .build();
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
    OkHttpClient provideClient(Interceptor intercept, List<Interceptor> interceptors,
                               final GlobeHttpHandler handler) {
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
        return builder.build();
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
    RxCache provideRxCache(@Named("RxCacheDirectory") File cacheDirectory) {
        return new RxCache
                .Builder()
                .persistence(cacheDirectory, new GsonSpeaker());
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
