package cn.mz.live.toast;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import cn.mz.live.R;


/**
 * Author: gavin
 * Date: 2016/2/23
 * Time: 17:56
 * Email:2415580905@qq.com
 */
public class ToastCommom {


    private static ToastCommom toastCommom;

    private Toast toast;

    private ToastCommom() {
    }

    public static ToastCommom createToastConfig() {

        if (toastCommom == null) {
            toastCommom = new ToastCommom();
        }
        return toastCommom;
    }

    /**
     * 显示Toast
     *
     * @param context
     * @param root
     * @param tvString
     */

    public void ToastShow(Context context, ViewGroup root, String tvString, int duration) {
        View layout = LayoutInflater.from(context).inflate(R.layout.toast_xml, root);
        TextView text = (TextView) layout.findViewById(R.id.text);
        text.setText(tvString);
        toast = new Toast(context);
        toast.setGravity(Gravity.BOTTOM, 0, 200);
        toast.setDuration(duration);
        toast.setView(layout);
        toast.show();
    }

}
