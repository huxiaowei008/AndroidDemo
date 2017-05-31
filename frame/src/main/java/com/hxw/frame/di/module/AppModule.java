package com.hxw.frame.di.module;

import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v4.util.ArrayMap;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hxw.frame.integration.IRepositoryManager;
import com.hxw.frame.integration.RepositoryManager;

import java.util.Map;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;

/**
 * Created by hxw on 2017/2/8.
 */
@Module
public class AppModule {
    private Application mApplication;

    public AppModule(Application application) {
        this.mApplication = application;
    }

    /**
     * 提供application
     *
     * @return
     */
    @Singleton
    @Provides
    public Application provideApplication() {
        return mApplication;
    }


    /**
     * 提供Gson
     *
     * @return
     */
    @Singleton
    @Provides
    public Gson provideGson(Application application,@Nullable GsonConfiguration configuration) {
        GsonBuilder builder = new GsonBuilder();
        if (configuration!=null) {
            configuration.configGson(application, builder);
        }
        return builder.create();
    }


    public interface GsonConfiguration {

        void configGson(Context context, GsonBuilder builder);
    }

    /**
     * 提供数据来源管理
     *
     * @param repositoryManager
     * @return
     */
    @Singleton
    @Provides
    public IRepositoryManager provideRepositoryManager(RepositoryManager repositoryManager) {
        return repositoryManager;
    }

    /**
     * 用于存放一些整个APP公用的数据,切勿大量存放大容量数据
     *
     * @return
     */
    @Singleton
    @Provides
    public Map<String, Object> provideExtras() {
        return new ArrayMap<>();
    }
}
