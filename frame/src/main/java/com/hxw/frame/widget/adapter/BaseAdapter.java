package com.hxw.frame.widget.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.ArrayList;
import java.util.List;

/**
 * recyclerView的适配器基类,后期有功能在添加,现在很简单,若功能复杂推荐用
 * 第三方开源库,如BaseRecyclerViewAdapterHelper
 * Created by hxw on 2017/2/14.
 */

public abstract class BaseAdapter<B, H extends BaseHolder> extends RecyclerView.Adapter<H> {


    protected final String TAG = this.getClass().getSimpleName();
    protected Context mContext;
    protected int mLayoutResId;
    protected LayoutInflater mLayoutInflater;
    protected List<B> mData;

    public BaseAdapter() {
        this(0);
    }

    public BaseAdapter(int layoutResId) {
        this.mData = new ArrayList<>();
        if (layoutResId != 0) {
            this.mLayoutResId = layoutResId;
        }
    }

    /**
     * 创建Holder
     *
     * @param parent
     * @param viewType
     * @return
     */
    @Override
    public H onCreateViewHolder(ViewGroup parent, int viewType) {

        this.mContext = parent.getContext();
        this.mLayoutInflater = LayoutInflater.from(mContext);
        View view = mLayoutInflater.inflate(mLayoutResId, parent, false);
        H baseHolder = (H) new BaseHolder(view);
        return baseHolder;
    }

    /**
     * 绑定数据
     *
     * @param holder
     * @param position
     */
    @Override
    public void onBindViewHolder(H holder, int position) {
        convert(holder, mData.get(position));
    }

    /**
     * 数据的个数
     *
     * @return
     */
    @Override
    public int getItemCount() {
        return mData.size();
    }

    protected abstract void convert(H holder, B item);


}
