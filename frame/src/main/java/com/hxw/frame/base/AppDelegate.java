package com.hxw.frame.base;

import android.app.Application;

import com.hxw.frame.di.AppComponent;
import com.hxw.frame.di.DaggerAppComponent;
import com.hxw.frame.di.module.AppModule;
import com.hxw.frame.di.module.ClientModule;
import com.hxw.frame.di.module.GlobeConfigModule;
import com.hxw.frame.di.module.ImageModule;
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
    private Application mApplication;
    private AppComponent mAppComponent;

    private final List<ConfigModule> mModules;
    private List<Lifecycle> mLifecycles = new ArrayList<>();
    @Inject
    protected ActivityLifecycle mActivityLifecycle;

    public AppDelegate(Application application){
        this.mApplication=application;
        //解析清单文件配置的自定义ConfigModule的metadata标签，返回一个ConfigModule集合
        this.mModules = new ManifestParser(mApplication).parse();
        for (ConfigModule module : mModules) {
            module.injectAppLifecycle(mApplication, mLifecycles);
        }
    }

    public void onCreate(){
        mAppComponent = DaggerAppComponent
                .builder()
                .appModule(new AppModule(mApplication))////提供application
                .clientModule(new ClientModule())//用于提供okhttp和retrofit的单例
                .imageModule(new ImageModule())//图片加载框架默认使用glide
                .globeConfigModule(getGlobeConfigModule(mApplication, mModules))
                .build();
        mAppComponent.inject(this);

        mApplication.registerActivityLifecycleCallbacks(mActivityLifecycle);

        for (ConfigModule module : mModules) {
            module.registerComponents(mApplication, mAppComponent.repositoryManager());
        }

        for (Lifecycle lifecycle : mLifecycles) {
            lifecycle.onCreate(mApplication);
        }
    }

    public void onTerminate(){
        if (mActivityLifecycle != null) {//释放资源
            mApplication.unregisterActivityLifecycleCallbacks(mActivityLifecycle);
            mActivityLifecycle.release();
        }
        this.mAppComponent = null;
        this.mActivityLifecycle = null;
        this.mApplication = null;

        for (Lifecycle lifecycle : mLifecycles) {
            lifecycle.onTerminate(mApplication);
        }
    }

    /**
     * 将app的全局配置信息封装进module(使用Dagger注入到需要配置信息的地方)
     * 需要在AndroidManifest中声明{@link ConfigModule}的实现类,和Glide的配置方式相似
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

    public interface Lifecycle {
        void onCreate(Application application);

        void onTerminate(Application application);
    }
}
