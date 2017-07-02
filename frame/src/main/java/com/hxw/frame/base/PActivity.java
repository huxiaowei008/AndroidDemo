package com.hxw.frame.base;

import com.hxw.frame.mvp.IPresenter;

import javax.inject.Inject;

/**
 * 使用P的activity,必须实现dagger注入
 * Created by hxw on 2017/6/30.
 */

public abstract class PActivity<P extends IPresenter> extends BaseActivity {
    @Inject
    protected P mPresenter;


    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {//释放资源
            mPresenter.onDestroy();
        }
        this.mPresenter = null;
    }

}
