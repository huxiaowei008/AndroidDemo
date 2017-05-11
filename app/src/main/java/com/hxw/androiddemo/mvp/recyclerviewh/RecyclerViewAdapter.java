package com.hxw.androiddemo.mvp.recyclerviewh;

import android.support.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.hxw.androiddemo.R;

import java.util.List;

/**
 * Created by hxw on 2017/5/11.
 */

public class RecyclerViewAdapter extends BaseQuickAdapter<String, BaseViewHolder> {
    public RecyclerViewAdapter(@Nullable List<String> data) {
        super(R.layout.item_recycler_view, data);
    }

    /**
     * Implement this method and use the helper to adapt the view to the given item.
     *
     * @param holder A fully initialized helper.
     * @param item   The item that needs to be displayed.
     */
    @Override
    protected void convert(BaseViewHolder holder, String item) {
        holder.setText(R.id.tv_number, item);
    }
}
