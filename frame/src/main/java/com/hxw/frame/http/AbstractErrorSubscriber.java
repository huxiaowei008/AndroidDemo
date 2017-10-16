package com.hxw.frame.http;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;

/**
 *
 * @author hxw
 * @date 2017/7/20
 */

public abstract class AbstractErrorSubscriber<T> implements Observer<T> {


    private ErrorHandler handler;

    protected AbstractErrorSubscriber(ErrorHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onError(@NonNull Throwable e) {
        handler.handleError(e);
    }
}