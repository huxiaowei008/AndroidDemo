package com.hxw.androiddemo.mvp;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.utils.UIUtils;

import butterknife.BindView;

/**
 * Created by hxw on 2017/9/5.
 */

public class DrawerActivity extends BaseActivity {
    @BindView(R.id.navigation)
    NavigationView navigation;
    @BindView(R.id.drawer)
    DrawerLayout drawer;

    TextView name;
    ImageView head;

    @Override
    public int getLayoutId() {
        return R.layout.activity_drawer;
    }

    @Override
    public void componentInject(AppComponent appComponent) {

    }

    @Override
    public void init(Bundle savedInstanceState) {
        //设置NavigationView菜单条目的点击监听
        navigation.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                UIUtils.makeText(DrawerActivity.this, item.getTitle().toString());

                //关闭侧滑
                drawer.closeDrawers();
                return true;
            }
        });

        name = (TextView) navigation.getHeaderView(0).findViewById(R.id.tv_name);
        head = (ImageView) navigation.getHeaderView(0).findViewById(R.id.img_head);

        name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.makeText(DrawerActivity.this, "姓名");
            }
        });

        head.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UIUtils.makeText(DrawerActivity.this, "头像");
            }
        });
    }
}
