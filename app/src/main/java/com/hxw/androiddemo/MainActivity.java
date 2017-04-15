package com.hxw.androiddemo;

import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

public class MainActivity extends BaseActivity {


    /**
     * @return 返回布局资源ID
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 依赖注入的入口,提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
     *
     * @param appComponent
     */
    @Override
    protected void componentInject(AppComponent appComponent) {

    }

    /**
     * 初始化，会在onCreate中执行
     */
    @Override
    protected void init() {

    }
}
