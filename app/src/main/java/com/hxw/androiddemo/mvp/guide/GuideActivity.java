package com.hxw.androiddemo.mvp.guide;

import android.graphics.Color;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.view.ViewPager;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.hxw.androiddemo.R;
import com.hxw.androiddemo.base.Constant;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.utils.RxUtils;
import com.tmall.ultraviewpager.UltraViewPager;

import butterknife.BindView;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

/**
 * Created by hxw on 2017/4/15.
 */

public class GuideActivity extends BaseActivity {

    @BindView(R.id.tv_cd)
    TextView tvCd;
    @BindView(R.id.ultra_viewpager)
    UltraViewPager ultraViewpager;
    @BindView(R.id.btn_guide)
    Button btnGuide;

    private UltraPagerAdapter adapter;

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
     * @param appComponent 基础注入器
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

        btnGuide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

    }

    private void initViews() {
        ultraViewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        adapter = new UltraPagerAdapter(R.mipmap.uoko_guide_background_1,
                R.mipmap.uoko_guide_background_2, R.mipmap.uoko_guide_background_3);
        ultraViewpager.setAdapter(adapter);
        //内置indicator初始化
        ultraViewpager.initIndicator();
        ultraViewpager.getIndicator()
                .setFocusColor(Color.WHITE)
                .setNormalColor(Color.alpha(0))
                .setRadius(10)
                .setStrokeWidth(4)
                .setStrokeColor(Color.WHITE)
                .setOrientation(UltraViewPager.Orientation.HORIZONTAL)
                .setGravity(Gravity.CENTER_HORIZONTAL | Gravity.BOTTOM)
                .setMargin(0, 0, 0, 10)
                .build();
        ultraViewpager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (position == 2) {
                    btnGuide.setVisibility(View.VISIBLE);
                } else {
                    btnGuide.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

    }

    private void initSplash() {
        ultraViewpager.setScrollMode(UltraViewPager.ScrollMode.HORIZONTAL);
        adapter = new UltraPagerAdapter(R.mipmap.uoko_guide_background_1);
        ultraViewpager.setAdapter(adapter);

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
