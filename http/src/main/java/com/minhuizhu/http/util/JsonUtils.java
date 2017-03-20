package com.minhuizhu.http.util;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.internal.$Gson$Types;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * Created by Rex on 2016/8/2.
 */
public final class JsonUtils {
    private static final Gson gson = makeGson();

    private static Gson makeGson() {
        GsonBuilder builder = new GsonBuilder();
        return builder.create();
    }

    public static String toJson(Object source) {
        return gson.toJson(source);
    }

    public static <T> T fromJson(String json, Type typeOfT) {
        if (json == null || typeOfT == null) {
            return null;
        }
        try {
            return gson.fromJson(json, typeOfT);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static <T> T parseJson(String json, Type typeOfT) throws Exception {
        return gson.fromJson(json, typeOfT);
    }

    public static Type getGenericType(Object object) {
        if (object == null) {
            return null;
        }
        Type type = object.getClass().getGenericInterfaces()[0];
        if (type instanceof Class) {
            return null;
        }
        ParameterizedType parameterizedType = ((ParameterizedType) type);
        return $Gson$Types.canonicalize(parameterizedType.getActualTypeArguments()[0]);
    }

}
