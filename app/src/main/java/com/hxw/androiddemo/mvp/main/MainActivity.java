package com.hxw.androiddemo.mvp.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.view.View;

import com.hxw.androiddemo.R;
import com.hxw.androiddemo.base.Constant;
import com.hxw.androiddemo.mvp.StateActivity;
import com.hxw.androiddemo.mvp.bottomnavigation.BottomNavigationActivity;
import com.hxw.androiddemo.mvp.guide.GuideActivity;
import com.hxw.androiddemo.mvp.photopicker.PhotoPickerActivity;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.update.OnUpdateListener;
import com.hxw.frame.update.UpdateManager;

import javax.inject.Inject;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Inject
    UpdateManager updateManager;

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
     * @param appComponent
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
    public void init() {

    }

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5,R.id.btn_6})
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
                    public void error() {

                    }
                });
                break;
            case R.id.btn_6:
                goTo(6, PhotoPickerActivity.class);
                break;
        }
    }

    private void goTo(int index, Class targetActivity) {
        Intent intent = new Intent(MainActivity.this, targetActivity);
        intent.putExtra(Constant.INDEX, index);
        startActivity(intent);
    }

    /*  权限校验  */
    private void checkBluetoothPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            //校验是否已具有模糊定位权限
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                        reCode);
            } else {
                //具有权限

            }
        } else {
            //系统不高于6.0直接执行
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == reCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                //同意权限

            } else {
                // 权限拒绝，提示用户开启权限

            }
        }
    }
}
