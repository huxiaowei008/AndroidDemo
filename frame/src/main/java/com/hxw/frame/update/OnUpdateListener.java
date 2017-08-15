package com.hxw.frame.update;

/**更新结果监听
 * Created by hxw on 2017/2/15.
 */

public interface OnUpdateListener {
    /**
     * 有更新
     */
    void isUpdate();

    /**
     * 没有更新
     */
    void noUpdate();

    /**
     * 错误
     */
    void error(Throwable throwable);
}
