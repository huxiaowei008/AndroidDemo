package com.hxw.frame.http;

import android.content.Context;

/**
 * Created by hxw on 2017/2/8.
 */

public interface OnResponseErrorListener {
    void handleResponseError(Context context, Throwable t);

    OnResponseErrorListener EMPTY = new OnResponseErrorListener() {
        @Override
        public void handleResponseError(Context context, Throwable t) {

        }
    };
}
