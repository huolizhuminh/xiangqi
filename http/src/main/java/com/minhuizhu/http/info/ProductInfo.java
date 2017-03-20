package com.minhuizhu.http.info;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by Rex on 2016/8/3.
 */
public class ProductInfo {
    final String product = "broker";
    final String version;

    public ProductInfo(Context context) {
        version = getVersionName(context);

    }

    private String getVersionName(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager().getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String toString() {
        return product + "||" + "version=" + version;
    }
}
