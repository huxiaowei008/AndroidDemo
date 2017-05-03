package com.hxw.frame.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.hxw.frame.base.App;
import com.hxw.frame.base.IActivity;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hxw on 2017/5/3.
 */

public class ActivityDelegate implements IActivityDelegate {
    private Activity mActivity;
    private IActivity iActivity;
    private Unbinder mUnBinder;

    public ActivityDelegate(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (iActivity.useEventBus()) {
            EventBus.getDefault().register(mActivity);//注册到事件主线
        }
        mActivity.setContentView(iActivity.getLayoutId());
        //绑定到butterknife
        mUnBinder = ButterKnife.bind(mActivity);
        iActivity.componentInject(((App) mActivity.getApplication()).getAppComponent());
        iActivity.init();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroy() {
        if (iActivity.useEventBus()) {
            EventBus.getDefault().unregister(mActivity);
        }
        if (mUnBinder != Unbinder.EMPTY) {
            mUnBinder.unbind();
        }
        this.mUnBinder = null;
        this.iActivity = null;
        this.mActivity = null;
    }
}
