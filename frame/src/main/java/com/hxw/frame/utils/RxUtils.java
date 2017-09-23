package com.hxw.frame.utils;

import android.support.annotation.NonNull;

import com.hxw.frame.base.IActivity;
import com.hxw.frame.base.IFragment;
import com.hxw.frame.integration.lifecycle.ActivityLifecycleable;
import com.hxw.frame.integration.lifecycle.FragmentLifecycleable;
import com.hxw.frame.mvp.IView;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;
import com.trello.rxlifecycle2.android.RxLifecycleAndroid;

import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.functions.Function;
import io.reactivex.schedulers.Schedulers;

/**
 * rx的工具类
 * Created by hxw on 2017/2/23.
 */

public class RxUtils {

    private RxUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    /**
     * 绑定生命周期
     *
     * @param <T>
     * @param view
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(IView view) {
        if (view instanceof ActivityLifecycleable) {
            return RxLifecycleAndroid.bindActivity(((ActivityLifecycleable) view).provideLifecycleSubject());
        } else if (view instanceof FragmentLifecycleable) {
            return RxLifecycleAndroid.bindFragment(((FragmentLifecycleable) view).provideLifecycleSubject());
        } else {
            throw new IllegalArgumentException("view isn't activity or fragment");
        }
    }

    /**
     * 与指定的activity生命周期绑定
     *
     * @param view
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(IActivity view,
                                                             @NonNull ActivityEvent event) {
        if (view instanceof ActivityLifecycleable) {
            return ((ActivityLifecycleable) view).bindUntilEvent(event);
        } else {
            throw new IllegalArgumentException("view isn't activity");
        }
    }

    /**
     * 与指定的fragment生命周期绑定
     *
     * @param view
     * @param event
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindUntilEvent(IFragment view,
                                                             @NonNull FragmentEvent event) {
        if (view instanceof FragmentLifecycleable) {
            return ((FragmentLifecycleable) view).bindUntilEvent(event);
        } else {
            throw new IllegalArgumentException("view isn't fragment");
        }
    }

    /**
     * 倒计时
     *
     * @param time 计时秒数
     * @return
     */
    public static Observable<Integer> countdown(@NonNull int time) {
        if (time < 0) {
            time = 0;
        }
        final int countTime = time;

        return Observable.interval(0, 1, TimeUnit.SECONDS)
                .map(new Function<Long, Integer>() {
                    @Override
                    public Integer apply(Long increaseTime) throws Exception {
                        return countTime - increaseTime.intValue();
                    }
                }).take(countTime + 1)
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
}
