package com.dreamfactory.bluetooth.event;

import android.bluetooth.BluetoothGattService;

import java.util.List;

/**
 * Author：kurtishu on 2/29/16
 * Eevery one should have a dream, what if one day it comes true!
 */
public class GetServicesEvent {

    private final List<BluetoothGattService> services;

    public GetServicesEvent(List<BluetoothGattService> services) {
        this.services = services;
    }

    public List<BluetoothGattService> getServices() {
        return services;
    }
}
