package com.hxw.androiddemo.mvp.recyclerviewh;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * recyclerView横向使用
 * Created by hxw on 2017/5/11.
 */

public class RecyclerVeiwHActivity extends BaseActivity {
    @BindView(R.id.rv_h)
    RecyclerView rvH;
    @BindView(R.id.tv_position)
    TextView tvPosition;

    private RecyclerViewAdapter adapter;
    private List<String> data = new ArrayList<>();

    /**
     * @return 返回布局资源ID
     */
    @Override
    public int getLayoutId() {
        return R.layout.acitvity_recycler_h;
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
        for (int i = 0; i < 10; i++) {
            data.add(i + "");
        }

        adapter = new RecyclerViewAdapter(data);

        rvH.setLayoutManager(new LinearLayoutManager(this, LinearLayout.HORIZONTAL, false));
        rvH.setAdapter(adapter);
        //使用linearSnapHelper可以使recyclerView的item居中显示
        //重新实现可以左对齐或右对齐,网上有方法
        LinearSnapHelper snapHelper = new LinearSnapHelper();
        snapHelper.attachToRecyclerView(rvH);

        rvH.addOnScrollListener(new RecyclerView.OnScrollListener() {
            /**
             * Callback method to be invoked when RecyclerView's scroll state changes.
             *
             * @param recyclerView The RecyclerView whose scroll state has changed.
             * @param newState     The updated scroll state. One of {SCROLL_STATE_IDLE},
             *                     {SCROLL_STATE_DRAGGING} or {SCROLL_STATE_SETTLING}.
             */
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://静止没有滚动
                        int position = ((LinearLayoutManager) recyclerView.getLayoutManager())
                                .findFirstVisibleItemPosition();
                        tvPosition.setText(position + "");
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://正在被外部拖拽,一般为用户正在用手指滚动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://自动滚动
                        break;
                    default:
                        break;
                }
            }
        });
    }

}
