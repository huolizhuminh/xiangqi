package com.minhuizhu.http.action;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by rex.wei on 2017/1/18.
 */

public class HeaderInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        String uuid = HeaderConfig.getInstance().getGUID();
        Request request = chain.request().newBuilder()
                .addHeader(HTTPConstants.X_DEVICE, HeaderConfig.getInstance().getDevice())
                .addHeader(HTTPConstants.X_PRODUCT, HeaderConfig.getInstance().getProduct())
                .addHeader(HTTPConstants.COOKIE, HeaderConfig.getInstance().getCookie())
                .addHeader(HTTPConstants.X_GUID, uuid)
                .build();
        Response response = chain.proceed(request);
        HeaderConfig.getInstance().parseRespCookies(request.url(), response.headers().values("set-cookie"));
        return response.newBuilder().addHeader(HTTPConstants.X_LOCAL_ID, uuid).build();
    }
}