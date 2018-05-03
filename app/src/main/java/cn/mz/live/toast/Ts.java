package cn.mz.live.toast;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

/**
 * Author:Administrator on 2015/8/3 15:01
 * Email:edison_ctj@sina.cn
 * ToDo:My own fastToast
 */
public class Ts {

    private static ToastCommom toastConfig;

    private static final String TAG = "Ts";

    static {

        toastConfig = ToastCommom.createToastConfig();


        Log.e(TAG, "static{}");

    }

    public static void shortToast(Context context, String str) {

        toastConfig.ToastShow(context, null, str, Toast.LENGTH_SHORT);
        /*Toast.makeText(context,
                str,
                Toast.LENGTH_SHORT).show();*/
    }

    public static void longToast(Context context, String str) {
        //Toast.makeText(context, str, Toast.LENGTH_LONG).show();
        toastConfig.ToastShow(context, null, str, Toast.LENGTH_LONG);
    }


}
