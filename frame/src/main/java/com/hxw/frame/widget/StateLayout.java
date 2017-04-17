package com.hxw.frame.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.support.annotation.AttrRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;

import com.hxw.frame.R;

import java.util.HashMap;
import java.util.Map;

/**
 * 简单实用的页面状态统一管理 ，加载中、无网络、无数据、出错等状态的随意切换
 * Created by hxw on 2017/4/17.
 */

public class StateLayout extends FrameLayout {

    private int mEmptyViewResId;//空图
    private int mErrorViewResId;//出错图
    private int mLoadingViewResId;//加载图
    private int mNoNetworkViewResId;//网络出错图
    private int mContentViewResId;//内容图
    private LayoutInflater mInflater;
    private OnClickListener mOnRetryClickListener;//页面点击事件的监听,可用于重新加载等的监听
    private Map<Integer, View> mResId = new HashMap<>();//存放布局

    public StateLayout(@NonNull Context context) {
        this(context, null);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public StateLayout(@NonNull Context context, @Nullable AttributeSet attrs, @AttrRes int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mInflater = LayoutInflater.from(context);
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.StateLayout, defStyleAttr, 0);
        mEmptyViewResId = a.getResourceId(R.styleable.StateLayout_emptyView, R.layout.state_empty_view);
        mErrorViewResId = a.getResourceId(R.styleable.StateLayout_errorView, R.layout.state_error_view);
        mLoadingViewResId = a.getResourceId(R.styleable.StateLayout_loadingView, R.layout.state_loading_view);
        mNoNetworkViewResId = a.getResourceId(R.styleable.StateLayout_noNetworkView, R.layout.state_no_network_view);
        a.recycle();
    }

    private void setContentView(View view) {
        mContentViewResId = view.getId();
        mResId.put(mContentViewResId, view);
    }

    public void showEmpty() {
        show(mEmptyViewResId);
    }

    public void showError() {
        show(mErrorViewResId);
    }

    public void showLoading() {
        show(mLoadingViewResId);
    }

    public void showNoNetwork() {
        show(mNoNetworkViewResId);
    }

    public void showContent() {
        show(mContentViewResId);
    }

    private void show(int resId) {
        for (View view : mResId.values()) {
            view.setVisibility(GONE);
        }
        getLayout(resId).setVisibility(VISIBLE);
    }

    private View getLayout(int resId) {
        if (mResId.containsKey(resId)) {
            return mResId.get(resId);
        }
        View view = mInflater.inflate(resId, this, false);
        view.setVisibility(GONE);
        addView(view);
        mResId.put(resId, view);
        if (resId == mErrorViewResId || resId == mNoNetworkViewResId) {
            View v = view.findViewById(R.id.view_retry);
            if (mOnRetryClickListener != null) {
                if (v != null) {
                    v.setOnClickListener(mOnRetryClickListener);
                } else {
                    view.setOnClickListener(mOnRetryClickListener);
                }

            }
        }
        return view;
    }

    /**
     * 设置重试点击事件
     *
     * @param onRetryClickListener 重试点击事件
     */
    public void setOnRetryClickListener(OnClickListener onRetryClickListener) {
        this.mOnRetryClickListener = onRetryClickListener;
    }

    @Override
    protected void onFinishInflate() {
        super.onFinishInflate();
        if (getChildCount() == 0) {
            return;
        }
        if (getChildCount() > 1) {
            removeViews(1, getChildCount() - 1);
        }
        View view = getChildAt(0);
        setContentView(view);
    }
}
