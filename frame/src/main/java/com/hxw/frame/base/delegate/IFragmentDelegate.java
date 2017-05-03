package com.hxw.frame.base.delegate;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;

/**
 * Created by hxw on 2017/5/3.
 */

public interface IFragmentDelegate {
    void onAttach(Context context);

    void onCreate(@Nullable Bundle savedInstanceState);

    @Nullable
    void onCreateView(View view, @Nullable Bundle savedInstanceState);

    void onActivityCreate(@Nullable Bundle savedInstanceState);

    void onStart();

    void onResume();

    void onPause();

    void onStop();

    void onSaveInstanceState(Bundle outState);

    void onDestroyView();

    void onDestroy();

    void onDetach();
}
