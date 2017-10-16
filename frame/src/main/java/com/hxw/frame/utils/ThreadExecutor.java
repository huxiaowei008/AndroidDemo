/*
 *  Copyright (C) 2017 Bilibili
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *          http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.hxw.frame.utils;

import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import timber.log.Timber;

/**
 * 线程切换
 *
 * @author ChenSL
 */
public class ThreadExecutor {
    private static final ThreadExecutor INSTANCE = new ThreadExecutor();

    private ExecutorService mExecutorService;

    private ThreadExecutor() {
    }

    public static ThreadExecutor getInstance() {
        return INSTANCE;
    }

    public void runWorker(@NonNull Runnable runnable) {
        ensureWorkerHandlerNotNull();
        try {
            mExecutorService.execute(runnable);
        } catch (Exception e) {
            Timber.d("runnable stop running unexpected. " + e.getMessage());
        }
    }

    @Nullable
    public FutureTask<Boolean> runWorker(@NonNull Callable<Boolean> callable) {
        ensureWorkerHandlerNotNull();
        FutureTask<Boolean> task = null;
        try {
            task = new FutureTask<>(callable);
            mExecutorService.submit(task);
            return task;
        } catch (Exception e) {
            Timber.d("callable stop running unexpected. " + e.getMessage());
        }
        return task;
    }

    public void runUI(@NonNull Runnable runnable) {
        if (Looper.myLooper() == Looper.getMainLooper()) {
            runnable.run();
            return;
        }
        Handler handler = ensureUiHandlerNotNull();
        try {
            handler.post(runnable);
        } catch (Exception e) {
            Timber.d("update UI task fail. " + e.getMessage());
        }
    }

    private void ensureWorkerHandlerNotNull() {
        if (mExecutorService == null) {
            mExecutorService = new ThreadPoolExecutor(1, 8,
                    30L, TimeUnit.MILLISECONDS,
                    new LinkedBlockingQueue<Runnable>(1024), new ThreadPoolExecutor.AbortPolicy());
        }
    }

    private Handler ensureUiHandlerNotNull() {
        return new Handler(Looper.getMainLooper());
    }

}
