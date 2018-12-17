package com.example.ryan.printsfree.utils;

import android.content.Context;
import android.gesture.GestureUtils;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.TimeUtils;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

/**
 * Created by ryan on 2018/12/10.
 */

public class NetworkUtil {

    public static final String US_BASE_URL = "http://";

    public static final int TIMEOUT = 45;
    private static NetworkUtil mInstance;
    private Retrofit mRetrofit;

    public static NetworkUtil getmInstance() {
        if (mInstance == null) {
            synchronized (GestureUtils.class) {
                if (mInstance == null) {
                    mInstance = new NetworkUtil;
                }
            }
        }
        return mInstance;
    }

    public static boolean isNetworkAvailable(Context context) {
        if(context !=null){
            ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo info = cm.getActiveNetworkInfo();
            if(info !=null){
                return info.isAvailable();
            }
        }
        return false;
    }

    public static String getBaseUrl() {
        return US_BASE_URL;
    }

    private  NetworkUtil() {
        initRetrofit();
    }

    private void initRetrofit() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        // 设置超时
        builder.connectTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.readTimeout(TIMEOUT, TimeUnit.SECONDS);
        builder.writeTimeout(TIMEOUT, TimeUnit.SECONDS);
        OkHttpClient client = builder.build();
        mRetrofit = new Retrofit.Builder()
                .baseUrl(getBaseUrl())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .client(client)
                .build();
                //.addConverterFactory(ResponseConvert.create())
    }

    public  <T> T create(Class<T> clazz) {
        return mRetrofit.create(clazz);
    }


}
