package com.hxw.androiddemo.mvp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;
import com.hxw.input.InputView;

import butterknife.BindView;

/**
 * @author hxw
 * @date 2017/11/7
 */

public class InputActivity extends BaseActivity {


    @BindView(R.id.input1)
    InputView input1;
    @BindView(R.id.input2)
    InputView input2;
    @BindView(R.id.btn1)
    Button btn1;
    @BindView(R.id.btn2)
    Button btn2;

    @Override
    public int getLayoutId() {
        return R.layout.activity_input;
    }

    @Override
    public void componentInject(AppComponent appComponent) {

    }

    @Override
    public void init(Bundle savedInstanceState) {
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input1.setMaxLength(8);
                input2.setMaxLength(8);
            }
        });

        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                input1.setMaxLength(6);
                input2.setMaxLength(6);
            }
        });
    }


}
