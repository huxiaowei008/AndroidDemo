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
        return super.commitText(text, newCursorPosition);
    }

    //按键输入
    @Override
    public boolean sendKeyEvent(KeyEvent event) {
        Timber.d("KeyEventAction:" + event.getAction());
        Timber.d("KeyEventKeyCode:" + event.getKeyCode());

        return super.sendKeyEvent(event);
    }

    //删除
    @Override
    public boolean deleteSurroundingText(int beforeLength, int afterLength) {
        Timber.d("beforeLength:" + beforeLength + "  afterLength:" + afterLength);
        return super.deleteSurroundingText(beforeLength, afterLength);
    }

    //这个方法基本上会出现在切换输入法类型，点击回车（完成、搜索、发送、下一步）点击输入法右上角隐藏按钮会触发。
    @Override
    public boolean finishComposingText() {
        Timber.d("finishComposingText");
        return super.finishComposingText();
    }
}
