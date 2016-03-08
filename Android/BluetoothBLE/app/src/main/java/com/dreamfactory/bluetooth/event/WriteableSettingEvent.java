package com.dreamfactory.bluetooth.event;

import com.dreamfactory.library.model.BluetoothWriteableSetting;

/**
 * Author：kurtishu on 3/3/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class WriteableSettingEvent {

    private final int[] array;

    public WriteableSettingEvent(int[] array) {
        this.array = array;
    }

    public int[] getWriteableSetting() {
        return array;
    }
}

