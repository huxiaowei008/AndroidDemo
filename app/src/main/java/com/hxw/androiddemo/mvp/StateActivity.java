package com.hxw.androiddemo.mvp;

import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.widget.StateLayout;

import butterknife.BindView;

/**
 * Created by hxw on 2017/4/17.
 */

public class StateActivity extends BaseActivity {
    @BindView(R.id.sl_layout)
    StateLayout slLayout;

    /**
     * @return 返回布局资源ID
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_state;
    }

    /**
     * 依赖注入的入口,提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
     *
     * @param appComponent
     */
    @Override
    public void componentInject(AppComponent appComponent) {

    }

    /**
     * 初始化，会在onCreate中执行
     */
    @Override
    public void init() {
        slLayout.setOnRetryClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                slLayout.showLoading();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_empty:
                slLayout.showEmpty();
                return true;
            case R.id.action_loading:
                slLayout.showLoading();
                return true;
            case R.id.action_content:
                slLayout.showContent();
                return true;
            case R.id.action_error:
                slLayout.showError();
                return true;
            case R.id.action_network_error:
                slLayout.showNoNetwork();
                return true;
            case android.R.id.home:
                finish();
                return true;
        }
        return false;
    }
}
