package com.hxw.frame.base;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.trello.rxlifecycle2.components.support.RxFragment;

/**
 * 因为java只能单继承,所以如果有需要继承特定Fragment的三方库,那你就需要自己自定义Fragment
 * 继承于这个特定的Fragment,然后按照将BaseFragment的格式,复制过去,记住一定要实现{@link IFragment}
 * Created by hxw on 2017/2/9.
 */

public abstract class BaseFragment extends RxFragment implements IFragment {
    protected final String TAG = this.getClass().getSimpleName();

    public BaseFragment() {
        //必须确保在Fragment实例化时setArguments(),为存取FragmentDelegate
        setArguments(new Bundle());
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View mRootView = inflater.inflate(getLayoutId(), container, false);

        return mRootView;
    }

    @Override
    public boolean useEventBus() {
        return false;
    }
}
