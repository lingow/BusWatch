package com.lingoware.lingow.buswatch.app;

import android.os.Looper;

import com.lingoware.lingow.buswatch.BuildConfig;

/**
 * Created by lingow on 3/05/15.
 */
public class ThreadPreconditions {
    public static void checkOnMainThread() {
        if (BuildConfig.DEBUG) {
            if (Thread.currentThread() != Looper.getMainLooper().getThread()) {
                throw new IllegalStateException("Este metodo deberia ser llamado por el hilo principal");
            }
        }
    }
}
