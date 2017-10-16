package com.hxw.frame.base;

import com.hxw.frame.mvp.IPresenter;

import javax.inject.Inject;

/**
 * 使用P的fragment,必须实现dagger注入
 * Created by hxw on 2017/6/30.
 */

public abstract class BasePFragment<P extends IPresenter> extends BaseFragment {
    @Inject
    protected P mPresenter;

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {//释放资源
            mPresenter.onDestroy();
        }
        this.mPresenter = null;
    }
}
