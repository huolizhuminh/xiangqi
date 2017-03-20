package com.minhuizhu.common.util;

import android.content.Context;
import android.support.multidex.MultiDexApplication;

/**
 * Created by rex.wei on 2016/8/15.
 */
public class App extends MultiDexApplication {
    private static Context applicationContext;

    @Override
    public void onCreate() {
        super.onCreate();
        applicationContext = this.getApplicationContext();
    }


    public static Context getContext() {
        return applicationContext;
    }


}
