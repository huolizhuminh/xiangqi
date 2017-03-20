package com.minhuizhu.http.action;


import com.minhuizhu.http.util.JsonUtils;

import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * Created by Rex on 2016/7/15.
 */
public class HttpConverterFactory extends Converter.Factory {

    public static Converter.Factory create() {
        return new HttpConverterFactory();
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new ResponseConverter();
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        return new RequestConverter();
    }

    private class ResponseConverter implements Converter<ResponseBody, Object> {

        @Override
        public Object convert(ResponseBody value) throws IOException {
            return value;
        }
    }

    private class RequestConverter implements Converter<Object, RequestBody> {

        MediaType JSON_TYPE = MediaType.parse("application/json");
        MediaType PLAIN_TYPE = MediaType.parse("text/plain");
        MediaType BINARY_TYPE = MediaType.parse("application/binary");

        @Override
        public RequestBody convert(Object value) throws IOException {
            if (value == null) {
                return RequestWrapper.wrapper(PLAIN_TYPE, "");
            }
            if (value instanceof String) {
                return RequestWrapper.wrapper(JSON_TYPE, value.toString());
            } else if (value instanceof File) {
                return RequestWrapper.wrapper(BINARY_TYPE, ((File) value));
            } else if (value instanceof Convertible) {
                value = ((Convertible) value).convert();
                if (!(value instanceof String)) {
                    value = JsonUtils.toJson(value);
                }
                return RequestWrapper.wrapper(JSON_TYPE, value.toString());
            } else {
                String json = JsonUtils.toJson(value);
                return RequestWrapper.wrapper(JSON_TYPE, json);
            }
        }
    }
}
