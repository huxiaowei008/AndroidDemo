package com.hxw.androiddemo.mvp.camera2;

import android.os.Bundle;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

import butterknife.BindView;

/**
 * Created by hxw on 2017/9/26.
 */

public class Camera2Activity extends BaseActivity {
    @BindView(R.id.aft)
    AutoFitTextureView aft;

    @Override
    public int getLayoutId() {
        return R.layout.activity_camera2;
    }

    @Override
    public void componentInject(AppComponent appComponent) {

    }

    @Override
    public void init(Bundle savedInstanceState) {
        aft.onCreate(this);
    }

    @Override
    protected void onResume() {
        aft.onResume();
        super.onResume();
    }

    @Override
    protected void onPause() {
        aft.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        aft.onDestroy();
        super.onDestroy();
    }
}
