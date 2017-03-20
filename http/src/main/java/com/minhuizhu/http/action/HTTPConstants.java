package com.minhuizhu.http.action;


/**
 * Created by minhui.zhu on 2016/9/13.
 */
public interface HTTPConstants {
    String H5_URL = "h5url";
    String MARKET_URL = "market_url";
    String MARKET_URL_HOST = "market_url_host";
    String TRADE_URL = "trade_url";
    String SEARCH_URL = "search_url";
    String SEARCH_URL2 = "search_url2";
    String USER_URL = "user_url";
    String OSS_URL = "oss_url";
    String APPLOG_URL = "applog_url";
    String SSO_URL = "sso_url";
    String CMS_URL = "cms_url";
    String X_CONTENT = "Content-Type";
    String X_DEVICE = "X-device";
    String X_PRODUCT = "X-product";
    String X_LOCAL_TYPE = "local-type";
    String X_LOCAL_ID = "local-id";
    String X_GUID = "X-requestid";
    String X_TRADE_AUTH = "X-auth2";
    String X_CACHE = "X-cache";
    String HEADER_CACHE_FOREVER = X_CACHE + ":-1";
    String COOKIE = "Cookie";
    String UIN = "uin";
    String SESSION = "session";
    String ENCRYPT = "text/encrypted";
    String ENCRYPT_AVER = "text/encrypted;aver=1";
    String ENCRYPT_TRADE_QUERY = "1";
    String ENCRYPT_TRADE_OPERATOR = "2";
    String ENCRYPT_USER = "3";
    Integer AVER_VERSION = 1;
    Integer AVER2_VERSION = 1;
    int CODE_TIME_OUT = 0x12345;
    int CODE_NETWORK_UNAVAILABLE = CODE_TIME_OUT + 60;
    int CODE_PARSE_ERROR = CODE_TIME_OUT + 70;
    int CODE_NO_RESPONSE = CODE_TIME_OUT + 80;
    int CODE_UNKNOWN_HOST = CODE_TIME_OUT + 90;
    int CODE_INTERNAL = CODE_TIME_OUT + 100;
}
