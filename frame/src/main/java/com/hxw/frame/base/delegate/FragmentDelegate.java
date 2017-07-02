package com.hxw.frame.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.hxw.frame.base.App;
import com.hxw.frame.base.IFragment;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;
import timber.log.Timber;

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
        if (iFragment.useEventBus()) {
            EventBus.getDefault().register(mFragment);//注册到事件主线
        }
        iFragment.componentInject(((App) mFragment.getActivity().getApplication()).getAppComponent());
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
        iFragment.init(savedInstanceState);
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
        if (mUnBinder != null && mUnBinder != Unbinder.EMPTY) {
            try {
                mUnBinder.unbind();
            } catch (IllegalStateException e) {
                e.printStackTrace();
                //fix Bindings already cleared
                Timber.w("onDestroyView: " + e.getMessage());
            }
        }
    }

    @Override
    public void onDestroy() {
        if (iFragment != null && iFragment.useEventBus()) {
            EventBus.getDefault().unregister(mFragment);
        }
        this.mUnBinder = null;
        this.mFragmentManager = null;
        this.mFragment = null;
        this.iFragment = null;
    }

    @Override
    public void onDetach() {

    }

    @Override
    public boolean isAdded() {
        return mFragment == null ? false : mFragment.isAdded();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    protected FragmentDelegate(Parcel in) {
        this.mFragmentManager = in.readParcelable(FragmentManager.class.getClassLoader());
        this.mFragment = in.readParcelable(Fragment.class.getClassLoader());
        this.iFragment = in.readParcelable(IFragment.class.getClassLoader());
        this.mUnBinder = in.readParcelable(Unbinder.class.getClassLoader());
    }

    public static final Creator<FragmentDelegate> CREATOR = new Creator<FragmentDelegate>() {
        @Override
        public FragmentDelegate createFromParcel(Parcel source) {
            return new FragmentDelegate(source);
        }

        @Override
        public FragmentDelegate[] newArray(int size) {
            return new FragmentDelegate[size];
        }
    };
}
