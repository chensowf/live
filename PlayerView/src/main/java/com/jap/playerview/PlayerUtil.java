package com.jap.playerview;


import android.app.Activity;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.TrafficStats;
import android.support.annotation.NonNull;

public class PlayerUtil {


    /***
            * 获取当前网速
     *
             * @param activity 活动对象
     * @return long
     **/
    public static long getTotalRxBytes(@NonNull Activity activity) {
        return TrafficStats.getUidRxBytes(activity.getApplicationInfo().uid) == TrafficStats.UNSUPPORTED ?0: (TrafficStats.getTotalRxBytes() / 1024);//转为KB
    }

    /****
     * kb 转换mb
     *
     * @param k 该参数表示kb的值
     * @return double
     */
    public static double getM(long k) {

        double m;
        m = k / 1024.0;
        //返回kb转换之后的M值
        return m;
    }

    /**
     * 检查当前网络是否可用
     *
     * @param activity 活动
     * @return boolean
     */
    public static boolean isNetworkAvailable(@NonNull Activity activity) {
        Context context = activity.getApplicationContext();
        // 获取手机所有连接管理对象（包括对wi-fi,net等连接的管理）
        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager == null) {
            return false;
        } else {
            // 获取NetworkInfo对象
            NetworkInfo[] networkInfo = connectivityManager.getAllNetworkInfo();
            if (networkInfo != null && networkInfo.length > 0) {
                for (NetworkInfo aNetworkInfo : networkInfo) {
                    // 判断当前网络状态是否为连接状态
                    if (aNetworkInfo.getState() == NetworkInfo.State.CONNECTED) {
                        return true;
                    }
                }
            }
        }
        return false;
    }

    /**
     * 检查当前网络是否可用
     *
     * @param mContext 活动
     * @return boolean
     */
    public static boolean isWifi(@NonNull Context mContext) {
        ConnectivityManager connectivityManager = (ConnectivityManager) mContext
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetInfo != null
                && activeNetInfo.getType() == ConnectivityManager.TYPE_WIFI;
    }

    /***
     * 获取当前手机横屏状态
     *
     * @param activity 活动
     * @return int
     ***/
    public static boolean isLand(@NonNull Context activity) {
        Resources resources = activity.getResources();
        return !(resources == null || resources.getConfiguration() == null) && resources.getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;
    }

    /***
     * 获取当前手机状态
     *
     * @param activity 活动
     * @return int
     ***/
    public static int getOrientation(@NonNull Activity activity) {
        Resources resources= activity.getResources();
        if ( resources==null||resources.getConfiguration()==null){
            return Configuration.ORIENTATION_PORTRAIT;
        }
        return activity.getResources().getConfiguration().orientation;
    }

    /**
     * dp转px
     *
     * @param context 上下文
     * @param dpValue dp单位
     * @return int
     */
    public static int dip2px(@NonNull Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}
