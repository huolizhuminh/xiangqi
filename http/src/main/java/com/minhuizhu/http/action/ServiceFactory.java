package com.minhuizhu.http.action;


import retrofit2.Retrofit;

/**
 * Created by Rex on 2016/8/2.
 */
public final class ServiceFactory {

    public static <T> T make(final String baseUrl, final Class<T> service) {
        return getRetrofit(baseUrl).create(service);
    }

    private static Retrofit getRetrofit(String baseUrl) {
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(HttpConverterFactory.create())
                .addCallAdapterFactory(ActionAdapterFactory.create())
                .client(OkHttpClientFactory.create());
        return retrofitBuilder.build();
    }

}
