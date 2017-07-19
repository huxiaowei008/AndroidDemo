package com.hxw.androiddemo.mvp.zxing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Handler;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.zxing.Result;
import com.hxw.androiddemo.mvp.zxing.camera.CameraManager;
import com.hxw.androiddemo.mvp.zxing.camera.FrontLightMode;

import java.io.IOException;

/**
 * Created by hxw on 2017/7/18.
 */

public class ZxingView extends RelativeLayout implements SurfaceHolder.Callback {
    private static final String TAG = ZxingView.class.getSimpleName();
    private SurfaceView surfaceView;
    private ViewfinderView viewfinderView;
    private CameraManager cameraManager;//相机管理类
    private BeepManager beepManager;//提示音管理类
    private AmbientLightManager ambientLightManager;//灯光管理类
    private InactivityTimer inactivityTimer;
    private boolean hasSurface;
    private Activity mActivity;
    private CaptureActivityHandler handler;
    private ZxingResultListener listener;
    private FrontLightMode lightMode = FrontLightMode.OFF;//设置闪光灯模式

    public ZxingView(Context context) {
        this(context, null);
    }

    public ZxingView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ZxingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    /**
     * 初始化视图
     *
     * @param context
     * @param attrs
     */
    private void initView(Context context, AttributeSet attrs) {
        surfaceView = new SurfaceView(context);
        viewfinderView = new ViewfinderView(context, attrs);
        LayoutParams layoutParams = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
        addView(surfaceView, layoutParams);
        addView(viewfinderView, layoutParams);
    }

    /**
     * 初始化,在onCreate中调用
     * 注意,属性设置要在oCreate中进行
     *
     * @param activity
     */
    public void init(Activity activity) {
        mActivity = activity;
        hasSurface = false;
        inactivityTimer = new InactivityTimer(activity);
        beepManager = new BeepManager(activity);
        ambientLightManager = new AmbientLightManager(activity);
    }

    /**
     * 在activity的生命周期中调用
     */
    public void onResume() {
        //最好在这里初始,别在onCreate,原因看英文CameraManager must be initialized here, not in onCreate(). This is necessary because we don't
        // want to open the camera driver and measure the screen size if we're going to show the help on
        // first launch. That led to bugs where the scanning rectangle was the wrong size and partially
        // off screen.
        cameraManager = new CameraManager(mActivity.getApplication());
        inactivityTimer.onResume();
        viewfinderView.setCameraManager(cameraManager);
        ambientLightManager.start(cameraManager, lightMode);
        SurfaceHolder surfaceHolder = surfaceView.getHolder();
        if (hasSurface) {
            // The activity was paused but not stopped, so the surface still exists. Therefore
            // surfaceCreated() won't be called, so init the camera here.
            // 活动暂停了，但没有停止，所以表面仍然存在。因此
            // surface创建()不会被调用，所以在这里初始化相机。
            initCamera(surfaceHolder);
        } else {
            // Install the callback and wait for surfaceCreated() to init the camera.
            // 安装回调，并等待创建()初始化相机
            surfaceHolder.addCallback(this);
        }
    }

    /**
     * 最好在activity的onPause的super前调用
     */
    public void onPause() {
        if (handler != null) {
            handler.quitSynchronously();
            handler = null;
        }
        ambientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        inactivityTimer.onPause();
        if (!hasSurface) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
    }

    /**
     * 最好在activity的onDestroy的super前调用
     */
    public void onDestroy() {
        inactivityTimer.shutdown();
        mActivity = null;
    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {
        if (holder == null) {
            Log.e(TAG, "*** WARNING *** surfaceCreated() gave us a null surface!");
        }
        if (!hasSurface) {
            hasSurface = true;
            initCamera(holder);
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        hasSurface = false;
    }

    private void initCamera(SurfaceHolder surfaceHolder) {
        if (surfaceHolder == null) {
            throw new IllegalStateException("No SurfaceHolder provided");
        }
        if (cameraManager.isOpen()) {
            Log.w(TAG, "initCamera() while already open -- late SurfaceView callback?");
            return;
        }
        try {
            cameraManager.openDriver(surfaceHolder, lightMode);
            // Creating the handler starts the preview, which can also throw a RuntimeException.
            if (handler == null) {
                handler = new CaptureActivityHandler(this, cameraManager);
            }

        } catch (IOException ioe) {
            Log.w(TAG, ioe);
            displayFrameworkBugMessageAndExit();
        } catch (RuntimeException e) {
            // Barcode Scanner has seen crashes in the wild of this variety:
            // java.?lang.?RuntimeException: Fail to connect to camera service
            Log.w(TAG, "Unexpected error initializing camera", e);
            displayFrameworkBugMessageAndExit();
        }
    }

    /**
     * 无法启动相机，提示并退出
     */

    private void displayFrameworkBugMessageAndExit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(mActivity);
        builder.setTitle("温馨提示");
        builder.setMessage("很遗憾，Android 相机出现问题。你可能需要重启设备。");
        builder.setPositiveButton("确定", new FinishListener(mActivity));
        builder.setOnCancelListener(new FinishListener(mActivity));
        builder.show();
    }

    /**
     * 在delay毫秒后重启预览
     *
     * @param delayMS
     */
    public void restartPreviewAfterDelay(long delayMS) {
        if (handler != null) {
            handler.sendEmptyMessageDelayed(ZxingConstant.restart_preview, delayMS);
        }
    }

    /**
     * 开关闪关灯
     *
     * @param setting
     */
    public void setTorch(boolean setting) {
        cameraManager.setTorch(setting);
    }


    ViewfinderView getViewfinderView() {
        return viewfinderView;
    }

    CameraManager getCameraManager() {
        return cameraManager;
    }

    public Handler getHandler() {
        return handler;
    }

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        inactivityTimer.onActivity();
        beepManager.playBeepSoundAndVibrate();
        if (listener != null) {
            listener.result(rawResult, barcode, scaleFactor);
        }
    }

    public void setOnResultListener(ZxingResultListener listener) {
        this.listener = listener;
    }

    interface ZxingResultListener {
        void result(Result rawResult, Bitmap barcode, float scaleFactor);
    }


    /**
     * 设置提示音和震动
     *
     * @param isBeep    是否播放提示音
     * @param isVibrate 是否震动(震动需要权限)
     */
    public void setBeepSetting(boolean isBeep, boolean isVibrate) {
        beepManager.setting(isBeep, isVibrate);
    }

    public void setLightMode(FrontLightMode lightMode) {
        this.lightMode = lightMode;
    }
}
