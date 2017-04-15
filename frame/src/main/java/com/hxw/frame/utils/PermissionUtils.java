package com.hxw.frame.utils;

import android.Manifest;

import com.hxw.frame.R;
import com.hxw.frame.mvp.IView;
import com.tbruyelle.rxpermissions2.Permission;
import com.tbruyelle.rxpermissions2.RxPermissions;

import io.reactivex.functions.Consumer;
import timber.log.Timber;

/**
 * 权限相关工具类
 * 如果申请权限无效,请把每个方法中的
 * 「compose(PermissionUtil.<Boolean>bindToLifecycle(view))」删掉
 * Created by hxw on 2017/2/10.
 */

public class PermissionUtils {
    private static final String TAG = "PermissionUtils";

    private PermissionUtils() {
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    public interface RequestPermission {
        /**
         * 成功
         */
        void onSuccess();

        /**
         * 失败，通常不用写
         */
        void onFail();
    }

    /**
     * 请求摄像头权限
     *
     * @param requestPermission
     * @param rxPermissions
     * @param view
     */
    public static void launchCamera(final RequestPermission requestPermission,
                                    RxPermissions rxPermissions,
                                    final IView view) {
        //先确保是否已经申请过摄像头
        boolean isPermissionsGranted = rxPermissions.isGranted(Manifest.permission.CAMERA);
        if (isPermissionsGranted) { //已经申请过，直接执行操作
            requestPermission.onSuccess();
        } else {//没有申请过，则申请
            rxPermissions.request(Manifest.permission.CAMERA)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (granted) {
                                Timber.tag(TAG).d("request CAMERA success");
                                requestPermission.onSuccess();
                            } else {
                                view.showMessage(UIUtils.getString(R.string.requestFail));
                                requestPermission.onFail();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Timber.tag(TAG).e(throwable);
                            UIUtils.makeText(UIUtils.getString(R.string.requestError));
                        }
                    });
        }
    }

    /**
     * 请求外部存储的读写权限
     *
     * @param requestPermission
     * @param rxPermissions
     * @param view
     */
    public static void externalStorage(final RequestPermission requestPermission,
                                       RxPermissions rxPermissions,
                                       final IView view) {
        //先确保是否已经申请过外部存储的读写权限
        boolean isPermissionsGranted =
                rxPermissions.isGranted(Manifest.permission.WRITE_EXTERNAL_STORAGE) &&
                        rxPermissions.isGranted(Manifest.permission.READ_EXTERNAL_STORAGE);
        if (isPermissionsGranted) {//已经申请过，直接执行操作
            requestPermission.onSuccess();
        } else {//没有申请过，则申请
            rxPermissions.requestEach(Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                    .subscribe(new Consumer<Permission>() {
                        @Override
                        public void accept(Permission permission) throws Exception {
                            if (permission.granted) {//权限请求成功
                                Timber.tag(TAG).d("request WRITE_EXTERNAL_STORAGE " +
                                        "and READ_EXTERNAL_STORAGE success");
                                requestPermission.onSuccess();
                            } else if (permission.shouldShowRequestPermissionRationale) {
                                //请求被拒绝但没有不再访问 Denied permission without ask never again
                                Timber.tag(TAG).d("请求失败但可以再访问");
                                view.showMessage(UIUtils.getString(R.string.requestFail));
                                requestPermission.onFail();
                            } else {//请求被拒绝并且不再访问 Denied permission with ask never again
                                Timber.tag(TAG).d("请求失败并且不再访问");
                                view.showMessage(UIUtils.getString(R.string.requestFail));
                                requestPermission.onFail();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Timber.tag(TAG).e(throwable);
                            UIUtils.makeText(UIUtils.getString(R.string.requestError));
                        }
                    });
        }

    }

    /**
     * 请求发送短信权限
     *
     * @param requestPermission
     * @param rxPermissions
     * @param view
     */
    public static void sendSms(final RequestPermission requestPermission,
                               RxPermissions rxPermissions,
                               final IView view) {
        //先确保是否已经申请过权限
        boolean isPermissionsGranted =
                rxPermissions.isGranted(Manifest.permission.SEND_SMS);

        if (isPermissionsGranted) {//已经申请过，直接执行操作
            requestPermission.onSuccess();
        } else {//没有申请过，则申请
            rxPermissions.request(Manifest.permission.SEND_SMS)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (granted) {
                                Timber.tag(TAG).d("request SEND_SMS success");
                                requestPermission.onSuccess();
                            } else {
                                view.showMessage(UIUtils.getString(R.string.requestFail));
                                requestPermission.onFail();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Timber.tag(TAG).e(throwable);
                            UIUtils.makeText(UIUtils.getString(R.string.requestError));
                        }
                    });
        }
    }

    /**
     * 请求打电话权限
     *
     * @param requestPermission
     * @param rxPermissions
     * @param view
     */
    public static void callPhone(final RequestPermission requestPermission,
                                 RxPermissions rxPermissions,
                                 final IView view) {
        //先确保是否已经申请过权限
        boolean isPermissionsGranted =
                rxPermissions.isGranted(Manifest.permission.CALL_PHONE);

        if (isPermissionsGranted) {//已经申请过，直接执行操作
            requestPermission.onSuccess();
        } else {//没有申请过，则申请
            rxPermissions.request(Manifest.permission.CALL_PHONE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (granted) {
                                Timber.tag(TAG).d("request CALL_PHONE success");
                                requestPermission.onSuccess();
                            } else {
                                view.showMessage(UIUtils.getString(R.string.requestFail));
                                requestPermission.onFail();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Timber.tag(TAG).e(throwable);
                            UIUtils.makeText(UIUtils.getString(R.string.requestError));
                        }
                    });
        }
    }


    /**
     * 请求获取手机状态的权限
     *
     * @param requestPermission
     * @param rxPermissions
     * @param view
     */
    public static void readPhonestate(final RequestPermission requestPermission,
                                      RxPermissions rxPermissions,
                                      final IView view) {
        //先确保是否已经申请过权限
        boolean isPermissionsGranted =
                rxPermissions.isGranted(Manifest.permission.READ_PHONE_STATE);

        if (isPermissionsGranted) {//已经申请过，直接执行操作
            requestPermission.onSuccess();
        } else {//没有申请过，则申请
            rxPermissions
                    .request(Manifest.permission.READ_PHONE_STATE)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (granted) {
                                Timber.tag(TAG).d("request READ_PHONE_STATE success");
                                requestPermission.onSuccess();
                            } else {
                                view.showMessage(UIUtils.getString(R.string.requestFail));
                                requestPermission.onFail();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Timber.tag(TAG).e(throwable);
                            UIUtils.makeText(UIUtils.getString(R.string.requestError));
                        }
                    });
        }
    }

    /**
     * 请求精准定位权限
     *
     * @param requestPermission
     * @param rxPermissions
     * @param view
     */
    public static void getLocation(final RequestPermission requestPermission,
                                   RxPermissions rxPermissions,
                                   final IView view) {
        //先确保是否已经申请过权限
        boolean isPermissionsGranted =
                rxPermissions.isGranted(Manifest.permission.ACCESS_FINE_LOCATION);
        if (isPermissionsGranted) {//已经申请过，直接执行操作
            requestPermission.onSuccess();
        } else {//没有申请过，则申请
            rxPermissions.request(Manifest.permission.ACCESS_FINE_LOCATION)
                    .subscribe(new Consumer<Boolean>() {
                        @Override
                        public void accept(Boolean granted) throws Exception {
                            if (granted) {
                                Timber.tag(TAG).d("request ACCESS_FINE_LOCATION success");
                                requestPermission.onSuccess();
                            } else {
                                view.showMessage(UIUtils.getString(R.string.requestFail));
                                requestPermission.onFail();
                            }
                        }
                    }, new Consumer<Throwable>() {
                        @Override
                        public void accept(Throwable throwable) throws Exception {
                            Timber.tag(TAG).e(throwable);
                            UIUtils.makeText(UIUtils.getString(R.string.requestError));
                        }
                    });
        }
    }

}
