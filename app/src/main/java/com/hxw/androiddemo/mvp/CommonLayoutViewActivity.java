package com.hxw.androiddemo.mvp;

import android.os.Bundle;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

/**
 * Created by hxw on 2017/6/16.
 */

public class CommonLayoutViewActivity extends BaseActivity {
    @Override
    public int getLayoutId() {
        return R.layout.activity_commonlayoutview;
    }

    @Override
    public void componentInject(AppComponent appComponent) {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }
}
