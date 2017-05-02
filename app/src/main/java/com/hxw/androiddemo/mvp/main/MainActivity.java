package com.hxw.androiddemo.mvp.main;

import android.content.Intent;
import android.view.View;

import com.hxw.androiddemo.R;
import com.hxw.androiddemo.base.Constant;
import com.hxw.androiddemo.mvp.StateActivity;
import com.hxw.androiddemo.mvp.bottomnavigation.BottomNavigationActivity;
import com.hxw.androiddemo.mvp.guide.GuideActivity;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.update.OnUpdateListener;
import com.hxw.frame.update.UpdateManager;

import javax.inject.Inject;

import butterknife.OnClick;

public class MainActivity extends BaseActivity {

    @Inject
    UpdateManager updateManager;


    /**
     * @return 返回布局资源ID
     */
    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    /**
     * 依赖注入的入口,提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
     *
     * @param appComponent
     */
    @Override
    protected void componentInject(AppComponent appComponent) {
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
    protected void init() {

    }

    @OnClick({R.id.btn_1, R.id.btn_2, R.id.btn_3, R.id.btn_4, R.id.btn_5})
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
        }
    }

    private void goTo(int index, Class targetActivity) {
        Intent intent = new Intent(MainActivity.this, targetActivity);
        intent.putExtra(Constant.INDEX, index);
        startActivity(intent);
    }
}
