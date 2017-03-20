package com.minhuizhu.common.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Environment;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.orhanobut.logger.Logger;

import java.io.File;
import java.io.IOException;
import java.util.List;

/**
 * @author dingyubin
 *         功能描述：常量工具类
 */
public class ContextUtils {
    /**
     * 得到设备屏幕的宽度
     */
    public static int getScreenWidth(Context context) {
        return context.getResources().getDisplayMetrics().widthPixels;
    }

    /**
     * 得到设备屏幕的高度
     */
    public static int getScreenHeight(Context context) {
        return context.getResources().getDisplayMetrics().heightPixels;
    }

    /**
     * 得到设备的密度
     */
    public static float getScreenDensity(Context context) {
        return context.getResources().getDisplayMetrics().density;
    }

    /**
     * 把密度转换为像素
     */
    public static int dip2px(Context context, float px) {
        final float scale = getScreenDensity(context);
        return (int) (px * scale + 0.5);
    }

    /**
     * 根据手机的分辨率从 px(像素) 的单位 转成为 dp
     */
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }

    /**
     * 在ScrollView显示完整的listView
     *
     * @param listView
     */
    public static void setListViewHeightBasedOnChildren(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = listView.getPaddingTop() + listView.getPaddingBottom();
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            if (listItem instanceof ViewGroup) {
                listItem.setLayoutParams(new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT));
            }
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));
        listView.setLayoutParams(params);
    }

    /**
     * 检查是否存在SDCard
     *
     * @return 存在sd卡 true
     */
    public static boolean hasSdcard() {
        String state = Environment.getExternalStorageState();
        if (state.equals(Environment.MEDIA_MOUNTED)) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean isNetworkAvailable(Context context) {
        ConnectivityManager manager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = manager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            return true;
        }
        return false;
    }

    public static int getVersionCode(Context context) {
        try {
            return context.getPackageManager().getPackageInfo(context.getPackageName(), 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return -1;
    }

    public static File getExternalFile(Context context, String fileName, boolean isDirectory) {
        File externalCacheDir = context.getExternalCacheDir();
        File file = null;
        if (externalCacheDir != null) {
            file = new File(externalCacheDir.getParentFile(), fileName);
            if (isDirectory) {
                file.mkdirs();
            } else {
                try {
                    file.createNewFile();
                } catch (IOException e) {
                   Logger.w("when reading external storage --> " + e.getMessage());
                }
            }
        } else {
           Logger.w("this phone doesn't have external storage!");
        }
        return file;
    }

    public static String getNetworkInfo(Context context) {
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        if (activeNetInfo != null) {
            return activeNetInfo.getTypeName();
        }
        return "no active network";
    }
    /**
     * 检查当前WIFI是否连接，两层意思——是否连接，连接是不是WIFI
     *
     * @param context
     * @return true表示当前网络处于连接状态，且是WIFI，否则返回false
     */
    public static boolean isWifiConnected(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info != null && info.isConnected()
                && ConnectivityManager.TYPE_WIFI == info.getType()) {
            return true;
        }
        return false;
    }
    /**
     * 程序是否在前台运行
     *
     * @return
     */
    public static boolean isAppOnForeground() {
        // Returns a list of application processes that are running on the
        // device

        ActivityManager activityManager = (ActivityManager) App.getContext().getSystemService(Context.ACTIVITY_SERVICE);
        String packageName = App.getContext().getPackageName();

        List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager
                .getRunningAppProcesses();
        if (appProcesses == null)
            return false;

        for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
            // The name of the process that this object is associated with.
            if (appProcess.processName.equals(packageName)
                    && appProcess.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                return true;
            }
        }

        return false;
    }
}
