package com.minhuizhu.http.action;

import com.google.gson.annotations.SerializedName;

/**
 * Created by Rex on 2016/7/18.
 */
public class Data<T> {
    public final String serialKey;
    public final int code;
    @SerializedName("msg")
    public final String error;
    @SerializedName("data")
    public final T entity;

    public Data(Builder<T> builder) {
        this.code = builder.code;
        this.error = builder.error;
        this.entity = builder.entity;
        this.serialKey = builder.serialKey;
    }

    public Data(int code, String error, T entity) {
        this.serialKey = null;
        this.code = code;
        this.error = error;
        this.entity = entity;
    }

    public boolean isSuccess() {
        return code == 0;
    }

    public boolean hasEntity() {
        return entity != null;
    }

    @Override
    public String toString() {
        return "Data{" +
                "serialKey='" + serialKey + '\'' +
                ", code=" + code +
                ", error='" + error + '\'' +
                ", entity=" + entity +
                '}';
    }

    public Builder<T> newBuilder() {
        return new Builder<>(this);
    }

    public static class Builder<T> {
        private String serialKey;
        private int code = -1;
        private String error;
        private T entity;

        public Builder() {
        }

        public Builder(Data<T> data) {
            if (data == null) {
                return;
            }
            this.code = data.code;
            this.error = data.error;
            this.entity = data.entity;
            this.serialKey = data.serialKey;
        }

        public Builder<T> code(int code) {
            this.code = code;
            return this;
        }

        public Builder<T> serialKey(String key) {
            this.serialKey = key;
            return this;
        }

        public Builder<T> error(String error) {
            this.error = error;
            return this;
        }

        public Builder<T> entity(T entity) {
            this.entity = entity;
            return this;
        }

        public Data<T> build() {
            return new Data<>(this);
        }
    }

}
