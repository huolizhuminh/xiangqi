package com.minhuizhu.http.util;

import com.minhuizhu.common.util.App;
import com.minhuizhu.common.util.ContextUtils;
import com.minhuizhu.http.action.Data;
import com.minhuizhu.http.action.HTTPConstants;
import com.minhuizhu.http.action.HeaderInterceptor;
import com.minhuizhu.http.action.Receiver;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InterruptedIOException;
import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import okio.ByteString;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Converter;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.minhuizhu.http.action.HTTPConstants.CODE_INTERNAL;
import static com.minhuizhu.http.action.HTTPConstants.CODE_NETWORK_UNAVAILABLE;
import static com.minhuizhu.http.action.HTTPConstants.CODE_TIME_OUT;


/**
 * Created by rex.wei on 2016/8/17.
 */
public final class RetrofitUtils {

    private static final int HTTP_TIMEOUT = 5;
    private static final RetrofitInterface retrofit = make(HTTP_TIMEOUT);

    public static Observable<Data<String>> get(final int timeout, final String url) {
        return Observable.create(new Observable.OnSubscribe<Response<ResponseBody>>() {
            @Override
            public void call(Subscriber<? super Response<ResponseBody>> subscriber) {
                subscriber.onStart();
                try {
                    subscriber.onNext(make(timeout).getData(url).execute());
                } catch (Exception e) {
                    subscriber.onError(e);
                }
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()).flatMap(new Func1<Response<ResponseBody>, Observable<Data<String>>>() {
            @Override
            public Observable<Data<String>> call(Response<ResponseBody> response) {
                int code = CODE_INTERNAL;
                String error = null, entity = null;
                try {
                    if (response.isSuccessful()) {
                        entity = response.body().string();
                        code = 0;
                    } else {
                        code = response.code();
                        error = response.errorBody().string();
                    }
                } catch (Exception e) {
                    error = e.getLocalizedMessage();
                }
                return Observable.just(new Data<>(code, error, entity));
            }
        }).onErrorReturn(new Func1<Throwable, Data<String>>() {
                             @Override
                             public Data<String> call(Throwable throwable) {
                                 int code = CODE_INTERNAL;
                                 if (!ContextUtils.isNetworkAvailable(App.getContext())) {
                                     code = CODE_NETWORK_UNAVAILABLE;
                                 } else if (throwable instanceof InterruptedIOException) {
                                     code = CODE_TIME_OUT;
                                 }
                                 return new Data<>(code, throwable.getLocalizedMessage(), null);
                             }
                         }
        );
    }

    public static void get(String url, final Receiver<Data<String>> receiver) {
        retrofit.getData(url).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                try {
                    if (response.isSuccessful()) {
                        receiver.onReceive(new Data<>(0, null, response.body().string()));
                    } else {
                        receiver.onReceive(new Data<>(response.code(), null, response.errorBody().string()));
                    }
                } catch (Exception e) {
                    receiver.onReceive(new Data<String>(CODE_INTERNAL, e.getMessage(), null));
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                receiver.onReceive(new Data<String>(Integer.MIN_VALUE, t.getMessage(), null));
            }
        });
    }

    public static void getFile(String url, final File file, final Receiver<Data<String>> receiver) {
        Observable.just(retrofit.getData(url)).
                map(new Func1<Call<ResponseBody>, Boolean>() {
                    @Override
                    public Boolean call(Call<ResponseBody> responseBodyCall) {
                        try {
                            Response<ResponseBody> response = responseBodyCall.execute();
                            if (response.isSuccessful()) {
                                return writeResponseBodyToDisk(file, response.body());
                            }
                            return false;
                        } catch (IOException e) {
                            e.printStackTrace();
                            return false;
                        }
                    }
                }).subscribeOn(Schedulers.io()).
                observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Subscriber<Boolean>() {
                    @Override
                    public void onCompleted() {

                    }

                    @Override
                    public void onError(Throwable e) {
                        receiver.onReceive(new Data<String>(-1, "", null));
                    }

                    @Override
                    public void onNext(Boolean aBoolean) {
                        if (aBoolean) {
                            receiver.onReceive(new Data<String>(0, "", null));
                        } else {
                            receiver.onReceive(new Data<String>(-1, "", null));
                        }
                    }
                });

    }

    private static boolean writeResponseBodyToDisk(File futureStudioIconFile, ResponseBody body) {
        try {

            InputStream inputStream = null;
            OutputStream outputStream = null;

            try {
                byte[] fileReader = new byte[4096];
                inputStream = body.byteStream();
                outputStream = new FileOutputStream(futureStudioIconFile);

                while (true) {
                    int read = inputStream.read(fileReader);
                    if (read == -1) {
                        break;
                    }
                    outputStream.write(fileReader, 0, read);
                }
                outputStream.flush();
                return true;
            } catch (IOException e) {
                return false;
            } finally {
                if (inputStream != null) {
                    inputStream.close();
                }

                if (outputStream != null) {
                    outputStream.close();
                }
            }
        } catch (IOException e) {
            return false;
        }
    }




    private interface RetrofitInterface {
        @Streaming
        @GET
        Call<ResponseBody> getData(@Url String url);

        @POST
        Call<ResponseBody> postTradeAction(@Url String url, @Header(HTTPConstants.X_TRADE_AUTH) String tradeAuth, @Body String body);
    }

    private static RetrofitInterface make(int timeout) {
        // base url is required when use retrofit but useless for our request
        Retrofit.Builder retrofitBuilder = new Retrofit.Builder()
                .baseUrl("http://www.yyf.com")
                .client(createOkHttpClient(timeout))
                .addConverterFactory(new HttpConverterFactory());
        return retrofitBuilder.build().create(RetrofitInterface.class);
    }

    private static OkHttpClient createOkHttpClient(int timeout) {
        OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder();
        clientBuilder.connectTimeout(timeout, TimeUnit.SECONDS)
                .readTimeout(timeout, TimeUnit.SECONDS)
                .writeTimeout(timeout, TimeUnit.SECONDS)
                .addInterceptor(new HeaderInterceptor());
        return clientBuilder.build();
    }

    private static class HttpConverterFactory extends Converter.Factory {

        @Override
        public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
            return new ResponseConverter();
        }

        @Override
        public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
            return new RequestConverter();
        }

        class ResponseConverter implements Converter<ResponseBody, Object> {

            @Override
            public Object convert(ResponseBody value) throws IOException {
                return value.string();
            }
        }

        class RequestConverter implements Converter<Object, RequestBody> {

            @Override
            public RequestBody convert(Object value) throws IOException {
                if (value == null) {
                    return RequestBody.create(MediaType.parse("text/plain"), "");
                }
                return RequestBody.create(MediaType.parse("text/encrypted;aver=1"), ByteString.encodeUtf8(value.toString()));
            }
        }
    }

}
