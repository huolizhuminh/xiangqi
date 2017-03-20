package com.minhuizhu.http.base;

import android.text.TextUtils;

import com.minhuizhu.common.cache.ACache;
import com.minhuizhu.common.encode.Aes;
import com.minhuizhu.common.encode.md5.EncodeUtils;
import com.minhuizhu.common.util.App;


/**
 * Created by Sandy(IOS) on 2016/7/18.
 * Updated by Rex(Android)
 */
public class CryptoHelper {

    private static final String LOGIN_RANDOM_KEY = "random";
    private static final String TRADE_RANDOM_KEY = "trade_random";
    private static final String SHARE_NAME = "crypt_config_rnd";

    private static final ACache acache = ACache.get(App.getContext(), SHARE_NAME);
    private static String loginRandomKey = null;




    public static String getUserAuth(String uin, String salt, String password) {
        String userAuth = null;
        try {
            int _uin = Integer.parseInt(uin);
            String randomKey = getRandomKey(LOGIN_RANDOM_KEY);
            userAuth = EncodeUtils.getUserAuth(_uin, salt, password, randomKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return userAuth == null ? "" : userAuth;
    }





    public static String decodeByLoginRandomKey(String input) {
        String decodeResult = null;
        try {
            String randomKey = getRandomKey(LOGIN_RANDOM_KEY);
            decodeResult = Aes.decrypt(input, randomKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return decodeResult == null ? "" : decodeResult;
    }

    public static String encodeByLoginRandomKey(String input) {
        String encodeResult = null;
        try {
            String randomKey = getRandomKey(LOGIN_RANDOM_KEY);
            encodeResult = Aes.encrypt(input, randomKey);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return encodeResult == null ? "" : encodeResult;
    }

    public static String getLoginRandomKey() {
        return getRandomKey(LOGIN_RANDOM_KEY);
    }


    public static void updateLoginRandomKey(String randomKey) {
        loginRandomKey = randomKey;
        acache.putSpString(LOGIN_RANDOM_KEY, randomKey);
    }

    private static String getRandomKey(String key) {
        if (loginRandomKey == null) {
            loginRandomKey = getRealRandomKey(key);
        }
        return loginRandomKey;
    }

    private static String getRealRandomKey(String key) {
        String randomKey = acache.getSpString(key);
        if (TextUtils.isEmpty(randomKey)) {
            try {
                randomKey = EncodeUtils.generateRandomKey();
                acache.putSpString(key, randomKey);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return randomKey;
    }

    public static void clear() {
        loginRandomKey = null;
        acache.clear();
    }
}
