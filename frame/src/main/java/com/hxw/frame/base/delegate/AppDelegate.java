package com.hxw.frame.base.delegate;

import android.app.Application;
import android.content.ComponentCallbacks2;
import android.content.res.Configuration;

import com.hxw.frame.di.AppComponent;
import com.hxw.frame.di.DaggerAppComponent;
import com.hxw.frame.di.module.AppModule;
import com.hxw.frame.di.module.ClientModule;
import com.hxw.frame.di.module.GlobalConfigModule;
import com.hxw.frame.integration.ActivityLifecycle;
import com.hxw.frame.integration.ConfigModule;
import com.hxw.frame.integration.ManifestParser;

import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

/**
 * AppDelegate可以代理Application的生命周期,在对应的生命周期,执行对应的逻辑,因为Java只能单继承
 * 而我的框架要求Application要继承于BaseApplication
 * 所以当遇到某些三方库需要继承于它的Application的时候,就只有自定义Application继承于三方库的Application
 * 再将BaseApplication的代码复制进去,而现在就不用在复制代码,只用在对应的生命周期调用AppDelegate对应的方法,
 * Created by hxw on 2017/4/25.
 */

public class AppDelegate {

    //这个activity生命周期回调是本框架内部代码的实现,与外部无关
    @Inject
    protected ActivityLifecycle mActivityLifecycle;
    private Application mApplication;
    private AppComponent mAppComponent;
    private List<Lifecycle> mLifecycles = new ArrayList<>();//application的生命内容外部拓展
    //这里的activity生命周期回调是给外面拓展用的,在外面写好逻辑后通过注册这个直接使用
    private List<Application.ActivityLifecycleCallbacks> mActivityLifecycles = new ArrayList<>();//activity的生命内容外部拓展
    private final List<ConfigModule> mModules;
    private ComponentCallbacks2 mComponentCallback;

    public AppDelegate(Application application) {
        this.mApplication = application;
        //解析清单文件配置的自定义ConfigModule的metadata标签，返回一个ConfigModule集合
        this.mModules = new ManifestParser(mApplication).parse();
        for (ConfigModule module : mModules) {
            module.injectAppLifecycle(mApplication, mLifecycles);
            module.injectActivityLifecycle(mApplication, mActivityLifecycles);
        }
    }

    public void onCreate() {
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(mApplication))////提供application
                .clientModule(new ClientModule())//用于提供okhttp和retrofit的单例
                .globalConfigModule(getGlobeConfigModule(mApplication, mModules))
                .build();
        mAppComponent.inject(this);

        mAppComponent.extras().put(ConfigModule.class.getName(), mModules);

        //注册activity生命周期的回调
        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);
        //注册activity生命周期的回调
        for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
            mApplication.registerActivityLifecycleCallbacks(lifecycle);
        }

        for (ConfigModule module : mModules) {
            module.registerComponents(mApplication, mAppComponent.repositoryManager());
        }

        for (Lifecycle lifecycle : mLifecycles) {
            lifecycle.onCreate(mApplication);
        }

        mComponentCallback = new AppComponentCallbacks(mApplication, mAppComponent);

        mApplication.registerComponentCallbacks(mComponentCallback);
    }

    public void onTerminate() {
        if (mActivityLifecycle != null) {//释放资源
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
            mActivityLifecycle.release();
        }
        if (mComponentCallback != null) {
            mApplication.unregisterComponentCallbacks(mComponentCallback);
        }
        if (mActivityLifecycles != null && mActivityLifecycles.size() > 0) {
            for (Application.ActivityLifecycleCallbacks lifecycle : mActivityLifecycles) {
                mApplication.unregisterActivityLifecycleCallbacks(lifecycle);
            }
        }
        for (Lifecycle lifecycle : mLifecycles) {
            lifecycle.onTerminate(mApplication);
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mActivityLifecycles = null;
        this.mComponentCallback = null;
        this.mLifecycles = null;
        this.mApplication = null;


    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link ConfigModule}的实现类,和Glide的配置方式相似
     *
     * @return
     */
    private GlobalConfigModule getGlobeConfigModule(Application context, List<ConfigModule> modules) {
        GlobalConfigModule.Builder builder = GlobalConfigModule.builder();
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

    public interface Lifecycle {
        void onCreate(Application application);

        void onTerminate(Application application);
    }

    private static class AppComponentCallbacks implements ComponentCallbacks2 {
        private Application mApplication;
        private AppComponent mAppComponent;

        public AppComponentCallbacks(Application application, AppComponent appComponent) {
            this.mApplication = application;
            this.mAppComponent = appComponent;
        }

        @Override
        public void onTrimMemory(int level) {

        }

        @Override
        public void onConfigurationChanged(Configuration newConfig) {

        }

        @Override
        public void onLowMemory() {
            //内存不足时清理图片请求框架的内存缓存
            mAppComponent.imageLoader()
                    .clear(mApplication);
        }
    }
}
