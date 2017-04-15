package com.hxw.frame.http;

/**
 * Created by hxw on 2017/2/8.
 */

public interface OnResponseErrorListener {
    void handleResponseError(Exception e);

    OnResponseErrorListener EMPTY = new OnResponseErrorListener() {
        @Override
        public void handleResponseError(Exception e) {

        }
    };
}
