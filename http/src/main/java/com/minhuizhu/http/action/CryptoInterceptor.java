package com.minhuizhu.http.action;


import com.minhuizhu.http.base.CryptoHelper;
import com.orhanobut.logger.Logger;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * Created by rex.wei on 2017/1/18.
 */

public class CryptoInterceptor implements Interceptor {

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();
        Response response;
        String localTag = request.header(HTTPConstants.X_LOCAL_TYPE);
        localTag = localTag == null ? "" : localTag;

        if (HTTPConstants.ENCRYPT_USER.equals(localTag)) {
            request = encryptUserReq(request);
        }

        response = chain.proceed(request);
        if (response.isSuccessful()) {
            response = decryptRespBody(response);
        }
        return response;
    }

    private Request encryptUserReq(Request request) {
        RequestBody requestBody = request.body();
        if (requestBody == null) {
            return request.newBuilder().header(HTTPConstants.X_CONTENT, HTTPConstants.ENCRYPT_AVER).build();
        }
        String plain = bodyToString(requestBody);
        String bodyEncrypted = CryptoHelper.encodeByLoginRandomKey(plain);
        requestBody = RequestBody.create(MediaType.parse(HTTPConstants.ENCRYPT_AVER), bodyEncrypted.getBytes());
        return request.newBuilder().method(request.method(), requestBody).build();
    }



    private Response decryptRespBody(Response response) {
        ResponseBody responseBody = response.body();
        String contentType = response.header(HTTPConstants.X_CONTENT, "");
        boolean noEncrypted = responseBody.contentType() == null || !contentType.contains(HTTPConstants.ENCRYPT);
        if (noEncrypted) {
            return response;
        }
       Logger.d("this response need to be decrypted!");
        String bodyDecrypted = "";
        try {
            bodyDecrypted = CryptoHelper.decodeByLoginRandomKey(responseBody.string());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response.newBuilder().body(ResponseBody.create(responseBody.contentType(), bodyDecrypted)).build();
    }

    private String bodyToString(RequestBody request) {
        final Buffer buffer = new Buffer();
        try {
            if (request != null)
                request.writeTo(buffer);
            else
                return "";
            return buffer.readUtf8();
        } catch (Exception e) {
            return "internal exception -> " + e;
        } finally {
            buffer.close();
        }
    }

    private static class RawModel {
        public int code;
        public String msg;
        public Object data;
    }
}
