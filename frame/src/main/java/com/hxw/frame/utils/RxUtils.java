package com.hxw.frame.utils;

import android.support.annotation.NonNull;

import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.base.BaseFragment;
import com.hxw.frame.mvp.IView;
import com.trello.rxlifecycle2.LifecycleTransformer;
import com.trello.rxlifecycle2.android.ActivityEvent;
import com.trello.rxlifecycle2.android.FragmentEvent;

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
     * @param view
     * @param <T>
     * @return
     */
    public static <T> LifecycleTransformer<T> bindToLifecycle(IView view) {
        if (view instanceof BaseActivity) {
            return ((BaseActivity) view).<T>bindToLifecycle();
        } else if (view instanceof BaseFragment) {
            return ((BaseFragment) view).<T>bindToLifecycle();
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
    public static <T> LifecycleTransformer<T> bindUntilEvent(IView view,
                                                             @NonNull ActivityEvent event) {
        if (view instanceof BaseActivity) {
            return ((BaseActivity) view).<T>bindUntilEvent(event);
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
    public static <T> LifecycleTransformer<T> bindUntilEvent(IView view,
                                                             @NonNull FragmentEvent event) {
        if (view instanceof BaseFragment) {
            return ((BaseFragment) view).<T>bindUntilEvent(event);
        } else {
            throw new IllegalArgumentException("view isn't fragment");
        }
    }
}
