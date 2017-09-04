package com.hxw.androiddemo.mvp;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.view.View;
import android.widget.ImageView;

import com.hxw.androiddemo.R;
import com.hxw.androiddemo.mvp.imagedetail.ImageDetailActivity;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by hxw on 2017/9/4.
 */

public class ShareImageActivity extends BaseActivity {
    @BindView(R.id.img_1)
    ImageView img1;
    @BindView(R.id.img_2)
    ImageView img2;
    @BindView(R.id.img_3)
    ImageView img3;

    @Override
    public int getLayoutId() {
        return R.layout.activity_image;
    }

    @Override
    public void componentInject(AppComponent appComponent) {

    }

    @Override
    public void init(Bundle savedInstanceState) {

    }

    @OnClick({R.id.img_1, R.id.img_2, R.id.img_3})
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.img_1:
                start(img1, R.mipmap.ic_empty);
                break;
            case R.id.img_2:
                start(img2, R.mipmap.ic_error);
                break;
            case R.id.img_3:
                start(img3, R.mipmap.ic_launcher);
                break;
        }
    }

    private void start(ImageView imageView, int res) {

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            Intent intent = new Intent(ShareImageActivity.this, ImageDetailActivity.class);
            intent.putExtra("res", res);

            ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ShareImageActivity.this,
                    imageView, "share");
            ActivityCompat.startActivity(ShareImageActivity.this, intent, options.toBundle());
        }
    }
}
