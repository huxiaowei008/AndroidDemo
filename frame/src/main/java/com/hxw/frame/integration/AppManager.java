package com.hxw.frame.integration;

import android.app.Activity;
import android.app.Application;
import android.content.Intent;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.view.View;

import org.simple.eventbus.EventBus;
import org.simple.eventbus.Subscriber;
import org.simple.eventbus.ThreadMode;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import timber.log.Timber;

/**
 * 用于管理所有activity,和在前台的 activity
 * 可以通过直接持有AppManager对象执行对应方法
 * 也可以通过eventBus post事件,远程遥控执行对应方法
 * Created by hxw on 2017/2/8.
 */
@Singleton
public class AppManager {
    protected final String TAG = this.getClass().getSimpleName();
    public static final String APP_MANAGER_MESSAGE = "AppManager_message";
    public static final int START_ACTIVITY = 0;
    public static final int SHOW_SNACK_BAR = 1;
    public static final int KILL_ACTIVITY = 2;
    public static final int EXIT_APP = 3;
    private Application mApplication;

    //管理所有activity
    private List<Activity> mActivityList;
    //当前在前台的activity
    private Activity mCurrentActivity;

    @Inject
    public AppManager(Application application) {
        this.mApplication = application;
        EventBus.getDefault().register(this);
    }

    /**
     * 将在前台的activity保存
     *
     * @param currentActivity
     */
    public void setCurrentActivity(Activity currentActivity) {
        this.mCurrentActivity = currentActivity;
    }

    /**
     * 获得当前在前台的activity
     *
     * @return
     */
    public Activity getCurrentActivity() {
        return mCurrentActivity;
    }

    /**
     * 添加Activity到集合
     *
     * @param activity
     */
    public void addActivity(Activity activity) {
        if (mActivityList == null) {
            mActivityList = new LinkedList<>();
        }
        synchronized (AppManager.class) {
            //判断列表中是否已存在这元素,存在返回true,不存在返回false
            if (!mActivityList.contains(activity)) {
                mActivityList.add(activity);
            }
        }
    }

    /**
     * 返回一个存储所有未销毁的activity的集合
     *
     * @return
     */
    public List<Activity> getActivityList() {
        if (mActivityList == null) {
            mActivityList = new LinkedList<>();
        }
        return mActivityList;
    }

    /**
     * 删除集合里的指定activity
     *
     * @param activity
     */
    public void removeActivity(Activity activity) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("mActivityList == null when removeActivity(Activity)");
            return;
        }
        synchronized (AppManager.class) {
            if (mActivityList.contains(activity)) {
                mActivityList.remove(activity);
            }
        }
    }

    /**
     * 删除集合里的指定位置的activity
     *
     * @param location
     */
    public Activity removeActivity(int location) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("mActivityList == null when removeActivity(int)");
            return null;
        }
        synchronized (AppManager.class) {
            if (location > 0 && location < mActivityList.size()) {
                return mActivityList.remove(location);
            }
        }
        return null;
    }

    /**
     * 关闭指定activity
     *
     * @param activityClass
     */
    private void killActivity(Class<?> activityClass) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("mActivityList == null when killActivity");
            return;
        }
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(activityClass)) {
                activity.finish();
            }
        }
    }

    /**
     * 指定的activity class是否存活(一个activity可能有多个实例)
     *
     * @param activityClass
     * @return
     */
    public boolean activityClassIsLive(Class<?> activityClass) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("mActivityList == null when activityClassIsLive");
            return false;
        }
        for (Activity activity : mActivityList) {
            if (activity.getClass().equals(activityClass)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 指定的activity实例是否存活
     *
     * @param activity
     * @return
     */
    public boolean activityInstanceIsLive(Activity activity) {
        if (mActivityList == null) {
            Timber.tag(TAG).w("mActivityList == null when activityInstanceIsLive");
            return false;
        }
        return mActivityList.contains(activity);
    }

    /**
     * 通过eventBus post事件,远程遥控执行对应方法
     *
     * @param message
     */
    @Subscriber(tag = APP_MANAGER_MESSAGE, mode = ThreadMode.MAIN)
    public void onReceive(Message message) {
        switch (message.what) {
            case START_ACTIVITY:
                if (message.obj == null) {
                    Timber.tag(TAG).w("message.obj is null", message.obj);
                } else {
                    dispatchStart(message);
                }
                break;
            case SHOW_SNACK_BAR:
                if (message.obj == null) {
                    Timber.tag(TAG).w("message.obj is null", message.obj);
                } else {
                    showSnackBar((String) message.obj, message.arg1 != 0);
                }
                break;
            case KILL_ACTIVITY:
                if (message.obj instanceof Class) {
                    killActivity((Class) message.obj);
                } else {
                    Timber.tag(TAG).w("message.obj isn't Class", message.obj);
                }
                break;
            case EXIT_APP:
                exitApp();
                break;
            default:
                break;
        }
    }

    /**
     * 对message中的信息进行分辨，让后启动活动
     *
     * @param message
     */
    private void dispatchStart(Message message) {
        if (message.obj instanceof Intent)
            startActivity((Intent) message.obj);
        else if (message.obj instanceof Class)
            startActivity((Class) message.obj);
        return;
    }

    /**
     * 使用snackBar显示内容
     *
     * @param message
     * @param isLong
     */
    private void showSnackBar(String message, boolean isLong) {
        if (getCurrentActivity() == null) {
            Timber.tag(TAG).w("mCurrentActivity == null when showSnackBar(String,boolean)");
            return;
        }
        View view = getCurrentActivity().getWindow().getDecorView()
                .findViewById(android.R.id.content);
        Snackbar.make(view, message, isLong ? Snackbar.LENGTH_LONG : Snackbar.LENGTH_SHORT).show();
    }

    /**
     * 让在前台的activity,打开下一个activity
     *
     * @param intent
     */
    private void startActivity(Intent intent) {
        if (getCurrentActivity() == null) {
            Timber.tag(TAG).w("mCurrentActivity == null when startActivity(Intent)");
            //如果没有前台的activity就使用new_task模式启动activity
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            mApplication.startActivity(intent);
            return;
        }
        getCurrentActivity().startActivity(intent);
    }

    /**
     * 让在前台的activity,打开下一个activity
     *
     * @param activityClass
     */
    private void startActivity(Class activityClass) {
        if (getCurrentActivity() == null) {
            Timber.tag(TAG).w("mCurrentActivity == null when startActivity(Class)");
            startActivity(new Intent(mApplication, activityClass));
            return;
        }
        startActivity(new Intent(getCurrentActivity(), activityClass));
    }

    /**
     * 释放资源
     */
    public void release() {
        EventBus.getDefault().unregister(this);
        mActivityList.clear();
        mActivityList = null;
        mCurrentActivity = null;
        mApplication = null;
    }

    /**
     * 退出应用程序
     */
    private void exitApp() {
        try {
            //关闭所有activity
            Iterator<Activity> iterator = getActivityList().iterator();
            while (iterator.hasNext()) {
                Activity next = iterator.next();
                iterator.remove();
                next.finish();
            }


            if (mActivityList != null) {
                mActivityList = null;
            }

            //退出JVM(java虚拟机),释放所占内存资源,0表示正常退出(非0的都为异常退出)
            System.exit(0);
            //从操作系统中结束掉当前程序的进程
            android.os.Process.killProcess(android.os.Process.myPid());
        } catch (Exception e) {
            e.printStackTrace();
            System.exit(-1);
        }
    }
}
