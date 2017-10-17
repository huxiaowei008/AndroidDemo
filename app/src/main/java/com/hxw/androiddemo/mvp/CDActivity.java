package com.hxw.androiddemo.mvp;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;
import android.widget.TextView;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hxw on 2017/8/1.
 */

public class CDActivity extends BaseActivity {
    @BindView(R.id.tv_cd)
    TextView tvCd;
    @BindView(R.id.btn_cd)
    Button btnCd;

    CountDownTimer timer;

    @Override
    public int getLayoutId() {
        return R.layout.activity_cd;
    }

    @Override
    public void componentInject(AppComponent appComponent) {

    }

    @Override
    public void init(Bundle savedInstanceState) {

        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                long t = millisUntilFinished / 1000;
                tvCd.setText(t + "s");
            }

            @Override
            public void onFinish() {
                tvCd.setText("倒计时结束");
            }
        };
    }

    @OnClick(R.id.btn_cd)
    public void onClick() {
        timer.start();
    }

    @Override
    protected void onDestroy() {
        timer.cancel();
        super.onDestroy();
    }
}
