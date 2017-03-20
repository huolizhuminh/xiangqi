package com.minhuizhu.http.action;

/**
 * Created by Rex on 2016/7/18.
 */
public interface Receiver<T> {
    void onReceive(T data);
}
