package com.hxw.androiddemo.mvp.imagedetail;

import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.widget.ImageView;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

import butterknife.BindView;

/**
 * Created by hxw on 2017/9/4.
 */

public class ImageDetailActivity extends BaseActivity {


    @BindView(R.id.img)
    ImageView img;

    @Override
    public int getLayoutId() {
        return R.layout.activity_image_detail;
    }

    @Override
    public void componentInject(AppComponent appComponent) {

    }

    @Override
    public void init(Bundle savedInstanceState) {
        img.setImageResource(getIntent().getIntExtra("res", 0));
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
        ActivityCompat.finishAfterTransition(ImageDetailActivity.this);
    }
}
