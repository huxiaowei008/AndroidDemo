package com.hxw.androiddemo.mvp;

import android.graphics.PointF;
import android.os.Bundle;
import android.widget.TextView;

import com.dlazaro66.qrcodereaderview.QRCodeReaderView;
import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

import butterknife.BindView;

/**
 * Created by hxw on 2017/9/23.
 */

public class QRActivity extends BaseActivity {
    @BindView(R.id.qr)
    QRCodeReaderView qr;
    @BindView(R.id.tv_result)
    TextView tvResult;

    @Override
    public int getLayoutId() {
        return R.layout.activity_qr;
    }

    @Override
    public void componentInject(AppComponent appComponent) {

    }

    @Override
    public void init(Bundle savedInstanceState) {
        qr.setAutofocusInterval(2000L);
        qr.setBackCamera();
        qr.setOnQRCodeReadListener(new QRCodeReaderView.OnQRCodeReadListener() {
            @Override
            public void onQRCodeRead(String text, PointF[] points) {
                tvResult.setText(text);
            }
        });


    }

    @Override
    protected void onResume() {
        super.onResume();
        qr.startCamera();
    }

    @Override
    protected void onStop() {
        super.onStop();
        qr.stopCamera();
    }
}
