package com.hxw.androiddemo.mvp.bottomnavigation;

import android.widget.TextView;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseFragment;
import com.hxw.frame.di.AppComponent;

import butterknife.BindView;

/**
 * Created by hxw on 2017/4/26.
 */

public class SimpleFragment extends BaseFragment {


    @BindView(R.id.tv_text)
    TextView tvText;

    private String text = "";

    public static SimpleFragment getInstance(String text) {
        SimpleFragment simpleFragment = new SimpleFragment();
        simpleFragment.text = text;

        return simpleFragment;
    }

    /**
     * @return 返回布局资源ID
     */
    @Override
    protected int getLayoutId() {
        return R.layout.fragment_simple;
    }

    /**
     * 依赖注入的入口,提供AppComponent(提供所有的单例对象)给子类，进行Component依赖
     *
     * @param appComponent
     */
    @Override
    protected void componentInject(AppComponent appComponent) {

    }

    /**
     * 初始化,会在onActivityCreated中执行
     */
    @Override
    protected void init() {

        tvText.setText(text);
    }
}
