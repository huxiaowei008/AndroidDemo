package com.hxw.frame.base;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.support.annotation.Nullable;

import org.simple.eventbus.EventBus;

import io.reactivex.disposables.Disposable;

/**
 * Created by hxw on 2017/2/10.
 */

public abstract class BaseService extends Service {
    protected final String TAG = this.getClass().getSimpleName();

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        EventBus.getDefault().register(this);
        init();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    /**
     * 取消订阅
     *
     * @param disposable
     */
    protected void dispose(Disposable disposable) {
        if (disposable != null && !disposable.isDisposed()) {
            disposable.dispose();//保证service结束时取消所有正在执行的订阅
        }
    }

    /**
     * 初始化
     */
    public abstract void init();
}
