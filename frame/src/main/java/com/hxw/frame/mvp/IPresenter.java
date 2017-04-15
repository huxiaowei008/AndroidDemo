package com.hxw.frame.mvp;

/**
 * Created by hxw on 2017/2/9.
 */

public interface IPresenter {
    /**
     * 初始化
     */
    void init();

    /**
     * 释放资源
     */
    void onDestroy();

}
