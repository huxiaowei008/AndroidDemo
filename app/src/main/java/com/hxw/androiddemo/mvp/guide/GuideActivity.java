package com.hxw.androiddemo.mvp.guide;

import android.os.Bundle;
import android.widget.TextView;

import com.hxw.androiddemo.R;
import com.hxw.androiddemo.base.Constant;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.utils.RxUtils;

import butterknife.BindView;
import cn.bingoogolapple.bgabanner.BGABanner;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by hxw on 2017/4/15.
 */

public class GuideActivity extends BaseActivity {
    @BindView(R.id.banner_background)
    BGABanner bannerBackground;
    @BindView(R.id.banner_foreground)
    BGABanner bannerForeground;
    @BindView(R.id.tv_cd)
    TextView tvCd;

    /**
     * @return 返回布局资源ID
     */
    @Override
    public int getLayoutId() {
        return R.layout.activity_guide;
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
    public void init(Bundle savedInstanceState) {
//        //获取当前版本号
//        int currentVersion = AppUtils.getVersionCode(this);
//        //获取上次的版本号
//        int lastVersion = SPUtils.getInstance().getInt(R.string.version_code, 0);
//        if (currentVersion > lastVersion) {
//            //如果当前版本大于上次版本，该版本属于第一次启动
//
//            initViews();
//            //将当前版本写入preference中，则下次启动的时候，据此判断，不再为首次启动
//            SPUtils.getInstance().putInt(R.string.version_code, currentVersion);
//        } else {
//            initSplash();
//        }

        //上面是项目中该有的逻辑,这里为demo演示方便改的逻辑
        switch (getIntent().getIntExtra(Constant.INDEX, 0)) {
            case 1:
                initViews();
                break;
            case 2:
                initSplash();
                break;
            default:
                initViews();
                break;
        }

    }

    private void initViews() {
        //这动作设置要在数据设置前
        /**
         * 设置进入按钮和跳过按钮控件资源 id 及其点击事件
         * 如果进入按钮和跳过按钮有一个不存在的话就传 0
         * 在 BGABanner 里已经帮开发者处理了防止重复点击事件
         * 在 BGABanner 里已经帮开发者处理了「跳过按钮」和「进入按钮」的显示与隐藏
         */
        bannerBackground.setEnterSkipViewIdAndDelegate(R.id.btn_guide, 0, new BGABanner.GuideDelegate() {
            @Override
            public void onClickEnterOrSkip() {
                finish();
            }
        });

        // 设置数据源
        bannerBackground.setData(R.mipmap.uoko_guide_background_1,
                R.mipmap.uoko_guide_background_2, R.mipmap.uoko_guide_background_3);

        bannerForeground.setData(R.mipmap.uoko_guide_foreground_1,
                R.mipmap.uoko_guide_foreground_2, R.mipmap.uoko_guide_foreground_3);
    }

    private void initSplash() {

        bannerForeground.setData(R.mipmap.uoko_guide_background_1);
        RxUtils.countdown(3)
                .compose(this.<Integer>bindToLifecycle())
                .subscribe(new Observer<Integer>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(Integer integer) {
                        tvCd.setText(integer + "s");
                    }

                    @Override
                    public void onError(Throwable e) {
                        finish();
                    }

                    @Override
                    public void onComplete() {
                        finish();
                    }
                });
    }
}
