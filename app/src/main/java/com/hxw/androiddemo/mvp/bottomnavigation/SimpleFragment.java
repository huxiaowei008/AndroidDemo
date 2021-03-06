package com.hxw.androiddemo.mvp.bottomnavigation;

import android.os.Bundle;
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
    public int getLayoutId() {
        return R.layout.fragment_simple;
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
     * 初始化,会在onActivityCreated中执行
     */
    @Override
    public void init(Bundle savedInstanceState) {

        tvText.setText(text);
    }

    /**
     * 此方法是让外部调用使fragment做一些操作的,比如说外部的activity想让fragment对象执行一些方法,
     * 建议在有多个需要让外界调用的方法时,统一传Message,通过what字段,来区分不同的方法,在setData
     * 方法中就可以switch做不同的操作,这样就可以用统一的入口方法做不同的事
     * <p>
     * 使用此方法时请注意调用时fragment的生命周期,如果调用此setData方法时onActivityCreated
     * 还没执行,setData里调用presenter的方法时,是会报空的,因为dagger注入是在onActivityCreated
     * 方法中执行的,如果要做一些初始化操作,可以不必让外部调setData,在内部onActivityCreated中
     * 初始化就可以了
     *
     * @param data
     */
    @Override
    public void setData(Object data) {

    }
}
