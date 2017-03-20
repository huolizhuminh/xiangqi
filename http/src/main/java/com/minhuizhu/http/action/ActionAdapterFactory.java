package com.minhuizhu.http.action;


import android.text.TextUtils;

import com.google.gson.JsonParseException;
import com.minhuizhu.http.util.JsonUtils;
import com.orhanobut.logger.Logger;

import java.io.InterruptedIOException;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.net.UnknownHostException;

import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Response;
import retrofit2.Retrofit;
import rx.Observable;
import rx.Subscriber;
import rx.schedulers.Schedulers;

/**
 * Created by Rex on 2016/7/17.
 */
public class ActionAdapterFactory extends CallAdapter.Factory {

    public static CallAdapter.Factory create() {
        return new ActionAdapterFactory();
    }

    @Override
    public CallAdapter<?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        Type genericType = Object.class;
        if (returnType instanceof ParameterizedType) {
            genericType = getParameterUpperBound(0, (ParameterizedType) returnType);
        }
        return new ActionCallAdapter(genericType);
    }


    private class ActionCallAdapter implements CallAdapter<Action<?>> {

        private Type responseType;

        public ActionCallAdapter(Type type) {
            responseType = type;
        }

        @Override
        public Type responseType() {
            return responseType;
        }

        @Override
        @SuppressWarnings("unchecked")
        public Action<?> adapt(Call call) {
            return new Action<>(makeObservable(call), getCacheKey(call), getCacheTime(call));
        }

        private Observable makeObservable(final Call call) {
            return Observable.create(new Observable.OnSubscribe<Object>() {
                @Override
                @SuppressWarnings("unchecked")
                public void call(Subscriber<? super Object> subscriber) {
                    subscriber.onStart();
                    subscriber.onNext(executeCall(call, responseType));
                    subscriber.onCompleted();
                }
            }).subscribeOn(Schedulers.io());
        }

        private String getCacheKey(Call call) {
            Request request = call.request();
            String cacheHeader = request.header(HTTPConstants.X_CACHE);
            if (TextUtils.isEmpty(cacheHeader)) {
                return null;
            }
            String url = request.method() + request.url().toString();
            int UrlHashCode = url.hashCode();
            RequestBody requestBody = request.body();
            if (requestBody instanceof RequestWrapper) {
                int bodyHashCode = ((RequestWrapper) requestBody).getBody().hashCode();
                return String.valueOf(UrlHashCode + bodyHashCode);
            } else {
                return String.valueOf(UrlHashCode);
            }
        }

        private int getCacheTime(Call call) {
            String cacheHeader = call.request().header(HTTPConstants.X_CACHE);
            int time = -1;
            if (!TextUtils.isEmpty(cacheHeader)) {
                try {
                    time = Integer.parseInt(cacheHeader);
                } catch (Exception e) {
                }
            }
            return time;
        }
    }

    @SuppressWarnings("unchecked")
    public static <T> T executeCall(Call call, Type responseType) {
        String url = call.request().url().toString();
        String method = call.request().method();
        String requestInfo = getRequestInfo(call);
        boolean parseError = false;
        try {
            Response response = call.clone().execute();
            int httpCode = response.code();
            String uuid = response.headers().get(HTTPConstants.X_LOCAL_ID);
            Object resp;
            if (responseType == Object.class || responseType == ResponseBody.class) {
                resp = response.body();
            } else {
                resp = getStringResponse(response);
            }
            Logger.i(url + ", httpCode=" + httpCode + " ,method=" + method + " ,uuid=" + uuid + " ,request=" + requestInfo + ",resp=" + resp);
            if (resp instanceof String) {
                String respString = (String) resp;
                if (TextUtils.isEmpty(respString)) {
                    throw new ActionException(HTTPConstants.CODE_NO_RESPONSE, "empty response from http");
                }
                parseError = true;
                Object result = JsonUtils.parseJson(respString, responseType);
                parseError = false;
                return (T) result;
            } else {
                return (T) resp;
            }
        } catch (Throwable throwable) {
            String hint = "fail url=";
            if (parseError) {
                hint = " request successfully but parse by error,url=";
            }
            Logger.e(hint + url + " ,method=" + method + " ,request=" + requestInfo + " ,error=" + throwable);
            throw getActionException(throwable);
        }
    }

    private static ActionException getActionException(Throwable throwable) {
        Logger.e("the real exception info is " + throwable);
        if (throwable instanceof InterruptedIOException) {
            return new ActionException(HTTPConstants.CODE_TIME_OUT, throwable.getMessage());
        } else if (throwable instanceof ActionException) {
            throw (ActionException) throwable;
        } else if (throwable instanceof UnknownHostException) {
            return new ActionException(HTTPConstants.CODE_UNKNOWN_HOST, throwable.getMessage());
        } else if (throwable instanceof JsonParseException) {
            return new ActionException(HTTPConstants.CODE_PARSE_ERROR, throwable.getMessage());
        } else {
            return new ActionException(HTTPConstants.CODE_INTERNAL, throwable.getMessage());
        }
    }

    @SuppressWarnings("unchecked")
    private static String getStringResponse(Response response) throws Exception {
        if (response.isSuccessful()) {
            String respBody;
            if (response.body() == null) {
                respBody = "";
            } else if (response.body() instanceof ResponseBody) {
                respBody = ((ResponseBody) response.body()).string();
            } else {
                respBody = response.body().toString();
            }
            return respBody;
        } else {
            String httpError = response.errorBody().string();
            throw new ActionException(response.code(), httpError);
        }
    }

    private static String getRequestInfo(Call call) {
        RequestBody requestBody = call.request().body();
        if (requestBody instanceof RequestWrapper) {
            return ((RequestWrapper) requestBody).getBody();
        }
        return null;
    }


}
