package com.minhuizhu.http.action;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;

/**
 * Created by Rex on 2016/8/2.
 */
public class OkHttpClientFactory {

    private static final int HTTP_TIMEOUT = 10;

    public static OkHttpClient create() {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .readTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .writeTimeout(HTTP_TIMEOUT, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor())
                .addInterceptor(new CryptoInterceptor());
        return clientBuilder.build();
    }
}
