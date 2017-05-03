package com.hxw.frame.di.module;

import android.app.Application;
import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.hxw.frame.integration.IRepositoryManager;
import com.hxw.frame.integration.RepositoryManager;

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
    public Gson provideGson(Application application, GsonConfiguration configuration) {
        GsonBuilder builder=new GsonBuilder();
        configuration.configGson(application,builder);
        return builder.create();
    }


    public interface GsonConfiguration {
        GsonConfiguration EMPTY = new GsonConfiguration() {
            @Override
            public void configGson(Context context, GsonBuilder builder) {

            }
        };

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
}
