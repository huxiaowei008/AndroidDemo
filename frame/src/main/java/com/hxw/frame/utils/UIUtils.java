package com.hxw.frame.utils;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Build;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import org.simple.eventbus.EventBus;

import butterknife.ButterKnife;
import butterknife.Unbinder;

import static com.hxw.frame.integration.AppManager.APP_MANAGER_MESSAGE;
import static com.hxw.frame.integration.AppManager.EXIT_APP;
import static com.hxw.frame.integration.AppManager.KILL_ACTIVITY;
import static com.hxw.frame.integration.AppManager.SHOW_SNACK_BAR;
import static com.hxw.frame.integration.AppManager.START_ACTIVITY;

/**
 * Created by hxw on 2017/2/10.
 */

public class UIUtils {
    private static Toast mToast;

    private UIUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }


    /**
     * 获得资源
     *
     * @return
     */
    public static Resources getResources(Context context) {
        return context.getResources();
    }

    /**
     * 从String中获得字符
     *
     * @param stringID
     * @return
     */
    public static String getString(Context context, int stringID) {
        return context.getString(stringID);
    }


    /**
     * 获取颜色
     *
     * @param rId
     * @return
     */
    public static int getColor(Context context, int rId) {
        return ContextCompat.getColor(context, rId);
    }

    /**
     * 单列toast
     *
     * @param string
     */
    public static void makeText(Context context, String string) {
        if (mToast == null) {
            mToast = Toast.makeText(context, string, Toast.LENGTH_SHORT);
        }
        mToast.setText(string);
        mToast.show();
    }

    /**
     * 用snackBar显示
     *
     * @param string
     * @param duration 0短时,其他为长时
     */
    public static void showSnackBar(String string, int duration) {
        Message message = new Message();
        message.what = SHOW_SNACK_BAR;
        message.obj = string;
        message.arg1 = duration;
        EventBus.getDefault().post(message, APP_MANAGER_MESSAGE);
    }

    /**
     * 跳转界面
     *
     * @param targetActivity 目标activity
     */
    public static void startActivity(Class targetActivity) {
        Message message = new Message();
        message.what = START_ACTIVITY;
        message.obj = targetActivity;
        EventBus.getDefault().post(message, APP_MANAGER_MESSAGE);
    }

    /**
     * 跳转界面
     *
     * @param intent
     */
    public static void startActivity(Intent intent) {
        Message message = new Message();
        message.what = START_ACTIVITY;
        message.obj = intent;
        EventBus.getDefault().post(message, APP_MANAGER_MESSAGE);
    }

    /**
     * finish指定的activity
     *
     * @param targetActivity
     */
    public static void killActivity(Class targetActivity) {
        Message message = new Message();
        message.what = KILL_ACTIVITY;
        message.obj = targetActivity;
        EventBus.getDefault().post(message, APP_MANAGER_MESSAGE);
    }

    /**
     * 退出APP
     */
    public static void exitApp() {
        Message message = new Message();
        message.what = EXIT_APP;
        EventBus.getDefault().post(message, APP_MANAGER_MESSAGE);
    }

    /**
     * 全屏，并且沉侵式状态栏
     *
     * @param activity
     */
    public static void statuInScreen(Activity activity) {
        WindowManager.LayoutParams attrs = activity.getWindow().getAttributes();
        attrs.flags &= ~WindowManager.LayoutParams.FLAG_FULLSCREEN;
        activity.getWindow().setAttributes(attrs);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_IN_SCREEN);
        activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS);
    }

    /**
     * 获得屏幕的宽度
     *
     * @return
     */
    public static int getScreenWidth(Context context) {
        return getResources(context).getDisplayMetrics().widthPixels;
    }

    /**
     * 获得屏幕的高度
     *
     * @return
     */
    public static int getScreenHeidth(Context context) {
        return getResources(context).getDisplayMetrics().heightPixels;
    }

    /**
     * butterknife绑定视图
     *
     * @param target
     * @param source
     * @return
     */
    public static Unbinder bindTarget(Object target, Object source) {
        if (source instanceof Activity) {
            return ButterKnife.bind(target, (Activity) source);
        } else if (source instanceof View) {
            return ButterKnife.bind(target, (View) source);
        } else if (source instanceof Dialog) {
            return ButterKnife.bind(target, (Dialog) source);
        } else {
            return Unbinder.EMPTY;
        }
    }

    protected void fullScreen(Activity activity) {
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19) {
            // lower api
            View v = activity.getWindow().getDecorView();
            v.setSystemUiVisibility(View.GONE);
        } else if (Build.VERSION.SDK_INT >= 19) {
            //for new api versions.
            View decorView = activity.getWindow().getDecorView();
            int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY;
            decorView.setSystemUiVisibility(uiOptions);
        }
    }

}
