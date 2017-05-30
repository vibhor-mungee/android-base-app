package com.stasheasy.fos.utils;

/**
 * Created by vibhormungee on 30/05/17.
 */

public class AppLog {

    public static boolean isDebug = Config.isDebug;

    public static void Log(String tag, String message) {
        if (isDebug) {
            android.util.Log.i(tag, message + "");
        }
    }

    public static void handleException(String tag, Exception e) {
        if (isDebug) {
            if (e != null) {
                android.util.Log.d(tag, e.getMessage() + "");
                e.printStackTrace();
            }
        }
    }

    public static void Error(String tag, String e) {
        if (isDebug) {
            android.util.Log.e(tag, e + "");
        }
    }
}
