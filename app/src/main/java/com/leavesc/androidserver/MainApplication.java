package com.leavesc.androidserver;

import android.app.Application;

/**
 * 作者：leavesC
 * 时间：2018/4/5 16:28
 * 描述：
 */
public class MainApplication extends Application{

    private static MainApplication sInstance;

    @Override
    public void onCreate() {
        super.onCreate();
        if (sInstance == null) {
            sInstance = this;
        }
    }

    public static MainApplication get() {
        return sInstance;
    }

}
