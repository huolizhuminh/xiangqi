package com.minhuizhu.http.action;


import android.text.TextUtils;

import java.util.concurrent.TimeUnit;

import rx.Observable;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by Rex on 2016/7/17.
 */
public class Action<T> {
    protected final Observable<T> observable;
    private boolean isBackground;
    private Receiver receiver;
    private Receiver cacheReceiver;
    private Subscriber<T> subscriber;
    /**
     * action cannot cache response if key is null
     */
    private String cacheKey = "";
    private String serialKey = "";
    private int cacheTime = -1;
    private boolean isCachePosted = false;

    public static <T> Action<T> just(final T value) {
        return new Action<>(Observable.create(new Observable.OnSubscribe<T>() {
            @Override
            public void call(Subscriber<? super T> subscriber) {
                subscriber.onStart();
                subscriber.onNext(value);
                subscriber.onCompleted();
            }
        }).subscribeOn(Schedulers.io()));
    }

    protected Action(Observable<T> observable) {
        this.observable = observable;
    }

    protected Action(Observable<T> observable, String cacheKey, int cacheSeconds) {
        this.observable = observable;
        this.cacheKey = cacheKey;
        this.cacheTime = cacheSeconds;
    }

    public final <R> Action<R> map(Func1<? super T, ? extends R> func) {
        return new Action<>(observable.map(func));
    }

    public final <R> Action<R> flatMap(final Func1<T, Action<R>> func) {
        return new Action<R>(observable.flatMap(new Func1<T, Observable<? extends R>>() {
            @Override
            public Observable<R> call(T t) {
                return func.call(t).observable;
            }
        }));
    }

    public final Action<T> noError(Func1<Throwable, ? extends T> func1) {
        observable.onErrorReturn(func1);
        return this;
    }

    /**
     * this function is not so good, since the receiver will lost if there are other operators instead of {@link #run()} after it
     *
     * @param receiver
     * @return
     */
    public Action<T> listen(Receiver<? extends T> receiver) {
        this.receiver = receiver;
        return this;
    }

    /**
     * receiver runs in main thread by default, this function will transform it to back end.
     *
     * @return
     */
    public Action<T> onBackgroundReceive() {
        this.isBackground = true;
        return this;
    }

    public Action<T> onBackground() {
        observable.subscribeOn(Schedulers.io());
        return this;
    }

    public Action<T> listenCache(Receiver<?> receiver) {
        this.cacheReceiver = receiver;
        return this;
    }

    public void run() {
        run(null);
    }

    public void run(String serialKey) {
        if (!TextUtils.isEmpty(serialKey)) {
            this.serialKey = serialKey;
        }
        observable.subscribe(getSubscriber());
    }

    public void runInterval(int seconds) {
        Observable.interval(0, seconds, TimeUnit.SECONDS).flatMap(new Func1<Long, Observable<? extends T>>() {
            @Override
            public Observable<? extends T> call(Long aLong) {
                return observable;
            }
        }).subscribe(getSubscriber());
    }

    public void cancelInterval() {
        if (subscriber != null) {
            subscriber.unsubscribe();
        }
    }

    private Subscriber<T> getSubscriber() {
        if (subscriber == null || subscriber.isUnsubscribed()) {
            subscriber = new SimpleSubscriber<T>() {
                @Override
                public void onNext(T t) {
                    if (isBackground && receiver != null) {
                        receiver.onReceive(fixDataModule(t));
                        return;
                    }
                    Broadcaster.getInstance().postData(receiver, fixDataModule(t));
                }

                @Override
                @SuppressWarnings("unchecked")
                public void onError(Throwable e) {
                    super.onError(e);
                    int code = HTTPConstants.CODE_INTERNAL;
                    String error = e.getLocalizedMessage();
                    if (e instanceof ActionException) {
                        code = ((ActionException) e).getCode();
                    }
                    if (isBackground && receiver != null) {
                        receiver.onReceive(new Data(code, error, null));
                        return;
                    }
                    Broadcaster.getInstance().postData(receiver, new Data(code, error, null));
                }
            };
        }
        return subscriber;
    }

    private Object fixDataModule(Object resp) {
        if (resp instanceof Data) {
            Data data = addRequiredSerialKey((Data) resp);
        }
        return resp;
    }

    private Data addRequiredSerialKey(Data data) {
        if (!TextUtils.isEmpty(serialKey)) {
            Data.Builder builder = new Data.Builder<>(data);
            builder.serialKey(serialKey);
            data = builder.build();
            serialKey = "";
        }
        return data;
    }



    private static abstract class SimpleSubscriber<T> extends Subscriber<T> {
        @Override
        public void onCompleted() {

        }

        @Override
        public void onError(Throwable e) {
        }
    }
}
