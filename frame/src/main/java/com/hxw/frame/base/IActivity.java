package com.hxw.frame.base;

import android.os.Bundle;

import com.hxw.frame.di.AppComponent;

/**
 * Created by hxw on 2017/5/3.
 */

public interface IActivity {

    /**
     * @return 返回布局资源ID
     */
    int getLayoutId();

    /**
     * 依赖注入的入口,提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
     *
     * @param appComponent 基础注入器
     */
    void componentInject(AppComponent appComponent);

    /**
     * 初始化，会在onCreate中执行
     */
    void init(Bundle savedInstanceState);

    boolean useEventBus();

    /**
     * 这个Activity是否会使用Fragment,框架会根据这个属性判断是否注册{@link android.support.v4.app.FragmentManager.FragmentLifecycleCallbacks}
     * 如果返回false,那意味着这个Activity不需要绑定Fragment,那你再在这个Activity中绑定继承于 {@link com.hxw.frame.base.BaseFragment} 的Fragment将不起任何作用
     *
     * @return
     */
    boolean useFragment();
}
