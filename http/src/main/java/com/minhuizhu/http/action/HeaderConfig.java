package com.minhuizhu.http.action;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.webkit.CookieManager;
import android.webkit.CookieSyncManager;

import com.minhuizhu.common.util.App;
import com.minhuizhu.http.base.AccountManager;
import com.minhuizhu.http.info.DeviceInfo;
import com.minhuizhu.http.info.ProductInfo;
import com.orhanobut.logger.Logger;

import java.util.List;
import java.util.UUID;

import okhttp3.HttpUrl;


/**
 * Created by Rex on 2016/8/2.
 */
public class HeaderConfig {

    private String device = "Android";
    private String product = "YouYu";

    private static class Internal {
        private static final HeaderConfig INSTANCE = new HeaderConfig();
    }

    private HeaderConfig() {
    }

    public String getCookie() {
        return makeCookie();
    }

    private String makeCookie() {
        String uin = "";
        String session = "";
        AccountManager accountManager = AccountManager.Companion.getInstance();
        if (accountManager.isLogin()) {
            uin = accountManager.getUin();
            session = accountManager.getLoginSession();
        }
        return HTTPConstants.UIN + "=" + uin + ";" + HTTPConstants.SESSION + "=" + session;
    }

    public void parseRespCookies(HttpUrl url, List<String> cookieList) {
        if (cookieList.isEmpty()) {
            return;
        }
        saveCookies(Uri.parse(url.toString()).getHost(), cookieList);
        Logger.d("when parsing cookie --> " + cookieList);
    }

    private void saveCookies(String url, List<String> cookieList) {
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(App.getContext());
        }
        CookieManager cookieManager = CookieManager.getInstance();
        for (String cookie : cookieList) {
            cookieManager.setCookie(url, cookie);
        }
        if (Build.VERSION.SDK_INT < Build.VERSION_CODES.LOLLIPOP) {
            CookieSyncManager.createInstance(App.getContext()).sync();
        } else {
            cookieManager.flush();
        }
    }

    public String getGUID() {
        return UUID.randomUUID().toString();
    }

    public static HeaderConfig getInstance() {
        return Internal.INSTANCE;
    }

    public HeaderConfig initialize(Context context) {
        this.device = new DeviceInfo(context).toString();
        this.product = new ProductInfo(context).toString();
        return this;
    }

    public String getDevice() {
        return device;
    }

    public String getProduct() {
        return product;
    }

}