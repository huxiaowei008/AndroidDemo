package com.hxw.androiddemo.mvp.zxing;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.KeyEvent;
import android.widget.Button;
import android.widget.TextView;

import com.google.zxing.Result;
import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 注意相机权限
 * Created by hxw on 2017/7/18.
 */

public class ZxingActivity extends BaseActivity {

    @BindView(R.id.zxing_view)
    ZxingView zxingView;
    @BindView(R.id.btn_light)
    Button btnLight;
    @BindView(R.id.text_result)
    TextView textResult;

    @Override
    public int getLayoutId() {
        return R.layout.activity_zxing;
    }

    @Override
    public void componentInject(AppComponent appComponent) {

    }

    @Override
    public void init(Bundle savedInstanceState) {
        zxingView.init(this);
        zxingView.setOnResultListener(new ZxingView.ZxingResultListener() {
            @Override
            public void result(Result rawResult, Bitmap barcode, float scaleFactor) {
                textResult.setText(rawResult.getText());
            }
        });
        zxingView.setBeepSetting(true, true);
    }

    @Override
    protected void onResume() {
        super.onResume();
        zxingView.onResume();
    }

    @Override
    protected void onPause() {
        zxingView.onPause();
        super.onPause();
    }

    @Override
    protected void onDestroy() {
        zxingView.onDestroy();
        super.onDestroy();
    }

    @OnClick(R.id.btn_light)
    public void onClick() {
        zxingView.restartPreviewAfterDelay(0);
        textResult.setText("");
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        switch (keyCode) {
            case KeyEvent.KEYCODE_VOLUME_DOWN:
                zxingView.setTorch(false);
                return true;
            case KeyEvent.KEYCODE_VOLUME_UP:
                zxingView.setTorch(true);
                return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}
