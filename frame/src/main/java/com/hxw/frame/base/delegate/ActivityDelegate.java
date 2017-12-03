package com.hxw.frame.base.delegate;

import android.app.Activity;
import android.os.Bundle;
import android.os.Parcel;
import android.support.annotation.Nullable;

import com.hxw.frame.base.App;
import com.hxw.frame.base.IActivity;
import com.hxw.frame.utils.UIUtils;

import org.simple.eventbus.EventBus;

/**
 *
 * @author hxw
 * @date 2017/5/3
 */

public class ActivityDelegate implements IActivityDelegate {
    private Activity mActivity;
    private IActivity iActivity;

    public ActivityDelegate(Activity activity) {
        this.mActivity = activity;
        this.iActivity = (IActivity) activity;
    }

    @Nullable
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        if (iActivity != null && iActivity.useEventBus()) {
            EventBus.getDefault().register(mActivity);//注册到事件主线
        }
        iActivity.componentInject(UIUtils.getAppComponentFromContext(mActivity));
    }

    @Override
    public void onStart() {

    }

    @Override
    public void onRestart() {

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
    public void onDestroy() {
        if (iActivity != null && iActivity.useEventBus()) {
            EventBus.getDefault().unregister(mActivity);
        }
        this.iActivity = null;
        this.mActivity = null;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {

    }

    private ActivityDelegate(Parcel in) {
        this.mActivity = in.readParcelable(Activity.class.getClassLoader());
        this.iActivity = in.readParcelable(IActivity.class.getClassLoader());
    }

    public static final Creator<IActivityDelegate> CREATOR = new Creator<IActivityDelegate>() {
        @Override
        public ActivityDelegate createFromParcel(Parcel source) {
            return new ActivityDelegate(source);
        }

        @Override
        public ActivityDelegate[] newArray(int size) {
            return new ActivityDelegate[size];
        }
    };
}
