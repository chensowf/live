package cn.mz.live.utils;

import android.content.SharedPreferences;

import cn.mz.live.APP;

public class SP {
    private static final String name="mz";


    public static String getString(String dbname, String key){
        SharedPreferences sp = APP.get().getSharedPreferences(dbname, APP.get().MODE_PRIVATE);
        return sp.getString(key, "");
    }
    public static void putString(String dbname, String key, String v){
        SharedPreferences sp = APP.get().getSharedPreferences(dbname, APP.get().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putString(key, v);
        editor.commit();
    }

    public static int getInt(String dbname, String key){
        SharedPreferences sp = APP.get().getSharedPreferences(dbname, APP.get().MODE_PRIVATE);
        return sp.getInt(key,0);
    }
    public static void putInt(String dbname, String key, int v){
        SharedPreferences sp = APP.get().getSharedPreferences(dbname, APP.get().MODE_PRIVATE);
        SharedPreferences.Editor editor = sp.edit();
        editor.putInt(key, v);
        editor.commit();
    }
}
