package com.hxw.androiddemo.mvp.photopicker.ui;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.widget.FrameLayout;

import com.bilibili.boxing.AbsBoxingActivity;
import com.bilibili.boxing.AbsBoxingViewFragment;
import com.bilibili.boxing.model.entity.BaseMedia;
import com.bilibili.boxing.presenter.PickerContract;
import com.hxw.androiddemo.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by hxw on 2017/5/11.
 */

public class BoxingActivity extends AbsBoxingActivity {


    @BindView(R.id.tb_title)
    Toolbar tbTitle;
    @BindView(R.id.fl_content)
    FrameLayout flContent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_boxing);
        ButterKnife.bind(this);
    }

    /**
     * create a {@link PickerContract.View} attaching to
     * {@link PickerContract.Presenter},call in {@link #onCreate(Bundle)}
     *
     * @param medias
     */
    @NonNull
    @Override
    public AbsBoxingViewFragment onCreateBoxingView(ArrayList<BaseMedia> medias) {
        return null;
    }

    /**
     * live with {@link PickerContract.View#onFinish(List)}
     *
     * @param intent
     * @param medias the selection of medias.
     */
    @Override
    public void onBoxingFinish(Intent intent, @Nullable List<BaseMedia> medias) {
        setResult(Activity.RESULT_OK, intent);
        finish();
    }
}
