package com.hxw.frame.integration;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.view.View;

import com.hxw.frame.base.IActivity;
import com.hxw.frame.base.IFragment;
import com.hxw.frame.base.delegate.ActivityDelegate;
import com.hxw.frame.base.delegate.FragmentDelegate;
import com.hxw.frame.base.delegate.IActivityDelegate;
import com.hxw.frame.base.delegate.IFragmentDelegate;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * Created by hxw on 2017/4/14.
 */
@Singleton
public class ActivityLifecycle implements Application.ActivityLifecycleCallbacks {

    private AppManager mAppManager;
    private Application mApplication;
    private FragmentManager.FragmentLifecycleCallbacks mFragmentLifecycle;//fragment的生命本框架内部代码的实现
    private List<FragmentManager.FragmentLifecycleCallbacks> mFragmentLifecycles;//fragment的生命外部拓展
    private Map<String, Object> mExtras;

    @Inject
    ActivityLifecycle(Application application, AppManager appManager,
                      Map<String, Object> extras) {
        this.mApplication = application;
        this.mAppManager = appManager;
        this.mExtras = extras;
    }

    @Override
    public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
        mAppManager.addActivity(activity);

        if (activity instanceof IActivity && activity.getIntent() != null) {
            IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
            if (activityDelegate == null) {
                activityDelegate = new ActivityDelegate(activity);
                activity.getIntent().putExtra(IActivityDelegate.ACTIVITY_DELEGATE, activityDelegate);
            }
            Timber.d("onCreated" + activityDelegate.toString());
            activityDelegate.onCreate(savedInstanceState);
        }

