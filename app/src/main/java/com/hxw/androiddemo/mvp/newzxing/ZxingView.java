package com.hxw.androiddemo.mvp.newzxing;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.Point;
import android.hardware.Camera;
import android.util.AttributeSet;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.BinaryBitmap;
import com.google.zxing.DecodeHintType;
import com.google.zxing.MultiFormatReader;
import com.google.zxing.PlanarYUVLuminanceSource;
import com.google.zxing.ReaderException;
import com.google.zxing.Result;
import com.google.zxing.ResultPoint;
import com.google.zxing.ResultPointCallback;
import com.google.zxing.common.HybridBinarizer;
import com.hxw.androiddemo.mvp.newzxing.camera.CameraManager;
import com.hxw.androiddemo.mvp.newzxing.camera.FrontLightMode;

import java.io.IOException;
import java.util.Collection;
import java.util.EnumMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import timber.log.Timber;

/**
 * Created by hxw on 2017/7/18.
 */

public class ZxingView extends RelativeLayout implements SurfaceHolder.Callback, Camera.PreviewCallback {
    private static final String TAG = ZxingView.class.getSimpleName();
    private SurfaceView surfaceView;
    private ViewfinderView viewfinderView;
    private CameraManager cameraManager;//相机管理类
    private BeepManager beepManager;//提示音管理类
    private AmbientLightManager ambientLightManager;//灯光管理类
    private boolean hasSurface;
    private Activity mActivity;
    private ZxingResultListener listener;
    private FrontLightMode lightMode = FrontLightMode.OFF;//设置闪光灯模式
    private Collection<BarcodeFormat> decodeFormats = DecodeFormatManager.QR_CODE_FORMATS;//
    private Map<DecodeHintType, Object> decodeHints;
    private final MultiFormatReader multiFormatReader = new MultiFormatReader();
    private Disposable dispose;
    private CompositeDisposable mCompositeDisposable;

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

        decodeHints = new EnumMap<>(DecodeHintType.class);
        decodeHints.put(DecodeHintType.NEED_RESULT_POINT_CALLBACK, new ResultPointCallback() {
            @Override
            public void foundPossibleResultPoint(ResultPoint point) {
                viewfinderView.addPossibleResultPoint(point);
            }
        });
        decodeHints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
        Log.i("DecodeThread", "Hints: " + decodeHints);
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
        cameraManager = new CameraManager(mActivity.getApplication(), lightMode);
        viewfinderView.setCameraManager(cameraManager);
        ambientLightManager.start(cameraManager);
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
        ambientLightManager.stop();
        beepManager.close();
        cameraManager.closeDriver();
        if (!hasSurface) {
            SurfaceHolder surfaceHolder = surfaceView.getHolder();
            surfaceHolder.removeCallback(this);
        }
    }

    /**
     * 最好在activity的onDestroy的super前调用
     */
    public void onDestroy() {
        if (dispose != null) {
            dispose.dispose();
        }
        if (mCompositeDisposable != null) {
            //保证activity结束时取消所有正在执行的订阅
            mCompositeDisposable.clear();
        }
        mActivity = null;
        mCompositeDisposable = null;
    }

    public void addDisposable(Disposable disposable) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        //将所有disposable放入,集中处理
        mCompositeDisposable.add(disposable);
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

    @Override
    public void onPreviewFrame(final byte[] data, Camera camera) {
        Timber.d("onPreviewFrame");
        final Point cameraResolution = cameraManager.getCameraResolution();
        if (cameraResolution == null || dispose != null) {
            Log.d(TAG, "Got preview callback, but no dispose or resolution available");
            return;
        }
        Observable.create(new ObservableOnSubscribe<ZxingResult>() {
            @Override
            public void subscribe(@NonNull ObservableEmitter<ZxingResult> e) throws Exception {
                multiFormatReader.setHints(decodeHints);
                long start = System.currentTimeMillis();
                Result rawResult = null;
                PlanarYUVLuminanceSource source = cameraManager.buildLuminanceSource(data, cameraResolution.x, cameraResolution.y);
                if (source != null) {
                    BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                    try {
                        rawResult = multiFormatReader.decodeWithState(bitmap);
                    } catch (ReaderException re) {
                        // continue
                    } finally {
                        multiFormatReader.reset();
                    }
                }
                ZxingResult result = new ZxingResult();
                if (rawResult != null) {
                    result.bundleThumbnail(source);
                    result.result = rawResult;
                    e.onNext(result);
                } else {
                    e.onNext(result);
                }
                e.onComplete();
            }
        })
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<ZxingResult>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        dispose = d;
                    }

                    @Override
                    public void onNext(@NonNull ZxingResult result) {
                        Timber.d("next");
                        if (result.result != null) {
                            handleDecode(result.result, result.barcode, result.scaleFactor);
                        }else {
                            cameraManager.requestPreviewFrame(ZxingView.this);
                        }
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {
                        Timber.d("error");
                        e.printStackTrace();
                        cameraManager.requestPreviewFrame(ZxingView.this);
                    }

                    @Override
                    public void onComplete() {
                        dispose = null;
                        Timber.d("complete");
                    }
                });
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
            cameraManager.openDriver(surfaceHolder);
            cameraManager.startPreview();
            restartPreviewAndDecode();

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
        builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                mActivity.finish();
            }
        });
        builder.setOnCancelListener(new DialogInterface.OnCancelListener() {
            @Override
            public void onCancel(DialogInterface dialogInterface) {
                mActivity.finish();
            }
        });
        builder.show();
    }

    /**
     * 在delay毫秒后重启预览
     *
     * @param delayMS
     */
    public void restartPreviewAfterDelay(long delayMS) {
        Observable.just(0)
                .delay(delayMS, TimeUnit.MILLISECONDS)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {
                        addDisposable(d);
                    }

                    @Override
                    public void onNext(@NonNull Integer integer) {
                        restartPreviewAndDecode();
                    }

                    @Override
                    public void onError(@NonNull Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    /**
     * 重新启动预览和解码
     */
    private void restartPreviewAndDecode() {
        if (dispose != null) {
            dispose.dispose();
            dispose = null;
        }
        cameraManager.requestPreviewFrame(this);
        drawViewfinder();
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

    public void drawViewfinder() {
        viewfinderView.drawViewfinder();
    }

    public Activity getActivity() {
        return mActivity;
    }

    public void handleDecode(Result rawResult, Bitmap barcode, float scaleFactor) {
        beepManager.playBeepSoundAndVibrate();
        if (listener != null) {
            listener.result(rawResult, barcode, scaleFactor);
        }
    }

    public void setOnResultListener(ZxingResultListener listener) {
        this.listener = listener;
    }

    public interface ZxingResultListener {
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

    /**
     * 添加扫描的码类型，默认一个二维码
     *
     * @param formats DecodeFormatManager
     */
    public void addDecodeFormat(Set<BarcodeFormat> formats) {
        decodeFormats.addAll(formats);
        decodeHints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
    }

    /**
     * 移除扫描的码类型
     *
     * @param formats DecodeFormatManager
     */
    public void removeDecodeFormat(Set<BarcodeFormat> formats) {
        decodeFormats.removeAll(formats);
        decodeHints.put(DecodeHintType.POSSIBLE_FORMATS, decodeFormats);
    }
}
