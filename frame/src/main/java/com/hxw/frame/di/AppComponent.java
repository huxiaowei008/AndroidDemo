package com.hxw.frame.di;

import android.app.Application;

import com.google.gson.Gson;
import com.hxw.frame.base.delegate.AppDelegate;
import com.hxw.frame.di.module.AppModule;
import com.hxw.frame.di.module.ClientModule;
import com.hxw.frame.di.module.GlobalConfigModule;
import com.hxw.frame.integration.AppManager;
import com.hxw.frame.integration.IRepositoryManager;
import com.hxw.frame.update.UpdateManager;
import com.hxw.frame.widget.imageloader.ImageLoader;

import java.io.File;

import javax.inject.Singleton;

import dagger.Component;
import okhttp3.OkHttpClient;

/**
 * Created by hxw on 2017/2/8.
 */
@Singleton
@Component(modules = {AppModule.class, ClientModule.class,
        GlobalConfigModule.class})
public interface AppComponent {

    Application applicaton();

    //用于管理所有activity
    AppManager appManager();

    //用于管理网络请求层,以及数据缓存层
    IRepositoryManager repositoryManager();

    //用于自动更新的管理类
    UpdateManager updateManager();

    //gson
    Gson gson();

    OkHttpClient okHttpClient();

    //API管理器,retrofitApi

    //缓存文件根目录(RxCache和Glide的的缓存都已经作为子文件夹在这个目录里),
    //应该将所有缓存放到这个根目录里,便于管理和清理,可在GlobeConfigModule里配置
    File cacheFile();

    //图片加载器
    ImageLoader imageLoader();

    void inject(AppDelegate delegate);
}
