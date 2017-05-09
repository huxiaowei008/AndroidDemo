package com.hxw.frame.mvp;

import org.simple.eventbus.EventBus;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;

/**
 * Created by hxw on 2017/2/9.
 */

public class BasePresenter<M extends IModel, V extends IView> implements IPresenter {
    protected final String TAG = this.getClass().getSimpleName();
    protected CompositeDisposable mCompositeDisposable;

    protected M mModel;
    protected V mView;

    public BasePresenter(M model, V rootView) {
        this.mModel = model;
        this.mView = rootView;
        init();
    }

    public BasePresenter(V rootView) {
        this.mView = rootView;
        init();
    }

    public BasePresenter() {
        init();
    }

    /**
     * 初始化
     */
    @Override
    public void init() {
        if (useEventBus()) {//如果要使用eventBus请将此方法返回true
            EventBus.getDefault().register(this);//注册eventbus
        }
    }

    /**
     * 释放资源
     */
    @Override
    public void onDestroy() {
        if (useEventBus()) {//如果要使用eventBus请将此方法返回true
            EventBus.getDefault().unregister(this);//解除注册eventbus
        }
        dispose();//解除订阅
        if (mModel != null) {
            mModel.onDestroy();
            this.mModel = null;
        }
        this.mView = null;
        this.mCompositeDisposable = null;
    }

    /**
     * 是否使用eventBus,默认为不使用(false)，
     *
     * @return
     */
    protected boolean useEventBus() {
        return false;
    }

    /**
     * 添加disposable到compositeDisposable,
     * 统一管理disposable,方便取消订阅
     *
     * @param disposable
     */
    protected void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        //将所有disposable放入,集中处理
        mCompositeDisposable.add(disposable);
    }

    /**
     * rxJava取消订阅
     */
    protected void dispose() {
        if (mCompositeDisposable != null) {
            //保证activity结束时取消所有正在执行的订阅
            mCompositeDisposable.clear();
        }
    }

}
