package com.hxw.frame.widget;

import android.view.KeyEvent;
import android.view.inputmethod.BaseInputConnection;

import timber.log.Timber;

/**
 * @author hxw
 * @date 2017/12/5
 */

public class EasyInputConnection extends BaseInputConnection {

    private InputView mInputView;

    public EasyInputConnection(InputView inputView) {
        super(inputView, true);
        this.mInputView = inputView;
    }

    //文本输入
    @Override
    public boolean commitText(CharSequence text, int newCursorPosition) {
        Timber.d("commitText:" + text + "  newCursorPosition:" + newCursorPosition);
        mInputView.sendOnTextChanged(text, true);
        return super.commitText(text, newCursorPosition);
    }

    //按键输入
    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        Timber.d("KeyEventAction:" + event.getAction());
        Timber.d("KeyEventKeyCode:" + event.getKeyCode());
        if (event.getAction()==KeyEvent.ACTION_UP){
            //只监听按键抬起来的动作
            if (event.getKeyCode()==KeyEvent.KEYCODE_DEL){
                mInputView.sendOnTextChanged("",false);
            }
        }
        return super.sendKeyEvent(event);
    }

    //这个方法基本上会出现在切换输入法类型，点击回车（完成、搜索、发送、下一步）点击输入法右上角隐藏按钮会触发。
    @Override
    public boolean finishComposingText() {
        Timber.d("finishComposingText");
        return super.finishComposingText();
    }
}
