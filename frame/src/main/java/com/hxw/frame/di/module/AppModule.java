package com.hxw.frame.di.module;

import android.app.Application;

import com.google.gson.Gson;
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
    public Gson provideGson() {
        return new Gson();
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
