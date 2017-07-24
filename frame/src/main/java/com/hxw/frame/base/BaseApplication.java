package com.hxw.frame.base;

import android.app.Application;
import android.content.Context;

import com.hxw.frame.base.delegate.AppDelegate;
import com.hxw.frame.base.delegate.AppLifecycles;
import com.hxw.frame.di.AppComponent;

/**
 * Created by hxw on 2017/2/7.
 */

public class BaseApplication extends Application implements App{
    protected final String TAG = this.getClass().getSimpleName();
    private AppLifecycles mAppDelegate;

    /**
     * 这里会在 {@link BaseApplication#onCreate} 之前被调用,可以做一些较早的初始化
     * 常用于 MultiDex 以及插件化框架的初始化
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        this.mAppDelegate = new AppDelegate(base);
        this.mAppDelegate.attachBaseContext(base);
    }


    @Override
    public void onCreate() {
        super.onCreate();
        this.mAppDelegate.onCreate(this);
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        this.mAppDelegate.onTerminate(this);
    }


    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    @Override
    public AppComponent getAppComponent() {
        return ((App) mAppDelegate).getAppComponent();
    }
}
