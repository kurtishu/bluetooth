package com.dreamfactory.bluetooth.util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.dreamfactory.bluetooth.BluetoothApp;

/**
 * Author：kurtishu on 3/3/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class PreferenceUtil {

    private static SharedPreferences sharedPreferences;

    static {
        sharedPreferences = PreferenceManager.getDefaultSharedPreferences(BluetoothApp.getContext());
    }

    public static void setIntValue(String key, int value) {
        sharedPreferences.edit().putInt(key, value).apply();
    }

    public static int getIntValue(String key) {
        return sharedPreferences.getInt(key, 0);
    }
}
