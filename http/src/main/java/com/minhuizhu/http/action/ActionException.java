package com.minhuizhu.http.action;

/**
 * Created by rex.wei on 2016/12/5.
 */

public class ActionException extends RuntimeException {
    private int code = HTTPConstants.CODE_INTERNAL;

    public ActionException(String message) {
        super(message);
    }

    public ActionException(int code, String message) {
        super(message);
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
