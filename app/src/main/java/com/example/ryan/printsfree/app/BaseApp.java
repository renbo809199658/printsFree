package com.example.ryan.printsfree.app;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;


/**
 * Created by ryan on 2018/9/10.
 */

public class BaseApp extends Application {

    private  static Context mContext;
    private  static  Thread mMainThread;
    private  static  long mMainThreadId;
    private static Looper mMainLooper;
    private  static Handler mHandler;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
    }
}
