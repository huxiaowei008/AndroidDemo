package com.hxw.frame.http;

import android.content.Context;

/**
 * Created by hxw on 2017/7/20.
 */

public class ErrorHandler {

    private Context mContext;
    private OnResponseErrorListener mErrorListener;

    public ErrorHandler(Context context, OnResponseErrorListener errorListener) {
        this.mContext = context;
        this.mErrorListener = errorListener;
    }

    /**
     * 处理错误
     *
     * @param throwable 错误
     */
    void handleError(Throwable throwable) {
        mErrorListener.handleResponseError(mContext, throwable);
    }
}
