package com.hxw.androiddemo.mvp.bottomnavigation;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * Created by hxw on 2017/4/26.
 */

public class BottomNavigationActivity extends BaseActivity {
    @BindView(R.id.fl_content)
    FrameLayout flContent;
    @BindView(R.id.bottom_navigation)
    BottomNavigationView bottomNavigation;

    /**
     * Fragment切换数组
     */
    private List<Fragment> fragmentList;
    private FragmentManager fManager;

    /**
     * @return 返回布局资源ID
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_bottom_navigation;
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
        initFragment();
        initNavigationBar();
    }

    /**
     * 初始化fragment
     */
    private void initFragment() {
        fragmentList = new ArrayList<>();
        fragmentList.add(SimpleFragment.getInstance("首页"));
        fragmentList.add(SimpleFragment.getInstance("发现"));
        fragmentList.add(SimpleFragment.getInstance("我的"));
        fManager = getSupportFragmentManager();
        FragmentTransaction transaction = fManager.beginTransaction();
        //把fragment加到transaction里
        for (Fragment fragment : fragmentList) {
            transaction.add(R.id.fl_content, fragment)
                    .hide(fragment);
        }
        //展现首页
        transaction.show(fragmentList.get(0)).commit();
    }

    /**
     * 初始化底部栏
     */
    private void initNavigationBar() {
        bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.home:
                        setFragment(0);
                        break;
                    case R.id.find:
                        setFragment(1);
                        break;
                    case R.id.my:
                        setFragment(2);
                        break;
                    default:
                        break;
                }

                return true;
            }
        });
    }

    /**
     * 界面切换控制
     */
    private void setFragment(int index) {
        int size = fragmentList.size();
        Fragment fragment = null;
        FragmentTransaction transaction = fManager.beginTransaction();
        for (int i = 0; i < size; i++) {
            fragment = fragmentList.get(i);
            if (i == index) {
                transaction.show(fragment);
            } else {
                transaction.hide(fragment);
            }
        }
        transaction.commit();

    }
}
