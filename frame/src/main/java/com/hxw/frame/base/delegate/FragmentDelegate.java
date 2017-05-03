package com.hxw.frame.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.hxw.frame.base.App;
import com.hxw.frame.base.IFragment;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by hxw on 2017/5/3.
 */

public class FragmentDelegate implements IFragmentDelegate {
    private FragmentManager mFragmentManager;
    private Fragment mFragment;
    private IFragment iFragment;
    private Unbinder mUnBinder;

    public FragmentDelegate(FragmentManager fragmentManager, Fragment fragment) {
        this.mFragmentManager = fragmentManager;
        this.mFragment = fragment;
        this.iFragment = (IFragment) fragment;
    }

    @Override
    public void onAttach(Context context) {

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

    }

    @Nullable
    @Override
    public void onCreateView(View view, @Nullable Bundle savedInstanceState) {
        //绑定到butterknife
        if (view != null)
            mUnBinder = ButterKnife.bind(mFragment, view);
    }

    @Override
    public void onActivityCreate(@Nullable Bundle savedInstanceState) {
        if (iFragment.useEventBus()) {
            EventBus.getDefault().register(mFragment);//注册到事件主线
        }
        iFragment.componentInject(((App) mFragment.getActivity().getApplication()).getAppComponent());
        iFragment.init();
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onResume() {

    }

    @Override
    public void onPause() {

    }

    @Override
    public void onStop() {

    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

    }

    @Override
    public void onDestroyView() {
        if (mUnBinder != Unbinder.EMPTY) {
            mUnBinder.unbind();
        }
    }

    @Override
    public void onDestroy() {
        if (iFragment.useEventBus()) {
            EventBus.getDefault().unregister(mFragment);
        }
        mUnBinder = null;
        iFragment = null;
        mFragment = null;
    }

    @Override
    public void onDetach() {

    }
}
