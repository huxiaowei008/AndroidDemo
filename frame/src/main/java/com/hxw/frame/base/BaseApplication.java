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

public abstract class BaseApplication extends Application {
    protected final String TAG = this.getClass().getSimpleName();
    private static BaseApplication mApplication;
    private AppComponent mAppComponent;
    @Inject
    protected ActivityLifecycle mActivityLifecycle;

    @Override
    public void onCreate() {
        super.onCreate();
        if (LeakCanary.isInAnalyzerProcess(this)) {
            // This process is dedicated to LeakCanary for heap analysis.
            // You should not init your app in this process.
            return;
        }
        mApplication = this;
//解析清单文件配置的自定义ConfigModule的metadata标签，返回一个ConfigModule集合
        List<ConfigModule> modules = new ManifestParser(this).parse();

        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(this))
                .clientModule(new ClientModule())
                .imageModule(new ImageModule())
                .globeConfigModule(getGlobeConfigModule(this, modules))
                .build();
        mAppComponent.inject(this);

        for (ConfigModule module : modules) {
            module.registerComponents(this, mAppComponent.repositoryManager());
        }

        registerActivityLifecycleCallbacks(mActivityLifecycle);
    }

    /**
     * 程序终止的时候执行
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        if (mActivityLifecycle != null) {//释放资源
            unregisterActivityLifecycleCallbacks(mActivityLifecycle);
            mActivityLifecycle.release();
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mApplication = null;

    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     *
     * @return
     */
    private GlobeConfigModule getGlobeConfigModule(Application context, List<ConfigModule> modules) {
        GlobeConfigModule.Builder builder = GlobeConfigModule.builder();
        //循环集合，执行ConfigModule 实现类中的方法
        for (ConfigModule module : modules) {
            module.applyOptions(context, builder);
        }

        return builder.build();
    }

    /**
     * 将AppComponent返回出去,供其它地方使用, AppComponent接口中声明的方法返回的实例,
     * 在getAppComponent()拿到对象后都可以直接使用
     *
     * @return
     */
    public AppComponent getAppComponent() {
        return mAppComponent;
    }

    /**
     * 返回上下文
     *
     * @return
     */
    public static Context getContext() {
        return mApplication;
    }

}
