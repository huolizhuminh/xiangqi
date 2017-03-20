package com.minhuizhu.http.action;

/**
 * Created by Rex on 2016/8/8.
 */
public interface Event {
    String CREATE = "create_event";
    String LOGIN = "login_event";
    String LOGOUT = "logout_event";
    String PORTFOLIO_SYNC = "portfolio_sync";
    String REFRESH_PHOTO = "refresh_photo";
    String SUCCESS_MODIFY_PWD = "successModifyPwd";
    String SUCCESS_MODIFY_ACCOUNT = "successModifyAccount";
}
