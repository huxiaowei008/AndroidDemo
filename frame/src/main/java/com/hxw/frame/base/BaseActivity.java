package com.hxw.frame.base;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.trello.rxlifecycle2.components.support.RxAppCompatActivity;


/**
 * 因为java只能单继承,所以如果有需要继承特定Activity的三方库,那你就需要自己自定义Activity
 * 继承于这个特定的Activity,然后按照将BaseActivity的格式,复制过去记住一定要实现{@link IActivity}
 * Created by hxw on 2017/2/8.
 */

public abstract class BaseActivity extends RxAppCompatActivity implements IActivity {
    protected final String TAG = this.getClass().getSimpleName();

    @Nullable
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        init(savedInstanceState);

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    @Override
    public boolean useEventBus() {
        return false;
    }

    @Override
    public boolean useFragment() {
        return false;
    }
}
