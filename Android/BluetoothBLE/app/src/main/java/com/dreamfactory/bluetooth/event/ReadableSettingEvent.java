package com.dreamfactory.bluetooth.event;

import com.dreamfactory.library.model.BluetoothReadableSetting;

/**
 * Author：kurtishu on 3/3/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class ReadableSettingEvent {

    private final BluetoothReadableSetting setting;

    public ReadableSettingEvent(BluetoothReadableSetting setting) {
        this.setting = setting;
    }

    public BluetoothReadableSetting getReadableSetting() {
        return setting;
    }
}

