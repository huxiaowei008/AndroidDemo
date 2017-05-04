package com.hxw.frame.base.delegate;

import android.os.Bundle;
import android.support.annotation.Nullable;

import java.io.Serializable;

/**
 * Created by hxw on 2017/5/3.
 */

public interface IActivityDelegate extends Serializable {
    String ACTIVITY_DELEGATE = "activity_delegate";

    @Nullable
    void onCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onRestart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroy();
}
