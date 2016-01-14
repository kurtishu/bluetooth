package com.dreamfactory.bluetooth.util;

import android.text.TextUtils;
import android.util.Log;

import com.dreamfactory.bluetooth.config.Constants;

/**
 * Author：kurtishu on 1/14/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public final class LogUtil {

    private LogUtil () {
    }

    public static void d(String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (Constants.IS_DEBUG) {
            Log.d(tag, message);
        }
    }

    public static void d(String tag, String messageTitle, Object object) {
        if (Constants.IS_DEBUG) {
            Log.d(tag, messageTitle + " " + object.toString());
        }
    }

    public static void e(String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (Constants.IS_DEBUG) {
            Log.e(tag, message);
        }
    }

    public static void e(String tag, String messageTitle, Object object) {
        if (Constants.IS_DEBUG) {
            Log.e(tag, messageTitle + " " + object.toString());
        }
    }

    public static void i(String tag, String message) {
        if (Constants.IS_DEBUG) {
            Log.i(tag, message);
        }
    }

    public static void i(String tag, String messageTitle, Object object) {
        if (Constants.IS_DEBUG) {
            Log.i(tag, messageTitle + " " + object.toString());
        }
    }

    public static void v(String tag, String message) {
        if (Constants.IS_DEBUG) {
            Log.v(tag, message);
        }
    }

    public static void v(String tag, String messageTitle, Object object) {
        if (Constants.IS_DEBUG) {
            Log.v(tag, messageTitle + " " + object.toString());
        }
    }

    public static void w(String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        if (Constants.IS_DEBUG) {
            Log.w(tag, message);
        }
    }

    public static void w(String tag, String messageTitle, Object object) {
        if (Constants.IS_DEBUG) {
            Log.w(tag, messageTitle + " " + object.toString());
        }
    }

}