package com.dreamfactory.bluetooth.event;

import com.dreamfactory.library.model.BluetoothWriteableSetting;

/**
 * Author：kurtishu on 3/3/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class WriteableSettingEvent {

    private final BluetoothWriteableSetting setting;

    public WriteableSettingEvent(BluetoothWriteableSetting setting) {
        this.setting = setting;
    }

    public BluetoothWriteableSetting getWriteableSetting() {
        return setting;
    }
}

