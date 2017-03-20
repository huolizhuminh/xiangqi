package com.minhuizhu.http.action;

/**
 * Created by Rex on 2016/7/18.
 */
public final class CacheData<T> {
    public final long existedTime;
    public final T entity;

    public CacheData(long existedTime, T entity) {
        this.existedTime = existedTime;
        this.entity = entity;
    }

    @Override
    public String toString() {
        return "CacheData{" +
                "existedTime=" + existedTime +
                ", entity=" + entity +
                '}';
    }
}
