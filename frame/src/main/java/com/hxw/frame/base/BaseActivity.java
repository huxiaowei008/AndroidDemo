package com.hxw.frame.base;

import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

import com.hxw.frame.di.AppComponent;
import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;


/**
 * Created by hxw on 2017/2/8.
 */

public abstract class BaseActivity extends RxAppCompatActivity {
    protected final String TAG = this.getClass().getSimpleName();
    protected BaseApplication mApplication;
    private Unbinder mUnBinder;


    @Nullable
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mApplication = (BaseApplication) getApplication();

        EventBus.getDefault().register(this);//注册到事件主线

        setContentView(getLayoutId());

        //绑定到butterknife
        mUnBinder = ButterKnife.bind(this);
        componentInject(mApplication.getAppComponent());//依赖注入
        init();
    }

    /**
     * @return 返回布局资源ID
     */
    protected abstract int getLayoutId();

    /**
     * 依赖注入的入口,提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
     *
     * @param appComponent
     */
    protected abstract void componentInject(AppComponent appComponent);

    /**
     * 初始化，会在onCreate中执行
     */
    protected abstract void init();

    protected void fullScreen() {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            // lower api
            View v = this.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mUnBinder != Unbinder.EMPTY) {
            mUnBinder.unbind();
        }
        EventBus.getDefault().unregister(this);
        this.mUnBinder = null;
        this.mApplication = null;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}
