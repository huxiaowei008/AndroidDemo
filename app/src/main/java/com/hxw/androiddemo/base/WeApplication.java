package com.hxw.androiddemo.base;


import com.hxw.androiddemo.BuildConfig;
import com.hxw.frame.base.BaseApplication;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import timber.log.Timber;

/**
 * Created by hxw on 2017/2/10.
 */

public class WeApplication extends BaseApplication {

    private RefWatcher mRefWatcher;//leakCanary观察器

    @Override
    public void onCreate() {
        super.onCreate();

        //Timber日志打印
        if (BuildConfig.LOG_DEBUG) {
            Timber.plant(new Timber.DebugTree());
        }

        //安装leakCanary检测内存泄露
        this.mRefWatcher = BuildConfig.USE_CANARY ? LeakCanary.install(this) : RefWatcher.DISABLED;

    }


    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        this.mRefWatcher = null;

    }

    /**
     * 获得leakCanary观察器
     *
     * @return
     */
    public RefWatcher getRefWatcher() {
        return this.mRefWatcher;
    }

}
