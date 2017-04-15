package com.hxw.frame.update;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.StringRes;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hxw.frame.R;
import com.hxw.frame.utils.ConvertUtils;

/**
 * Created by hxw on 2017/2/16.
 */

public class ProgressDialog extends AlertDialog {
    TextView textPercent;
    TextView textSize;
    ProgressBar progressBar;
    private Context context;

    protected ProgressDialog(@NonNull Context context) {
        this(context, false, null);
    }

    protected ProgressDialog(@NonNull Context context, boolean cancelable, @Nullable OnCancelListener cancelListener) {
        super(context, cancelable, cancelListener);
        this.context = context;

        View view = LayoutInflater.from(context).inflate(R.layout.dialog_progress, null);
        textPercent = (TextView) view.findViewById(R.id.text_percent);
        textSize = (TextView) view.findViewById(R.id.text_size);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        setView(view);

    }


    /**
     * 设置内容文章
     *
     * @param resId
     */
    public void setContentMessage(@StringRes int resId) {
        setMessage(context.getText(resId));
    }

    /**
     * 设置progress的数据
     *
     * @param current 当前量
     * @param total   总量
     */
    public void setDate(long current, long total) {
        progressBar.setMax((int) total);
        progressBar.setProgress((int) current);
        textPercent.setText(ConvertUtils.getPercent(current,total));
        textSize.setText(ConvertUtils.byte2FitMemorySize(current)+"/"
                        +ConvertUtils.byte2FitMemorySize(total));
    }


}
