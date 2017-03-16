package com.minhuizhu.mynewchess.core;

import android.app.Application;
import android.content.Context;

/**
 * Created by zhuminh on 2017/2/21.
 */

public class MyApplication extends Application {
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
