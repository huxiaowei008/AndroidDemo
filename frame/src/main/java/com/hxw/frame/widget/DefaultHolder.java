package com.hxw.frame.widget;

import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.hxw.frame.utils.UIUtils;

/**参考实现，后期会删的
 * Created by jess on 2015/11/24.
 */
public abstract class DefaultHolder<T> extends RecyclerView.ViewHolder implements View.OnClickListener {
    protected OnViewClickListener mOnViewClickListener = null;
    protected final String TAG = this.getClass().getSimpleName();

    public DefaultHolder(View itemView) {
        super(itemView);
        itemView.setOnClickListener(this);//点击事件
        UIUtils.bindTarget(this, itemView);//绑定
    }



    /**
     * 设置数据
     * 刷新界面
     *
     * @param
     * @param position
     */
    public abstract void setData(T data, int position);


    /**
     * 释放资源
     */
    protected void onRelease(){

    }

    @Override
    public void onClick(View view) {
        if (mOnViewClickListener != null) {
            mOnViewClickListener.onViewClick(view, this.getPosition());
        }
    }

    public interface OnViewClickListener {
        void onViewClick(View view, int position);
    }

    public void setOnItemClickListener(OnViewClickListener listener) {
        this.mOnViewClickListener = listener;
    }
}
