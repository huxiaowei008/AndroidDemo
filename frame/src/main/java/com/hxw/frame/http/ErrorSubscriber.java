package com.hxw.frame.http;

import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;

/**
 * Created by hxw on 2017/7/20.
 */

public abstract class ErrorSubscriber<T> implements Observer<T> {


    private ErrorHandler handler;

    protected ErrorSubscriber(ErrorHandler handler) {
        this.handler = handler;
    }

    @Override
    public void onError(@NonNull Throwable e) {
        handler.handleError(e);
    }
}