        boolean useFragment = (activity instanceof IActivity) && ((IActivity) activity).useFragment();
        if (activity instanceof FragmentActivity && useFragment) {
            if (mFragmentLifecycle == null) {
                mFragmentLifecycle = new FragmentLifecycle();
            }
            ((FragmentActivity) activity).getSupportFragmentManager()//注册内部代码
                    .registerFragmentLifecycleCallbacks(mFragmentLifecycle, true);

            if (mFragmentLifecycles == null && mExtras.containsKey(ConfigModule.class.getName())) {
                mFragmentLifecycles = new ArrayList<>();
                List<ConfigModule> modules = (List<ConfigModule>) mExtras
                        .get(ConfigModule.class.getName());
                for (ConfigModule module : modules) {
                    module.injectFragmentLifecycle(mApplication, mFragmentLifecycles);
                }
                mExtras.put(ConfigModule.class.getName(), null);
            }
            //注册拓展的代码
            for (FragmentManager.FragmentLifecycleCallbacks fragmentLifecycle : mFragmentLifecycles) {
                ((FragmentActivity) activity).getSupportFragmentManager()
                        .registerFragmentLifecycleCallbacks(fragmentLifecycle, true);
            }
        }
    }

    @Override
    public void onActivityStarted(Activity activity) {
        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            Timber.d("onStart" + activityDelegate.toString());
            activityDelegate.onStart();
        }
    }

    @Override
    public void onActivityResumed(Activity activity) {
        mAppManager.setCurrentActivity(activity);

        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            Timber.d("onResume" + activityDelegate.toString());
            activityDelegate.onResume();
        }
    }

    @Override
    public void onActivityPaused(Activity activity) {

        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            Timber.d("onPaused" + activityDelegate.toString());
            activityDelegate.onPause();
        }
    }

    @Override
    public void onActivityStopped(Activity activity) {
        if (mAppManager.getCurrentActivity() == activity) {
            mAppManager.setCurrentActivity(null);
        }
        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            Timber.d("onStop" + activityDelegate.toString());
            activityDelegate.onStop();
        }
    }

    @Override
    public void onActivitySaveInstanceState(Activity activity, Bundle outState) {
        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            Timber.d("onSaveInstanceState" + activityDelegate.toString());
            activityDelegate.onSaveInstanceState(outState);
        }
    }

    @Override
    public void onActivityDestroyed(Activity activity) {
        mAppManager.removeActivity(activity);

        IActivityDelegate activityDelegate = fetchActivityDelegate(activity);
        if (activityDelegate != null) {
            Timber.d("onDestroy" + activityDelegate.toString());
            activityDelegate.onDestroy();
            activity.getIntent().removeExtra(IActivityDelegate.ACTIVITY_DELEGATE);
        }
    }

    public void release() {
        mAppManager.release();
        mAppManager = null;
        mExtras = null;
        mApplication = null;

    }

    private IActivityDelegate fetchActivityDelegate(Activity activity) {
        if (activity instanceof IActivity && activity.getIntent() != null) {
            return activity.getIntent()
                    .getParcelableExtra(ActivityDelegate.ACTIVITY_DELEGATE);
        }
        return null;
    }

    private class FragmentLifecycle extends FragmentManager.FragmentLifecycleCallbacks {

        @Override
        public void onFragmentAttached(FragmentManager fm, Fragment f, Context context) {
            super.onFragmentAttached(fm, f, context);
            if (f instanceof IFragment && f.getArguments() != null) {
                IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
                if (fragmentDelegate == null || !fragmentDelegate.isAdded()) {
                    fragmentDelegate = new FragmentDelegate(fm, f);
                    f.getArguments().putParcelable(IFragmentDelegate.FRAGMENT_DELEGATE, fragmentDelegate);
                }
                Timber.d("onFragmentAttach" + fragmentDelegate.toString());
                fragmentDelegate.onAttach(context);
            }
        }

        @Override
        public void onFragmentCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            super.onFragmentCreated(fm, f, savedInstanceState);
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate != null) {
                Timber.d("onFragmentCreate" + fragmentDelegate.toString());
                fragmentDelegate.onCreate(savedInstanceState);
            }
        }

        @Override
        public void onFragmentViewCreated(FragmentManager fm, Fragment f, View v, Bundle savedInstanceState) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState);
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate != null) {
                Timber.d("onFragmentViewCreate" + fragmentDelegate.toString());
                fragmentDelegate.onCreateView(v, savedInstanceState);
            }
        }

        @Override
        public void onFragmentActivityCreated(FragmentManager fm, Fragment f, Bundle savedInstanceState) {
            super.onFragmentActivityCreated(fm, f, savedInstanceState);
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate != null) {
                Timber.d("onFragmentActivityCreate" + fragmentDelegate.toString());
                fragmentDelegate.onActivityCreate(savedInstanceState);
            }
        }

        @Override
        public void onFragmentStarted(FragmentManager fm, Fragment f) {
            super.onFragmentStarted(fm, f);
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate != null) {
                Timber.d("onFragmentStart" + fragmentDelegate.toString());
                fragmentDelegate.onStart();
            }
        }

        @Override
        public void onFragmentResumed(FragmentManager fm, Fragment f) {
            super.onFragmentResumed(fm, f);
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate != null) {
                Timber.d("onFragmentResume" + fragmentDelegate.toString());
                fragmentDelegate.onResume();
            }
        }

        @Override
        public void onFragmentPaused(FragmentManager fm, Fragment f) {
            super.onFragmentPaused(fm, f);
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate != null) {
                Timber.d("onFragmentPause" + fragmentDelegate.toString());
                fragmentDelegate.onPause();
            }
        }

        @Override
        public void onFragmentStopped(FragmentManager fm, Fragment f) {
            super.onFragmentStopped(fm, f);
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate != null) {
                Timber.d("onFragmentStop" + fragmentDelegate.toString());
                fragmentDelegate.onStop();
            }
        }

        @Override
        public void onFragmentSaveInstanceState(FragmentManager fm, Fragment f, Bundle outState) {
            super.onFragmentSaveInstanceState(fm, f, outState);
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate != null) {
                Timber.d("onFragmentSaveInstanceState" + fragmentDelegate.toString());
                fragmentDelegate.onSaveInstanceState(outState);
            }
        }

        @Override
        public void onFragmentViewDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentViewDestroyed(fm, f);
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate != null) {
                Timber.d("onFragmentViewDestroy" + fragmentDelegate.toString());
                fragmentDelegate.onDestroyView();
            }
        }

        @Override
        public void onFragmentDestroyed(FragmentManager fm, Fragment f) {
            super.onFragmentDestroyed(fm, f);
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate != null) {
                Timber.d("onFragmentDestroy" + fragmentDelegate.toString());
                fragmentDelegate.onDestroy();
            }
        }

        @Override
        public void onFragmentDetached(FragmentManager fm, Fragment f) {
            super.onFragmentDetached(fm, f);
            IFragmentDelegate fragmentDelegate = fetchFragmentDelegate(f);
            if (fragmentDelegate != null) {
                Timber.d("onFragmentDetach" + fragmentDelegate.toString());
                fragmentDelegate.onDetach();
                f.getArguments().clear();
            }
        }

        private IFragmentDelegate fetchFragmentDelegate(Fragment fragment) {
            if (fragment instanceof IFragment) {
                return fragment.getArguments() == null ? null : (IFragmentDelegate) fragment.getArguments()
                        .getParcelable(IFragmentDelegate.FRAGMENT_DELEGATE);
            }
            return null;
        }
    }
}
