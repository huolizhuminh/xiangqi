package com.minhuizhu.http.info;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import com.minhuizhu.common.util.ContextUtils;

import java.util.Locale;

/**
 * Created by Rex on 2016/8/3.
 */
public class DeviceInfo {
    private final String platform = "Android";
    private final String channel;
    private final String osver;
    private final String romver;
    private final String model;
    private String identity;
    private final String lang;
    private final String network;

    public DeviceInfo(Context context) {
        osver = Build.VERSION.RELEASE;
        romver = Build.BRAND;
        model = Build.DISPLAY;
        identity = Build.SERIAL;
        lang = Locale.getDefault().toString();
        network = ContextUtils.getNetworkInfo(context);
        channel = getMetaValue(context, "UMENG_CHANNEL", "defaultOfficial");
    }

    private String getMetaValue(Context context, String key, String defaultValue) {
        String value = defaultValue;
        try {
            String packageName = context.getPackageName();
            ApplicationInfo appInfo = context.getPackageManager().getApplicationInfo(packageName, PackageManager.GET_META_DATA);
            value = appInfo.metaData.getString(key);
            value = value == null ? defaultValue : value;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    @Override
    public String toString() {
        return platform + "||" +
                "osver=" + osver + "||" +
                "romver=" + romver + "||" +
                "model=" + model + "||" +
                "identity=" + identity + "||" +
                "lang=" + lang + "||" +
                "network=" + network + "||" +
                "channel=" + channel;
    }
}
