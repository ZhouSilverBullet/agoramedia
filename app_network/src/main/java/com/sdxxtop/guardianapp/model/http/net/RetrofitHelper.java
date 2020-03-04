package com.sdxxtop.guardianapp.model.http.net;


import com.sdxxtop.guardianapp.app.Constants;
import com.sdxxtop.guardianapp.model.NetWorkSession;
import com.sdxxtop.guardianapp.model.http.api.ApiService;
import com.sdxxtop.guardianapp.model.http.net.interceptor.NetInterceptor;
import com.sdxxtop.guardianapp.model.http.net.interceptor.NoNetInterceptor;

import java.io.File;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitHelper {

    private static OkHttpClient okHttpClient;
    private static ApiService apiService;

    private static OkHttpClient okHttpLongClient;

    public static ApiService getApi() {
        initOkHttp();
        if (apiService == null) {
            apiService = new Retrofit.Builder()
                    .baseUrl(ApiService.BASE_URL)
                    .client(okHttpClient)
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
                    .create(ApiService.class);
        }
        return apiService;
    }

    private static void initOkHttp() {
        if (okHttpClient != null) {
            return;
        }
        OkHttpClient.Builder builder = new OkHttpClient.Builder();
        if (NetWorkSession.DEBUG()) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);
            builder.addInterceptor(loggingInterceptor);
        }

        builder.addInterceptor(new NoNetInterceptor())
                .addNetworkInterceptor(new NetInterceptor());
        File file = new File(Constants.PATH_CACHE);
        //最多缓存100m
        builder.cache(new Cache(file, 100 * 1024 * 1024));

        builder.connectTimeout(10, TimeUnit.SECONDS);
        builder.readTimeout(10, TimeUnit.SECONDS);
        builder.writeTimeout(10, TimeUnit.SECONDS);

        builder.retryOnConnectionFailure(true);

        okHttpClient = builder.build();

    }
}
