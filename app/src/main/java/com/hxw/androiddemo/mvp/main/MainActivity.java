package com.hxw.androiddemo.mvp.main;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.hxw.androiddemo.R;
import com.hxw.androiddemo.api.ComAPI;
import com.hxw.androiddemo.base.Constant;
import com.hxw.androiddemo.mvp.CDActivity;
import com.hxw.androiddemo.mvp.CommonLayoutViewActivity;
import com.hxw.androiddemo.mvp.DrawerActivity;
import com.hxw.androiddemo.mvp.InputActivity;
import com.hxw.androiddemo.mvp.QRActivity;
import com.hxw.androiddemo.mvp.ShareImageActivity;
import com.hxw.androiddemo.mvp.StateActivity;
import com.hxw.androiddemo.mvp.bottomnavigation.BottomNavigationActivity;
import com.hxw.androiddemo.mvp.camera2.Camera2Activity;
import com.hxw.androiddemo.mvp.guide.GuideActivity;
import com.hxw.androiddemo.mvp.newzxing.NewZxingActivity;
import com.hxw.androiddemo.mvp.photopicker.PhotoPickerActivity;
import com.hxw.androiddemo.mvp.recyclerviewh.RecyclerVeiwHActivity;
import com.hxw.androiddemo.mvp.zxing.ZxingActivity;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.http.AbstractErrorSubscriber;
import com.hxw.frame.http.ErrorHandler;
import com.hxw.frame.integration.IRepositoryManager;
import com.hxw.frame.update.OnUpdateListener;
import com.hxw.frame.update.UpdateManager;
import com.hxw.frame.utils.UIUtils;

import javax.inject.Inject;

import butterknife.OnClick;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainActivity extends BaseActivity {

    @Inject
    UpdateManager updateManager;
    @Inject
    IRepositoryManager repositoryManager;
    @Inject
    ErrorHandler handler;
    private int reCode = 8;

    /**
     * @return 返回布局资源ID
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 依赖注入的入口,提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
     *
     * @param appComponent 基础注入器
     */
    @Override
    public void componentInject(AppComponent appComponent) {
        DaggerMainComponent.builder()
                .appComponent(appComponent)
                .mainModule(new MainModule())
                .build()
                .inject(this);
    }

    /**
     * 初始化，会在onCreate中执行
     */
    @Override
    public void init(Bundle savedInstanceState) {
        repositoryManager.getRetrofitService(ComAPI.class)
                .getSetMeal("00001903")
                .subscribeOn(Schedulers.io())
                .unsubscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .compose(this.<String>bindToLifecycle())
                .subscribe(new AbstractErrorSubscriber<String>(handler) {
                    @Override
                    public void onSubscribe(@NonNull Disposable d) {

                    }

                    @Override
                    public void onNext(@NonNull String s) {
                        UIUtils.makeText(MainActivity.this, "成功");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5, R.id.btn_6, R.id.btn_7,
            R.id.btn_8, R.id.btn_9, R.id.btn_10, R.id.btn_11, R.id.btn_12, R.id.btn_13, R.id.btn_14,
            R.id.btn_15, R.id.btn_16})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btn_1:
                goTo(1, GuideActivity.class);
                break;
            case R.id.btn_2:
                goTo(2, GuideActivity.class);
                break;
            case R.id.btn_3:
                goTo(3, StateActivity.class);
                break;
            case R.id.btn_4:
                goTo(4, BottomNavigationActivity.class);
                break;
            case R.id.btn_5:
                updateManager.checkUpdate(MainActivity.this, new OnUpdateListener() {
                    @Override
                    public void isUpdate() {
                        updateManager.showNoticeUpdate();
                    }

                    @Override
                    public void noUpdate() {

                    }

                    @Override
                    public void error(Throwable throwable) {
                        Log.e(TAG, "error: " + throwable.toString(), throwable);
                    }
                });
                break;
            case R.id.btn_6:
                goTo(6, PhotoPickerActivity.class);
                break;
            case R.id.btn_7:
                goTo(7, RecyclerVeiwHActivity.class);
                break;
            case R.id.btn_8:
                goTo(8, CommonLayoutViewActivity.class);
                break;
            case R.id.btn_9:
                goTo(9, ZxingActivity.class);
                break;
            case R.id.btn_10:
                goTo(10, CDActivity.class);
                break;
            case R.id.btn_11:
                goTo(11, ShareImageActivity.class);
                break;
            case R.id.btn_12:
                goTo(12, DrawerActivity.class);
                break;
            case R.id.btn_13:
                goTo(13, QRActivity.class);
                break;
            case R.id.btn_14:
                goTo(14, NewZxingActivity.class);
                break;
            case R.id.btn_15:
                goTo(15, Camera2Activity.class);
                break;
            case R.id.btn_16:
                goTo(16, InputActivity.class);
                break;
            default:
                break;
        }
    }

    private void goTo(int index, Class targetActivity) {
        Intent intent = new Intent(MainActivity.this, targetActivity);
        intent.putExtra(Constant.INDEX, index);
        startActivity(intent);
    }

}
