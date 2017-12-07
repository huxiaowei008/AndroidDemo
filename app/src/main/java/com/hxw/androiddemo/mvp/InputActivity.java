package com.hxw.androiddemo.mvp;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.hxw.androiddemo.R;
import com.hxw.frame.base.BaseActivity;
import com.hxw.frame.di.AppComponent;
import com.hxw.frame.widget.InputView;

import butterknife.BindView;

/**
 * @author hxw
 * @date 2017/11/7
 */

public class InputActivity extends BaseActivity {
    @BindView(R.id.input)
    InputView inputView;
    @BindView(R.id.edt_max)
    EditText edtMax;
    @BindView(R.id.btn_sure)
    Button btn;

    @Override
    public int getLayoutId() {
        return R.layout.activity_input;
    }

    @Override
    public void componentInject(AppComponent appComponent) {

    }

    @Override
    public void init(Bundle savedInstanceState) {
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int max = Integer.valueOf(edtMax.getText().toString());
                if (max > 0) {
                    inputView.setMaxLength(max);
                }
            }
        });




    }
}
