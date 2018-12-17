package com.example.ryan.printsfree.utils;

import android.content.Context;
import android.content.res.Resources;
import android.util.TypedValue;
import android.widget.Toast;
import android.os.Handler;
import com.example.ryan.printsfree.app.BaseApp;

/**
 * Created by ryan on 2018/9/13.
 */

public class UIUtils {

    public  static Toast mToast;

    public static void showToast(String msg) {
        showToast(msg, Toast.LENGTH_SHORT);
    }

    public static void  showToast(String msg, int duration) {
        if (mToast == null) {
            mToast = Toast.makeText(getContext(), "", duration);
        }
        mToast.setText(msg);
        mToast.show();
    }

    public static void  showToastSafely(final String msg) {
        UIUtils.getMainThreadHandler().post(new Runnable() {
            @Override
            public void run() {
                if (mToast == null) {
                    mToast = Toast.makeText(getContext(), "", Toast.LENGTH_SHORT);
                }
                mToast.setText(msg);
                mToast.show();
            }
        });
    }

    public static Context getContext() {
        return BaseApp.getContext();
    }


    public static Resources getResource() {
        return getContext().getResources();
    }

    public static String getString(int resId) {
        return getResource().getString(resId);
    }

    public static String getString(int id, Object...formatArgs) {
        return getResource().getString(id, formatArgs);
    }

    public static String[] getStringArr(int resId) {
        return getResource().getStringArray(resId);
    }

    public static int getColor(int colorId) {
        return getResource().getColor(colorId);
    }

    public static String getPackageName() {
        return getContext().getPackageName();
    }


    public static Handler getMainThreadHandler() {
        return BaseApp.getMainHandler();
    }

    public static long getMainThreadId() {
        return BaseApp.getMainThreadId();
    }

    public static void postTaskInMainThread(Runnable task) {
        int currentThreadId = android.os.Process.myTid();
        if (currentThreadId == getMainThreadId()) {
            task.run();
        } else {
            getMainThreadHandler().post(task);
        }
    }

    public static void postTaskInMainThreadDelay(Runnable task, int delayMillis) {
        getMainThreadHandler().postDelayed(task, delayMillis);
    }

    public static void  removeTask(Runnable task) {
        getMainThreadHandler().removeCallbacks(task);
    }

    public static int dipToPx(int dip) {
        float density = getResource().getDisplayMetrics().density;
        int px = (int) (dip * density + 0.5f);
        return px;
    }

    public static int pxToDip(int px) {
        float density = getResource().getDisplayMetrics().density;
        int dip = (int)(px / density + 0.5f);
        return dip;
    }

    public static int spTopx(int sp) {
        return (int) (TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp, getResource().getDisplayMetrics()) + 0.5f);
    }

}
