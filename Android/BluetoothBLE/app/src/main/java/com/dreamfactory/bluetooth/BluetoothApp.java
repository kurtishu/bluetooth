package com.dreamfactory.bluetooth;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;

import com.dreamfactory.bluetooth.service.BluetoothLeService;

/**
 * 作者：Kurtis on 2016/1/14 21:14
 * Eevery one should have a dream, what if one day it comes true!
 */
public class BluetoothApp extends Application {

    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;
        startService();
    }

    private void startService() {

        if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
            //Show toast

        }
        startService(new Intent(this, BluetoothLeService.class));
    }

    /**
     * Get the application Context
     * @return
     */
    public static Context getContext() {
        return context;
    }
}
