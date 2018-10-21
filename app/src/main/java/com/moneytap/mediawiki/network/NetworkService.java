package com.moneytap.mediawiki.network;

import android.content.Context;
import android.util.Log;

import com.jakewharton.retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class NetworkService {

    private NetworkService() {
    }

    public static String BASE_URL =
            "https://en.wikipedia.org//w/";
    private static HttpLoggingInterceptor httpLoggingInterceptor;
    private static File cacheFile;
    //private static Cache cache;
    private static OkHttpClient okHttpClient;


    public static MediaWikiAPI create(Context context) {
        Retrofit.Builder builder = new Retrofit.Builder()
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .client(getOkHttpClient(context.getApplicationContext()))
                .baseUrl(BASE_URL);
        return builder.build().create(MediaWikiAPI.class);
    }

    public static OkHttpClient getOkHttpClient(Context context) {
        OkHttpClient.Builder builder = new OkHttpClient.Builder()
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .connectTimeout(60, TimeUnit.SECONDS)
                .addInterceptor(getHttpLoggingInterceptor());
        List<Protocol> protocols = new ArrayList();
        protocols.add(Protocol.HTTP_1_1);
        builder.protocols(protocols);
        okHttpClient = builder.build();
        return okHttpClient;
    }

    public static HttpLoggingInterceptor getHttpLoggingInterceptor() {
        if (httpLoggingInterceptor == null)
            synchronized (NetworkService.class) {
                if (httpLoggingInterceptor == null) {
                    httpLoggingInterceptor = new HttpLoggingInterceptor(new HttpLoggingInterceptor.Logger() {
                        @Override
                        public void log(String message) {
                            Log.e("Network", message);
                        }
                    });
                    httpLoggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
                }
            }
        return httpLoggingInterceptor;
    }

    public static File getCacheFile(Context context) {
        if (cacheFile == null)
            synchronized (NetworkService.class) {
                if (cacheFile == null) {
                    cacheFile = new File(context.getApplicationContext().getCacheDir(), "okhttp_cache");
                }
            }
        return cacheFile;
    }
}
