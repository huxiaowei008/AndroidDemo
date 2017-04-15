package com.hxw.androiddemo.api;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import retrofit2.Retrofit;

/**
 * 网络接口模块提供,可以建立不同的网络接口在这里提供,写法一样
 * Created by hxw on 2017/2/11.
 */
@Module
public class APIModule {

    /**
     * 提供comAPI
     *
     * @param retrofit
     * @return
     */
    @Singleton
    @Provides
    ComAPI provideComAPI(Retrofit retrofit) {
        return retrofit.create(ComAPI.class);
    }
}
