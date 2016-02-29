package com.dreamfactory.bluetooth.event;

import android.bluetooth.BluetoothDevice;

/**
 * Author：kurtishu on 2/29/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class ConnectDeviceEvent {

    private final BluetoothDevice mDevice;

    public ConnectDeviceEvent(BluetoothDevice mDevice) {
        this.mDevice = mDevice;
    }

    public BluetoothDevice getDevice() {
        return mDevice;
    }
}
