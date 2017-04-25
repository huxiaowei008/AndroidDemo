package com.hxw.frame.base;

import android.app.Application;
import android.content.Context;

import com.hxw.frame.di.AppComponent;
import com.hxw.frame.di.DaggerAppComponent;
import com.hxw.frame.di.module.AppModule;
import com.hxw.frame.di.module.ClientModule;
import com.hxw.frame.di.module.GlobeConfigModule;
import com.hxw.frame.di.module.ImageModule;
import com.hxw.frame.integration.ActivityLifecycle;
import com.hxw.frame.integration.ConfigModule;
import com.hxw.frame.integration.ManifestParser;
import com.squareup.leakcanary.LeakCanary;

import java.util.List;

import javax.inject.Inject;

/**
 * Created by hxw on 2017/2/7.
 */

public class BaseApplication extends Application {
    protected final String TAG = this.getClass().getSimpleName();
    private AppDelegate mAppDelegate;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mAppDelegate = new AppDelegate(this);
        this.mAppDelegate.onCreate();
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        this.mAppDelegate.onTerminate();
    }

    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    public AppComponent getAppComponent() {
        return mAppDelegate.getAppComponent();
    }
}
