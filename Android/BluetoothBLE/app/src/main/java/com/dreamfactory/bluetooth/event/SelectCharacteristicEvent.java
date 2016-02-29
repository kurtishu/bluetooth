package com.dreamfactory.bluetooth.event;

import android.bluetooth.BluetoothGattCharacteristic;

/**
 * Author：kurtishu on 2/29/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class SelectCharacteristicEvent {

    private final BluetoothGattCharacteristic characteristic;

    public SelectCharacteristicEvent(BluetoothGattCharacteristic characteristic) {
        this.characteristic = characteristic;
    }

    public BluetoothGattCharacteristic getCharacteristic() {
        return characteristic;
    }
}
